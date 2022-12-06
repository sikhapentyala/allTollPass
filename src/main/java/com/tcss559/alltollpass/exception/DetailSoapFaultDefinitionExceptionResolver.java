package com.tcss559.alltollpass.exception;

/**
 * @author sikha
 * SOAP based Exception
 *
 */

import javax.xml.namespace.QName;

import org.springframework.http.HttpStatus;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver{
    private static final QName CODE = new QName("statusCode");
    private static final QName MESSAGE = new QName("message");

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
            SoapFaultDetail detail = fault.addFaultDetail();
            detail.addFaultDetailElement(CODE).addText(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            detail.addFaultDetailElement(MESSAGE).addText(ex.getMessage());
    }

}
