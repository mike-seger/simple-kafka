package com.net128.tool.generic.avro.client;

import com.net128.tool.generic.avro.client.util.AvroUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AbstractConsumerSeekAware;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class Listener extends AbstractConsumerSeekAware {
    private final AvroUtils avroUtils;

    @KafkaListener(topicPattern = ".*")
    public void listen(
            byte [] messageData,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.OFFSET) Long offset,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp
    ) throws Exception {
        var message = avroUtils.deserializeAvro(topic, messageData);
        log.info("Received message from topic {}, at offset {}, on {}:\n{}",
            topic, offset, Instant.ofEpochMilli(timestamp), message);
    }
}
