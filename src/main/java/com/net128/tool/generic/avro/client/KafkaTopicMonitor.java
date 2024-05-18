package com.net128.tool.generic.avro.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

//@Component
public class KafkaTopicMonitor {

    @Autowired
    private KafkaListenerService kafkaListenerService;

    private AdminClient adminClient;

    public KafkaTopicMonitor(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        this.adminClient = AdminClient.create(config);
    }

    @Scheduled(fixedRate = 1000)  // Check every 5 seconds
    public void checkForNewTopics() throws ExecutionException, InterruptedException {
        Set<String> currentTopics = adminClient.listTopics().names().get();
        kafkaListenerService.updateTopics(currentTopics);
    }
}
