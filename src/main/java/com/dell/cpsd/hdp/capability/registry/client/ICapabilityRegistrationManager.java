/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client;

import java.util.List;

import com.dell.cpsd.hdp.capability.registry.api.Capability;
import com.dell.cpsd.hdp.capability.registry.api.Identity;

import com.dell.cpsd.hdp.capability.registry.client.CapabilityRegistryException;

import com.dell.cpsd.service.common.client.exception.ServiceTimeoutException;

import com.dell.cpsd.hdp.capability.registry.client.callback.ListCapabilityProvidersResponse;

/**
 * This interface should be implemented by a manager for capability provider 
 * registration.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public interface ICapabilityRegistrationManager 
{
    /**
     * This registers the capability profiles for a capability provider.
     * 
     * @param   identity        The capability provider identity.
     * @param   capabilities    The capabilities of the provider.
     * 
     * @throws  CapabilityRegistryException Thrown if the register fails.
     * 
     * @since   1.0
     */
    void registerCapabilityProvider(final Identity identity, 
                        final List<Capability> capabilities)
        throws CapabilityRegistryException;

    
    
    /**
     * This unregisters the capability profiles for a capability provider.
     * 
     * @param   identity    The capability provider identity.
     * 
     * @throws  CapabilityRegistryException Thrown if the unregister fails.
     * 
     * @since   1.0
     */
    void unregisterCapabilityProvider(final Identity identity)
        throws CapabilityRegistryException;
}
