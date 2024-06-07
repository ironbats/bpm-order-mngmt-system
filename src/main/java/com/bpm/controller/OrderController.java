package com.bpm.controller;

import com.bpm.dto.OrderDTO;
import com.bpm.events.OrderPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderPublishService orderPublishService;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO order) {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(UUID.randomUUID().toString());
        orderDTO.setStatus(order.getStatus());
        String orderId = orderPublishService.createOrder(orderDTO);
        return ResponseEntity.ok("Order Created : " + orderId);
    }

    private String randomStatus() {
        final String[] STATUS = new String[]{"CHECK", "SUSPECT", "APPROVED"};
        Random random = new Random();
        int index = random.nextInt(STATUS.length);
        return STATUS[index];
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String orderId) {
        OrderDTO orderDTO = orderPublishService.getOrder(orderId);
        return ResponseEntity.ok(orderDTO);
    }

}
