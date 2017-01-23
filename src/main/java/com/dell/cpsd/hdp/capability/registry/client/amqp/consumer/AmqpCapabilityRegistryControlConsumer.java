/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.consumer;

import com.dell.cpsd.common.logging.ILogger;

import com.dell.cpsd.common.rabbitmq.consumer.UnhandledMessageConsumer;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;
import com.dell.cpsd.hdp.capability.registry.client.log.HDCRMessageCode;

import com.dell.cpsd.hdp.capability.registry.api.DataProvider;
import com.dell.cpsd.hdp.capability.registry.api.PingDataProviderMessage;

import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;

import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.IAmqpCapabilityRegistryControlProducer;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;

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
     * The HAL data provider information
     */
    private DataProvider dataProvider = null;
    
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
    public void setDataProvider(final DataProvider dataProvider)
    {
        if (dataProvider == null)
        {
            throw new IllegalArgumentException("The data provider is null");
        }
        
        this.dataProvider = dataProvider;
    }

    
    /**
     * This handles the <code>PingDataProviderMessage</code> that is
     * consumed from the control queue.
     *
     * @param   message             The message to process.
     * @param   messageProperties   The message properties.
     * 
     * @since   1.0
     */
    public void handleMessage(final PingDataProviderMessage message,
            final MessagePropertiesContainer messageProperties)
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

        final String correlationId = messageProperties.getCorrelationId();

        try
        {
            this.capabilityRegistryControlProducer.publishDataProviderPong(
                correlationId, this.dataProvider);
            
        } catch (CapabilityRegistryException exception)
        {
            
        }
    }
}
