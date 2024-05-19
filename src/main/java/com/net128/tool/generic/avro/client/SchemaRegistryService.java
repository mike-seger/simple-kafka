package com.net128.tool.generic.avro.client;

import com.net128.tool.generic.avro.client.util.ResourceUtil;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SchemaRegistryService {

    private final ConcurrentHashMap<String, Schema> schemaMap = new ConcurrentHashMap<>();

    public void addSchema(String name, String avroSchema) {
        if(avroSchema.startsWith("classpath:")) {
            try {
                var path = ResourceUtil.copyResourceToTempFile(
                    avroSchema.substring("classpath:".length()).trim()).getAbsolutePath();
                avroSchema = new String(Files.readAllBytes(Paths.get(path)));
            } catch (IOException e) { throw new RuntimeException(e); }
        }
        var schema = new Schema.Parser().parse(avroSchema);
        schemaMap.put(name, schema);
    }

    public Schema getSchema(String name) {
        if (schemaMap.containsKey(name)) { return schemaMap.get(name); } else { throw new IllegalArgumentException("Schema not found for: " + name); }
    }

    public List<String> getSchemaNames() {
        return new ArrayList<>(schemaMap.keySet());
    }
}
