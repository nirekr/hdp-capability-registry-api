/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.hdp.capability.registry.client.i18n;

import java.util.ListResourceBundle;

/**
 * This is the resource bundle for service client.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class HDCRMessageBundle extends ListResourceBundle
{
    /*
     * The content of this message resource bundle.
     */
    private static final Object[][] CONTENTS = {
            {"HDCR2001E", "HDCR2001E Rabbit consumer [{0}] threw an error. Reason [{1}]"},
            {"HDCR2002E", "HDCR2002E Unable to consume message after retries. Reason [{0}]"},
            {"HDCR2003I", "HDCR2003I The service message producer routing key : [{0}]"},
            {"HDCR2004I", "HDCR2004I The service message consumer queue name : [{0}]"},
            {"HDCR2005I", "HDCR2005I The service message consumer binding key: [{0}]"},
            {"HDCR2006E", "HDCR2006E Error publishing message [{0}]. Reason [{1}]"},
            {"HDCR2007W", "HDCR2007W The service message to handle is null."},
            {"HDCR2008W", "HDCR2008W The service error message to handle is null."},
            {"HDCR2009W", "HDCR2009W The service message handler is not set."},
            {"HDCR2010E", "HDCR2010E The service request [{0}] has timed out after [{1}] msec."},
            {"HDCR2011E", "HDCR2011E Could not publish the message. Reason [{0}]"},
            {"HDCR2012E", "HDCR2012E Unexpected error on callback [{0}]. Reason [{1}]"}, 
            {"HDCR2013W", "HDCR2013W The message to process is null."},
            {"HDCR2014E", "HDCR2014E Unexpected error on capability registry notification. Reason [{0}]."}, 
            {"HDCR2015W", "HDCR2015W No capability provider is registered."},
            {"HDCR2016E", "HDCR2016E The client is shutting down. Requests are not being accepted."}, 
            {"HDCR2017E", "HDCR2017E ."},
            {"HDCR2018I", "HDCR2018I Shutting down the scheduled executor service."},
            {"HDCR2019I", "HDCR2019I Waiting for pending requests to complete."}, 
            {"HDCR2020I", "HDCR2020I ."},
            {"HDCR2021E", "HDCR2021E Unexpected error on request timeout checking. Reason [{0}]"}
        };

    /**
     * HDCRMessageBundle constructor.
     *
     * @since   1.0
     */
    public HDCRMessageBundle()
    {
        super();
    }

    /**
     * This returns the messages for this resource bundle.
     *
     * @return  The messages for this resource bundle.
     * 
     * @since   1.0
     */
    @Override
    protected Object[][] getContents()
    {
        return CONTENTS;
    }
}
