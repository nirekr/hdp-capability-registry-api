/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.producer;

import java.util.Date;
import java.util.List;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;

import com.dell.cpsd.hdp.capability.registry.api.CapabilityProvider;
import com.dell.cpsd.hdp.capability.registry.api.Identity;

/**
 * This interface should be implemented by a producer of service control
 * messages.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public interface IAmqpCapabilityRegistryControlProducer
{
    /**
     * This publishes a message to register a capability provider.
     *
     * @param   correlationId       The message correlation identifier.
     * @param   capabilityProvider  The capability provider information.
     * 
     * @throws  CapabilityRegistryException Thrown if the registration fails.
     * 
     * @since   1.0
     */
    public void publishRegisterCapabilityProvider(final String correlationId,
            final CapabilityProvider capabilityProvider)
        throws CapabilityRegistryException;

    
    /**
     * This publishes a message to unregister a capability provider.
     *
     * @param   correlationId   The message correlation identifier.
     * @param   identity        The capability provider identity.
     * 
     * @throws  CapabilityRegistryException Thrown if the registration fails.
     * 
     * @since   1.0
     */
    public void publishUnregisterCapabilityProvider(final String correlationId,
            final Identity identity)
        throws CapabilityRegistryException;
    
    
    /**
     * This publishes a pong message to the capability registry registration 
     * exchange.
     *
     * @param   correlationId       The message correlation identifier.
     * @param   capabilityProvider  The capability provider information.
     * 
     * @throws  CapabilityRegistryException Thrown if it fails to send.
     * 
     * @since   1.0
     */
    public void publishCapabilityProviderPong(final String correlationId,
            final CapabilityProvider capabilityProvider)
        throws CapabilityRegistryException;

}
