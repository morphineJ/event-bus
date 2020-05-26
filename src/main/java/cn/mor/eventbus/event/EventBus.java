package cn.mor.eventbus.event;

public interface EventBus {

    void post(Event<?> event);

    void register(String paramString, Receiver<?> paramReceiver);

    void unregister(String paramString, Receiver<?> paramReceiver);
}
