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

import net.opengis.eml.x001.EMLDocument;
import net.opengis.om.x10.ObservationPropertyType;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.ses.x00.CapabilitiesDocument;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ses.adapter.ISESRequestBuilder;
import org.n52.oxf.ses.adapter.SESAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import yuan.flood.until.ReadConfig;
import yuan.flood.until.SOSSESConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The SESConnector class manages the communication between the feeder and the SES.
 *
 * @author Jan Schulte
 */
@Component
public class SESConnector {

    /** The logger. */
    private static final Logger log = Logger.getLogger(SESConnector.class);

    /** The ses adapter. */
    private SESAdapter_01 sesAdapter;

    /** The ses url. */
    private String sesUrl;

    private String topic;

    private String dialect;

    private boolean closed = false;

    /**
     * Instantiates a new SESConnector.
     */
    public SESConnector() {
        this.sesAdapter = new SESAdapter_01();
        ReadConfig readConfig=new ReadConfig();
        readConfig.read();
        try {
            this.topic = SOSSESConfig.getSesdefaulttopic();
            this.dialect = SOSSESConfig.getSesdefaulttopicdialect();
            this.sesUrl = SOSSESConfig.getSesurl()
                    + SOSSESConfig.getSesbasicporttypepath();
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
            log.debug("Configuration is not available (anymore).");
        }
    }

    /**
     * Initialize the connection to the SES.
     *
     * @return true - if service is running
     */
    public boolean initService() {
        log.trace("initService()");

        try {
            ServiceDescriptor desc = this.sesAdapter.initService(this.sesUrl);
            log.info(desc.toXML());
        }
        catch (ExceptionReport e) {
            log.error("SES not accessible");
            return false;
        }
        catch (OXFException e) {
            log.error(String.format("SES '%s' not accessible.", this.sesUrl));
            return false;
        }
        log.info("SES accessible");
        return true;
    }

    /**
     * Register the publisher by sending a sensorML document to the SES.
     *
     * @param sensorML
     *        The sensorML document
     * @return The ID given by the SES
     */
    public String registerPublisher(SensorMLDocument sensorML) {
        log.trace("registerPublisher");

        ParameterContainer parameter = new ParameterContainer();

        OperationResult opRes;

        try {

            String defaultTopicDialect = SOSSESConfig.getSesdefaulttopicdialect();
            String defaultTopic =  SOSSESConfig.getSesdefaulttopic();
            String lifetime =SOSSESConfig.getSesregisterpublisherlifetime();
            String localEndpoint =SOSSESConfig.getSesregisterpublisherendpoint();
            String sensorMLString = sensorML.toString();
            parameter.addParameterShell(ISESRequestBuilder.REGISTER_PUBLISHER_SES_URL, this.sesUrl);
            parameter.addParameterShell(ISESRequestBuilder.REGISTER_PUBLISHER_SENSORML, sensorMLString);
            parameter.addParameterShell(ISESRequestBuilder.REGISTER_PUBLISHER_TOPIC_DIALECT, defaultTopicDialect);
            parameter.addParameterShell(ISESRequestBuilder.REGISTER_PUBLISHER_TOPIC, defaultTopic);
            parameter.addParameterShell(ISESRequestBuilder.REGISTER_PUBLISHER_LIFETIME_DURATION, lifetime);
            parameter.addParameterShell(ISESRequestBuilder.REGISTER_PUBLISHER_FROM, localEndpoint);

            log.debug("RegisterPublisher request: \n"
                    + new SESRequestBuilder_01().buildRegisterPublisherRequest(parameter));

            opRes = this.sesAdapter.doOperation(new Operation(SESAdapter.REGISTER_PUBLISHER,
                                                              this.sesUrl + "?",
                                                              this.sesUrl), parameter);

            XmlObject response = this.sesAdapter.handleResponse(SESAdapter.REGISTER_PUBLISHER,
                                                        opRes.getIncomingResultAsStream());

            log.debug("RegisterPublisher response: \n" + response);
            String tmp = response.toString();
            String sesID = tmp.substring(tmp.indexOf('>', tmp.indexOf("ResourceId")) + 1,
                                         tmp.indexOf('<', tmp.indexOf("ResourceId")));

            return sesID;
        }
        catch (OXFException e) {
            log.error("Error while sending registerPublisher request to SES: " + e.getMessage());
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
            log.debug("Configuration is not available (anymore).");
        }
        return null;
    }

