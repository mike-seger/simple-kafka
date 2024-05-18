package com.net128.tool.generic.avro.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unused")
public class Controller {

    private final ProducerService producerService;
    private final Listener listener;
    private final SchemaRegistryService schemaRegistryService;
    private final AvroUtils avroUtils;

    @PostMapping("/send/{topic}")
    @Operation(summary = "Send a JSON message to a topic in AVRO format",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(implementation = Object.class),
                examples = { @ExampleObject(value = exampleObject) })),
        parameters = {@Parameter(name = "topic",
            required = true, example = "user1",
            schema = @Schema(type = "string"))}
    )
    @ApiResponse(responseCode = "200",
        content = @Content(examples = { @ExampleObject(value = sentResponse)}))

    public String sendMessage(@PathVariable String topic, @RequestBody String message) {
        producerService.sendMessage(topic, message);
        return sentResponse;
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
    @Operation(summary = "List all AVRO schemas")
    public List<String> getSchemaNames() {
        return schemaRegistryService.getSchemaNames();
    }

    @PostMapping("/schema/{name}")
    @Operation(summary = "Sets the AVRO schema")
    public void setSchema(@RequestBody String schema, @PathVariable String name) {
        schemaRegistryService.addSchema(name, schema);
    }

    @PostMapping(value = "/schema/{name}/to-avro-hex/", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Convert JSON to Avro Hex Dump",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(implementation = Object.class),
                examples = { @ExampleObject(value = exampleObject) })),
        parameters = {@Parameter(name = "name", description = "Name of the schema / topic",
            required = true, example = "user1",
            schema = @Schema(type = "string"))})
    public String toAvroHexDump(@RequestBody String json, @PathVariable String name) throws Exception {
        return avroUtils.jsonToAvroHexDump(name, json);
    }

    private final static String exampleObject =
        """
        {
            "name": "Joe2",
            "age": 8000
        }
        """;
    private final static String sentResponse = "Message sent\n";
}
