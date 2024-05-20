package com.net128.tool.generic.avro.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        System.out.printf("Tempdir: %s", System.getProperty("java.io.tmpdir"));
        SpringApplication.run(Application.class, args);
    }
}
