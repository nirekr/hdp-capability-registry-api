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

import com.dell.cpsd.hdp.capability.registry.api.RegisterDataProviderMessage;
import com.dell.cpsd.hdp.capability.registry.api.UnregisterDataProviderMessage;
import com.dell.cpsd.hdp.capability.registry.api.ListDataProvidersMessage;
import com.dell.cpsd.hdp.capability.registry.api.DataProvidersFoundMessage;

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
public class CapabilityRegistryRabbitConfig
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = HDCRLoggingManager.getLogger(CapabilityRegistryRabbitConfig.class);

    /*
     * The retry template information for the client.
     */
    private static final int    MAX_ATTEMPTS     = 10;
    private static final int    INITIAL_INTERVAL = 100;
    private static final double MULTIPLIER       = 2.0;
    private static final int    MAX_INTERVAL     = 50000;

    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    
    
    /**
     * The name of the capability profile registry request exchange
     */
    public static final String EXCHANGE_HDP_CAPABILITY_REGISTRY_REQUEST = 
                         "exchange.dell.cpsd.hdp.capability.registry.request";
    
    /**
     * The name of the capability profile registry request exchange
     */
    public static final String EXCHANGE_HDP_CAPABILITY_REGISTRY_RESPONSE = 
                        "exchange.dell.cpsd.hdp.capability.registry.response";
    
    /*
     * The binding key to the service message response queue.
     */
    private static final String BINDING_HDP_CAPABILITY_REGISTRY_RESPONSE = 
                                 "dell.cpsd.hdp.capability.registry.response";

    /*
     * The fragment of the service message queue name.
     */
    private static final String QUEUE_HDP_CAPABILITY_REGISTRY_RESPONSE = 
                           "queue.dell.cpsd.hdp.capability.registry.response";
    
    /**
     * The name of the capability profile registry heartbeat exchange
     */
    public static final String EXCHANGE_HDP_CAPABILITY_REGISTRY_HEARTBEAT = 
                       "exchange.dell.cpsd.hdp.capability.registry.heartbeat";

    /**
     * The name of the capability profile registry event exchange
     */
    public static final String EXCHANGE_HDP_CAPABILITY_REGISTRY_EVENT = 
                           "exchange.dell.cpsd.hdp.capability.registry.event";
    
    
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

        messageClasses.add(RegisterDataProviderMessage.class);
        messageClasses.add(UnregisterDataProviderMessage.class);
        messageClasses.add(ListDataProvidersMessage.class);
        messageClasses.add(DataProvidersFoundMessage.class);

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
     * This returns the <code>TopicExchange</code> for the service request
     * messages.
     *
     * @return  The <code>TopicExchange</code> for the service requests.
     * 
     * @since   1.0
     */
    @Bean
    TopicExchange capabilityRegistryRequestExchange()
    {
        return new TopicExchange(EXCHANGE_HDP_CAPABILITY_REGISTRY_REQUEST);
    }
    
    
    /**
     * This returns the <code>TopicExchange</code> for the service response
     * messages.
     *
     * @return  The <code>TopicExchange</code> for the service responses.
     * 
     * @since   1.0
     */
    @Bean
    TopicExchange capabilityRegistryResponseExchange()
    {
        return new TopicExchange(EXCHANGE_HDP_CAPABILITY_REGISTRY_RESPONSE);
    }

    
    /**
     * This returns the <code>TopicExchange</code> for the service heartbeat
     * messages.
     *
     * @return  The <code>TopicExchange</code> for heartbeat messages.
     * 
     * @since   1.0
     */
    @Bean
    TopicExchange capabilityRegistryHeartbeatExchange()
    {
        return new TopicExchange(EXCHANGE_HDP_CAPABILITY_REGISTRY_HEARTBEAT);
    }
    
    
    /**
     * This returns the <code>Queue</code> for response messages from the
     * service.
     *
     * @return  The <code>Queue</code> for service response messages.
     * 
     * @since   1.0
     */
    @Bean
    Queue capabilityRegistryResponseQueue()
    {
        final String bindingPostFix = this.getResponseQueuePostfix();

        final StringBuilder builder = new StringBuilder();

        builder.append(QUEUE_HDP_CAPABILITY_REGISTRY_RESPONSE);
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
     * This returns the <code>Binding</code> for the service response message
     * queue and exchange.
     *
     * @return  The <code>Binding</code> for the service response message queue.
     * 
     * @since   1.0
     */
    @Bean
    public Binding capabiltyRegistryResponseQueueBinding()
    {
        final String bindingPostFix = this.getResponseQueuePostfix();

        final StringBuilder builder = new StringBuilder();

        builder.append(BINDING_HDP_CAPABILITY_REGISTRY_RESPONSE);
        builder.append(".");
        builder.append(bindingPostFix);

        final String binding = builder.toString();

        Object[] lparams = {binding};
        LOGGER.info(HDCRMessageCode.SERVICE_RESPONSE_BINDING_I.getMessageCode(), lparams);

        return BindingBuilder.bind(capabilityRegistryResponseQueue()).
                to(capabilityRegistryResponseExchange()).
                with(binding);
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
