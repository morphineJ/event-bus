package cn.mor.eventbus.service;


import cn.mor.eventbus.body.SimpleBody;
import cn.mor.eventbus.body.SimpleEvent;
import cn.mor.eventbus.event.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostEvent {
    @Autowired
    private EventBus eventBus;



    public void test(){
       eventBus.post(SimpleEvent.valueOf(SimpleBody.valueOf("11",22)));
    }

}
