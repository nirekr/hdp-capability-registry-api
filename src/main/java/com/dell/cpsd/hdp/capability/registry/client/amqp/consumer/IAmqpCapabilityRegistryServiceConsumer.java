/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.amqp.consumer;

/**
 * This interface should be implemented by a consumer of service messages.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since SINCE-TBD
 */
public interface IAmqpCapabilityRegistryServiceConsumer
{
    /**
     * This returns the reply to destination for the service message queue for
     * this consumer.
     *
     * @return  The reply to destination for the service message queue.
     * 
     * @since   1.0
     */
    public String getReplyTo();
    
    
    /**
     * This returns the <code>IAmqpCapabilityRegistryMessageHandler</code> for 
     * the consumer.
     *
     * @return  The service message handler for the consumer.
     * 
     * @since   1.0
     */
    public IAmqpCapabilityRegistryMessageHandler getMessageHandler();

    
    /**
     * This sets the <code>IAmqpCapabilityRegistryMessageHandler</code> for the
     * consumer.
     *
     * @param    handler    The service message handler for the consumer.
     * 
     * @throws  IllegalArgumentException    Thrown if the handler is null.
     * 
     * @since   1.0
     */
    public void setMessageHandler(final IAmqpCapabilityRegistryMessageHandler messageHandler);
}
