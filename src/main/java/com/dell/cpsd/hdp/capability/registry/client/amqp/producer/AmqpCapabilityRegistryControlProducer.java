/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.producer;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.dell.cpsd.common.logging.ILogger;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;
import com.dell.cpsd.hdp.capability.registry.client.log.HDCRMessageCode;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.MessageProperties;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.dell.cpsd.hdp.capability.registry.api.CapabilityProvider;
import com.dell.cpsd.hdp.capability.registry.api.Identity;
import com.dell.cpsd.hdp.capability.registry.api.CapabilityProviderPongMessage;
import com.dell.cpsd.hdp.capability.registry.api.RegisterCapabilityProviderMessage;
import com.dell.cpsd.hdp.capability.registry.api.UnregisterCapabilityProviderMessage;

import com.dell.cpsd.common.rabbitmq.producer.AbstractAmqpMessageProducer;

/**
 * This is the message producer that sends control messages to the service.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class AmqpCapabilityRegistryControlProducer extends AbstractAmqpMessageProducer 
                        implements IAmqpCapabilityRegistryControlProducer
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = 
            HDCRLoggingManager.getLogger(AmqpCapabilityRegistryControlProducer.class);
    
    /*
     * The name of the exchange bean.
     */
    private static final String EXCHANGE_CAPABILITY_REGISTRY_REGISTRATION =
                                        "capabilityRegistryRegistrationExchange";

    
    /**
     * AmqpCapabilityRegistryControlProducer constructor.
     *
     * @param   rabbitTemplate  The RabbitMQ template.
     * @param   exchange        The service registration exchange.
     * @param   hostname        The host name of the client.
     * 
     * @throws  IllegalArgumentException Thrown if the parameters are null.
     * 
     * @since   1.0
     */
    public AmqpCapabilityRegistryControlProducer(final RabbitTemplate rabbitTemplate, 
            final Exchange exchange, final String hostname)
    {
        super(rabbitTemplate, hostname);

        this.addExchange(EXCHANGE_CAPABILITY_REGISTRY_REGISTRATION, exchange);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void publishRegisterCapabilityProvider(final String correlationId,
            final CapabilityProvider capabilityProvider)
        throws CapabilityRegistryException
    {
        final RegisterCapabilityProviderMessage message = 
                        new RegisterCapabilityProviderMessage(
                                this.getHostname(), capabilityProvider);

        this.sendMessage(correlationId, null, 
                EXCHANGE_CAPABILITY_REGISTRY_REGISTRATION, "", message);
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishUnregisterCapabilityProvider(final String correlationId,
            final Identity identity)
        throws CapabilityRegistryException
    {
        final UnregisterCapabilityProviderMessage message = 
                        new UnregisterCapabilityProviderMessage(
                                            this.getHostname(), identity);

        this.sendMessage(correlationId, null, 
                EXCHANGE_CAPABILITY_REGISTRY_REGISTRATION, "", message);
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishCapabilityProviderPong(final String correlationId,
                final CapabilityProvider capabilityProvider)
        throws CapabilityRegistryException
    {
        // if there is no capability provider registered then don't publish
        if (capabilityProvider == null)
        {
            LOGGER.warn(HDCRMessageCode.NO_CAPABILITY_PROVIDER_W.getMessageCode());
            return;
        }
        
        final CapabilityProviderPongMessage message = 
                        new CapabilityProviderPongMessage(
                                this.getHostname(), capabilityProvider);

        this.sendMessage(correlationId, null, 
                EXCHANGE_CAPABILITY_REGISTRY_REGISTRATION, "", message);
    }
        
        
    /**
     * This publishes a message to a specified exchange.
     * 
     * @param   correlationId   The correlation identifier.
     * @param   replyTo         The reply to destination.
     * @param   exchangeName    The exchange to publish on.
     * @param   routingKey      The routing key.
     * @param   message         The message to publish.
     * 
     * throws   CapabilityRegistryException   Thrown if the message fails to publish.
     * 
     * @since   1.0
     */
    protected void sendMessage(final String correlationId, final String replyTo,
            final String exchangeName, final String routingKey, final Object message)
        throws CapabilityRegistryException
    {
        try
        {
            this.publishMessage(
                    correlationId, replyTo, exchangeName, routingKey, message);
        } 
        catch (Exception exception)
        {
            final Object[] logParams = {message, exception.getMessage()};
            final String logMessage = LOGGER.error(
                                HDCRMessageCode.PRODUCER_PUBLISH_E.getMessageCode(), 
                                logParams, exception);

            throw new CapabilityRegistryException(logMessage, exception);
        }        
    }
}
