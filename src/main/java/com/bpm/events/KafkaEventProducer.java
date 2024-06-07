package com.bpm.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class KafkaEventProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //@Transactional
    public void produceEvent(String topic, String order) {

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, order);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Message sent: {}" + result.getRecordMetadata().offset());
            } else {
                System.out.println("Failed to send message: " + ex.getMessage());
            }
        });

    }
}
