package com.net128.tool.generic.avro.client;

import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AvroUtils {
    private SchemaRegistryService schemaRegistryService;

    public static GenericRecord jsonToGenericRecord(Schema schema, String jsonData) throws IOException {
        var reader = new GenericDatumReader<GenericRecord>(schema);
        var decoder = DecoderFactory.get().jsonDecoder(schema, jsonData);
        return reader.read(null, decoder);
    }

    public byte[] serializeAvroRecordGeneric(String schemaName, String jsonData ) throws Exception {
        var schema = schemaRegistryService.getSchema(schemaName);
        var record = jsonToGenericRecord(schema, jsonData);
        var outputStream = new ByteArrayOutputStream();
        var datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        var dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(schema, outputStream);
        dataFileWriter.append(record);
        dataFileWriter.close();
        return outputStream.toByteArray();
    }

    public GenericRecord deserializeAvroRecordGeneric(String schemaName, byte[] data) throws Exception {
        var schema = schemaRegistryService.getSchema(schemaName); // Assuming you have a schema registry service
        var inputStream = new ByteArrayInputStream(data);
        var datumReader = new GenericDatumReader<GenericRecord>(schema);
        var decoder = DecoderFactory.get().binaryDecoder(inputStream, null);
        return datumReader.read(null, decoder); // Read the data using the decoder
    }
}
