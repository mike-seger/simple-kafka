package com.net128.tool.generic.avro.client;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AbstractConsumerSeekAware;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Listener extends AbstractConsumerSeekAware {
    private final AvroUtils avroUtils;
    @KafkaListener(topicPattern = ".*")
    public void listen(byte [] messageData, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws Exception {
        var message = avroUtils.deserializeAvroRecordGeneric(topic, messageData);
        System.out.printf("Received Message in %s:\n%s\n", topic, message);
    }
}
