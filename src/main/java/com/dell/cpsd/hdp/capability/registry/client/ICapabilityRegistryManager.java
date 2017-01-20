/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client;

import java.util.List;

import com.dell.cpsd.hdp.capability.registry.api.ProviderCapability;
import com.dell.cpsd.hdp.capability.registry.api.ProviderIdentity;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;

import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;

import com.dell.cpsd.hdp.capability.registry.client.callback.ListDataProvidersResponse;

/**
 * This interface should be implemented by a capability registry manager.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public interface ICapabilityRegistryManager 
{
    /**
     * This registers the capability profiles for a HAL data provider.
     * 
     * @param   identity        The HAL data provider identity.
     * @param   capabilities    The capabilities of the HAL data provider.
     * 
     * @throws  CapabilityRegistryException Thrown if the register fails.
     * 
     * @since   1.0
     */
    void registerDataProvider(final ProviderIdentity identity, 
                        final List<ProviderCapability> capabilities)
        throws CapabilityRegistryException;

    
    
    /**
     * This unregisters the capability profiles for a HAL data provider.
     * 
     * @param   identity        The HAL data provider identity.
     * 
     * @throws  CapabilityRegistryException Thrown if the unregister fails.
     * 
     * @since   1.0
     */
    void unregisterDataProvider(final ProviderIdentity identity)
        throws CapabilityRegistryException;
    
 
    /**
     * This returns the list of HAL data providers.
     *
     * @param   timeout   The timeout in milliseconds.
     * 
     * @return  The response with the list of available data providers.
     * 
     * @throw   CapabilityRegistryException Thrown if the request fails.
     * @throw   ServiceTimeoutException     Thrown if the request times out.
     * 
     * @since   1.0
     */
    public ListDataProvidersResponse listDataProviders(final long timeout) 
        throws CapabilityRegistryException, ServiceTimeoutException;
}