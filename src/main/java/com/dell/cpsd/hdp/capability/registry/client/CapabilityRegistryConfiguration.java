/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dell.cpsd.hdp.capability.registry.client.amqp.config.CapabilityRegistryRabbitConfig;
import com.dell.cpsd.hdp.capability.registry.client.amqp.config.CapabilityRegistryConsumerConfig;
import com.dell.cpsd.hdp.capability.registry.client.amqp.config.CapabilityRegistryProducerConfig;

import com.dell.cpsd.hdp.capability.registry.client.amqp.consumer.IAmqpCapabilityRegistryConsumer;

import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.IAmqpCapabilityRegistryProducer;

/**
 * This is the configuration for the service client.
 *
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 * 
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class CapabilityRegistryConfiguration implements ICapabilityRegistryConfiguration
{
    /*
     * The <code>IAmqpCapabilityRegistryConsumer</code> to use.
     */
    private IAmqpCapabilityRegistryConsumer capabilityRegistryConsumer = null;
    
    
    /*
     * The <code>IAmqpCapabilityRegistryProducer</code> to use.
     */
    private IAmqpCapabilityRegistryProducer capabilityRegistryProducer = null;

    
    /**
     * CapabilityRegistryConfiguration constructor.
     * 
     * @param   capabilityRegistryConsumer   The capability registry consumer.
     * @param   capabilityRegistryProducer   The capability registry producer.
     * 
     * @throws  IllegalArgumentException    Thrown if the arguments are null.
     * 
     * @since   1.0
     */
    public CapabilityRegistryConfiguration(
            final IAmqpCapabilityRegistryConsumer capabilityRegistryConsumer,
            final IAmqpCapabilityRegistryProducer capabilityRegistryProducer)
    {
        super();
        
        this.setCapabilityRegistryConsumer(capabilityRegistryConsumer);
        this.setCapabilityRegistryProducer(capabilityRegistryProducer);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public IAmqpCapabilityRegistryConsumer getCapabilityRegistryConsumer()
    {
        return this.capabilityRegistryConsumer;
    }
    
    
    /**
     * This sets the <code>IAmqpCapabilityRegistryConsumer</code> implementation
     * to use.
     * 
     * @param   capabilityRegistryConsumer  The capability registry consumer.
     * 
     * @thrown  IllegalArgumentException    Thrown if the consumer is null.
     * 
     * @since   1.0
     */
    protected void setCapabilityRegistryConsumer(
              final IAmqpCapabilityRegistryConsumer capabilityRegistryConsumer)
    {
        if (capabilityRegistryConsumer == null)
        {
            throw new IllegalArgumentException(
                                    "The capability registry consumer is null");
        }
        
        this.capabilityRegistryConsumer = capabilityRegistryConsumer;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public IAmqpCapabilityRegistryProducer getCapabilityRegistryProducer()
    {
        return this.capabilityRegistryProducer;
    }
    
    
    /**
     * This sets the <code>IAmqpCapabilityRegistryProducer</code> implementation
     * to use.
     * 
     * @param   capabilityRegistryProducer  The capability registry producer.
     * 
     * @thrown  IllegalArgumentException    Thrown if the producer is null.
     * 
     * @since   1.0
     */
    protected void setCapabilityRegistryProducer(
            final IAmqpCapabilityRegistryProducer capabilityRegistryProducer)
    {
        if (capabilityRegistryProducer == null)
        {
            throw new IllegalArgumentException(
                                    "The capability registry producer is null");
        }
        
        this.capabilityRegistryProducer = capabilityRegistryProducer;
    }
}
