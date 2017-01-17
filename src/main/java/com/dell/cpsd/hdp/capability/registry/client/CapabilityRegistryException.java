/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client;

/**
 * This is the base exception for the service client.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class CapabilityRegistryException extends Exception
{
    /*
     * serial version id
     */
    private static final long serialVersionUID = 4119241334523852220L;

    
    /**
     * CapabilityRegistryException constructor.
     *
     * @since   1.0
     */
    public CapabilityRegistryException()
    {
        super();
    }
    

    /**
     * CapabilityRegistryException constructor.
     *
     * @param   cause   The cause of the exception.
     * 
     * @since   1.0
     */
    public CapabilityRegistryException(Throwable cause)
    {
        super(cause);
    }

    
    /**
     * CapabilityRegistryException constructor.
     *
     * @param   message     The exception message.
     * 
     * @since   1.0
     */
    public CapabilityRegistryException(String message)
    {
        super(message);
    }
    

    /**
     * CapabilityRegistryException constructor.
     *
     * @param   message     The exception message.
     * @param   cause       The cause of the exception.
     * 
     * @since   1.0
     */
    public CapabilityRegistryException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
