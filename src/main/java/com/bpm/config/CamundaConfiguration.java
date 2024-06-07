package com.bpm.config;

import com.bpm.camunda.OrderVerificationService;
import com.bpm.camunda.OrderStartService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.TaskServiceImpl;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.pvm.runtime.ExecutionImpl;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringExpressionManager;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;


@Configuration
@ComponentScan(basePackages = {"com.bpm.service"})
public class CamundaConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SpringProcessEngineConfiguration engineConfiguration(
            DataSource source, PlatformTransactionManager transactionManager,
            @Value("classpath*:*.bpmn") Resource[] deploymentResources) {

        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setProcessEngineName("engine");
        configuration.setDataSource(source);
        configuration.setTransactionManager(transactionManager);
        configuration.setDatabaseSchemaUpdate("true");
        configuration.setJobExecutorActivate(true);
        configuration.setDeploymentResources(deploymentResources);

        ExpressionManager expressionManager = new SpringExpressionManager(applicationContext, null);
        configuration.setExpressionManager(expressionManager);

        return configuration;

    }

    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean(SpringProcessEngineConfiguration configuration) {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(configuration);
        return factoryBean;
    }

    @Bean
    public ProcessEngine processEngine(ProcessEngineFactoryBean factoryBean) throws Exception {
        return factoryBean.getObject();
    }

    @Bean
    public OrderStartService start(RuntimeService runtimeService, TaskService taskService, DelegateExecution delegateExecution) throws Exception {
        OrderStartService orderStartService = new OrderStartService();
        orderStartService.setRuntimeService(runtimeService);
        orderStartService.setTaskService(taskService);
        orderStartService.setDelegateExecution(delegateExecution);
        return orderStartService;
    }

    @Bean
    public DelegateExecution delegateExecution(){
        return new ExecutionImpl();
    }


    @Bean
    public TaskService taskService(){
        return new TaskServiceImpl();
    }

    @Bean
    public OrderVerificationService orderVerificationService() {
        return new OrderVerificationService();
    }

}
