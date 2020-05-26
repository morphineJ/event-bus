package cn.mor.eventbus;

import cn.mor.eventbus.service.PostEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String args[]){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        PostEvent postEvent= (PostEvent) applicationContext.getBean("postEvent");
        postEvent.test();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
