package cn.mor.eventbus.receiver;

import cn.mor.eventbus.body.SimpleEvent;
import cn.mor.eventbus.event.AbstractReceiver;
import org.springframework.stereotype.Component;

@Component
public class SimpleEventReceiver extends AbstractReceiver<SimpleEvent> {


    @Override
    public String[] getEventNames() {
        return new String[]{SimpleEvent.NAME};
    }

    @Override
    public void doEvent(SimpleEvent paramT) {
        System.err.println(paramT.getName());
    }



}
