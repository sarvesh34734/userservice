package dev.sarvesh.userservice.clients;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@AllArgsConstructor
public class KafkaProducerConfig {

    private KafkaTemplate<String,String> kafkaTemplate;

    public void send(String topicName,String msg){
        kafkaTemplate.send(topicName, msg);
    }
}
