package com.net128.tool.generic.avro.client;

import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SchemaRegistryService {

    private final ConcurrentHashMap<String, Schema> schemaMap = new ConcurrentHashMap<>();

    public void addSchema(String name, String avroSchema) {
        Schema schema = new Schema.Parser().parse(avroSchema);
        schemaMap.put(name, schema);
    }

    public Schema getSchema(String name) {
        if (schemaMap.containsKey(name)) {
            return schemaMap.get(name);
        } else {
            throw new IllegalArgumentException("Schema not found for: " + name);
        }
    }
}
