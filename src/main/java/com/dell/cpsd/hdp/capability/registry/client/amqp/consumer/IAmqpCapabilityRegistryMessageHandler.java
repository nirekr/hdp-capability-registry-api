/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.consumer;

import com.dell.cpsd.hdp.capability.registry.api.DataProvidersFoundMessage;

import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;

/**
 * This interface should be implemented by a class that handles the processing
 * of service response and error messages.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public interface IAmqpCapabilityRegistryMessageHandler
{
    /**
     * This handles a <code>DataProvidersFoundMessage</code> message.
     *
     * @param   message             The message to process.
     * @param   messageProperties   The message properties.
     * 
     * @since   1.0
     */
    public void handleDataProvidersFound(final DataProvidersFoundMessage message,
            final MessagePropertiesContainer messageProperties);
}
