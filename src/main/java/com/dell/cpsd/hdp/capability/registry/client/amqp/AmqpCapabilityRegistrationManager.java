/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp;

import java.util.List;
import java.util.UUID;

import com.dell.cpsd.common.logging.ILogger;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;
import com.dell.cpsd.hdp.capability.registry.client.log.HDCRMessageCode;

import com.dell.cpsd.hdp.capability.registry.api.ProviderCapability;
import com.dell.cpsd.hdp.capability.registry.api.ProviderIdentity;
import com.dell.cpsd.hdp.capability.registry.api.DataProvider;

import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.IAmqpCapabilityRegistryControlProducer;

import com.dell.cpsd.hdp.capability.registry.client.amqp.consumer.IAmqpCapabilityRegistryControlConsumer;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistrationManager;

import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;

/**
 * This is a capability register service client.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class AmqpCapabilityRegistrationManager implements ICapabilityRegistrationManager
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = 
            HDCRLoggingManager.getLogger(AmqpCapabilityRegistrationManager.class);

    
    /*
     * The capability registry control consumer.
     */
    private IAmqpCapabilityRegistryControlConsumer  capabilityRegistryControlConsumer = null;
    
    /*
     * The capability registry control producer.
     */
    private IAmqpCapabilityRegistryControlProducer capabilityRegistryControlProducer = null;

    
    /**
     * AmqpCapabilityRegistryManager constructor.
     * 
     * @param   capabilityRegistryControlConsumer   The capability registry control consumer.
     * @param   capabilityRegistryControlProducer   The capability registry control producer.
     * 
     * @since   1.0
     */
    public AmqpCapabilityRegistrationManager(
                final IAmqpCapabilityRegistryControlConsumer capabilityRegistryControlConsumer,
                final IAmqpCapabilityRegistryControlProducer capabilityRegistryControlProducer)
    {
        super();
        
        if (capabilityRegistryControlConsumer == null)
        {
            throw new IllegalArgumentException
                        ("The capability registry control consumer is null.");
        }
        
        this.capabilityRegistryControlConsumer = capabilityRegistryControlConsumer;
        
        
        if (capabilityRegistryControlProducer == null)
        {
            throw new IllegalArgumentException
                        ("The capability registry control producer is null.");
        }
        
        this.capabilityRegistryControlProducer = capabilityRegistryControlProducer;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerDataProvider(final ProviderIdentity identity, 
                        final List<ProviderCapability> capabilities)
        throws CapabilityRegistryException
    {
        final DataProvider dataProvider = new DataProvider(identity, capabilities);
        
        // the control consumer is expected to be in the configuration
        this.capabilityRegistryControlConsumer.setDataProvider(dataProvider);
        
        final String correlationId = UUID.randomUUID().toString();
        
        this.capabilityRegistryControlProducer.publishRegisterDataProvider(
                correlationId, dataProvider);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterDataProvider(final ProviderIdentity identity)
        throws CapabilityRegistryException
    {
        final String correlationId = UUID.randomUUID().toString();
        
        this.capabilityRegistryControlProducer.publishUnregisterDataProvider(
                correlationId, identity);
    }
}
