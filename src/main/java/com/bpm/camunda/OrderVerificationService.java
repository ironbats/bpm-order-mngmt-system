package com.bpm.camunda;

import com.bpm.model.Order;
import com.bpm.repository.OrderRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "orderVerificationService")
public class OrderVerificationService implements JavaDelegate {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;


    public OrderVerificationService() {
        LOG.info("OrderVerificationService bean created");
    }

    private static final Logger LOG = LoggerFactory.getLogger(OrderVerificationService.class);

    @Override
    public void execute(DelegateExecution log) throws Exception {

       LOG.info("DecisionFlow Called:  {}", log.getActivityInstanceId());
        final Object orderId = log.getVariable("orderId");
        Optional<Order> order = orderRepository.findById(orderId.toString());

        if (order.isPresent()) {
            if (order.get().getStatus().equals("APPROVED")) {
                log.setVariable("orderStatus", "APPROVED");

            } else if (order.get().getStatus().equals(("SUSPECT"))) {
                log.setVariable("orderStatus", "SUSPECT");
            } else {
                runtimeService.createIncident("INCIDENT_CREATE", log.getVariable("id").toString(), "Please Check de Order" + orderId);
                log.setVariable("orderStatus", "CHECK");
            }
        } else {
            throw new BpmnError("INCIDENT_CREATE", "The order doesn't exist" + orderId);

        }

    }
}
