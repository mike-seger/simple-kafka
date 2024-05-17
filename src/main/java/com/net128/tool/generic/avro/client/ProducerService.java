package com.net128.tool.generic.avro.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final AvroUtils avroUtils;

    public void sendMessage(String topic, String jsonData) {
        try {
            var avroData = avroUtils.serializeToAvro(topic, jsonData);
            log.info("\n"+new String(avroData));
            kafkaTemplate.send(topic, avroData);
            var future = kafkaTemplate.send(topic, avroData);
            try {
                var result = future.get();
                log.info("Message sent successfully with offset: {} to topic: {}",
                    result.getRecordMetadata().offset(), topic);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread was interrupted while waiting for the send to complete.");
            } catch (Exception e) {
                log.error("Failed to send message due to: " + e.getCause());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
