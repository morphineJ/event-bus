package cn.mor.eventbus.body;

import cn.mor.eventbus.event.Event;

public class SimpleEvent extends Event<SimpleBody> {


    public static final String NAME = "simpleEvent:simple";

    @Override
    public String getName() {
        return NAME;
    }

    public SimpleEvent(String name) {
        super(name);
    }

    public SimpleEvent(String name, SimpleBody body) {
        super(name, body);

    }


    public static Event<SimpleEvent> valueOf(SimpleBody body) {
        return new Event<SimpleEvent>(NAME, new SimpleEvent(NAME,body));
    }


}
