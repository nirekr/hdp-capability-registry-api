/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.callback;

import java.util.List;

import com.dell.cpsd.hdp.capability.registry.api.DataProvider;

import com.dell.cpsd.service.common.client.callback.ServiceResponse;

/**
 * This contains the response to list the available RCM definitions.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class ListDataProvidersResponse extends ServiceResponse<List<DataProvider>>
{
    /**
     * ListDataProvidersResponse constructor
     *
     * @param   requestId   The request identifier.
     * @param   response    The reponse data.
     * @since 1.0
     */
    public ListDataProvidersResponse(
                    final String requestId, final List<DataProvider> response)
    {
        super(requestId, response, "");
    }
}
