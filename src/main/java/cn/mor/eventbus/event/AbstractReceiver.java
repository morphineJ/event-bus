package cn.mor.eventbus.event;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class AbstractReceiver<T> implements Receiver<T> {

    @PostConstruct
    private void init() {
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = getEventNames()).length, b = 0; b < i; ) {
            String name = arrayOfString[b];
            this.eventBus.register(name, this);
            b++;
        }

    }

    @Autowired
    protected EventBus eventBus;

    public abstract String[] getEventNames();

    public void onEvent(Event<T> event) {
        T content = event.getBody();
        doEvent(content);
    }

    public abstract void doEvent(T paramT);
}
