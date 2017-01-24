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

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.dell.cpsd.hdp.capability.registry.api.CapabilityProvider;
import com.dell.cpsd.hdp.capability.registry.api.Identity;
import com.dell.cpsd.hdp.capability.registry.api.CapabilityProviderPongMessage;
import com.dell.cpsd.hdp.capability.registry.api.RegisterCapabilityProviderMessage;
import com.dell.cpsd.hdp.capability.registry.api.UnregisterCapabilityProviderMessage;

import com.dell.cpsd.common.rabbitmq.processor.PropertiesPostProcessor;

import com.dell.cpsd.common.rabbitmq.message.DefaultMessageProperties;

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
public class AmqpCapabilityRegistryControlProducer implements IAmqpCapabilityRegistryControlProducer
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = 
            HDCRLoggingManager.getLogger(AmqpCapabilityRegistryControlProducer.class);

    /*
     * The RabbitMQ template used by the producer.
     */
    private RabbitTemplate rabbitTemplate;

    /*
     * The service registration <code>Exchange</code>.
     */
    private Exchange exchange;

    /*
     * The host name of the client.
     */
    private String hostname = null;

    /*
     * The <code>Calendar</code> used by this producer.
     */
    private Calendar calendar = null;

    
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
        super();

        this.calendar = Calendar.getInstance();

        this.setRabbitTemplate(rabbitTemplate);

        this.setExchange(exchange);

        this.setHostname(hostname);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void publishRegisterCapabilityProvider(final String correlationId,
            final CapabilityProvider capabilityProvider)
        throws CapabilityRegistryException
    {
        final DefaultMessageProperties messageProperties = 
                new DefaultMessageProperties(this.calendar.getTime(), correlationId);
       
        final RegisterCapabilityProviderMessage message = new RegisterCapabilityProviderMessage(
                this.getHostname(), capabilityProvider);

        final PropertiesPostProcessor messagePostProcessor = 
                                new PropertiesPostProcessor(messageProperties);

        if (LOGGER.isDebugEnabled())
        {
            StringBuilder builder = new StringBuilder();

            builder.append(" publishRegisterCapabilityProvider : ");
            builder.append("exchange [").append(this.exchange.getName());
            builder.append("], message [").append(message).append("]");

            LOGGER.debug(builder.toString());
        }

        try
        {
            rabbitTemplate.convertAndSend(this.exchange.getName(), 
                    "", 
                    message, 
                    messagePostProcessor);
        }
        catch (Exception exception)
        {
            Object[] lparams = {message, exception.getMessage()};
            String lmessage = LOGGER.error(HDCRMessageCode.PRODUCER_PUBLISH_E.getMessageCode(), lparams, exception);

            throw new CapabilityRegistryException(lmessage, exception);
        }
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishUnregisterCapabilityProvider(final String correlationId,
            final Identity identity)
        throws CapabilityRegistryException
    {
        final DefaultMessageProperties messageProperties = 
                new DefaultMessageProperties(this.calendar.getTime(), correlationId);
        
        final UnregisterCapabilityProviderMessage message = new UnregisterCapabilityProviderMessage(
                this.getHostname(), identity);

        final PropertiesPostProcessor messagePostProcessor = 
                                new PropertiesPostProcessor(messageProperties);
        
        if (LOGGER.isDebugEnabled())
        {
            StringBuilder builder = new StringBuilder();

            builder.append(" publishUnregisterCapabilityProvider : ");
            builder.append("exchange [").append(this.exchange.getName());
            builder.append("], message [").append(message).append("]");

            LOGGER.debug(builder.toString());
        }

        try
        {
            rabbitTemplate.convertAndSend(this.exchange.getName(), 
                    "", 
                    message,
                    messagePostProcessor);
        }
        catch (Exception exception)
        {
            Object[] lparams = {message, exception.getMessage()};
            String lmessage = LOGGER.error(HDCRMessageCode.PRODUCER_PUBLISH_E.getMessageCode(), lparams, exception);

            throw new CapabilityRegistryException(lmessage, exception);
        }
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishCapabilityProviderPong(final String correlationId,
                final CapabilityProvider capabilityProvider)
        throws CapabilityRegistryException
    {
        final DefaultMessageProperties messageProperties = 
                new DefaultMessageProperties(this.calendar.getTime(), correlationId);
        
        final CapabilityProviderPongMessage message = new CapabilityProviderPongMessage(
                this.getHostname(), capabilityProvider);

        final PropertiesPostProcessor messagePostProcessor = 
                                new PropertiesPostProcessor(messageProperties);

        if (LOGGER.isDebugEnabled())
        {
            StringBuilder builder = new StringBuilder();

            builder.append(" publishCapabilityProviderPong : ");
            builder.append("exchange [").append(this.exchange.getName());
            builder.append("], message [").append(message).append("]");

            LOGGER.debug(builder.toString());
        }

        try
        {
            rabbitTemplate.convertAndSend(this.exchange.getName(), 
                    "", 
                    message, 
                    messagePostProcessor);
        }
        catch (Exception exception)
        {
            Object[] lparams = {message, exception.getMessage()};
            String lmessage = LOGGER.error(HDCRMessageCode.PRODUCER_PUBLISH_E.getMessageCode(), lparams, exception);

            throw new CapabilityRegistryException(lmessage, exception);
        }
    }
    
    
    /**
     * This returns the exchange <code>Exchange</code> for this producer.
     *
     * @return  The exchange <code>Exchange</code> for this producer.
     * 
     * @since   1.0
     */
    public Exchange getExchange()
    {
        return this.exchange;
    }
    

    /**
     * This sets the registration <code>Exchange</code> for this producer.
     *
     * @param   exchange    The registration <code>Exchange</code>.
     * 
     * @throws  IllegalArgumentException Thrown if the exchange is null.
     * 
     * @since   1.0
     */
    public void setExchange(final Exchange exchange)
    {
        if (exchange == null)
        {
            throw new IllegalArgumentException(
                                "The capability registration exchange is not set.");
        }

        this.exchange = exchange;
    }
    

    /**
     * This returns the <code>RabbitTemplate</code> for this producer.
     *
     * @return  The <code>RabbitTemplate</code> for this producer.
     * 
     * @since   1.0
     */
    public RabbitTemplate getRabbitTemplate()
    {
        return this.rabbitTemplate;
    }

    
    /**
     * This sets the <code>RabbitTemplate</code> for this producer.
     *
     * @param   rabbitTemplate  The <code>RabbitTemplate</code> for this producer.
     * 
     * @throws  IllegalArgumentException    Thrown if the template is null.
     * 
     * @since   1.0
     */
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate)
    {
        if (rabbitTemplate == null)
        {
            throw new IllegalArgumentException("The rabbit template is not set.");
        }

        this.rabbitTemplate = rabbitTemplate;
    }
    

    /**
     * This returns the host name of the client.
     *
     * @return  The host name of the client.
     * 
     * @since   1.0
     */
    public String getHostname()
    {
        return this.hostname;
    }
    

    /**
     * This sets the host name of the client.
     *
     * @param   hostname    The host name of the client.
     * 
     * @throws  IllegalArgumentException    Thrown if the host name is null.
     * 
     * @since   1.0
     */
    public void setHostname(String hostname)
    {
        if (hostname == null)
        {
            throw new IllegalArgumentException("The host name is not set.");
        }

        this.hostname = hostname;
    }
}
