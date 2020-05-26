package cn.mor.eventbus.event;

public interface Receiver<T> {
    void onEvent(Event<T> paramEvent);
}
