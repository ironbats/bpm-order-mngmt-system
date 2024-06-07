package com.bpm;

import com.bpm.dto.OrderDTO;
import com.bpm.events.OrderPublishService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Random;
import java.util.UUID;


@SpringBootApplication(scanBasePackages = {"com.bpm", "com.bpm.model"})
@EnableJpaRepositories
public class Application {


    @Autowired
    private OrderPublishService orderPublishService;

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void startProcessBpm() {
        for (int i = 0; i < 10000; i++) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setOrderId(UUID.randomUUID().toString());
            orderDTO.setStatus(randomStatus());
            orderPublishService.createOrder(orderDTO);
        }
    }

    private String randomStatus() {
        final String[] STATUS = new String[]{"CHECK", "SUSPECT", "APPROVED"};
        Random random = new Random();
        int index = random.nextInt(STATUS.length);
        return STATUS[index];
    }

}