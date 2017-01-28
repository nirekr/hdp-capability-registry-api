/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.notifier;

import java.util.UUID;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;

import com.dell.cpsd.hdp.capability.registry.api.CapabilityProvider;

import com.dell.cpsd.hdp.capability.registry.client.amqp.producer.IAmqpCapabilityRegistryControlProducer;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryNotifier;

/**
 * This class sends notification messages at a fixed interval to capability 
 * registries.
 * 
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @since   1.0
 */
public class AmqpCapabilityRegistryNotifier implements ICapabilityRegistryNotifier
{
    /*
     * The default value for the shutdown wait time.
     */
    private static final long        SHUTDOWN_WAIT         = 2;
    
    /*
     * The <code>ScheduledExecutorService</code> used to poll.
     */
    private ScheduledExecutorService executorService = null;
    
    /*
     * The initial delay before the poller starts.
     */
    private long initialDelay = -1;
    
    /*
     * The delay between polling requests.
     */
    private long delay = -1;
    
    /*
     * The capability registry control producer
     */
    private IAmqpCapabilityRegistryControlProducer producer = null;
    
    /*
     * The capability provider information
     */
    private CapabilityProvider capabilityProvider = null;
    
    
    /**
     * AmqpCapabilityRegistryNotifier constructor.
     * 
     * @param   initialDelay    The initial delay before polling starts.
     * @param   delay           The delay between polls.
     * @param   producer        The control message producer.
     * 
     * @since   1.0
     */
    public AmqpCapabilityRegistryNotifier(long initialDelay, long delay,
            final IAmqpCapabilityRegistryControlProducer producer) 
    {
        super();
        
        this.setProducer(producer);
        
        this.setInitialDelay(initialDelay);
        this.setDelay(delay);
    }
    
    
    /**
     * This returns the capability provider information.
     * 
     * @return  The capability provider information.
     * 
     * @since   1.0
     */
    public CapabilityProvider getCapabilityProvider()
    {
        return this.capabilityProvider;
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
     * {@inheritDoc}
     */
    @Override
    public void start()
    {
        if (this.executorService == null)
        {
            this.executorService = this.makeScheduledExecutorService(
                    this.getInitialDelay(), this.getDelay(), TimeUnit.MILLISECONDS);
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyRegistry() throws CapabilityRegistryException
    {
        // generate a correlation identifier
        final String correlationId = UUID.randomUUID().toString();
        
        this.producer.publishCapabilityProviderPong(
                                         correlationId, this.capabilityProvider);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void stop()
    {
        // shutdown the executor service
        if (this.executorService != null)
        {
            this.shutdown(this.executorService);
        }
        
        this.executorService = null;
    }
    
    
    /**
     * This creates the <code>ScheduledExecutorService</code> for this poller.
     * 
     * @param   initialDelay    The time to delay first execution.
     * @param   delay           The period between successive executions.
     * @param   timeUnit        The <code>TimeUnit</code> for the delays.
     * 
     * @return  The <code>ScheduledExecutorService</code> for this poller.
     * 
     * @since   1.0
     */
    protected ScheduledExecutorService makeScheduledExecutorService(
            long initialDelay, long delay, TimeUnit timeUnit)
    {
        final NotifierTask notifierTask = new NotifierTask(this);
        
        final ScheduledExecutorService executorService = 
                                Executors.newSingleThreadScheduledExecutor();
        
        executorService.scheduleWithFixedDelay(
                    notifierTask, initialDelay, delay, TimeUnit.MILLISECONDS);
        
        return executorService;
    }
    
    
    /**
     * This shuts down the specified <code>ExecutorService</code>.
     * 
     * @param   executorService  The <code>ExecutorService</code> to shutdown.
     * 
     * @since   1.0
     */
    protected void shutdown(final ExecutorService executorService)
    {
        if (executorService == null)
        {
            return;
        }
        
        // stop new tasks from being submitted
        executorService.shutdown();
        
        try 
        {
            boolean graceful = 
                executorService.awaitTermination(SHUTDOWN_WAIT, TimeUnit.SECONDS);
            
            // allow pending tasks to finish
            if (graceful == false) 
            {
                // cancel currently executing tasks
                executorService.shutdownNow();
              
                executorService.awaitTermination(SHUTDOWN_WAIT, TimeUnit.SECONDS);
            }
            
        } catch (InterruptedException ie) 
        {
            // (re-)cancel if current thread also interrupted
            executorService.shutdownNow();
            
            // preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
    
    
    /**
     * This returns the capability registry control message producer.
     * 
     * @return  The capability registry control message producer.
     * 
     * @since   1.0
     */
    public IAmqpCapabilityRegistryControlProducer getProducer()
    {
        return this.producer;
    }
    
    
    /**
     * This sets the delay between capability provider polls.
     * 
     * @param   delay    The delay between capability provider polls.
     * 
     * @throws  IllegalArgumentException    Thrown if the producer is null.
     * 
     * @since   1.0
     */
    protected void setProducer(final IAmqpCapabilityRegistryControlProducer producer)
    {
        if (producer == null)
        {
            throw new IllegalArgumentException(
                        "The capability registry control producer is null.");
        }
        
        this.producer = producer;    
    }   
    
    
    /**
     * This returns the delay between capability provider polls.
     * 
     * @return  The delay between capability provider polls.
     * 
     * @since   1.0
     */
    public long getDelay()
    {
        return this.delay;
    }
    
    
    /**
     * This sets the delay between capability provider polls.
     * 
     * @param   delay    The delay between capability provider polls.
     * 
     * @throws  IllegalArgumentException    Thrown if the delay is less than zero.
     * 
     * @since   1.0
     */
    protected void setDelay(final long delay)
    {
        // the delay should be greater than or equal to zero
        if (delay < 0)
        {
            throw new IllegalArgumentException("The delay is less than zero.");
        }
        
        this.delay = delay;      
    }
    
    
    /**
     * This returns the initial delay for the capability provider poller.
     * 
     * @return  The initial delay for the capability provider poller.
     * 
     * @since   1.0
     */
    public long getInitialDelay()
    {
        return this.initialDelay;
    }
    
    
    /**
     * This sets the initial delay for the capability provider poller.
     * 
     * @param   initialDelay    The initial delay.
     * 
     * @throws  IllegalArgumentException    Thrown if the initial delay is less than zero.
     * 
     * @since   1.0
     */
    protected void setInitialDelay(final long initialDelay)
    {
        // the inital delay should be greater than or equal to zero
        if (initialDelay < 0)
        {            
            throw new IllegalArgumentException(
                                        "The initial delay is less than zero.");
        }
        
        this.initialDelay = initialDelay;        
    }
}
