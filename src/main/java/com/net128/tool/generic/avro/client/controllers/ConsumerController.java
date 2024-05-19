package com.net128.tool.generic.avro.client.controllers;

import com.net128.tool.generic.avro.client.Listener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("consumer")
@SuppressWarnings("unused")
public class ConsumerController {
    private final Listener listener;

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
}
