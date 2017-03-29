package yuan.flood.sos;

import net.opengis.ows.x11.AcceptVersionsType;
import net.opengis.ows.x11.SectionsType;
import net.opengis.ows.x11.VersionType;
import net.opengis.sos.x10.DescribeSensorDocument;
import net.opengis.sos.x10.GetCapabilitiesDocument;
import net.opengis.sos.x10.GetObservationDocument;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.store.Cursor;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuan on 2017/1/15.
 */
@Component
public class Encode {
    final String sosUrl="http://www.opengis.net/sos/1.0";
    /**
     * create capability request xml
     * @return
     */
    public String getCapability(){
        String capablityXML=null;
        GetCapabilitiesDocument capabilitiesDocument=GetCapabilitiesDocument.Factory.newInstance();
        GetCapabilitiesDocument.GetCapabilities capabilities= capabilitiesDocument.addNewGetCapabilities();
        capabilities.setService("SOS");
        AcceptVersionsType acceptVersionsType= capabilities.addNewAcceptVersions();
        VersionType versionType= acceptVersionsType.addNewVersion();
        versionType.setStringValue("1.0.0");

        SectionsType sectionsType= capabilities.addNewSections();
        XmlString xmlString= sectionsType.addNewSection();
        xmlString.setStringValue("Contents");
        XmlOptions options=new XmlOptions();
        Map<String,String> nameSpace=new HashMap<String,String>();
        nameSpace.put("http://www.opengis.net/sos/1.0","");
        nameSpace.put("http://www.opengis.net/ows/1.1","ows");
        nameSpace.put("http://www.opengis.net/ogc","ogc");
        nameSpace.put("http://www.w3.org/2001/XMLSchema-instance","xsi");
        options.setSaveSuggestedPrefixes(nameSpace);
        options.setSaveAggressiveNamespaces();
        options.setSavePrettyPrint();
        capablityXML=capabilitiesDocument.xmlText(options);
        return capablityXML;
    }
    /**
     * describe sensor xml creatation
     * @param sensorID
     * @return
     */
    public String getDescribeSensorXML(String sensorID){
        //load station xml by StationID using DescribeSensor Operation
        DescribeSensorDocument dsDocument=DescribeSensorDocument.Factory.newInstance();
        DescribeSensorDocument.DescribeSensor describeSensor= dsDocument.addNewDescribeSensor();
        describeSensor.setVersion("1.0.0");
        describeSensor.setService("SOS");
//        File file=new File("DescribeSensor.xml");
//        DescribeSensorDocument describeSensorDocument=DescribeSensorDocument.Factory.parse(file);
//        DescribeSensorDocument.DescribeSensor describeSensor1=describeSensorDocument.getDescribeSensor();
//        String serivce=describeSensor1.getService();
//        String output=describeSensor1.getOutputFormat();

        //add outputFormat or it will be error
        describeSensor.setOutputFormat("text/xml;subtype=\"sensorML/1.0.1\"");
        describeSensor.setProcedure(sensorID);

        //output xml
        XmlOptions options=new XmlOptions();
        options.setSavePrettyPrint();
        String dsXML=dsDocument.xmlText(options);//get describesensor xml
        return dsXML;
    }

    /**
     * get observation information when sensorID and propertyID is given
     * @param sensorID the sensorID for get observation information
     * @param propertyID the propertyID for get observation information
     * @return the GetObservationXML
     */
    public String getGetObservationXML(String sensorID,String propertyID){
        String getObservationXML=null;
        GetObservationDocument getObservationDocument=GetObservationDocument.Factory.newInstance();
        //get the data in past time
        GetObservationDocument.GetObservation observation= getObservationDocument.addNewGetObservation();
        observation.setService("SOS");
        observation.setVersion("1.0.0");
        XmlCursor cursor=observation.newCursor();
        cursor.toFirstContentToken();
        cursor.insertElementWithText(new QName(sosUrl, "offering"), "LIESMARS");
        cursor.toEndToken();
        cursor.insertElementWithText(new QName(sosUrl, "procedure"), sensorID);
        cursor.toEndToken();
        cursor.insertElementWithText(new QName(sosUrl, "observedProperty"), propertyID);
        cursor.toEndToken();
        cursor.insertElementWithText(new QName(sosUrl,"responseFormat"),"text/xml;subtype=\"om/1.0.0\"");
        cursor.dispose();;
        getObservationXML=getObservationDocument.xmlText();
        return getObservationXML;
    }

}
