/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp;

import java.util.List;
import java.util.UUID;

import com.dell.cpsd.common.logging.ILogger;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;
import com.dell.cpsd.hdp.capability.registry.client.log.HDCRMessageCode;

import com.dell.cpsd.hdp.capability.registry.api.ProviderCapability;
import com.dell.cpsd.hdp.capability.registry.api.ProviderIdentity;
import com.dell.cpsd.hdp.capability.registry.api.DataProvider;
import com.dell.cpsd.hdp.capability.registry.api.DataProvidersFoundMessage;

import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.IAmqpCapabilityRegistryServiceProducer;

import com.dell.cpsd.hdp.capability.registry.client.amqp.consumer.IAmqpCapabilityRegistryServiceConsumer;
import com.dell.cpsd.hdp.capability.registry.client.amqp.consumer.IAmqpCapabilityRegistryMessageHandler;
import com.dell.cpsd.hdp.capability.registry.client.amqp.consumer.IAmqpCapabilityRegistryControlConsumer;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryManager;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryConfiguration;

import com.dell.cpsd.service.common.client.callback.IServiceCallback;
import com.dell.cpsd.service.common.client.callback.ServiceCallback;
import com.dell.cpsd.service.common.client.callback.ServiceError;
import com.dell.cpsd.service.common.client.callback.ServiceTimeout;

import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;

import com.dell.cpsd.service.common.client.task.ServiceTask;

import com.dell.cpsd.service.common.client.manager.AbstractServiceCallbackManager;

import com.dell.cpsd.hdp.capability.registry.client.callback.ListDataProvidersResponse;

import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;

/**
 * This is a capability register service client.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class AmqpCapabilityRegistryManager extends AbstractServiceCallbackManager 
        implements IAmqpCapabilityRegistryMessageHandler, ICapabilityRegistryManager
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = 
            HDCRLoggingManager.getLogger(AmqpCapabilityRegistryManager.class);
    
    /*
     * The configuration used by this service manager
     */
    private ICapabilityRegistryConfiguration configuration              = null;

    /*
     * The capability registry message producer.
     */
    private IAmqpCapabilityRegistryServiceProducer  capabilityRegistryServiceProducer = null;
    
    /*
     * The capability registry service consumer.
     */
    private IAmqpCapabilityRegistryServiceConsumer  capabilityRegistryServiceConsumer = null;

    
    /**
     * AmqpCapabilityRegistryManager constructor.
     * 
     * @param   configuration   The capability registry configuration.
     * 
     * @since   1.0
     */
    public AmqpCapabilityRegistryManager(
                        final ICapabilityRegistryConfiguration configuration)
    {
        super();
        
        if (configuration == null)
        {
            throw new IllegalArgumentException("The configuration is not set.");
        }
        
        this.configuration = configuration;
       
        this.capabilityRegistryServiceProducer = 
                     this.configuration.getCapabilityRegistryServiceProducer();
        
        this.capabilityRegistryServiceConsumer =
                     this.configuration.getCapabilityRegistryServiceConsumer();
        
        this.capabilityRegistryServiceConsumer.setMessageHandler(this);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerDataProvider(final ProviderIdentity identity, 
                        final List<ProviderCapability> capabilities)
        throws CapabilityRegistryException
    {
        // set the provider identity on the control consumer
        final IAmqpCapabilityRegistryControlConsumer capabilityRegistryControlConsumer =
                this.configuration.getCapabilityRegistryControlConsumer();
        
        final DataProvider dataProvider = new DataProvider(identity, capabilities);
        
        // the control consumer is expected to be in the configuration
        capabilityRegistryControlConsumer.setDataProvider(dataProvider);
        
        final String correlationId = UUID.randomUUID().toString();
        
        this.capabilityRegistryServiceProducer.publishRegisterDataProvider(
                correlationId, dataProvider);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterDataProvider(final ProviderIdentity identity)
        throws CapabilityRegistryException
    {
        final String correlationId = UUID.randomUUID().toString();
        
        this.capabilityRegistryServiceProducer.publishUnregisterDataProvider(
                correlationId, identity);
    }
    
    
    /**
     * This returns the service for the specified system.
     *
     * @param   timeout   The timeout in milliseconds.
     * 
     * @return  The response with the list of available data providers.
     * 
     * @throw   CapabilityRegistryException Thrown if the request fails.
     * @throw   CapabilityRegistryException Thrown if the request times out.
     * 
     * @since   1.0
     */
    public ListDataProvidersResponse listDataProviders(final long timeout) 
        throws CapabilityRegistryException, ServiceTimeoutException
    {
        if (this.isShutDown())
        {
            final String logMessage = LOGGER.error(HDCRMessageCode.MANAGER_SHUTDOWN_E.getMessageCode());
            throw new CapabilityRegistryException(logMessage);
        }

        // create a correlation identifier for the operation
        final String requestId = UUID.randomUUID().toString();

        final ServiceCallback<ListDataProvidersResponse> callback = 
                            new ServiceCallback<ListDataProvidersResponse>();

        // the infinite timeout is used for the task because it is handled with     
        // this synchronous call.
        final ServiceTask<IServiceCallback<?>> task = 
                new ServiceTask<IServiceCallback<?>>(requestId, callback, timeout);

        // add the callback using the correlation identifier as key
        this.addServiceTask(requestId, task);

        final String replyTo = this.capabilityRegistryServiceConsumer.getReplyTo();
        
        // publish the list system compliance message to the service
        try
        {
            this.capabilityRegistryServiceProducer.publishListDataProviders(requestId, replyTo);
        }
        catch (Exception exception)
        {
            // remove the callback if the message cannot be published
            this.removeServiceTask(requestId);

            Object[] logParams = {exception.getMessage()};
            String logMessage = LOGGER.error(HDCRMessageCode.PUBLISH_MESSAGE_FAIL_E.getMessageCode(), logParams, exception);

            throw new CapabilityRegistryException(logMessage, exception);
        }

        // wait from the response from the service
        this.waitForServiceCallback(callback, requestId, timeout);

        // check to see if a error has been handled by the manager
        final ServiceError error = callback.getServiceError();

        // throw a compute exception using the message in the error
        if (error != null)
        {
            throw new CapabilityRegistryException(error.getErrorMessage());
        }

        // if there was no error, then return the response
        return callback.getServiceResponse();
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void handleDataProvidersFound(final DataProvidersFoundMessage message,
            final MessagePropertiesContainer messageProperties)
    {
        if (message == null)
        {
            return;
        }

        final String correlationId = messageProperties.getCorrelationId();

        final IServiceCallback<?> callback = 
                                    this.removeServiceCallback(correlationId);

        if (callback == null)
        {
            return;
        }

        final List<DataProvider> dataProviders = message.getDataProviders();

        final ListDataProvidersResponse response = 
                    new ListDataProvidersResponse(correlationId, dataProviders);

        // TODO : Take the callback processing off the message thread
        try
        {
            ((ServiceCallback<ListDataProvidersResponse>) callback).handleServiceResponse(response);
        }
        catch (Exception exception)
        {
            // log the exception thrown by the compute callback
            Object[] logParams = {"handleServiceResponse", exception.getMessage()};
            LOGGER.error(HDCRMessageCode.ERROR_CALLBACK_FAIL_E.getMessageCode(), logParams, exception);
        }
    }
}
