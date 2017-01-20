/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client;

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
public interface ICapabilityRegistryConfiguration 
{
    /**
     * This returns the <code>IAmqpCapabilityRegistryServiceConsumer</code> for the 
     * manager.
     * 
     * @return  The <code>IAmqpCapabilityRegistryServiceConsumer</code> that is used.
     * 
     * @since   1.0
     */
    public IAmqpCapabilityRegistryServiceConsumer getCapabilityRegistryServiceConsumer();
    
    
    /**
     * This returns the <code>IAmqpCapabilityRegistryServiceProducer</code> for 
     * the manager.
     * 
     * @return  The <code>IAmqpCapabilityRegistryServiceProducer</code> that is used.
     * 
     * @since   1.0
     */
    public IAmqpCapabilityRegistryServiceProducer getCapabilityRegistryServiceProducer();
    
    
    /**
     * This returns the <code>IAmqpCapabilityRegistryControlConsumer</code> for 
     * the manager.
     * 
     * @return  The <code>IAmqpCapabilityRegistryControlConsumer</code> that is used.
     * 
     * @since   1.0
     */
    public IAmqpCapabilityRegistryControlConsumer getCapabilityRegistryControlConsumer();
}
