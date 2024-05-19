package com.net128.tool.generic.avro.client.controllers;

import com.net128.tool.generic.avro.client.SchemaRegistryService;
import com.net128.tool.generic.avro.client.util.AvroUtils;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("avro")
@SuppressWarnings("unused")
public class AvroController {
    private final SchemaRegistryService schemaRegistryService;
    private final AvroUtils avroUtils;

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

    @PostMapping(value = "/schema/{name}/serialize/", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Serialize JSON to Avro",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = Object.class),
                examples = { @ExampleObject(value = exampleObject) })),
        parameters = {@Parameter(name = "name", description = "Name of the schema / topic",
            example = "user1", schema = @Schema(type = "string"))})
    public byte [] toAvro(@RequestBody String json, @PathVariable String name) throws Exception {
        return avroUtils.serializeToAvro(name, json);
    }

    @PostMapping(value = "/schema/{name}/hexdump/", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Serialize JSON to Avro Hex Dump",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = Object.class),
                examples = { @ExampleObject(value = exampleObject) })),
        parameters = {@Parameter(name = "name", description = "Name of the schema / topic",
            example = "user1", schema = @Schema(type = "string"))})
    @ApiResponse(responseCode = "200", content = @Content(examples = { @ExampleObject(
        value = "0000  08 4A 6F 65 32 80 7D                             |.Joe2.}|")}))
    public String toAvroHexDump(@RequestBody String json, @PathVariable String name) throws Exception {
        return avroUtils.jsonToAvroHexDump(name, json);
    }

    final static String exampleObject =
        """
        {
            "name": "Joe2",
            "age": 8000
        }
        """;
}
