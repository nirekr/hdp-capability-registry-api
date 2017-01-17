/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.message;

import java.util.Date;

import com.dell.cpsd.hdp.capability.registry.api.MessageProperties;

import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;

/**
 * This class is a container for message properties published with the message.
 * 
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class DefaultMessageProperties extends MessageProperties implements MessagePropertiesContainer
{
    /**
     * DefaultMessageProperties no args constructor for use in serialization
     * 
     * @since   1.0
     */
    public DefaultMessageProperties() 
    {
        super();
    }

    
    /**
     * DefaultMessageProperties constructor
     * 
     * @param   timestamp       The message timestamp
     * @param   correlationId   The message correlation id
     * @param   replyTo         The message reply to destination.
     * 
     * @since   1.0
     */
    public DefaultMessageProperties(final Date timestamp, 
            final String correlationId, final String replyTo) 
    {
        super(timestamp, correlationId, replyTo);
    }
}