    /**
     * Send a given Observation to the SES.
     *
     * @param obsPropType
     *        The given Observation
     * @return true - when sending successful
     * @throws OXFException
     */
    public boolean publishObservation(ObservationPropertyType obsPropType) throws OXFException {
        log.trace("publishObservation()");

        String observationXML = obsPropType.xmlText();
        String[] observations;

        // split in individual observations
        try {
            observations = splitObservations(observationXML);
        }
        catch (Exception e1) {
            log.error("Error while splitting observations. Send observations in one request.");
            createAndSendRequest(observationXML);
            return true;
        }

        // send request as single observation
        for (String observation : observations) {
                createAndSendRequest(observation);
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    //
                }
        }
        return false;
    }

    private void createAndSendRequest(String observationXML) {
        ParameterContainer parameter = new ParameterContainer();
        try {
            parameter.addParameterShell(ISESRequestBuilder.NOTIFY_SES_URL, sesUrl);
            parameter.addParameterShell(ISESRequestBuilder.NOTIFY_TOPIC, topic);
            parameter.addParameterShell(ISESRequestBuilder.NOTIFY_TOPIC_DIALECT, dialect);
            parameter.addParameterShell(ISESRequestBuilder.NOTIFY_XML_MESSAGE, observationXML);

            log.debug("Notify request: \n" + new SESRequestBuilder_01().buildNotifyRequest(parameter));

            this.sesAdapter.doOperation(new Operation(SESAdapter.NOTIFY, this.sesUrl + "?", this.sesUrl), parameter);

        }
        catch (OXFException e) {
            log.error("Error while sending notify message to SES: " + e.getMessage());
        }
        catch (NullPointerException e) {
            log.debug("Response of notify is null");
        }
    }

    /**
     * Gets the content lists.
     *
     * @return the content lists
     */
    public List<String> getContentLists() {
        ArrayList<String> registeredSensors = new ArrayList<String>();

        ParameterContainer parameter = new ParameterContainer();

        OperationResult opsRes;
        try {
            parameter.addParameterShell(ISESRequestBuilder.GET_CAPABILITIES_SES_URL, this.sesUrl);
            opsRes = this.sesAdapter.doOperation(new Operation(SESAdapter.GET_CAPABILITIES,
                                                               this.sesUrl + "?",
                                                               this.sesUrl), parameter);

            Envelope env = EnvelopeDocument.Factory.parse(opsRes.getIncomingResultAsStream()).getEnvelope();
            Body body = env.getBody();

            XmlObject result = XmlObject.Factory.parse(body.toString());
            if (result instanceof CapabilitiesDocument) {
                CapabilitiesDocument capDoc = (CapabilitiesDocument) result;
                if (capDoc.getCapabilities().getContents().isSetRegisteredSensors()) {
                    String[] sensors = capDoc.getCapabilities().getContents().getRegisteredSensors().getSensorIDArray();
                    for (int i = 0; i < sensors.length; i++) {
                        registeredSensors.add(sensors[i]);
                        log.debug("Sensor is in the SES: " + sensors[i]);
                    }
                }
            }
            else {
                log.error("Get no valid capabilities!");
                registeredSensors = null;
                log.debug(result);
            }
        }
        catch (OXFException e) {
            log.error("Error while init SES: " + e.getMessage());
        }
        catch (XmlException e) {
            log.error("Error while init SES: " + e.getMessage());
        }
        catch (IOException e) {
            log.error("Error while init SES: " + e.getMessage());
        }
        return registeredSensors;
    }

    private String[] splitObservations(String inputObservation) throws Exception {

        try {
            // Determining how many observations are contained in the observation collection
            Pattern countPattern = Pattern.compile("<swe:value>(.*?)</swe:value>");
            Matcher countMatcher = countPattern.matcher(inputObservation);
            String countString = null;
            if (countMatcher.find()) {
                countString = countMatcher.group(1).trim();
            }
            int observationCount = Integer.parseInt(countString);

            // This array will contain one observation string for each observation of the observation
            // collection
            String[] outputStrings;

            // If the observation collection contains only one value it can be directly returned
            if (observationCount == 1) {
                outputStrings = new String[]{inputObservation};
            }

            // If the observation collection contains more than one value it must be split
            else {

                // Extracting the values that are contained in the observation collection and creating a
                // StringTokenizer that allows to access the values
                Pattern valuesPattern = Pattern.compile("<swe:values>(.*?)</swe:values>");
                Matcher valuesMatcher = valuesPattern.matcher(inputObservation);
                String valuesString = null;
                if (valuesMatcher.find()) {
                    valuesString = valuesMatcher.group(1).trim();
                }

                // Read the id of the observation collection
                Pattern idPattern =
                        Pattern.compile("ObservationCollection gml:id=\"(.*?)\"(.*?)xsi:schemaLocation=");
                Matcher idMatcher = idPattern.matcher(inputObservation);
                String idString = "";
                if (idMatcher.find()) {
                    idString = idMatcher.group(1).trim();
                }

                StringTokenizer valuesTokenizer = new StringTokenizer(valuesString, ";");
                // If only the latest observation is wished, find youngest
                // observation.
                if (true) {
                    DateTime youngest = new DateTime(0);
                    String youngestValues = "";
                    DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                    while (valuesTokenizer.hasMoreElements()) {
                        String valueString = (String) valuesTokenizer.nextElement();
                        DateTime time = fmt.parseDateTime(valueString.split(",")[0]);
                        if (time.isAfter(youngest.getMillis())) {
                            youngest = time;
                            youngestValues = valueString;
                        }
                    }
                    outputStrings = new String[]{createSingleObservationString(inputObservation, youngestValues)};
                } else {
                    outputStrings = new String[observationCount];

                    for (int i = 0; i < observationCount; i++) {

                        // Add the extracted observation to an array containing
                        // all extracted observations
                        outputStrings[i] = createSingleObservationString(inputObservation, valuesTokenizer.nextToken());
                    }
                }

            }
            // Returning the extracted observations
            return outputStrings;
        }

        catch (Exception e) {
            throw e;
        }
    }

    private String createSingleObservationString(String observation, String individualValuesString) {


        // This string will contain the next extracted single
        // observation
        String singleObservationString = observation;

//        // Replace the id of the observation collection
//        singleObservationString = singleObservationString.replaceFirst(idString, idString + "_" + i);

        // Replace the time period of the observation with the
        // time of the contained observation
        Pattern timePattern = Pattern.compile("(.*?),");
        Matcher timeMatcher = timePattern.matcher(individualValuesString);
        String timeString = "";
        if (timeMatcher.find()) {
            timeString = timeMatcher.group(1).trim();
        }
        singleObservationString =
                singleObservationString.replaceAll("<gml:beginPosition>[^<]*</gml:beginPosition>",
                        "<gml:beginPosition>" + timeString + "</gml:beginPosition>");
        singleObservationString =
                singleObservationString.replaceAll("<gml:endPosition>[^<]*</gml:endPosition>",
                        "<gml:endPosition>" + timeString + "</gml:endPosition>");

        // Set the number of elements in the observation
        // collection to 1
        singleObservationString =
                singleObservationString.replaceAll("<swe:value>[^<]*</swe:value>",
                        "<swe:value>1</swe:value>");

        // Replace the elements in the values element with one
        // singe value
        singleObservationString =
                singleObservationString.replaceAll("<swe:values>[^<]*</swe:values>", "<swe:values>"
                        + individualValuesString + ";</swe:values>");

        return singleObservationString;
    }

    /**
     * @return the closed
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @param closed the closed to set
     */
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    /**
     * add by Yuan for subscribeEvent
     */
    public String subscribeEvent(EMLDocument emlDocument){

        ParameterContainer parameter = new ParameterContainer();
        OperationResult opRes;
        try {

            String sesurl=SOSSESConfig.getSesurl() + SOSSESConfig.getSesbasicporttypepath();
            String defaultTopicDialect = SOSSESConfig.getSesdefaulttopicdialect();
            String defaultTopic = SOSSESConfig.getSesdefaulttopic();
            String lifetime = SOSSESConfig.getSesregisterpublisherlifetime();
            String localEndpoint = SOSSESConfig.getSesregisterpublisherendpoint();
            String emlString = emlDocument.toString();
            parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_SES_URL, sesurl);
            parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT, emlString);
            opRes = this.sesAdapter.doOperation(new Operation(SESAdapter.SUBSCRIBE,
                    sesurl + "?",
                    sesurl), parameter);
            XmlObject response = this.sesAdapter.handleResponse(SESAdapter.SUBSCRIBE,
                    opRes.getIncomingResultAsStream());

            String tmp = response.toString();
            String sesID = tmp.substring(tmp.indexOf('>', tmp.indexOf("ResourceId")) + 1,
                    tmp.indexOf('<', tmp.indexOf("ResourceId")));
            return sesID;
        } catch (OXFException e) {
            e.printStackTrace();
        }
        return null;
    }

}
