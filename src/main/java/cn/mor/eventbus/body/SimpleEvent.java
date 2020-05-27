package cn.mor.eventbus.body;

import cn.mor.eventbus.event.Event;

public class SimpleEvent {


    public static final String NAME = "simpleEvent:simple";

    private String name ;

    private int age;

    public static SimpleEvent valueOf(String name, int age) {
        SimpleEvent result = new SimpleEvent();
        result.name = name;
        result.age = age;
        return result;
    }
    
    public static Event<SimpleEvent> valueOf(SimpleEvent simpleEvent) {
        return new Event<SimpleEvent>(NAME, simpleEvent);
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
