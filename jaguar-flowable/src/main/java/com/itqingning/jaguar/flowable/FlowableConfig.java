package com.itqingning.jaguar.flowable;

import org.flowable.engine.*;
import org.flowable.spring.ProcessEngineFactoryBean;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by lvws on 2019/4/18.
 */
@Configuration
@PropertySource("classpath:flowable.properties")
public class FlowableConfig {

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            DataSource dataSource, PlatformTransactionManager transactionManager) {
        SpringProcessEngineConfiguration engineConfiguration = new SpringProcessEngineConfiguration();
        engineConfiguration.setDataSource(dataSource);
        engineConfiguration.setTransactionManager(transactionManager);
        engineConfiguration.setDatabaseSchemaUpdate("true");
        engineConfiguration.setAsyncExecutorActivate(false);
        return engineConfiguration;
    }

    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean(SpringProcessEngineConfiguration engineConfiguration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(engineConfiguration);
        return processEngineFactoryBean;
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngineFactoryBean processEngine) throws Exception {
        return processEngine.getObject().getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngineFactoryBean processEngine) throws Exception {
        return processEngine.getObject().getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngineFactoryBean processEngine) throws Exception {
        return processEngine.getObject().getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngineFactoryBean processEngine) throws Exception {
        return processEngine.getObject().getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngineFactoryBean processEngine) throws Exception {
        return processEngine.getObject().getManagementService();
    }

}
