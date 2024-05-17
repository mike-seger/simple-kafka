package com.net128.tool.generic.avro.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Profile("dev")
public class TestBroker {
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
    List<String> topics;
    String listeners;
    String advertisedListeners;
    String listenerSecurityProtocolMap;
}
