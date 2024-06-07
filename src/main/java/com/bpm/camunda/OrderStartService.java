package com.bpm.camunda;

import com.bpm.model.Order;
import com.bpm.repository.OrderRepository;
import lombok.Setter;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.runtime.ExecutionImpl;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Setter
public class OrderStartService {


    private static final Logger LOG = LoggerFactory.getLogger(OrderStartService.class);

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderVerificationService orderVerificationService;
    @Autowired
    private DelegateExecution delegateExecution;


    public void execute(String orderId) throws Exception {

        try {

            Map<String, Object> variables = new HashMap<>();
            variables.put("OrderVerificationService", "orderVerificationService");
            variables.put("orderId", orderId);
            variables.put("problem: ","Verificar se este pedido é alguma suspeita de fraude, indicente criado");
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("order-service-v4", variables);

            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isPresent() && order.get().getStatus().equals("CHECK")) {
                variables.put("orderStatus", "CHECK");


                variables.put("id", processInstance.getId());
                variables.put("businessKey", processInstance.getBusinessKey());
                variables.put("processInstanceId", processInstance.getProcessInstanceId());

                delegateExecution.setVariables(variables);
                orderVerificationService.execute(delegateExecution);
            } else {
                List<Task> tasks = taskService.createTaskQuery().
                        taskName("orderStartService")
                        .active()
                        .list();

                if (!CollectionUtils.isEmpty(tasks)) {
                    variables.put("OrderVerificationService", "orderVerificationService");
                    variables.put("orderId", orderId);
                    for (Task task : tasks) {
                        taskService.completeWithVariablesInReturn(task.getId(), variables, false);
                    }
                }
            }

            LOG.info("Process instance started");
        } catch (Exception e) {
            LOG.error("Error starting process instance", e);

        }
    }
}




