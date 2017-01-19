/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.producer;

import java.util.Date;
import java.util.List;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;

import com.dell.cpsd.hdp.capability.registry.api.ProviderIdentity;
import com.dell.cpsd.hdp.capability.registry.api.ProviderCapability;

/**
 * This interface should be implemented by a producer of service request
 * messages
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public interface IAmqpCapabilityRegistryProducer
{
    /**
     * This publishes a message to register a HAL data provider.
     *
     * @param   correlationId   The message correlation identifier.
     * @param   identity        The HAL data provider identity.
     * @param   capabilities    The list of capabilities to register.
     * 
     * @throws  CapabilityRegistryException Thrown if the registration fails.
     * 
     * @since   1.0
     */
    public void publishRegisterDataProvider(final String correlationId,
            final ProviderIdentity identity, 
            final List<ProviderCapability> capabilities)
        throws CapabilityRegistryException;

    
    /**
     * This publishes a message to unregister a HAL data provider.
     *
     * @param   correlationId   The message correlation identifier.
     * @param   identity        The HAL data provider identity.
     * 
     * @throws  CapabilityRegistryException Thrown if the registration fails.
     * 
     * @since   1.0
     */
    public void publishUnregisterDataProvider(final String correlationId,
            final ProviderIdentity identity)
        throws CapabilityRegistryException;
    
    
    /**
     * This publishes a message to get the list of HAL data providers.
     *
     * @param   correlationId   The message correlation identifier.
     * @param   replyTo         The reply to destination
     * 
     * @throws  CapabilityRegistryException Thrown if the request fails to send.
     * 
     * @since   1.0
     */
    public void publishListDataProviders(final String correlationId,
            final String replyTo)
        throws CapabilityRegistryException;

}
