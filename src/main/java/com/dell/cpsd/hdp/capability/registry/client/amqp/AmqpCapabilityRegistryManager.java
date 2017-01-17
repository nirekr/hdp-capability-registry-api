/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp;

import java.util.List;
import java.util.UUID;

import com.dell.cpsd.hdp.capability.registry.api.ProviderCapability;
import com.dell.cpsd.hdp.capability.registry.api.ProviderIdentity;

import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.IAmqpCapabilityRegistryProducer;

import com.dell.cpsd.hdp.capability.registry.client.amqp.consumer.IAmqpCapabilityRegistryMessageHandler;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryManager;

/**
 * This is the base exception for the service client.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class AmqpCapabilityRegistryManager implements IAmqpCapabilityRegistryMessageHandler, ICapabilityRegistryManager
{
    /*
     * The capability registry message producer.
     */
    private IAmqpCapabilityRegistryProducer capabilityRegistryProducer;
    
    
    /**
     * AmqpCapabilityRegistryManager constructor.
     * 
     * @param   capabilityRegistryProducer  The capability registry producer.
     * 
     * @since   1.0
     */
    public AmqpCapabilityRegistryManager(
            final IAmqpCapabilityRegistryProducer capabilityRegistryProducer)
    {
        super();
        
        if (capabilityRegistryProducer == null)
        {
            throw new IllegalArgumentException("The capability registry producer is null.");
        }
        
        this.capabilityRegistryProducer = capabilityRegistryProducer;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerDataProvider(final ProviderIdentity identity, 
                        final List<ProviderCapability> capabilities)
        throws CapabilityRegistryException
    {
        final String correlationId = UUID.randomUUID().toString();
        
        this.capabilityRegistryProducer.publishRegisterDataProvider(
                correlationId, identity, capabilities);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterDataProvider(final ProviderIdentity identity)
        throws CapabilityRegistryException
    {
        final String correlationId = UUID.randomUUID().toString();
        
        this.capabilityRegistryProducer.publishUnregisterDataProvider(
                correlationId, identity);
    }
}
