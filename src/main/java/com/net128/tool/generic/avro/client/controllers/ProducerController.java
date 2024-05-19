package com.net128.tool.generic.avro.client.controllers;

import com.net128.tool.generic.avro.client.ProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name="1 - Producer", description = "Some convenience producer related methods")
@RequestMapping("producer")
@SuppressWarnings("unused")
public class ProducerController {
    private final ProducerService producerService;

    @PostMapping("/send/{topic}")
    @Operation(summary = "Send a JSON message to a topic in AVRO format",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(implementation = Object.class),
                examples = { @ExampleObject(value = AvroController.exampleObject) })),
        parameters = {@Parameter(name = "topic", example = "user1")}
    )
    @ApiResponse(responseCode = "200",
        content = @Content(examples = { @ExampleObject(value = sentResponse)}))
    public String sendMessage(@PathVariable String topic, @RequestBody String message) {
        producerService.sendMessage(topic, message);
        return sentResponse;
    }

    @GetMapping("/send/{topic}/{n}/{idField}")
    @Operation(summary = "Send random messages to a topic in AVRO format",
        parameters = {
            @Parameter(name = "topic", example = "user1"),
            @Parameter(name = "n", example = "5"),
            @Parameter(name = "idField", example = "id")}
    )
    @ApiResponse(responseCode = "200",
        content = @Content(examples = { @ExampleObject(value = sentResponse)}))
    public String sendRandomMessages(@PathVariable String topic, int n, String idField) {
        producerService.sendRandomMessages(topic, n, idField);
        return sentResponse;
    }

    final static String sentResponse = "Message(s) sent\n";
}
