package com.bpm.events;

import com.bpm.camunda.OrderStartService;
import com.bpm.model.Order;
import com.bpm.repository.OrderRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KafkaEventConsumer {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private OrderStartService orderStartService;

    private static final Logger LOG = LoggerFactory.getLogger(OrderPublishService.class);

    @KafkaListener(topics = "order-events")
    public void consumerOrderEvent(String orderId) throws Exception {

        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            LOG.info("Starting process instance with key 'order-service-v4'");
            orderStartService.execute(orderId);

        } else {
            LOG.info("Nothing to Start for order:{}", orderId);
            throw new BpmnError("ORDER_NOT_FOUND: " + orderId);
        }

    }
}
