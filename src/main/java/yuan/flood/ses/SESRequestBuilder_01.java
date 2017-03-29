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

import org.apache.xmlbeans.XmlCursor;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ses.adapter.ISESRequestBuilder;
import org.n52.oxf.ses.adapter.SESRequestBuilder_00;
import org.n52.oxf.ses.adapter.SESUtils;
import org.springframework.stereotype.Component;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3.x2003.x05.soapEnvelope.Header;

import javax.xml.namespace.QName;
import java.util.UUID;

/**
 * The Class SESRequestBuilder_01.
 *
 * @author Jan Schulte
 */
public class SESRequestBuilder_01 extends SESRequestBuilder_00 {

    /** The ns_addressing. */
    private final String ns_addressing = "http://www.w3.org/2005/08/addressing";

    /** The ns_ses. */
    private final String ns_ses = "http://www.opengis.net/ses/0.0";

    /** The ns_ws notification. */
    private final String ns_wsNotification = "http://docs.oasis-open.org/wsn/b-2";

    /** The ns_ws brokered notification. */
    private final String ns_wsBrokeredNotification = "http://docs.oasis-open.org/wsn/br-2";

    /* (non-Javadoc)
     * @see org.n52.oxf.serviceAdapters.ses.SESRequestBuilder_00#buildNotifyRequest(org.n52.oxf.serviceAdapters.ParameterContainer)
     */
    @SuppressWarnings("unqualified-field-access")
	@Override
    public String buildNotifyRequest(ParameterContainer parameter) {
        String request;

        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.NOTIFY_SES_URL).getSpecifiedValue();
        XmlCursor cur = null;

        SESUtils.addNamespacesToEnvelope_000(env);

        cur = header.newCursor();

        cur.toFirstContentToken();
        cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),sesURL);
        cur.insertElementWithText(new QName(ns_addressing,"Action","wsa"),
        "http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/Notify");
        cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"),
                UUID.randomUUID().toString());
        cur.beginElement(new QName(ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),
        "http://www.w3.org/2005/08/addressing/role/anonymous");
        cur.dispose();


        cur = body.newCursor();
        cur.toFirstContentToken();
        cur.beginElement(new QName(ns_wsNotification,"Notify","wsnt"));

        cur.beginElement(new QName(ns_wsNotification,"NotificationMessage","wsnt"));

        cur.beginElement(new QName(ns_wsNotification,"Topic","wsnt"));
        cur.insertAttributeWithValue(/*new QName(ns_wsNotification,"Dialect","wsnt")*/"Dialect",
                (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.NOTIFY_TOPIC_DIALECT).getSpecifiedValue());
        cur.insertChars((String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.NOTIFY_TOPIC).getSpecifiedValue());
        cur.toNextToken();

        cur.beginElement(new QName(ns_wsNotification,"Message","wsnt"));
        cur.insertChars("@MSG_REPLACER@");
        cur.dispose();

        request = envDoc.xmlText();
        request = request.replaceAll("@MSG_REPLACER@",(String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.NOTIFY_XML_MESSAGE).getSpecifiedValue());

        return request;
    }

    /* (non-Javadoc)
     * @see org.n52.oxf.serviceAdapters.ses.SESRequestBuilder_00#buildRegisterPublisherRequest(org.n52.oxf.serviceAdapters.ParameterContainer)
     */
    @SuppressWarnings("unqualified-field-access")
	@Override
    public String buildRegisterPublisherRequest(ParameterContainer parameter) {
        String request = "";

        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_SES_URL).getSpecifiedValue();
        XmlCursor cur = null;
        String from = "http://www.w3.org/2005/08/addressing/role/anonymous";
        if(parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_FROM) != null){
            from = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_FROM).getSpecifiedValue();
        }
        SESUtils.addNamespacesToEnvelope_000(env);

        cur = header.newCursor();

        cur.toFirstContentToken();
        cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),sesURL);
        cur.insertElementWithText(new QName(ns_addressing,"Action","wsa"),
        "http://docs.oasis-open.org/wsn/brw-2/RegisterPublisher/RegisterPublisherRequest");
        cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"),
                UUID.randomUUID().toString());
        cur.beginElement(new QName(ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),from);
        cur.dispose();

        cur = body.newCursor();

        cur.toFirstContentToken();
        cur.beginElement(new QName(ns_wsBrokeredNotification,"RegisterPublisher","wsbn"));
        cur.insertElementWithText(new QName("http://docs.oasis-open.org/wsrf/rl-2","RequestedLifetimeDuration","wsrf"),
                (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_LIFETIME_DURATION).getSpecifiedValue());

        cur.beginElement(new QName(ns_wsBrokeredNotification,"Topic","wsbn"));
        // modify by Yuan change "dialect" to "Dialect"
       //  cur.insertAttributeWithValue(/*new QName(ns_wsBrokeredNotification,"Dialect","wsrf")*/"dialect",
       //          (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_TOPIC_DIALECT).getSpecifiedValue());
        cur.insertAttributeWithValue(/*new QName(ns_wsBrokeredNotification,"Dialect","wsrf")*/"Dialect",
                (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_TOPIC_DIALECT).getSpecifiedValue());
        cur.insertChars((String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_TOPIC).getSpecifiedValue());
        cur.toNextToken();

