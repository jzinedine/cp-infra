
package com.hamrahvas.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SmsBuffer", targetNamespace = "http://tempuri.org/", wsdlLocation = "file:/Users/jani/Projects/cp-infra/config/SMSBuffer.wsdl")
public class SmsBuffer
    extends Service
{

    private final static URL SMSBUFFER_WSDL_LOCATION;
    private final static WebServiceException SMSBUFFER_EXCEPTION;
    private final static QName SMSBUFFER_QNAME = new QName("http://tempuri.org/", "SmsBuffer");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/Users/jani/Projects/cp-infra/config/SMSBuffer.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SMSBUFFER_WSDL_LOCATION = url;
        SMSBUFFER_EXCEPTION = e;
    }

    public SmsBuffer() {
        super(__getWsdlLocation(), SMSBUFFER_QNAME);
    }

    public SmsBuffer(WebServiceFeature... features) {
        super(__getWsdlLocation(), SMSBUFFER_QNAME, features);
    }

    public SmsBuffer(URL wsdlLocation) {
        super(wsdlLocation, SMSBUFFER_QNAME);
    }

    public SmsBuffer(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SMSBUFFER_QNAME, features);
    }

    public SmsBuffer(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SmsBuffer(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SmsBufferSoap
     */
    @WebEndpoint(name = "SmsBufferSoap")
    public SmsBufferSoap getSmsBufferSoap() {
        return super.getPort(new QName("http://tempuri.org/", "SmsBufferSoap"), SmsBufferSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SmsBufferSoap
     */
    @WebEndpoint(name = "SmsBufferSoap")
    public SmsBufferSoap getSmsBufferSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "SmsBufferSoap"), SmsBufferSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SMSBUFFER_EXCEPTION!= null) {
            throw SMSBUFFER_EXCEPTION;
        }
        return SMSBUFFER_WSDL_LOCATION;
    }

}
