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

import com.dell.cpsd.hdp.capability.registry.client.amqp.AmqpCapabilityRegistryManager;

import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryManager;

import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.IAmqpCapabilityRegistryServiceProducer;

import com.dell.cpsd.hdp.capability.registry.client.amqp.consumer.IAmqpCapabilityRegistryServiceConsumer;
import com.dell.cpsd.hdp.capability.registry.client.amqp.consumer.IAmqpCapabilityRegistryControlConsumer;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryConfiguration;

/**
 * The configuration for the capability registry manager.
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
@Import({CapabilityRegistryRabbitConfig.class, CapabilityRegistryProducerConfig.class, 
            CapabilityRegistryConsumerConfig.class})
public class CapabilityRegistryManagerConfig
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = HDCRLoggingManager.getLogger(CapabilityRegistryManagerConfig.class);


    /*
     * The capability registry producer.
     */
    @Autowired
    private IAmqpCapabilityRegistryServiceProducer capabilityRegistryServiceProducer;
    
    /*
     * The capability registry service consumer.
     */
    @Autowired
    private IAmqpCapabilityRegistryServiceConsumer capabilityRegistryServiceConsumer;
    
    
    /*
     * The capability registry control consumer.
     */
    @Autowired
    private IAmqpCapabilityRegistryControlConsumer capabilityRegistryControlConsumer;
    
    
    /**
     * This returns the capability registry manager.
     *
     * @return  The capability registry manager.
     * 
     * @since   1.0
     */
    @Bean
    @Qualifier("capabilityRegistryManager")
    ICapabilityRegistryManager capabilityRegistryManager()
    {
        final CapabilityRegistryConfiguration configuration = new CapabilityRegistryConfiguration(
                capabilityRegistryServiceConsumer, capabilityRegistryServiceProducer,
                capabilityRegistryControlConsumer);
        
        return new AmqpCapabilityRegistryManager(configuration);
    }
}
