/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client;

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
