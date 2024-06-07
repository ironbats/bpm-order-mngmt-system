package com.bpm.camunda;

import com.bpm.model.Order;
import com.bpm.repository.OrderRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("orderIncident")
public class OrderIncident implements JavaDelegate {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        final String orderId = delegateExecution.getVariable("orderId").toString();
        final String orderStatus = delegateExecution.getVariable("orderStatus").toString();
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent() && "CHECK".equals(orderStatus)) {
            order.get().setActionOrderStatus("WAITING_FOR_APPROVE");
            orderRepository.save(order.get());
        }

    }
}
