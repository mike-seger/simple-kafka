package com.net128.tool.generic.avro.client;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
@Slf4j
public class AvroSchemaPreloader {

    @Autowired
    private SchemaRegistryService schemaRegistryService; // Assume this service handles schema registration

    @PostConstruct
    public void loadAvroSchemas() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:avro/*.avsc");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                String schemaName = filename.substring(0, filename.length() - 5);
                schemaRegistryService.addSchema(schemaName, ResourceUtil.loadResourceContent(resource));
                System.out.println("Loaded and registered schema: " + schemaName);
            }
        } catch (IOException e) {
            log.error("Failed to load AVSC files: {}", e.getMessage());
        }
    }
}
