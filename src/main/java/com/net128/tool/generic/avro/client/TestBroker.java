package com.net128.tool.generic.avro.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("dev")
public class TestBroker {
//    @Bean
//    @Profile("dev")
//    EmbeddedKafkaBroker broker(TestBrokerConfiguration tc) {
//        Map<String, String> properties = new HashMap<>();
//        properties.put("listeners", tc.listeners);
//        properties.put("advertised.listeners", tc.advertisedListeners);
//        properties.put("listener.security.protocol.map", tc.listenerSecurityProtocolMap);
//        return new EmbeddedKafkaZKBroker(1)
//            .kafkaPorts(9092)
//            .brokerProperties(properties)
//            .brokerListProperty("spring.kafka.bootstrap-servers");
//    }

    @Bean
    @Profile("dev")
    public EmbeddedKafkaBroker broker(TestBrokerConfiguration tc, Environment env) {
        int kafkaPort = 9092;
        if (PortChecker.arePortsAvailable(tc.listeners)) {
            Map<String, String> properties = new HashMap<>();
            properties.put("listeners", tc.listeners);
            properties.put("advertised.listeners", tc.advertisedListeners);
            properties.put("listener.security.protocol.map", tc.listenerSecurityProtocolMap);
            properties.put("num.partitions", "1")
            ;

            return new EmbeddedKafkaZKBroker(1)
                    .kafkaPorts(kafkaPort)
                    .brokerProperties(properties)
                    .brokerListProperty("spring.kafka.bootstrap-servers");
        } else {
            System.out.println("Port " + kafkaPort + " is already in use. Using external Kafka broker.");
            System.setProperty("spring.kafka.bootstrap-servers", "localhost:" + kafkaPort);
            return null;
        }
    }
}

@Configuration
@ConfigurationProperties("test")
@Data
class TestBrokerConfiguration {
    String listeners;
    String advertisedListeners;
    String listenerSecurityProtocolMap;
}
