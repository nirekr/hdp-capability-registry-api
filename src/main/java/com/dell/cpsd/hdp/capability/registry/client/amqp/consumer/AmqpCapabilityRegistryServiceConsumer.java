/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.consumer;

import com.dell.cpsd.common.logging.ILogger;

import com.dell.cpsd.common.rabbitmq.consumer.UnhandledMessageConsumer;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;
import com.dell.cpsd.hdp.capability.registry.client.log.HDCRMessageCode;

import com.dell.cpsd.hdp.capability.registry.api.CapabilityProvidersFoundMessage;

import org.springframework.amqp.core.MessageProperties;

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
public class AmqpCapabilityRegistryServiceConsumer extends UnhandledMessageConsumer 
                            implements IAmqpCapabilityRegistryServiceConsumer
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = 
            HDCRLoggingManager.getLogger(AmqpCapabilityRegistryServiceConsumer.class);

    /*
     * The <code>IAmqpCapabilityRegistryMessageHandler</code> that handles messages.
     */
    private IAmqpCapabilityRegistryMessageHandler messageHandler = null;
    
    /*
     * The reply to destination
     */
    private String replyTo = null;

    
    /**
     * AmqpCapabilityRegistryServiceConsumer constructor
     * 
     * @param   replyTo The reply to destination
     *
     * @since   1.0
     */
    public AmqpCapabilityRegistryServiceConsumer(final String replyTo)
    {
        super();

        if (replyTo == null)
        {
            throw new IllegalArgumentException("The reply to value is not set.");
        }

        this.replyTo = replyTo;
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReplyTo()
    {
        return this.replyTo;
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
    
    
    /**
     * This handles the <code>CapabilityProvidersFoundMessage</code> that is
     * consumed from the service queue.
     *
     * @param   message             The message to process.
     * @param   messageProperties   The message properties.
     * 
     * @since   1.0
     */
    public void handleMessage(final CapabilityProvidersFoundMessage message,
            final MessageProperties messageProperties)
    {
        if (message == null)
        {
            LOGGER.warn(HDCRMessageCode.NULL_MESSAGE_W.getMessageCode());
            return;
        }

        if (this.messageHandler == null)
        {
            LOGGER.warn(HDCRMessageCode.SERVICE_HANDLER_NULL_W.getMessageCode());
            return;
        }

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(" handleMessage : " + message);
        }

        if (this.messageHandler != null)
        {
            this.messageHandler.handleCapabilityProvidersFound(message, messageProperties);
        }
    }
}
