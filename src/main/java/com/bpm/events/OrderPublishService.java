package com.bpm.events;

import com.bpm.dto.OrderDTO;
import com.bpm.model.Order;
import com.bpm.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderPublishService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderPublishService.class);

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private KafkaEventProducer kafkaEventProducer;


    public String createOrder(OrderDTO order) {

        Order newOrder = new Order();
        newOrder.setExternalId(order.getOrderId());
        newOrder.setStatus(order.getStatus());

        Order orderModel = orderRepository.save(newOrder);
        kafkaEventProducer.produceEvent("order-events",  orderModel.getId());
        return orderModel.getId();
    }

    public OrderDTO getOrder(String order) {
        return new OrderDTO();
    }

}
