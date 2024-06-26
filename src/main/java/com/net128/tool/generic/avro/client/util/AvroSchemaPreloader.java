package com.net128.tool.generic.avro.client.util;

import com.net128.tool.generic.avro.client.SchemaRegistryService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;

@SuppressWarnings("unused")
@Component
@Slf4j
public class AvroSchemaPreloader {

    @Autowired
    private SchemaRegistryService schemaRegistryService;

    @PostConstruct
    public void loadAvroSchemas() {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:avro/*.avsc");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                assert filename != null;
                String schemaName = filename.substring(0, filename.length() - 5);
                schemaRegistryService.addSchema(schemaName, ResourceUtil.loadResourceContent(resource));
            }
        } catch (IOException e) {
            log.error("Failed to load AVSC files: {}", e.getMessage());
        }
    }
}
