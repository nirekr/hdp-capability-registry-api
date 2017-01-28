/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client;

import com.dell.cpsd.hdp.capability.registry.api.CapabilityProvider;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;

/**
 * This interface should be implemented to notify capability registries that
 * the provider is running.
 * 
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @since   1.0
 */
public interface ICapabilityRegistryNotifier
{
    /**
     * This sets the capability provider information.
     * 
     * @param   capabilityProvider    The capability provider information.
     * 
     * @since   1.0
     */
    public void setCapabilityProvider(final CapabilityProvider capabilityProvider);
    
    
    /**
     * This starts notifying the capability registries.
     * 
     * @since   1.0
     */
    public void start();
    

    /**
     * This method notifies capability registries.
     * 
     * @throws  CapabilityRegistryException Thrown if the notification fails.
     * 
     * @since   1.0
     */
    public void notifyRegistry() throws CapabilityRegistryException;
    
    
    /**
     * This stops notifying the capability registries.
     * 
     * @since   1.0
     */
    public void stop();
}
