package com.net128.tool.generic.avro.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final AvroUtils avroUtils;

    public void sendMessage(String topic, String jsonData) {
        try {
            // Assume schemaName is either derived from the topic or another source
            String schemaName = "yourSchemaName"; // This should be dynamic based on context
            byte[] avroData = avroUtils.serializeAvroRecordGeneric(schemaName, jsonData);
            kafkaTemplate.send(topic, avroData);
        } catch (Exception e) {
            // Handle serialization errors (log, throw, etc.)
            e.printStackTrace();
        }
    }
}
