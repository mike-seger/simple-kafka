package com.net128.tool.generic.avro.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.GenericData;

import java.util.Random;
import java.util.UUID;

public class AvroRandomDataGenerator {
    private static final Random random = new Random();
    public static String generateRandomJsonFromSchema(Schema schema, String idFieldName) throws JsonProcessingException {
        var record = new GenericData.Record(schema);

        for (Field field : schema.getFields()) {
            Object value = getRandomValueForType(field.schema(), field.name(), field.name().equals(idFieldName));
            record.put(field.name(), value);
        }

        return record.toString();
    }

    private static Object getRandomValueForType(Schema schema, String fieldName, boolean isIdField) {
        return switch (schema.getType()) {
            case STRING, FIXED ->
                padTrimIfFixed(schema, isIdField ? UUID.randomUUID().toString() :
                    padStringWithRandomDigits(fieldName, fieldName.length()+8));
            case INT -> Math.abs(random.nextInt());
            case LONG -> Math.abs(random.nextLong());
            case DOUBLE -> Math.abs(random.nextDouble());
            case BOOLEAN -> random.nextBoolean();
            default -> throw new IllegalArgumentException("Unsupported Avro type: " + schema.getType());
        };
    }

    private static String padTrimIfFixed(Schema schema, String value) {
        if(schema.getType() != Schema.Type.FIXED) return value;
        var length = schema.getFixedSize();
        if(value.length()==length) return value;
        if(value.length()>length) return value.substring(value.length()-length);
        return padStringWithRandomDigits(value, length);
    }

    public static String padStringWithRandomDigits(String input, int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(input);
        int additionalLength = length - sb.length(); // Calculate how many more digits are needed
        while (additionalLength > 0) {
            long randomNumber = Math.abs(random.nextLong());
            String randomDigits = Long.toString(randomNumber);
            if (randomDigits.length() > additionalLength) {
                sb.append(randomDigits, 0, additionalLength);
            } else {
                sb.append(randomDigits);
            }
            additionalLength -= randomDigits.length();
        }
        return sb.toString();
    }
}
