/**
 * ﻿Copyright (C) 2014-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package yuan.flood.ses;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ses.adapter.ISESRequestBuilder;
import org.n52.oxf.ses.adapter.SESAdapter;
import org.n52.oxf.util.web.HttpClientException;
import org.springframework.stereotype.Component;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3.x2003.x05.soapEnvelope.Header;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Class SESAdapter_01.
 *
 * @author Jan Schulte
 */
public class SESAdapter_01 extends SESAdapter {

    @Override
    public XmlObject handleResponse(String operationName, ByteArrayInputStream input) throws OXFException {
        XmlObject result = null;
      if(operationName != null && input != null)
          if (operationName.equals("Register")){
            return super.handleResponse(operationName, input);}
        else if (operationName.equals("Subscribe")){
              Envelope env = null;
              try {
                  env = EnvelopeDocument.Factory.parse(input).getEnvelope();
                  Header header = env.getHeader();
                  result = org.apache.xmlbeans.XmlObject.Factory.parse(env.newInputStream());
                  return result;
              } catch (XmlException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          return result;
    }

    /**
     * Do operation.
     *
     * @param operation the operation
     * @param parameterContainer the parameter container
     * @return the operation result
     * @throws OXFException the oXF exception
     * @see
     */

    public OperationResult doOperation(Operation operation, ParameterContainer parameterContainer)
            throws OXFException, IllegalStateException {
        String request = null;
        ISESRequestBuilder requestBuilder = new SESRequestBuilder_01();
        OperationResult result = null;
        if(operation!=null){

            // SUBSCRIBE
            if (operation.getName().equals(SESAdapter.SUBSCRIBE)) {
                request = requestBuilder.buildSubscribeRequest(parameterContainer);

                // GET_CAPABILITIES
            } else if(operation.getName().equals(SESAdapter.GET_CAPABILITIES)){
                request = requestBuilder.buildGetCapabilitiesRequest(parameterContainer);

                // NOTIFY
            }
            else if(operation.getName().equals(SESAdapter.NOTIFY)){
                request = requestBuilder.buildNotifyRequest(parameterContainer);

                // REIGSER_PUBLISHER
            } else if(operation.getName().equals(SESAdapter.REGISTER_PUBLISHER)){
                request = requestBuilder.buildRegisterPublisherRequest(parameterContainer);

                // DESCRIBE_SENSOR
            } else if(operation.getName().equals(SESAdapter.DESCRIBE_SENSOR)){
                request = requestBuilder.buildDescribeSensorRequest(parameterContainer);

                // Operation not supported
            } else {
                throw new OXFException("The operation '" + operation.getName()
                        + "' is not supported.");
            }
            try {
                InputStream is = IOHelper_01.sendPostMessage(operation.getDcps()[0]
                                                                          .getHTTPPostRequestMethods().get(0).getOnlineResource()
                                                                          .getHref(), request);

            /*
                        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                        String line;
                System.out.println("Request vom SES: ");
                while ((line = rd.readLine()) != null) {
                        System.out.println(line);
                }
             */
                result = new OperationResult(is, parameterContainer, request);
            } catch (IOException e) {
                throw new OXFException("Could not send POST message.", e);
            }
            catch (HttpClientException e) {
                throw new OXFException(e);
            }

        }

        return result;
    }
}
