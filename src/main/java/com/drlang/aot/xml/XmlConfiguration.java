package com.drlang.aot.xml;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Configuration
@ImportResource("/app.xml")
public class XmlConfiguration {

}

class MessageProducer implements Supplier<String> {

    @Override
    public String get() {
        return "Hello, world!";
    }
}

class XmlLoggingApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
    @Nullable
    private  MessageProducer producer;

    public void setProducer(MessageProducer producer) {
        this.producer = producer;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("the message is " + producer.get());
    }
}