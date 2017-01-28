/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.log;

import com.dell.cpsd.hdp.capability.registry.client.i18n.HDCRMessageBundle;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This is the message code enum for the service client.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public enum HDCRMessageCode
{
    RABBIT_AMQP_LISTENER_E(2001, "HDCR2001E"),
    RABBIT_AMQP_RECOVERY_E(2002, "HDCR2002E"),
    PRODUCER_ROUTING_I(2003, "HDCR2003I"),
    SERVICE_RESPONSE_QUEUE_I(2004, "HDCR2004I"),
    SERVICE_RESPONSE_BINDING_I(2005, "HDCR2005I"),
    PRODUCER_PUBLISH_E(2006, "HDCR2006E"),
    HDCR_2007_W(2007, "HDCR2007W"),
    SERVICE_ERROR_NULL_W(2008, "HDCR2008W"),
    SERVICE_HANDLER_NULL_W(2009, "HDCR2009W"),
    MESSAGE_TIMEOUT_E(2010, "HDCR2010E"),
    PUBLISH_MESSAGE_FAIL_E(2011, "HDCR2011E"),
    ERROR_CALLBACK_FAIL_E(2012, "HDCR2012E"),
    NULL_MESSAGE_W(2013, "HDCR2013W"),
    NOTIFY_TASK_E(2014, "HDCR2014E"),
    NO_CAPABILITY_PROVIDER_W(2015, "HDCR2015W"),
    MANAGER_SHUTDOWN_E(2016, "HDCR2016E"),
    REUSE_2017_E(2017, "HDCR2017E"),
    EXECUTOR_SHUTDOWN_I(2018, "HDCR2018I"),
    WAIT_ON_REQUESTS_I(2019, "HDCR2019I"),
    REUSE_2020_I(2020, "HDCR2020I"),
    TIMEOUT_TASK_CHECK_E(2021, "HDCR2021E");

    /*
     * The path to the resource bundle
     */
    private static ResourceBundle BUNDLE = ResourceBundle.getBundle(HDCRMessageBundle.class.getName());

    /*
     * The error code.
     */
    private final int errorCode;

    /*
     * The message code.
     */
    private final String messageCode;

    /**
     * HDCRMessageCode constructor
     *
     * @param   errorCode   The error code.
     * @param   messageCode The message code.
     * 
     * @since   1.0
     */
    private HDCRMessageCode(int errorCode, String messageCode)
    {
        this.errorCode = errorCode;
        this.messageCode = messageCode;
    }

    
    /**
     * This returns the message code.
     *
     * @return  The message code.
     * 
     * @since   1.0
     */
    public String getMessageCode()
    {
        return this.messageCode;
    }

    
    /**
     * This returns the error code.
     *
     * @return  The error code.
     * 
     * @since   1.0
     */
    public int getErrorCode()
    {
        return this.errorCode;
    }

    
    /**
     * This returns the error text.
     *
     * @return  The error text.
     * 
     * @since   1.0
     */
    public String getErrorText()
    {
        try
        {
            return BUNDLE.getString(this.messageCode);
        }
        catch (MissingResourceException exception)
        {
            return this.messageCode;
        }
    }
    

    /**
     * This formats the  message using the array of parameters.
     *
     * @param   params  The message parameters.
     * 
     * @return  The localized message populated with the parameters.
     * 
     * @since   1.0
     */
    public String getMessageText(Object[] params)
    {
        String message = null;

        try
        {
            message = BUNDLE.getString(this.messageCode);

        }
        catch (MissingResourceException exception)
        {
            return this.messageCode;
        }

        if ((params == null) || (params.length == 0))
        {
            return message;
        }

        return MessageFormat.format(message, params);
    }

}
