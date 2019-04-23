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
@PropertySource("flowable.properties")
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
    public RepositoryService repositoryService(ProcessEngine processEngine) throws Exception {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) throws Exception {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) throws Exception {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) throws Exception {
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) throws Exception {
        return processEngine.getManagementService();
    }

}
