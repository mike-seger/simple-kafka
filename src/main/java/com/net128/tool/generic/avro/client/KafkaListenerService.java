package com.net128.tool.generic.avro.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.regex.Pattern;

//@Service
public class KafkaListenerService {

    private ConcurrentMessageListenerContainer<String, String> container;

    @Autowired
    private ConcurrentKafkaListenerContainerFactory<String, String> containerFactory;

    public void startContainer(String[] topics) {
        if (this.container != null && this.container.isRunning()) {
            this.container.stop();
        }
        this.container = containerFactory.createContainer(/*topics*/Pattern.compile(".*"));
        this.container.setupMessageListener((MessageListener<String, String>) record -> {
            // Your listener logic here
            //System.out.println("Received: " + record.value());
        });
        this.container.start();
    }

    public void updateTopics(Set<String> newTopics) {
        startContainer(newTopics.toArray(new String[0]));
    }
}
