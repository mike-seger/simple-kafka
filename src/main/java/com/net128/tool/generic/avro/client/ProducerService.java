package com.net128.tool.generic.avro.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.net128.tool.generic.avro.client.util.AvroRandomDataGenerator;
import com.net128.tool.generic.avro.client.util.AvroUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final AvroUtils avroUtils;
    private final SchemaRegistryService schemaRegistryService;
    private final AtomicInteger counter = new AtomicInteger();

    public void sendMessage(String topic, String message) {
        try {
            var avroData = avroUtils.serializeToAvro(topic, message);
            var future = kafkaTemplate.send(topic, avroData);
            try {
                var count = counter.incrementAndGet();
                log.info("Posting message {} to topic: {} -> {}", count, topic, message);
                var result = future.get();
                log.info("Message {} sent successfully with offset: {}, to topic: {}, at {}",
                    count, result.getRecordMetadata().offset(), topic,
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

    public void sendRandomMessages(String topic, int nRandom, String idField) {
        var schema = schemaRegistryService.getSchema(topic);
        var count = new AtomicInteger();
        IntStream.range(0,nRandom).parallel().forEach(i -> {
            try {
                var message = AvroRandomDataGenerator.generateRandomJsonFromSchema(schema, idField);
                sendMessage(topic, message);
                count.incrementAndGet();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        log.info("Sent {} random messages.", nRandom);
    }
}
