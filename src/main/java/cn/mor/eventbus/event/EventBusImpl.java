package cn.mor.eventbus.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;


@Component
public class EventBusImpl implements EventBus, ApplicationListener<ContextClosedEvent> {


    private ConcurrentHashMap<String, CopyOnWriteArraySet<Receiver<?>>> receivers = new ConcurrentHashMap<String, CopyOnWriteArraySet<Receiver<?>>>();

    @Value("${event_queue_size}")
    private int queueSize;

    private BlockingQueue<Event<?>> eventQueue;


    @Value("${event_pool_size}")
    private int poolSize;

    private int poolMaxSize = Runtime.getRuntime().availableProcessors();


    @Value("${event_pool_alive_time}")
    private Integer poolKeepAlive;



    private ExecutorService pool;


    private volatile boolean stop;



    private Runnable consumerRunner = new Runnable() {
        public void run() {
            while (true) {
                try {
                    Event<?> event = eventQueue.take();
                    String name = event.getName();
                    if (!receivers.containsKey(name)) {
                        continue;
                    }
                    for (Receiver<?> receiver :  receivers.get(name)) {
                        Runnable runner = createRunner(receiver, event);
                        try {
                            pool.submit(runner);
                        } catch (RejectedExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        private void onRejected(Receiver receiver, Event event) {
            try {
                receiver.onEvent(event);
            } catch (ClassCastException e) {

            } catch (Throwable t) {

            }
        }


        private Runnable createRunner(final Receiver receiver, final Event event) {
            return new Runnable() {
                public void run() {
                    try {
                        receiver.onEvent(event);
                    } catch (ClassCastException e) {
                        e.printStackTrace();

                    } catch (Throwable t) {
                        throw new RuntimeException(t);
                    }
                }
            };
        }
    };




    @PostConstruct
    protected void initialize() {
        this.eventQueue = new LinkedBlockingQueue<Event<?>>(queueSize);
        this.pool = new ThreadPoolExecutor(poolSize, poolMaxSize, poolKeepAlive, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(queueSize));
        Thread consumer = new Thread(consumerRunner, "事件消费者线程");
        consumer.setDaemon(true);
        consumer.start();
    }


    public void onApplicationEvent(ContextClosedEvent event) {
        shutdown();
    }


    public void shutdown() {
        if (isStop())
            return;
        this.stop = true;

        while (!this.eventQueue.isEmpty()) {

            Thread.yield();
        }
        this.pool.shutdown();

        while (!this.pool.isTerminated()) {

            Thread.yield();
        }
    }


    public boolean isStop() {
        return this.stop;
    }


    @Override
    public void post(Event<?> event) {
        if (event == null) {
            throw new IllegalArgumentException("事件为空");
        }
        if (isStop()) {
            throw new IllegalStateException("事件消费者已停止运行");
        }
        try {
            this.eventQueue.put(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void register(String name, Receiver<?> receiver) {
        if (name == null || receiver == null) {
            throw new IllegalArgumentException("事件名或接收者为空");
        }

        CopyOnWriteArraySet<Receiver<?>> set = this.receivers.get(name);
        if (set == null) {
            set = new CopyOnWriteArraySet<Receiver<?>>();
            CopyOnWriteArraySet<Receiver<?>> prev = this.receivers.putIfAbsent(name, set);
            set = (prev != null) ? prev : set;
        }

        set.add(receiver);
    }

    @Override
    public void unregister(String name, Receiver<?> receiver) {
        if (name == null || receiver == null) {
            throw new IllegalArgumentException("事件名或接收者为空");
        }
        CopyOnWriteArraySet<Receiver<?>> set = this.receivers.get(name);
        if (set != null)
            set.remove(receiver);
    }



}
