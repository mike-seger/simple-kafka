package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final ProducerService producerService;
    private final Listener listener;

    @GetMapping("/send/{topic}/{message}")
    public String sendMessage(@PathVariable String topic, @PathVariable String message) {
        producerService.sendMessage(topic, message);
        return "Message sent";
    }

    @GetMapping("/seek-time/{time}")
    public void seekToTime(@PathVariable OffsetDateTime time) {
        listener.seekToTimestamp(time.toInstant().toEpochMilli());
    }

    @GetMapping("/seek-back/{duration}")
    public void seekBack(@PathVariable String duration) {
        listener.seekToTimestamp(System.currentTimeMillis() - Duration.parse(duration).toMillis());
    }

    @GetMapping("/seek-to-beginning")
    public void seekToBeginning() {
        listener.seekToBeginning();
    }

    @GetMapping("/seek-to-end")
    public void seekToEnd() {
        listener.seekToEnd();
    }
}
