package yuan.flood.sos;

import net.opengis.gml.ReferenceType;

import net.opengis.om.x10.ObservationCollectionDocument;
import net.opengis.om.x10.ObservationPropertyType;

import net.opengis.sensorML.x101.*;
import net.opengis.sos.x10.CapabilitiesDocument;

import net.opengis.swe.x10.impl.DataValuePropertyTypeImpl;
import net.opengis.swe.x101.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;

import org.apache.xmlbeans.XmlObject;
import org.springframework.stereotype.Component;
import yuan.flood.dao.Entity.ObservedProperty;
import yuan.flood.dao.Entity.Sensor;

import javax.xml.namespace.QName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Yuan on 2017/1/15.
 */
@Component(value = "decode")
public class Decode {

    /**
     * get sensorID by analysis capability xml
     * @param capabilityXML
     * @return sensorIDs
     * @throws Exception
     */
    public List<String> decodeCapability(String capabilityXML) throws Exception {
        List<String> sensorIDs=new ArrayList<String>();
        try {
            CapabilitiesDocument capabilitiesDocument=CapabilitiesDocument.Factory.parse(capabilityXML);
            CapabilitiesDocument.Capabilities capabilities= capabilitiesDocument.getCapabilities();
            ReferenceType[] procedures= capabilities.getContents().getObservationOfferingList().getObservationOfferingArray(0).getProcedureArray();
            for (int i=0;i<procedures.length;i++){
               String sensorID= procedures[i].getHref();
                sensorIDs.add(sensorID);
            }
        }
        catch (Exception e){
           throw new Exception("½âÎö´íÎó");
        }

        return sensorIDs;
    }

    /**
     * decode describesensor xml to get sensor
     * @param sensorXML
     * @return
     * @throws XmlException
     */
    public Sensor decodeDescribeSensor(String sensorXML) throws XmlException {
        Sensor sensor=new Sensor();
        //parse the station sensorML to get the sensor name under the station
        SensorMLDocument parseSationDocument=SensorMLDocument.Factory.parse(sensorXML);
        SensorMLDocument.SensorML sensorML1=parseSationDocument.getSensorML();
        AbstractProcessType xb_prc=parseSationDocument.getSensorML().getMemberArray(0).getProcess();
        SystemType staSystem=(SystemType)xb_prc;

        //get sensor id and sensor name
        IdentificationDocument.Identification.IdentifierList.Identifier[] identifiers= staSystem.getIdentificationArray(0).getIdentifierList().getIdentifierArray();
        for (int i=0;i<identifiers.length;i++){
            if (identifiers[i].getTerm().getDefinition().equalsIgnoreCase("urn:ogc:def:identifier:OGC:1.0:uniqueID")
                    ||identifiers[i].getTerm().getDefinition().equalsIgnoreCase("urn:ogc:def:identifier:OGC:uniqueID")){
                sensor.setSensorID(identifiers[i].getTerm().getValue());
            }else if (identifiers[i].getTerm().getDefinition().equalsIgnoreCase("urn:ogc:def:identifier:OGC:1.0:longName")){
                sensor.setSensorName(identifiers[i].getTerm().getValue());
            }
        }

        //get lat and lon
        if (staSystem.getPosition()!=null) {
            PositionType positionType = staSystem.getPosition().getPosition();
            //get latitude and longtitude
            VectorType.Coordinate[] coordinates = positionType.getLocation().getVector().getCoordinateArray();
            for (VectorType.Coordinate coordinate : coordinates) {
                String axisID = coordinate.getQuantity().getAxisID();
                if (axisID.equalsIgnoreCase("x")) sensor.setLon(coordinate.getQuantity().getValue());
                else if (axisID.equalsIgnoreCase("y")) sensor.setLat(coordinate.getQuantity().getValue());
            }
        }

        //get ObservationProperties
        //get the sensor property and every property is set by quantity class

        IoComponentPropertyType[] properties =staSystem.getOutputs().getOutputList().getOutputArray();
        Set<ObservedProperty> observedProperties=new HashSet<ObservedProperty>();
        for (IoComponentPropertyType property:properties){
            ObservedProperty obsProperty=new ObservedProperty();
            //get the property name
            String propertyName= property.getName();
            //get the property ID
            String propertyID=property.getQuantity().getDefinition();
            //get property unit
            String propertyUnit=property.getQuantity().getUom().getCode();
            obsProperty.setPropertyID(propertyID);
            obsProperty.setPropertyName(propertyName);
            obsProperty.setUnit(propertyUnit);
            observedProperties.add(obsProperty);
            sensor.setObservedProperties(observedProperties);
        }

        return sensor;
    }

    /**
     * decode observation information
     * @param observationResponseXML the response xml of observation
     * @return the datatimeseries list
     * @throws XmlException
     */

    public List<DataTimeSeries> decodeObservation(String observationResponseXML) throws XmlException {
        List<DataTimeSeries> dataTimeSeries=new ArrayList<DataTimeSeries>();
        ObservationCollectionDocument observationCollectionDocument=ObservationCollectionDocument.Factory.parse(observationResponseXML);
        ObservationPropertyType[] members= observationCollectionDocument.getObservationCollection().getMemberArray();
        if (members!= null) {
        for (int j=0;j<members.length;j++) {
            ObservationPropertyType member = members[j];
            if (member.getObservation() != null) {
                XmlObject resultObj = member.getObservation().getResult();
                XmlObject[] objects = resultObj.selectChildren(new QName("http://www.opengis.net/swe/1.0.1", "DataArray"));
                if (objects != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                    DataArrayType dataArrayType = (DataArrayType) objects[0];
                    TextBlockDocument.TextBlock textBlock = dataArrayType.getEncoding().getTextBlock();
                    String blockStr = textBlock.getBlockSeparator();
                    String decimalStr = textBlock.getDecimalSeparator();
                    String tokenStr = textBlock.getTokenSeparator();

                    DataValuePropertyType dataValue = dataArrayType.getValues();
                    XmlCursor xmlCursor = dataValue.newCursor();
                    String valuesStr = xmlCursor.getTextValue();

                    String[] allValues = valuesStr.split(blockStr);
                    for (int i = 0; i < allValues.length; i++) {
                        DataTimeSeries dataTime = new DataTimeSeries();
                        String[] timeAndValue = allValues[i].split(tokenStr);
                        String timeStr = timeAndValue[0].replace("+08:00", "+0800");
                        Date date = null;
                        try {
                            date = simpleDateFormat.parse(timeStr);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        dataTime.setTimeLon(date.getTime());
                        dataTime.setTimeStr(timeAndValue[0]);
                        dataTime.setDataValue(Double.valueOf(timeAndValue[1]));
                        dataTimeSeries.add(dataTime);
                    }
                }
            }
        }
        }
        return dataTimeSeries;
    }

}
