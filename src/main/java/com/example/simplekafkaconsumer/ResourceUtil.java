package com.example.simplekafkaconsumer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ResourceUtil {

    public static File copyResourceToTempFile(String resourcePath) throws IOException {
        // Create a temporary file
        File tempFile = File.createTempFile("temp", null);

        // Ensure the temporary file is deleted on exit
        tempFile.deleteOnExit();

        // Try with resources to ensure InputStream is closed
        try (InputStream resourceStream = ResourceUtil.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (resourceStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            // Copy the resource contents to the temporary file
            Files.copy(resourceStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        return tempFile;
    }
}