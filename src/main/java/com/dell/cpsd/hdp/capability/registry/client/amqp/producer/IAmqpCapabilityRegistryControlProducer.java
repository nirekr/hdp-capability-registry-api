/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.producer;

import java.util.Date;
import java.util.List;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;

import com.dell.cpsd.hdp.capability.registry.api.DataProvider;

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
     * This publishes a pong message to the capability registry heartbeat 
     * exchange.
     *
     * @param   correlationId   The message correlation identifier.
     * @param   replyTo         The reply to destination.
     * @param   dataProvider    The HAL data provider information.
     * 
     * @throws  CapabilityRegistryException Thrown if it fails to send.
     * 
     * @since   1.0
     */
    public void publishDataProviderPong(final String correlationId,
            final String replyTo, final DataProvider dataProvider)
        throws CapabilityRegistryException;

}
