/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.config;

import com.dell.cpsd.common.logging.ILogger;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;
import com.dell.cpsd.hdp.capability.registry.client.log.HDCRMessageCode;

import org.springframework.amqp.core.Exchange;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.dell.cpsd.hdp.capability.registry.client.amqp.AmqpCapabilityRegistrationManager;

import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistrationManager;

import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.IAmqpCapabilityRegistryControlProducer;

import com.dell.cpsd.hdp.capability.registry.client.amqp.consumer.IAmqpCapabilityRegistryControlConsumer;

/**
 * The configuration for the capability registration manager.
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
@Import({CapabilityRegistryControlRabbitConfig.class, CapabilityRegistryControlProducerConfig.class, 
            CapabilityRegistryControlConsumerConfig.class})
public class CapabilityRegistrationManagerConfig
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = 
            HDCRLoggingManager.getLogger(CapabilityRegistrationManagerConfig.class);


    /*
     * The capability registry producer.
     */
    @Autowired
    private IAmqpCapabilityRegistryControlProducer capabilityRegistryControlProducer;
 
    /*
     * The capability registry control consumer.
     */
    @Autowired
    private IAmqpCapabilityRegistryControlConsumer capabilityRegistryControlConsumer;
    
    
    /**
     * This returns the capability registration manager.
     *
     * @return  The capability registration manager.
     * 
     * @since   1.0
     */
    @Bean
    @Qualifier("capabilityRegistrationManager")
    ICapabilityRegistrationManager capabilityRegistrationManager()
    {
        return new AmqpCapabilityRegistrationManager(capabilityRegistryControlConsumer,
                capabilityRegistryControlProducer);
    }
}
