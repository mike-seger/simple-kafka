package com.example.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("dev")
public class KafkaTestConfig {
    @Bean
    @Profile("dev")
    EmbeddedKafkaBroker broker(TestConfiguration tc) {
        Map<String, String> properties = new HashMap<>();
        properties.put("listeners", tc.listeners);
        properties.put("advertised.listeners", tc.advertisedListeners);
        properties.put("listener.security.protocol.map", tc.listenerSecurityProtocolMap);
        return new EmbeddedKafkaZKBroker(1)
            .kafkaPorts(9092)
            .brokerProperties(properties)
            .brokerListProperty("spring.kafka.bootstrap-servers");
    }
}

@Configuration
@ConfigurationProperties("test")
@Data
class TestConfiguration {
    String topic1;
    String topic2;
    String listeners;
    String advertisedListeners;
    String listenerSecurityProtocolMap;
}
