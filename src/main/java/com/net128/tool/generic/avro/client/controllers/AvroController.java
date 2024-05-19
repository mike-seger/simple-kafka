package com.net128.tool.generic.avro.client.controllers;

import com.net128.tool.generic.avro.client.SchemaRegistryService;
import com.net128.tool.generic.avro.client.util.AvroUtil;
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
    private final AvroUtil avroUtil;

    @GetMapping("/schemas")
    @Operation(summary = "Get all AVRO schema names")
    public List<String> getSchemaNames() {
        return schemaRegistryService.getSchemaNames();
    }

    @GetMapping("/schema/{name}")
    @Operation(summary = "Get a raw AVRO schema",
        parameters = {@Parameter(name = "name", example = "user1")}
    )
    public String getSchemas( @PathVariable String name) {
        return schemaRegistryService.getRawSchema(name);
    }

    @PostMapping("/schema/{name}")
    @Operation(summary = "Sets an AVRO schema",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = Object.class),
                            examples = { @ExampleObject(value = exampleAvroSchema) })),
            parameters = {@Parameter(name = "name", description = "Name of the schema / topic",
                    example = "user1", schema = @Schema(type = "string"))})
    public void setSchema(@RequestBody String schema, @PathVariable String name) {
        schemaRegistryService.addSchema(name, schema);
    }

    @PostMapping(value = "/schema/{name}/serialize", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Serialize JSON to Avro",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = Object.class),
                examples = { @ExampleObject(value = exampleObject) })),
        parameters = {@Parameter(name = "name", description = "Name of the schema / topic",
            example = "user1", schema = @Schema(type = "string"))})
    public byte [] toAvro(@RequestBody String json, @PathVariable String name) throws Exception {
        return avroUtil.serializeToAvro(name, json);
    }

    @PostMapping(value = "/schema/{name}/hexdump", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Serialize JSON to Avro Hex Dump",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = Object.class),
                examples = { @ExampleObject(value = exampleObject) })),
        parameters = {@Parameter(name = "name", description = "Name of the schema / topic",
            example = "user1", schema = @Schema(type = "string"))})
    @ApiResponse(responseCode = "200", content = @Content(examples = { @ExampleObject(value = hexDump)}))
    public String toAvroHexDump(@RequestBody String json, @PathVariable String name) throws Exception {
        return avroUtil.jsonToAvroHexDump(name, json);
    }

    final static String exampleObject =
        """
        {
            "id": "dff964a6-7f97-49da-8eb5-108701e1e3b3",
            "name": "Maria do Carmo Mão de Ferro e Cunha de Almeida Santa Rita Santos Abreu",
            "age": 33
        }
        """;

    final static String exampleAvroSchema = """
        {
          "type": "record",
          "name": "User",
          "namespace": "com.example.demo.avro",
          "fields": [
            {"name": "id", "type": { "type": "fixed", "name": "id", "size": 36 }},
            {"name": "name", "type": "string"},
            {"name": "age", "type": "int"}
          ]
        }
        """;

    final static String hexDump = """
            0000  64 66 66 39 36 34 61 36 2D 37 66 39 37 2D 34 39  |dff964a6-7f97-49|
            0010  64 61 2D 38 65 62 35 2D 31 30 38 37 30 31 65 31  |da-8eb5-108701e1|
            0020  65 33 62 33 8E 01 4D 61 72 69 61 20 64 6F 20 43  |e3b3..Maria do C|
            0030  61 72 6D 6F 20 4D C3 A3 6F 20 64 65 20 46 65 72  |armo M..o de Fer|
            0040  72 6F 20 65 20 43 75 6E 68 61 20 64 65 20 41 6C  |ro e Cunha de Al|
            0050  6D 65 69 64 61 20 53 61 6E 74 61 20 52 69 74 61  |meida Santa Rita|
            0060  20 53 61 6E 74 6F 73 20 41 62 72 65 75 42        | Santos AbreuB|
            """;
}
