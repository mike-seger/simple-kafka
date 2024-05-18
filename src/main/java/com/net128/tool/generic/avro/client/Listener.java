package com.net128.tool.generic.avro.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AbstractConsumerSeekAware;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Listener extends AbstractConsumerSeekAware {
    private final AvroUtils avroUtils;
    @KafkaListener(topicPattern = ".*", groupId = "G1")
    public void listen(byte [] messageData, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws Exception {
        var message = avroUtils.deserializeAvro(topic, messageData);
        log.info("Received G1 Message in {}:\n{}", topic, message);
    }

    @KafkaListener(topics = "user", groupId = "G2", autoStartup = "false")
    public void listen2(byte [] messageData, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws Exception {
//        var message = avroUtils.deserializeAvro(topic, messageData);
//        log.info("Received G2 Message in {}:\n{}", topic, message);
    }
}

