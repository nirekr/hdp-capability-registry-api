/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.consumer;

import com.dell.cpsd.common.logging.ILogger;

import org.springframework.amqp.core.MessageProperties;

import com.dell.cpsd.common.rabbitmq.consumer.UnhandledMessageConsumer;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;
import com.dell.cpsd.hdp.capability.registry.client.log.HDCRMessageCode;

import com.dell.cpsd.hdp.capability.registry.api.CapabilityProvider;
import com.dell.cpsd.hdp.capability.registry.api.PingCapabilityProviderMessage;

import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.IAmqpCapabilityRegistryControlProducer;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;

import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesHelper;

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
public class AmqpCapabilityRegistryControlConsumer extends UnhandledMessageConsumer
                            implements IAmqpCapabilityRegistryControlConsumer
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = 
            HDCRLoggingManager.getLogger(AmqpCapabilityRegistryControlConsumer.class);
    
    /*
     * The reply to destination for the consumer.
     */
    private String replyTo = null;
    
    /*
     * The capability provider information
     */
    private CapabilityProvider capabilityProvider = null;
    
    /*
     * The capability registry control producer
     */
    private IAmqpCapabilityRegistryControlProducer capabilityRegistryControlProducer = null;
    
    
    /**
     * AmqpCapabilityRegistryControlConsumer constructor
     * 
     * @param   replyTo                             The reply to destination.
     * @param   capabilityRegistryControlProducer   The control message producer.
     * 
     * @throws  IllegalArgumentException    Thrown if the parameters are null.
     *
     * @since   1.0
     */
    public AmqpCapabilityRegistryControlConsumer(final String replyTo,
            final IAmqpCapabilityRegistryControlProducer capabilityRegistryControlProducer)
    {
        super();

        if (replyTo == null)
        {
            throw new IllegalArgumentException("The reply to value is not set.");
        }

        this.replyTo = replyTo;
        
        if (capabilityRegistryControlProducer == null)
        {
            throw new IllegalArgumentException(
                        "The capability registry control producer is not set.");
        }
        
        this.capabilityRegistryControlProducer = capabilityRegistryControlProducer;
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
    public void setCapabilityProvider(final CapabilityProvider capabilityProvider)
    {
        if (capabilityProvider == null)
        {
            throw new IllegalArgumentException("The capability provider is null");
        }
        
        this.capabilityProvider = capabilityProvider;
    }

    
    /**
     * This handles the <code>PingCapabilityProviderMessage</code> that is
     * consumed from the control queue.
     *
     * @param   message             The message to process.
     * @param   messageProperties   The message properties.
     * 
     * @since   1.0
     */
    public void handleMessage(final PingCapabilityProviderMessage message,
            final MessageProperties messageProperties)
        throws CapabilityRegistryException
    {
        if (message == null)
        {
            LOGGER.warn(HDCRMessageCode.NULL_MESSAGE_W.getMessageCode());
            return;
        }

        if (LOGGER.isDebugEnabled())
        {
            LOGGER.debug(" handleMessage : " + message);
        }
        

        final String correlationId =
                MessagePropertiesHelper.getCorrelationId(messageProperties);

        try
        {
            this.capabilityRegistryControlProducer.publishCapabilityProviderPong(
                correlationId, this.capabilityProvider);
            
        } catch (CapabilityRegistryException exception)
        {
            LOGGER.error(exception.getMessage(), exception);
        }
    }
}
