/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.producer;

import java.util.Date;
import java.util.List;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;

import com.dell.cpsd.hdp.capability.registry.api.DataProvider;
import com.dell.cpsd.hdp.capability.registry.api.ProviderIdentity;

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
     * This publishes a message to register a HAL data provider.
     *
     * @param   correlationId   The message correlation identifier.
     * @param   dataProvider    The HAL data provider information.
     * 
     * @throws  CapabilityRegistryException Thrown if the registration fails.
     * 
     * @since   1.0
     */
    public void publishRegisterDataProvider(final String correlationId,
            final DataProvider dataProvider)
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
     * This publishes a pong message to the capability registry registration 
     * exchange.
     *
     * @param   correlationId   The message correlation identifier.
     * @param   dataProvider    The HAL data provider information.
     * 
     * @throws  CapabilityRegistryException Thrown if it fails to send.
     * 
     * @since   1.0
     */
    public void publishDataProviderPong(final String correlationId,
            final DataProvider dataProvider)
        throws CapabilityRegistryException;

}
