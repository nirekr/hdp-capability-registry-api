/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.notifier;

import com.dell.cpsd.common.logging.ILogger;

import com.dell.cpsd.hdp.capability.registry.client.log.HDCRLoggingManager;
import com.dell.cpsd.hdp.capability.registry.client.log.HDCRMessageCode;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;
import com.dell.cpsd.hdp.capability.registry.client.ICapabilityRegistryNotifier;

/**
 * This task is used to trigger a poll of capability providers.
 *
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 * 
 * @since   1.0
 */
public class NotifierTask implements Runnable
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = 
                            HDCRLoggingManager.getLogger(NotifierTask.class);
    
    /*
     * The <code>ICapabilityRegistryNotifier</code> for the task
     */
    private ICapabilityRegistryNotifier notifier = null;
   
    
    /**
     * NotifierTask constructor
     * 
     * @param   notifier  The <code>ICapabilityRegistryNotifier</code> to use.
     * 
     * @since   1.0
     */
    public NotifierTask(final ICapabilityRegistryNotifier notifier)
    {
        super();
        
        if (notifier == null)
        {
            throw new IllegalArgumentException(
                                    "The capability registry notifier is null.");
        }
        
        this.notifier = notifier;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void run()
    {
        try
        {
            this.notifier.notifyRegistry();
        }
        catch (Exception exception)
        {
            // log the exception thrown by the check
            Object[] lparams = {exception.getMessage()};
            LOGGER.error(HDCRMessageCode.NOTIFY_TASK_E.getMessageCode(),
                        lparams, exception);            
        }
    }
}
