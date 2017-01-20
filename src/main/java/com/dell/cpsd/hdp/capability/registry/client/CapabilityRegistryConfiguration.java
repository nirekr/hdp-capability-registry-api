/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dell.cpsd.hdp.capability.registry.client.amqp.config.CapabilityRegistryRabbitConfig;
import com.dell.cpsd.hdp.capability.registry.client.amqp.config.CapabilityRegistryConsumerConfig;
import com.dell.cpsd.hdp.capability.registry.client.amqp.config.CapabilityRegistryProducerConfig;

import com.dell.cpsd.hdp.capability.registry.client.amqp.consumer.IAmqpCapabilityRegistryServiceConsumer;
import com.dell.cpsd.hdp.capability.registry.client.amqp.consumer.IAmqpCapabilityRegistryControlConsumer;

import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.IAmqpCapabilityRegistryServiceProducer;

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
     * The <code>IAmqpCapabilityRegistryServiceConsumer</code> to use.
     */
    private IAmqpCapabilityRegistryServiceConsumer capabilityRegistryServiceConsumer = null;
    
    /*
     * The <code>IAmqpCapabilityRegistryServiceProducer</code> to use.
     */
    private IAmqpCapabilityRegistryServiceProducer capabilityRegistryServiceProducer = null;
    
    /*
     * The <code>IAmqpCapabilityRegistryControlConsumer</code> to use.
     */
    private IAmqpCapabilityRegistryControlConsumer capabilityRegistryControlConsumer = null;

    
    /**
     * CapabilityRegistryConfiguration constructor.
     * 
     * @param   capabilityRegistryServiceConsumer   The capability registry service consumer.
     * @param   capabilityRegistryServiceProducer   The capability registry service producer.
     * @param   capabilityRegistryControlConsumer   The capability registry control consumer.
     * 
     * @throws  IllegalArgumentException    Thrown if the arguments are null.
     * 
     * @since   1.0
     */
    public CapabilityRegistryConfiguration(
            final IAmqpCapabilityRegistryServiceConsumer capabilityRegistryServiceConsumer,
            final IAmqpCapabilityRegistryServiceProducer capabilityRegistryServiceProducer,
            final IAmqpCapabilityRegistryControlConsumer capabilityRegistryControlConsumer)
    {
        super();
        
        this.setCapabilityRegistryServiceConsumer(capabilityRegistryServiceConsumer);
        this.setCapabilityRegistryServiceProducer(capabilityRegistryServiceProducer);
        
        this.setCapabilityRegistryControlConsumer(capabilityRegistryControlConsumer);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public IAmqpCapabilityRegistryServiceConsumer getCapabilityRegistryServiceConsumer()
    {
        return this.capabilityRegistryServiceConsumer;
    }
    
    
    /**
     * This sets the <code>IAmqpCapabilityRegistryServiceConsumer</code> 
     * implementation to use.
     * 
     * @param   capabilityRegistryServiceConsumer   The capability registry consumer.
     * 
     * @thrown  IllegalArgumentException    Thrown if the consumer is null.
     * 
     * @since   1.0
     */
    protected void setCapabilityRegistryServiceConsumer(
            final IAmqpCapabilityRegistryServiceConsumer capabilityRegistryServiceConsumer)
    {
        if (capabilityRegistryServiceConsumer == null)
        {
            throw new IllegalArgumentException(
                                    "The capability registry consumer is null");
        }
        
        this.capabilityRegistryServiceConsumer = capabilityRegistryServiceConsumer;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public IAmqpCapabilityRegistryServiceProducer getCapabilityRegistryServiceProducer()
    {
        return this.capabilityRegistryServiceProducer;
    }
    
    
    /**
     * This sets the <code>IAmqpCapabilityRegistryServiceProducer</code> 
     * implementation to use.
     * 
     * @param   capabilityRegistryServiceProducer   The capability registry producer.
     * 
     * @thrown  IllegalArgumentException    Thrown if the producer is null.
     * 
     * @since   1.0
     */
    protected void setCapabilityRegistryServiceProducer(
            final IAmqpCapabilityRegistryServiceProducer capabilityRegistryServiceProducer)
    {
        if (capabilityRegistryServiceProducer == null)
        {
            throw new IllegalArgumentException(
                            "The capability registry service producer is null");
        }
        
        this.capabilityRegistryServiceProducer = capabilityRegistryServiceProducer;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public IAmqpCapabilityRegistryControlConsumer getCapabilityRegistryControlConsumer()
    {
        return this.capabilityRegistryControlConsumer;
    }
    
    
    /**
     * This sets the <code>IAmqpCapabilityRegistryControlConsumer</code> 
     * implementation to use.
     * 
     * @param   capabilityRegistryControlConsumer   The capability registry control consumer.
     * 
     * @thrown  IllegalArgumentException    Thrown if the producer is null.
     * 
     * @since   1.0
     */
    protected void setCapabilityRegistryControlConsumer(
            final IAmqpCapabilityRegistryControlConsumer capabilityRegistryControlConsumer)
    {
        if (capabilityRegistryControlConsumer == null)
        {
            throw new IllegalArgumentException(
                            "The capability registry constrol consumer is null");
        }
        
        this.capabilityRegistryControlConsumer = capabilityRegistryControlConsumer;
    }
}
