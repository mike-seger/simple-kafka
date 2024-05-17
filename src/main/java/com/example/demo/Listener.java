package com.example.demo;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AbstractConsumerSeekAware;
import org.springframework.stereotype.Component;

@Component
public class Listener extends AbstractConsumerSeekAware {
    @KafkaListener(topics = "embedded-test-topic", groupId = "group_id")
    public void listen(String message) {
        System.out.println("Received Message in group group_id: " + message);
    }
}
