package com.bpm;

import com.bpm.camunda.OrderVerificationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private OrderVerificationService orderVerificationService;


    @Test
    public void testOrderServiceCalled() {
        boolean beanPresent = applicationContext.containsBean("orderVerificationService");
        Assert.assertTrue("OrderVerificationService bean should be present", beanPresent);
    }

    @Test
    public void testOrderVerificationService() throws Exception{
        DelegateExecution execution = new ExecutionEntity();
        orderVerificationService.execute(execution);

    }

}
