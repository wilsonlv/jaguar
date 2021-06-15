package org.jaguar.commons.activemq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

/**
 * @author lvws
 * @since 2020/5/30
 */
@Configuration
public class ActivemqConfig {

    @Bean
    public JmsTemplate jmsSessionQueneTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(false);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setDeliveryPersistent(true);
        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsQueneTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(false);
        return jmsTemplate;
    }

    @Bean
    public JmsTemplate jmsTopicTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }

    @Bean
    public JmsListenerContainerFactory<?> sessionQueueListener(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        factory.setSessionTransacted(true);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> queueListener(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(false);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> topicListener(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }

}
