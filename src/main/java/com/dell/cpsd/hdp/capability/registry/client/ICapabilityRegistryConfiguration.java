/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client;

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
public interface ICapabilityRegistryConfiguration 
{
    /**
     * This returns the <code>IAmqpCapabilityRegistryConsumer</code> for the 
     * manager.
     * 
     * @return  The <code>IAmqpCapabilityRegistryConsumer</code> that is used.
     * 
     * @since   1.0
     */
    public IAmqpCapabilityRegistryConsumer getCapabilityRegistryConsumer();
    
    
    /**
     * This returns the <code>IAmqpCapabilityRegistryProducer</code> for the 
     * manager.
     * 
     * @return  The <code>IAmqpCapabilityRegistryProducer</code> that is used.
     * 
     * @since   1.0
     */
    public IAmqpCapabilityRegistryProducer getCapabilityRegistryProducer();
}
