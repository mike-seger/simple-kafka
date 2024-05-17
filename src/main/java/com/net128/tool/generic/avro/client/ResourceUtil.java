package com.net128.tool.generic.avro.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class ResourceUtil {
    public static File copyResourceToTempFile(String resourcePath) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        tempFile.deleteOnExit();
        try (InputStream resourceStream = ResourceUtil.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (resourceStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            Files.copy(resourceStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return tempFile;
    }

    public static String loadResource(String location) throws URISyntaxException, IOException {
        return Files.readString(Paths.get(Objects.requireNonNull(ResourceUtil.class.getClassLoader().getResource(location)).toURI()));
    }
}