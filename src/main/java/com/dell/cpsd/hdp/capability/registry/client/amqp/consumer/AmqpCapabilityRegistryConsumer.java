/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.consumer;

import com.dell.cpsd.common.logging.ILogger;

import com.dell.cpsd.common.rabbitmq.consumer.UnhandledMessageConsumer;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;
import com.dell.cpsd.hdp.capability.registry.client.log.HDCRMessageCode;


/**
 * This is the message consumer that handles responses from the service.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class AmqpCapabilityRegistryConsumer extends UnhandledMessageConsumer implements IAmqpCapabilityRegistryConsumer
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = HDCRLoggingManager.getLogger(AmqpCapabilityRegistryConsumer.class);

    /*
     * The <code>IAmqpCapabilityRegistryMessageHandler</code> that handles messages.
     */
    private IAmqpCapabilityRegistryMessageHandler messageHandler = null;

    
    /**
     * AmqpCapabilityRegistryConsumer constructor
     *
     * @since   1.0
     */
    public AmqpCapabilityRegistryConsumer()
    {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public IAmqpCapabilityRegistryMessageHandler getMessageHandler()
    {
        return this.messageHandler;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setMessageHandler(final IAmqpCapabilityRegistryMessageHandler messageHandler)
    {
        if (messageHandler == null)
        {
            throw new IllegalArgumentException("The message handler is null.");
        }

        this.messageHandler = messageHandler;
    }
}
