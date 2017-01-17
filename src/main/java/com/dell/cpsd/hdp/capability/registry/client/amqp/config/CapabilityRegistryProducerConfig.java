/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.config;

import com.dell.cpsd.common.logging.ILogger;

import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.AmqpCapabilityRegistryProducer;
import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.IAmqpCapabilityRegistryProducer;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;
import com.dell.cpsd.hdp.capability.registry.client.log.HDCRMessageCode;

import org.springframework.amqp.core.Exchange;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration for the service message producer.
 * 
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
public class CapabilityRegistryProducerConfig
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = HDCRLoggingManager.getLogger(CapabilityRegistryProducerConfig.class);

    /*
     * The Spring RabbitMQ template.
     */
    @Autowired
    private RabbitTemplate capabilityRegistryRabbitTemplate;
   
    /*
     * The host name of the client.
     */
    @Autowired
    @Qualifier("hostName")
    private String hostname;

    /*
     * The service request exchange.
     */
    @Autowired
    @Qualifier("capabilityRegistryRequestExchange")
    private Exchange capabilityRegistryRequestExchange;
    

    /**
     * This returns the service message producer that publishes to the
     * exchange.
     *
     * @return  The service message producer.
     * 
     * @since   1.0
     */
    @Bean
    IAmqpCapabilityRegistryProducer capabilityRegistryProducer()
    {
        return new AmqpCapabilityRegistryProducer(
                capabilityRegistryRabbitTemplate, capabilityRegistryRequestExchange, hostname);
    }
}
