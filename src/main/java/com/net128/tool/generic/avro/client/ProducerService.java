package com.net128.tool.generic.avro.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final AvroUtils avroUtils;

    public void sendMessage(String topic, String message) {
        try {
            var avroData = avroUtils.serializeToAvro(topic, message);
            var future = kafkaTemplate.send(topic, avroData);
            try {
                var result = future.get();
                log.info("Message sent successfully with offset: {}, to topic: {}, at {}",
                    result.getRecordMetadata().offset(), topic,
                    Instant.ofEpochMilli(result.getRecordMetadata().timestamp()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread was interrupted while waiting for the send to complete.");
            } catch (Exception e) {
                log.error("Failed to send message due to: " + e.getCause());
            }
        } catch (Exception e) {
            log.error("Error sending message: {}, to topic: {}", message, topic);
        }
    }
}
