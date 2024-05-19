package com.net128.tool.generic.avro.client.controllers;

import com.net128.tool.generic.avro.client.Listener;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="2 - Consumer", description = "Some convenience consumer related methods")
@SuppressWarnings("unused")
public class ConsumerController {
    private final Listener listener;

    @GetMapping("/seek-time/{time}")
    @Operation(summary = "Seeks the consumed topics to a specific time",
        parameters = {@Parameter(name = "time", example = "2023-01-01T15:37:41.123456Z",
        schema = @Schema(type = "string", format="date-time"))})
    public void seekToTime(@PathVariable OffsetDateTime time) {
        log.info("seekToTime: {}", time);
        listener.seekToTimestamp(time.toInstant().toEpochMilli());
    }

    @GetMapping("/seek-back/{duration}")
    @Operation(summary = "Seeks the consumed topics to a specific time offset.",
        parameters = {@Parameter(name = "duration", example = "PT30M",
            schema = @Schema(type = "string"))})
    public void seekBack(@PathVariable String duration) {
        var timeStamp = System.currentTimeMillis() - Duration.parse(duration).toMillis();
        log.info("seekBack: {} ({})", timeStamp, Instant.ofEpochMilli(timeStamp));
        listener.seekToTimestamp(timeStamp);
    }

    @GetMapping("/seek-to-beginning")
    @Operation(summary = "Seeks the consumed topics to the beginning.")
    public void seekToBeginning() {
        log.info("seekToBeginning");
        listener.seekToBeginning();
    }

    @GetMapping("/seek-to-end")
    @Operation(summary = "Seeks the consumed topics to the end.")
    public void seekToEnd() {
        log.info("seekToEnd");
        listener.seekToEnd();
    }
}
