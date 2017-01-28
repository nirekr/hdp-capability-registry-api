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

import com.dell.cpsd.hdp.capability.registry.api.Capability;
import com.dell.cpsd.hdp.capability.registry.api.Identity;
import com.dell.cpsd.hdp.capability.registry.api.CapabilityProvider;
import com.dell.cpsd.hdp.capability.registry.api.ListCapabilityProvidersMessage;

import com.dell.cpsd.common.rabbitmq.producer.AbstractAmqpMessageProducer;

/**
 * This is the message producer that sends messages to the service.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class AmqpCapabilityRegistryServiceProducer extends AbstractAmqpMessageProducer 
                        implements IAmqpCapabilityRegistryServiceProducer
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = 
            HDCRLoggingManager.getLogger(AmqpCapabilityRegistryServiceProducer.class);
    
    /*
     * The routing for the capability profile registry message queue
     */
    private static final String ROUTING_HDP_CAPABILITY_REGISTRY_REQUEST = 
                                "dell.cpsd.hdp.capability.registry.request";
    
    /*
     * The name of the exchange bean.
     */
    private static final String EXCHANGE_CAPABILITY_REGISTRY_REQUEST =
                                        "capabilityRegistryRequestExchange";

    /**
     * AmqpCapabilityRegistryServiceProducer constructor.
     *
     * @param   rabbitTemplate  The RabbitMQ template.
     * @param   exchange        The service request exchange.
     * @param   hostname        The host name of the client.
     * 
     * @throws  IllegalArgumentException Thrown if the parameters are null.
     * 
     * @since   1.0
     */
    public AmqpCapabilityRegistryServiceProducer(final RabbitTemplate rabbitTemplate, 
            final Exchange exchange, final String hostname)
    {
        super(rabbitTemplate, hostname);
        
        this.addExchange(EXCHANGE_CAPABILITY_REGISTRY_REQUEST, exchange);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void publishListCapabilityProviders(final String correlationId,
            final String replyTo)
        throws CapabilityRegistryException
    {
        final ListCapabilityProvidersMessage message = 
                   new ListCapabilityProvidersMessage(this.getHostname());
        
        this.sendMessage(correlationId, replyTo, 
                            EXCHANGE_CAPABILITY_REGISTRY_REQUEST,
                            ROUTING_HDP_CAPABILITY_REGISTRY_REQUEST, message);
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
