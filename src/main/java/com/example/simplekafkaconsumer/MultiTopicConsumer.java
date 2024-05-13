package com.example.simplekafkaconsumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class MultiTopicConsumer {
    public static void main(String[] args) {
        var topics = List.of(args).subList(1, args.length);
        var props = new Properties();
        try (var fis = new FileInputStream(args[0])) { props.load(fis);
        } catch (Exception e) { throw new RuntimeException(e); }
        var consumer = new KafkaConsumer<>(props);
        consumer.subscribe(topics);

        try {
            while (true) {
                var records = consumer.poll(Duration.ofMillis(100));
                for (var record : records) {
                    System.out.printf("Topic: %s, Partition: %d, Offset: %d, Key: %s, Value: %s%n",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }
}
