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

import com.dell.cpsd.hdp.capability.registry.api.DataProvider;
import com.dell.cpsd.hdp.capability.registry.api.ProviderIdentity;
import com.dell.cpsd.hdp.capability.registry.api.DataProviderPongMessage;
import com.dell.cpsd.hdp.capability.registry.api.RegisterDataProviderMessage;
import com.dell.cpsd.hdp.capability.registry.api.UnregisterDataProviderMessage;

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
     * The routing for the capability profile registry message queue
     */
    private static final String ROUTING_HDP_CAPABILITY_REGISTRY_REQUEST = 
                                "dell.cpsd.hdp.capability.registry.request";
    
    /*
     * The RabbitMQ template used by the producer.
     */
    private RabbitTemplate rabbitTemplate;

    /*
     * The service request <code>Exchange</code>.
     */
    private Exchange capabilityRegistryRequestExchange;
    
    /*
     * The service heartbeat <code>Exchange</code>.
     */
    private Exchange capabilityRegistryHeartbeatExchange;

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
     * @param   capabilityRegistryRequestExchange   The service request exchange.
     * @param   capabilityRegistryHeartbeatExchange The service heartbeat exchange.
     * @param   hostname        The host name of the client.
     * 
     * @throws  IllegalArgumentException Thrown if the parameters are null.
     * 
     * @since   1.0
     */
    public AmqpCapabilityRegistryControlProducer(final RabbitTemplate rabbitTemplate, 
            final Exchange capabilityRegistryRequestExchange, 
            final Exchange capabilityRegistryHeartbeatExchange, final String hostname)
    {
        super();

        this.calendar = Calendar.getInstance();

        this.setRabbitTemplate(rabbitTemplate);

        this.setCapabilityRegistryRequestExchange(capabilityRegistryRequestExchange);
        this.setCapabilityRegistryHeartbeatExchange(capabilityRegistryHeartbeatExchange);

        this.setHostname(hostname);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void publishRegisterDataProvider(final String correlationId,
            final DataProvider dataProvider)
        throws CapabilityRegistryException
    {
        final DefaultMessageProperties messageProperties = 
                new DefaultMessageProperties(this.calendar.getTime(), correlationId);
       
        final RegisterDataProviderMessage message = new RegisterDataProviderMessage(
                this.getHostname(), dataProvider);

        final PropertiesPostProcessor messagePostProcessor = 
                                new PropertiesPostProcessor(messageProperties);

        if (LOGGER.isDebugEnabled())
        {
            StringBuilder builder = new StringBuilder();

            builder.append(" publishRegisterDataProvider : ");
            builder.append("exchange [").append(this.capabilityRegistryRequestExchange.getName());
            builder.append("], message [").append(message).append("]");

            LOGGER.debug(builder.toString());
        }

        try
        {
            rabbitTemplate.convertAndSend(this.capabilityRegistryRequestExchange.getName(), 
                    ROUTING_HDP_CAPABILITY_REGISTRY_REQUEST, message, messagePostProcessor);
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
    public void publishUnregisterDataProvider(final String correlationId,
            final ProviderIdentity identity)
        throws CapabilityRegistryException
    {
        final DefaultMessageProperties messageProperties = 
                new DefaultMessageProperties(this.calendar.getTime(), correlationId);
        
        final UnregisterDataProviderMessage message = new UnregisterDataProviderMessage(
                this.getHostname(), identity);

        final PropertiesPostProcessor messagePostProcessor = 
                                new PropertiesPostProcessor(messageProperties);
        
        if (LOGGER.isDebugEnabled())
        {
            StringBuilder builder = new StringBuilder();

            builder.append(" publishUnregisterDataProvider : ");
            builder.append("exchange [").append(this.capabilityRegistryRequestExchange.getName());
            builder.append("], message [").append(message).append("]");

            LOGGER.debug(builder.toString());
        }

        try
        {
            rabbitTemplate.convertAndSend(this.capabilityRegistryRequestExchange.getName(), 
                    ROUTING_HDP_CAPABILITY_REGISTRY_REQUEST, message, messagePostProcessor);
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
    public void publishDataProviderPong(final String correlationId,
            final String replyTo, final DataProvider dataProvider)
        throws CapabilityRegistryException
    {
        final DefaultMessageProperties messageProperties = 
                new DefaultMessageProperties(this.calendar.getTime(), correlationId);
        
        final DataProviderPongMessage message = new DataProviderPongMessage(
                this.getHostname(), dataProvider);

        final PropertiesPostProcessor messagePostProcessor = 
                                new PropertiesPostProcessor(messageProperties);

        if (LOGGER.isDebugEnabled())
        {
            StringBuilder builder = new StringBuilder();

            builder.append(" publishDataProviderPong : ");
            builder.append("exchange [").append(this.capabilityRegistryHeartbeatExchange.getName());
            builder.append("], message [").append(message).append("]");

            LOGGER.debug(builder.toString());
        }

        try
        {
            rabbitTemplate.convertAndSend(this.capabilityRegistryHeartbeatExchange.getName(), 
                    replyTo, message, messagePostProcessor);
        }
        catch (Exception exception)
        {
            Object[] lparams = {message, exception.getMessage()};
            String lmessage = LOGGER.error(HDCRMessageCode.PRODUCER_PUBLISH_E.getMessageCode(), lparams, exception);

            throw new CapabilityRegistryException(lmessage, exception);
        }
    }


    /**
     * This returns the service request <code>Exchange</code> for this producer.
     *
     * @return  The service request <code>Exchange</code> for this producer.
     * 
     * @since   1.0
     */
    public Exchange getCapabilityRegistryRequestExchange()
    {
        return this.capabilityRegistryRequestExchange;
    }
    

    /**
     * This sets the service request <code>Exchange</code> for this producer.
     *
     * @param   capabilityRegistryRequestExchange    The <code>Exchange</code>.
     * 
     * @throws  IllegalArgumentException Thrown if the exchange is null.
     * 
     * @since   1.0
     */
    public void setCapabilityRegistryRequestExchange(
                        final Exchange capabilityRegistryRequestExchange)
    {
        if (capabilityRegistryRequestExchange == null)
        {
            throw new IllegalArgumentException(
                                 "The service request exchange is not set.");
        }

        this.capabilityRegistryRequestExchange = capabilityRegistryRequestExchange;
    }
    
    
    /**
     * This returns the <code>Exchange</code> for this producer.
     *
     * @return  The <code>Exchange</code> for this producer.
     * 
     * @since   1.0
     */
    public Exchange getCapabilityRegistryHeartbeatExchange()
    {
        return this.capabilityRegistryHeartbeatExchange;
    }
    

    /**
     * This sets the service heartbeat <code>Exchange</code> for this producer.
     *
     * @param   capabilityRegistryHeartbeatExchange The <code>Exchange</code>.
     * 
     * @throws  IllegalArgumentException Thrown if the exchange is null.
     * 
     * @since   1.0
     */
    public void setCapabilityRegistryHeartbeatExchange(
                        final Exchange capabilityRegistryHeartbeatExchange)
    {
        if (capabilityRegistryHeartbeatExchange == null)
        {
            throw new IllegalArgumentException(
                                "The service heartbeat exchange is not set.");
        }

        this.capabilityRegistryHeartbeatExchange = capabilityRegistryHeartbeatExchange;
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
