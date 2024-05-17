package com.net128.tool.generic.avro.client;

import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AvroUtils {
    private final SchemaRegistryService schemaRegistryService;

    public static GenericRecord jsonToGenericRecord(Schema schema, String jsonData) throws IOException {
        var reader = new GenericDatumReader<GenericRecord>(schema);
        var decoder = DecoderFactory.get().jsonDecoder(schema, jsonData);
        return reader.read(null, decoder);
    }

    public static String genericRecordToJson(GenericRecord record) throws IOException {
        var writer = new GenericDatumWriter<GenericRecord>(record.getSchema());
        try (var outputStream = new ByteArrayOutputStream()) {
            var jsonEncoder = EncoderFactory.get().jsonEncoder(record.getSchema(), outputStream);
            writer.write(record, jsonEncoder);
            jsonEncoder.flush();
            return outputStream.toString();
        }
    }

    public byte[] serializeToAvro(String schemaName, String jsonData ) throws Exception {
        var schema = schemaRegistryService.getSchema(schemaName);
        var record = jsonToGenericRecord(schema, jsonData);
        var outputStream = new ByteArrayOutputStream();
        var datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        datumWriter.write(record, encoder);
        encoder.flush();
        return outputStream.toByteArray();
    }

    public String deserializeAvro(String schemaName, byte[] data) throws Exception {
        var schema = schemaRegistryService.getSchema(schemaName);
        var datumReader = new GenericDatumReader<GenericRecord>(schema);
        var decoder = DecoderFactory.get().binaryDecoder(data, null);
        var resultRecord = datumReader.read(null, decoder);
        return genericRecordToJson(resultRecord);
    }
}