//        cur.beginElement(new QName("http://www.opengis.net/sensorML/1.0.1","SensorML","sml"));
//        cur.insertAttributeWithValue(new QName("http://www.opengis.net/sensorML/1.0.1","version","sml"), "1.0.1");
        cur.insertChars("@SML_REPLACER@");
        cur.toNextToken();

        cur.insertElementWithText(new QName(ns_wsBrokeredNotification,"Demand","wsbn"),"no");
        cur.insertElementWithText(new QName(ns_addressing,"EndpointReferenceType","wsa"),"ignore");
        cur.dispose();

        request = envDoc.xmlText();
        request = request.replaceAll("@SML_REPLACER@", (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.REGISTER_PUBLISHER_SENSORML).getSpecifiedValue());

//        request="<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"\n" +
//                "xmlns:sml=\"http://www.opengis.net/sensorML/1.0.1\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"\n" +
//                "xmlns:wsn-b=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:wsn-br=\"http://docs.oasis-open.org/wsn/br-2\"\n" +
//                "xmlns:wsn-t=\"http://docs.oasis-open.org/wsn/t-1\" xmlns:wsntw=\"http://docs.oasis-open.org/wsn/bw-2\"\n" +
//                "xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
//                "<SOAP-ENV:Header>\n" +
//                "        <wsa:Action>http://docs.oasis-open.org/wsn/brw-2/RegisterPublisher/RegisterPublisherRequest</wsa:Action>\n" +
//                "        <wsa:To>http://localhost:8080/52n-ses-1.2.2/services/Broker</wsa:To>\n" +
//                "        <wsa:From>\n" +
//                "            <wsa:Address>http://www.w3.org/2005/08/addressing/role/anonymous</wsa:Address>\n" +
//                "        </wsa:From>\n" +
//                "        <wsa:MessageID>1410b537-aeea-43b6-a0fd-eeb97d2ff829</wsa:MessageID>\n" +
//                "    </SOAP-ENV:Header>\n" +
//                "    <SOAP-ENV:Body>\n" +
//                "        <wsn-br:RegisterPublisher>\n" +
//                "            <wsn-br:Topic Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">tses:SensorManagement/tses:Registered</wsn-br:Topic>\n" +
//                "            <wsn-br:Demand>false</wsn-br:Demand>\n" +
//                "            <sml:SensorML version=\"1.0.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:swe=\"http://www.opengis.net/swe/1.0.1\">\n" +
//                "\t\t\t<sml:member>\n" +
//                "\t\t\t\t<sml:System xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
//                "\t\t\t\t\n" +
//                "\t\t\t\t\t<sml:identification>\n" +
//                "\t\t\t\t\t\t<sml:IdentifierList>\n" +
//                "\t\t\t\t\t\t\t<sml:identifier>\n" +
//                "\t\t\t\t\t\t\t\t<sml:Term definition=\"urn:ogc:def:identifier:OGC:uniqueID\">\n" +
//                "\t\t\t\t\t\t\t\t\t<sml:value>urn:liesmars:object:feature:Platform:Station:Weather:sta-a001</sml:value>\n" +
//                "\t\t\t\t\t\t\t\t</sml:Term>\n" +
//                "\t\t\t\t\t\t\t</sml:identifier>\n" +
//                "\t\t\t\t\t\t</sml:IdentifierList>\n" +
//                "\t\t\t\t\t</sml:identification>\n" +
//                "\t\t\t\t\t\n" +
//                "\t\t\t\t\t<sml:outputs>\n" +
//                "\t\t\t\t\t\t<sml:OutputList>\t\n" +
//                "\t\t\t\t\t\t\t<sml:output name=\"AirTemperature\">\n" +
//                "\t\t\t\t\t\t\t\t<swe:Quantity definition=\"urn:liesmars:def:phenomenon:LIESMARS:1.0.0:AirTemperature\">\n" +
//                "\t\t\t\t\t\t\t\t\t<gml:metaDataProperty>\n" +
//                "\t\t\t\t\t\t\t\t\t\t<offering>\n" +
//                "\t\t\t\t\t\t\t\t\t\t\t<id>LIESMARS</id>\n" +
//                "\t\t\t\t\t\t\t\t\t\t\t<name>liesmars</name>\n" +
//                "\t\t\t\t\t\t\t\t\t\t</offering>\n" +
//                "\t\t\t\t\t\t\t\t\t</gml:metaDataProperty>\n" +
//                "\t\t\t\t\t\t\t\t\t<swe:uom code=\"Cel\"/>\n" +
//                "\t\t\t\t\t\t\t\t</swe:Quantity>\n" +
//                "\t\t\t\t\t\t\t</sml:output>\n" +
//                "\t\t\t\t\t\t\t<sml:output name=\"WindSpeed\">\n" +
//                "\t\t\t\t\t\t\t\t<swe:Quantity definition=\"urn:liesmars:def:phenomenon:LIESMARS:1.0.0:WindSpeed\">\n" +
//                "\t\t\t\t\t\t\t\t\t<gml:metaDataProperty>\n" +
//                "\t\t\t\t\t\t\t\t\t\t<offering>\n" +
//                "\t\t\t\t\t\t\t\t\t\t\t<id>LIESMARS</id>\n" +
//                "\t\t\t\t\t\t\t\t\t\t\t<name>liesmars</name>\n" +
//                "\t\t\t\t\t\t\t\t\t\t</offering>\n" +
//                "\t\t\t\t\t\t\t\t\t</gml:metaDataProperty>\n" +
//                "\t\t\t\t\t\t\t\t\t<swe:uom code=\"m/s\"/>\n" +
//                "\t\t\t\t\t\t\t\t</swe:Quantity>\n" +
//                "\t\t\t\t\t\t\t</sml:output>\t\t\t\t\t\n" +
//                "\t\t\t\t\t\t\t<sml:output name=\"WindDirection\">\n" +
//                "\t\t\t\t\t\t\t\t<swe:Quantity definition=\"urn:liesmars:def:phenomenon:LIESMARS:1.0.0:WindDirection\">\n" +
//                "\t\t\t\t\t\t\t\t\t<gml:metaDataProperty>\n" +
//                "\t\t\t\t\t\t\t\t\t\t<offering>\n" +
//                "\t\t\t\t\t\t\t\t\t\t\t<id>LIESMARS</id>\n" +
//                "\t\t\t\t\t\t\t\t\t\t\t<name>liesmars</name>\n" +
//                "\t\t\t\t\t\t\t\t\t\t</offering>\n" +
//                "\t\t\t\t\t\t\t\t\t</gml:metaDataProperty>\n" +
//                "\t\t\t\t\t\t\t\t\t<swe:uom code=\"degree\"/>\n" +
//                "\t\t\t\t\t\t\t\t</swe:Quantity>\n" +
//                "\t\t\t\t\t\t\t</sml:output>\n" +
//                "\t\t\t\t\t\t</sml:OutputList>\n" +
//                "\t\t\t\t\t\t\n" +
//                "\t\t\t\t\t</sml:outputs>\n" +
//                "\t\t\t\t</sml:System>\n" +
//                "\t\t\t</sml:member>\n" +
//                "\t\t</sml:SensorML>\n" +
//                "\n" +
//                "        </wsn-br:RegisterPublisher>\n" +
//                "    </SOAP-ENV:Body>\n" +
//                "</SOAP-ENV:Envelope>";

        return request;
    }

    /* (non-Javadoc)-------------------p
     * @see org.n52.oxf.serviceAdapters.ses.SESRequestBuilder_00#buildGetCapabilitiesRequest(org.n52.oxf.serviceAdapters.ParameterContainer)
     */
    @Override
    public String buildGetCapabilitiesRequest(ParameterContainer parameter){
        String request = "";

        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.GET_CAPABILITIES_SES_URL).getSpecifiedValue();
        XmlCursor cur = null;

        SESUtils.addNamespacesToEnvelope_000(env);

        cur = header.newCursor();

        cur.toFirstContentToken();
        cur.insertElementWithText(new QName(this.ns_addressing,"To","wsa"),sesURL);
        cur.insertElementWithText(new QName(this.ns_addressing,"Action","wsa"), "http://www.opengis.net/ses/GetCapabilitiesRequest");
        cur.insertElementWithText(new QName(this.ns_addressing,"MessageID","wsa"),UUID.randomUUID().toString());
        cur.beginElement(new QName(this.ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(this.ns_addressing,"Address","wsa"),"http://www.w3.org/2005/08/addressing/role/anonymous");
        cur.dispose();

        cur = body.newCursor();

        cur.toFirstContentToken();
        cur.beginElement(new QName(this.ns_ses,"GetCapabilities","ses"));
        cur.insertAttributeWithValue("Service", "SES");
        cur.dispose();

        request = envDoc.xmlText();

        return request;
    }

    @Override
    public String buildSubscribeRequest(ParameterContainer parameter) {
        String request=null;

        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = (String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_SES_URL).getSpecifiedValue();

        XmlCursor cur = null;
        SESUtils.addNamespacesToEnvelope_000(env);
        cur = header.newCursor();
        cur.toFirstContentToken();
        cur.insertElementWithText(new QName(this.ns_addressing, "Action", "wsa"), "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeRequest");
        cur.insertElementWithText(new QName(this.ns_addressing,"To","wsa"),sesURL );

        cur.beginElement(new QName(this.ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(this.ns_addressing,"Address","wsa"),"http://www.w3.org/2005/08/addressing/role/anonymous");
        cur.toNextToken();
        cur.insertElementWithText(new QName(this.ns_addressing,"MessageID","wsa"),UUID.randomUUID().toString());
        cur.dispose();

        cur = body.newCursor();
        cur.toFirstContentToken();
        cur.beginElement(new QName(ns_wsNotification, "Subscribe", "wsn-b"));
        cur.addToSelection();
        cur.beginElement(new QName(ns_wsNotification,"ConsumerReference","wsn-b"));
        cur.insertElementWithText(new QName(ns_addressing, "Address","wsa"),"http://localhost:8092/");
        cur.toSelection(0);
        cur.toEndToken();

        cur.beginElement(new QName(ns_wsNotification,"Filter","wsn-b"));
        cur.beginElement(new QName(ns_wsNotification,"MessageContent","wsn-b"));
        cur.insertAttributeWithValue("Dialect","http://www.opengis.net/ses/filter/level3");
        cur.insertChars("@MSG_REPLACER@");
        cur.dispose();

        request = envDoc.xmlText();
        request = request.replaceAll("@MSG_REPLACER@",(String)parameter.getParameterShellWithCommonName(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT).getSpecifiedValue());
        return request;
    }
}
