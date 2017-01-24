/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.config;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessor;
import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessorCallback;
import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;
import com.dell.cpsd.hdp.capability.registry.client.log.HDCRMessageCode;

import com.dell.cpsd.service.common.client.context.IConsumerContextConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dell.cpsd.hdp.capability.registry.api.RegisterCapabilityProviderMessage;
import com.dell.cpsd.hdp.capability.registry.api.UnregisterCapabilityProviderMessage;
import com.dell.cpsd.hdp.capability.registry.api.PingCapabilityProviderMessage;
import com.dell.cpsd.hdp.capability.registry.api.CapabilityProviderPongMessage;

/**
 * This is the configuration for the RabbitMQ artifacts used by a client.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
@Configuration
public class CapabilityRegistryControlRabbitConfig
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = 
            HDCRLoggingManager.getLogger(CapabilityRegistryControlRabbitConfig.class);

    /*
     * The retry template information for the client.
     */
    private static final int    MAX_ATTEMPTS     = 10;
    private static final int    INITIAL_INTERVAL = 100;
    private static final double MULTIPLIER       = 2.0;
    private static final int    MAX_INTERVAL     = 50000;

    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    
    /**
     * The name of the capability profile registry registration exchange
     */
    public static final String EXCHANGE_HDP_CAPABILITY_REGISTRY_REGISTRATION = 
                       "exchange.dell.cpsd.hdp.capability.registry.registration";
    

    /*
     * The binding key to the service message control queue.
     */
    private static final String BINDING_HDP_CAPABILITY_REGISTRY_CONTROL = 
                                  "dell.cpsd.hdp.capability.registry.control";

    /*
     * The fragment of the service control queue name.
     */
    private static final String QUEUE_HDP_CAPABILITY_REGISTRY_CONTROL = 
                            "queue.dell.cpsd.hdp.capability.registry.control";
    
    /*
     * The name of the capability registry control exchange
     */
    private static final String EXCHANGE_HDP_CAPABILITY_REGISTRY_CONTROL = 
                         "exchange.dell.cpsd.hdp.capability.registry.control";
    
    
    /*
     * The RabbitMQ connection factory.
     */
    @Autowired
    @Qualifier("rabbitConnectionFactory")
    private ConnectionFactory rabbitConnectionFactory;

    /*
     * The configuration properties for the client.
     */
    @Autowired
    @Qualifier("rabbitPropertiesConfig")
    private IRabbitMqPropertiesConfig propertiesConfig;
    
    /*
     * The configuration properties for the client.
     */
    @Autowired
    private IConsumerContextConfig consumerContextConfig;

    /*
     * The environment properties.
     */
    @Autowired
    private Environment environment;
    

    /**
     * This returns the RabbitMQ template used by the producer.
     *
     * @return  The RabbitMQ the rabbit template used by the producer.
     * 
     * @since   1.0
     */
    @Bean
    RabbitTemplate capabilityRegistryRabbitTemplate()
    {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setMessageConverter(capabilityRegistryMessageConverter());
        template.setRetryTemplate(capabiltyRegistryRetryTemplate());
        return template;
    }
    

    /**
     * This returns the <code>RetryTemplate</code> for the <code>RabbitTemplate
     * </code>.
     *
     * @return  The <code>RetryTemplate</code>.
     * 
     * @since   1.0
     */
    @Bean
    RetryTemplate capabiltyRegistryRetryTemplate()
    {
        RetryTemplate retryTemplate = new RetryTemplate();

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(INITIAL_INTERVAL);
        backOffPolicy.setMultiplier(MULTIPLIER);
        backOffPolicy.setMaxInterval(MAX_INTERVAL);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_ATTEMPTS);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    
    /**
     * This returns the <code>MessageConverter</code> for the
     * <code>RabbitTemplate</code>.
     *
     * @return  The <code>MessageConverter</code> for the template.
     * 
     * @since   1.0
     */
    @Bean
    public MessageConverter capabilityRegistryMessageConverter()
    {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setClassMapper(capabilityRegistryClassMapper());
        messageConverter.setCreateMessageIds(true);

        final ObjectMapper objectMapper = new ObjectMapper();

        // use ISO8601 format for dates
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat(ISO8601_DATE_FORMAT));

        // ignore properties we don't need or aren't expecting
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        messageConverter.setJsonObjectMapper(objectMapper);

        return messageConverter;
    }
    

    /**
     * This returns the <code>ClassMapper</code> for the message converter.
     *
     * @return  The <ocde>ClassMapper</code> for the message converter.
     * 
     * @since   1.0
     */
    @Bean
    ClassMapper capabilityRegistryClassMapper()
    {
        //stub
        final DefaultClassMapper classMapper = new DefaultClassMapper();
        final Map<String, Class<?>> classMappings = new HashMap<>();

        List<Class<?>> messageClasses = new ArrayList<Class<?>>();

        messageClasses.add(RegisterCapabilityProviderMessage.class);
        messageClasses.add(UnregisterCapabilityProviderMessage.class);
        messageClasses.add(PingCapabilityProviderMessage.class);
        messageClasses.add(CapabilityProviderPongMessage.class);

        final MessageAnnotationProcessor messageAnnotationProcessor = new MessageAnnotationProcessor();

        messageAnnotationProcessor.process(new MessageAnnotationProcessorCallback()
        {
            @Override
            public void found(String messageType, Class messageClass)
            {
                classMappings.put(messageType, messageClass);
            }
        }, messageClasses);

        classMapper.setIdClassMapping(classMappings);

        return classMapper;
    }
    

    /**
     * This returns the host name for the client.
     *
     * @return  The host name for the client.
     * 
     * @since   1.0
     */
    @Bean
    String hostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
            throw new RuntimeException("Unable to identify hostname", e);
        }
    }

    
    /**
     * This returns the <code>FanoutExchange</code> for the service registration
     * messages.
     *
     * @return  The <code>FanoutExchange</code> for registration messages.
     * 
     * @since   1.0
     */
    @Bean
    FanoutExchange capabilityRegistryRegistrationExchange()
    {
        return new FanoutExchange(EXCHANGE_HDP_CAPABILITY_REGISTRY_REGISTRATION);
    }
    
    
    /**
     * This returns the <code>FanoutExchange</code> for the service control
     * messages.
     *
     * @return  The <code>FanoutExchange</code> for control messages.
     * 
     * @since   1.0
     */
    @Bean
    FanoutExchange capabilityRegistryControlExchange()
    {
        return new FanoutExchange(EXCHANGE_HDP_CAPABILITY_REGISTRY_CONTROL);
    }
    
    
    /**
     * This returns the <code>Queue</code> for control messages from the
     * service.
     *
     * @return  The <code>Queue</code> for service control messages.
     * 
     * @since   1.0
     */
    @Bean
    Queue capabilityRegistryControlQueue()
    {
        final String bindingPostFix = this.getResponseQueuePostfix();

        final StringBuilder builder = new StringBuilder();

        builder.append(QUEUE_HDP_CAPABILITY_REGISTRY_CONTROL);
        builder.append(".");
        builder.append(bindingPostFix);

        final String queueName = builder.toString();

        Object[] lparams = {queueName};
        LOGGER.info(HDCRMessageCode.SERVICE_RESPONSE_QUEUE_I.getMessageCode(), lparams);
        
        boolean stateful = this.consumerContextConfig.stateful();

        final Queue queue = new Queue(queueName, true, false, !stateful);

        return queue;
    }

    
    /**
     * This returns the <code>Binding</code> for the service control message
     * queue and exchange.
     *
     * @return  The <code>Binding</code> for the service control message queue.
     * 
     * @since   1.0
     */
    @Bean
    public Binding capabilityRegistryControlQueueBinding()
    {
        return BindingBuilder.bind(capabilityRegistryControlQueue()).
                to(capabilityRegistryControlExchange());
    }
    

    /**
     * This returns the <code>AmqpAdmin</code> for the connection factory.
     *
     * @return  The AMQP admin object for the connection factory.
     * 
     * @since   1.0
     */
    @Bean
    AmqpAdmin amqpAdmin()
    {
        return new RabbitAdmin(rabbitConnectionFactory);
    }
    
    
    /*
     * This returns the generated postfix that is appended to the service
     * response message queue and exchange binding.
     * 
     * @return  The generated postfix.
     * 
     * @since   1.0
     */
    private String getResponseQueuePostfix()
    {
        return this.consumerContextConfig.consumerUuid();
    }
}