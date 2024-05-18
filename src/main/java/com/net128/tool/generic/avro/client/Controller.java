package com.net128.tool.generic.avro.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final ProducerService producerService;
    private final Listener listener;
    private final SchemaRegistryService schemaRegistryService;

    @GetMapping("/send/{topic}/{message}")
    public String sendSimpleMessage(@PathVariable String topic, @PathVariable String message) {
        return sendMessage(topic, message);
    }

    @PostMapping("/send/{topic}")
    public String sendMessage(@PathVariable String topic, @RequestBody String message) {
        producerService.sendMessage(topic, message);
        return "Message sent\n";
    }

    @GetMapping("/seek-time/{time}")
    public void seekToTime(@PathVariable OffsetDateTime time) {
        log.info("seekToTime: {}", time);
        listener.seekToTimestamp(time.toInstant().toEpochMilli());
    }

    @GetMapping("/seek-back/{duration}")
    public void seekBack(@PathVariable String duration) {
        var timeStamp = System.currentTimeMillis() - Duration.parse(duration).toMillis();
        log.info("seekBack: {} ({})", timeStamp, Instant.ofEpochMilli(timeStamp));
        listener.seekToTimestamp(timeStamp);
    }

    @GetMapping("/seek-to-beginning")
    public void seekToBeginning() {
        log.info("seekToBeginning");
        listener.seekToBeginning();
    }

    @GetMapping("/seek-to-end")
    public void seekToEnd() {
        log.info("seekToEnd");
        listener.seekToEnd();
    }

    @GetMapping("/schemas")
    public List<String> getSchemaNames() {
        return schemaRegistryService.getSchemaNames();
    }

    @PostMapping("/schema/{name}")
    public void setSchema(@RequestBody String schema, @PathVariable String name) {
        schemaRegistryService.addSchema(name, schema);
    }
}
