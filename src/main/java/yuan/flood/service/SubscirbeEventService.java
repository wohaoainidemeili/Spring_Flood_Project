package yuan.flood.service;

import net.opengis.eml.x001.ComplexPatternType;
import net.opengis.eml.x001.EMLDocument;
import net.opengis.eml.x001.SimplePatternType;
import org.apache.xmlbeans.XmlCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.Entity.UIEntity.SubscribeEventParamsDTO;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.service.IService.ISubScribeEventService;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuan on 2017/2/14.
 */
@Service
public class SubscirbeEventService implements ISubScribeEventService {
    final String fes = "http://www.opengis.net/fes/2.0";
    final String swe = "http://www.opengis.net/swe/1.0.1";
    final String emlurl = "http://www.opengis.net/eml/0.0.1";

    @Autowired
    private ISensorService sensorService;
    @Override

    public EMLDocument createSubscirbeEvent(SubscibeEventParams subscibeEventParams) {

        Long order = subscibeEventParams.getOrder();
        EMLDocument emlDocument = EMLDocument.Factory.newInstance();
        EMLDocument.EML eml = emlDocument.addNewEML();

        EMLDocument.EML.SimplePatterns simplePatterns = eml.addNewSimplePatterns();
        SimplePatternType diagnosisSimplePattern = simplePatterns.addNewSimplePattern();
        SimplePatternType diagnosisSimplePattern1 = simplePatterns.addNewSimplePattern();
        // SimplePatternType prepareSimplePattren1=simplePatterns.addNewSimplePattern();
        SimplePatternType prepareSimplePattern2 = simplePatterns.addNewSimplePattern();
        SimplePatternType prepareSimplePattern3 = simplePatterns.addNewSimplePattern();
        SimplePatternType responseSimplePattern1 = simplePatterns.addNewSimplePattern();
        SimplePatternType responseSimlePattern2 = simplePatterns.addNewSimplePattern();
        SimplePatternType recoverySimplePattern1 = simplePatterns.addNewSimplePattern();
        SimplePatternType recoverySimplePattern2 = simplePatterns.addNewSimplePattern();

        EMLDocument.EML.ComplexPatterns complexPatterns = eml.addNewComplexPatterns();
        ComplexPatternType preparePattern = complexPatterns.addNewComplexPattern();
        ComplexPatternType responsePattern = complexPatterns.addNewComplexPattern();
        ComplexPatternType recoveryPattern = complexPatterns.addNewComplexPattern();

        eml.addNewTimerPatterns();
        eml.addNewRepetitivePatterns();
        //**********diagnosis first part******************
        XmlCursor cursor = null;

        cursor = diagnosisSimplePattern.newCursor();
        cursor.addToSelection();
        cursor.toFirstContentToken();
        cursor.insertAttributeWithValue("inputName", "input");
        cursor.insertAttributeWithValue("patternID", "diagnosis_stream");

        cursor.beginElement(new QName(emlurl, "SelectFunctions"));
        cursor.beginElement(new QName(emlurl, "SelectFunction"));
        cursor.insertAttributeWithValue("newEventName", "diagnosis_event");
        cursor.beginElement(new QName(emlurl, "SelectCount"));
//        cursor.insertElementWithText(new QName(emlurl,"Message"), subscibeEventParams.getEventID()+":diagnosis");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "View", "eml"));
        cursor.beginElement(new QName(emlurl, "TimeView"));
        cursor.insertAttributeWithValue("isBatch", "false");
        cursor.insertElementWithText(new QName(emlurl, "Duration"), "PT" + subscibeEventParams.getDiagnosisDay() * 24 + subscibeEventParams.getDiagnosisHour() + "H" + subscibeEventParams.getDiagnosisMinute() + "M" + subscibeEventParams.getDiagnosisSecond() + "S");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "Guard"));
        cursor.beginElement(new QName(fes, "Filter", "fes"));
        cursor.beginElement(new QName(fes, "PropertyIsLessThan", "fes"));
        cursor.insertElementWithText(new QName(fes, "ValueReference", "fes"), "input/doubleValue");
        cursor.beginElement(new QName(fes, "Literal", "fes"));
        cursor.beginElement(new QName(swe, "Quantity", "swe"));
        cursor.insertElementWithText(new QName(swe, "value", "swe"), subscibeEventParams.getDiagnosisThreshold().toString());
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "PropertyRestrictions"));
        cursor.beginElement(new QName(emlurl, "PropertyRestriction"));
        cursor.insertElementWithText(new QName(emlurl, "name"), "observedProperty");
        cursor.insertElementWithText(new QName(emlurl, "value"), subscibeEventParams.getDiagnosisObservation());
        cursor.toParent();
        cursor.beginElement(new QName(emlurl, "PropertyRestriction"));
        cursor.insertElementWithText(new QName(emlurl, "name"), "sensorID");
        cursor.insertElementWithText(new QName(emlurl, "value"), subscibeEventParams.getDiagnosisSensor());
        cursor.dispose();

        //***************diagnosis second part***************//
        cursor = diagnosisSimplePattern1.newCursor();
        cursor.addToSelection();
        cursor.toFirstContentToken();
        cursor.insertAttributeWithValue("inputName", "diagnosis_event");
        cursor.insertAttributeWithValue("patternID", "diagnosis_result_stream");

        cursor.beginElement(new QName(emlurl, "SelectFunctions"));
        cursor.beginElement(new QName(emlurl, "SelectFunction"));
        cursor.insertAttributeWithValue("createCausality", "false");
        cursor.insertAttributeWithValue("newEventName", "diagnosisCom_event");
        cursor.insertAttributeWithValue("outputName", "diagnosis_out");
        cursor.beginElement(new QName(emlurl, "NotifyOnSelect"));
        cursor.insertElementWithText(new QName(emlurl, "Message"), subscibeEventParams.getEventID() + ":diagnosis");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "View"));
        cursor.beginElement(new QName(emlurl, "LengthView"));
        cursor.insertElementWithText(new QName(emlurl, "EventCount"), "1");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "Guard"));
        cursor.beginElement(new QName(fes, "Filter", "fes"));
        cursor.beginElement(new QName(fes, "PropertyIsGreaterThan", "fes"));
        cursor.insertElementWithText(new QName(fes, "ValueReference", "fes"), "diagnosis_event/doubleValue");
        cursor.insertElementWithText(new QName(fes, "Literal", "fes"), subscibeEventParams.getDiagnosisRepeatTimes().toString());
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "PropertyRestrictions"));
        cursor.dispose();
        //***************prepare simplePattern one***********//
//
//        cursor=prepareSimplePattren1.newCursor();
//        cursor.addToSelection();
//        cursor.toFirstContentToken();
//        cursor.insertAttributeWithValue("inputName","input");
//        cursor.insertAttributeWithValue("patternID", "diagnosis_prepare_stream");
//
//        cursor.beginElement(new QName(emlurl,"SelectFunctions"));
//        cursor.beginElement(new QName(emlurl,"SelectFunction"));
//        cursor.insertAttributeWithValue("createCausality","false");
//        cursor.insertAttributeWithValue("newEventName", "diagnosis_prepare_event");
//        cursor.beginElement(new QName(emlurl, "SelectEvent"));
//        cursor.insertAttributeWithValue("eventName","input");
//        cursor.toSelection(0);
//        cursor.toEndToken();
//        cursor.beginElement(new QName(emlurl,"View"));
//        cursor.beginElement(new QName(emlurl,"LengthView"));
//        cursor.insertElementWithText(new QName(emlurl,"EventCount"), "1");
//        cursor.toSelection(0);
//        cursor.toEndToken();
//        cursor.beginElement(new QName(emlurl,"Guard"));
//        cursor.beginElement(new QName(fes,"Filter","fes"));
//        cursor.beginElement(new QName(fes,"PropertyIsLessThan","fes"));
//        cursor.insertElementWithText(new QName(fes, "ValueReference", "fes"), "input/doubleValue");
//        cursor.beginElement(new QName(fes, "Literal", "fes"));
//        cursor.beginElement(new QName(swe,"Quantity","swe"));
//        cursor.insertElementWithText(new QName(swe, "value", "swe"), subscibeEventParams.getDiagnosisThreshold().toString());
//        cursor.toSelection(0);
//        cursor.toEndToken();
//        cursor.beginElement(new QName(emlurl,"PropertyRestrictions"));
//        cursor.beginElement(new QName(emlurl,"PropertyRestriction"));
//        cursor.insertElementWithText(new QName(emlurl,"name"),"observedProperty");
//        cursor.insertElementWithText(new QName(emlurl,"value"),subscibeEventParams.getDiagnosisObservation());
//        cursor.toParent();
//        cursor.beginElement(new QName(emlurl,"PropertyRestriction"));
//        cursor.insertElementWithText(new QName(emlurl,"name"),"sensorID");
//        cursor.insertElementWithText(new QName(emlurl,"value"),subscibeEventParams.getDiagnosisSensor());
//        cursor.dispose();


        //***************prepare simplepattern 2************//
        cursor = prepareSimplePattern2.newCursor();
        cursor.addToSelection();
        cursor.toFirstContentToken();
        cursor.insertAttributeWithValue("inputName", "input");
        cursor.insertAttributeWithValue("patternID", "prepare_stream");

        cursor.beginElement(new QName(emlurl, "SelectFunctions"));
        cursor.beginElement(new QName(emlurl, "SelectFunction"));
        cursor.insertAttributeWithValue("newEventName", "prepare_event");
        cursor.beginElement(new QName(emlurl, "SelectCount"));
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "View"));
        cursor.beginElement(new QName(emlurl, "TimeView"));
        cursor.insertAttributeWithValue("isBatch", "false");
        cursor.insertElementWithText(new QName(emlurl, "Duration"), "PT" + subscibeEventParams.getPrepareDay() * 24 + subscibeEventParams.getPrepareHour() + "H" + subscibeEventParams.getPrepareMinute() + "M" + subscibeEventParams.getPrepareSecond() + "S");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "Guard"));
        cursor.beginElement(new QName(fes, "Filter", "fes"));
        cursor.beginElement(new QName(fes, "And", "fes"));
        cursor.addToSelection();
        cursor.beginElement(new QName(fes, "PropertyIsGreaterThan", "fes"));
        cursor.insertElementWithText(new QName(fes, "ValueReference", "fes"), "input/doubleValue");
        cursor.beginElement(new QName(fes, "Literal", "fes"));
        cursor.beginElement(new QName(swe, "Quantity", "swe"));
        cursor.insertElementWithText(new QName(swe, "value", "swe"), subscibeEventParams.getPrepareThreshold().toString());
        cursor.toSelection(1);
        cursor.beginElement(new QName(fes, "PropertyIsLessThan", "fes"));
        cursor.insertElementWithText(new QName(fes, "ValueReference", "fes"), "input/doubleValue");
        cursor.beginElement(new QName(fes, "Literal", "fes"));
        cursor.beginElement(new QName(swe, "Quantity", "swe"));
        cursor.insertElementWithText(new QName(swe, "value", "swe"), subscibeEventParams.getResponseThreshold().toString());
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "PropertyRestrictions"));
        cursor.beginElement(new QName(emlurl, "PropertyRestriction"));
        cursor.insertElementWithText(new QName(emlurl, "name"), "observedProperty");
        cursor.insertElementWithText(new QName(emlurl, "value"), subscibeEventParams.getPrepareObservation());
        cursor.toParent();
        cursor.beginElement(new QName(emlurl, "PropertyRestriction"));
        cursor.insertElementWithText(new QName(emlurl, "name"), "sensorID");
        cursor.insertElementWithText(new QName(emlurl, "value"), subscibeEventParams.getPrepareSensor());
        cursor.dispose();

        //*********************prepare simplepattern 3***************
        cursor = prepareSimplePattern3.newCursor();
        cursor.addToSelection();
        cursor.toFirstContentToken();
        cursor.insertAttributeWithValue("inputName", "prepare_event");
        cursor.insertAttributeWithValue("patternID", "prepare_result_stream");

        cursor.beginElement(new QName(emlurl, "SelectFunctions"));
        cursor.beginElement(new QName(emlurl, "SelectFunction"));
        cursor.insertAttributeWithValue("newEventName", "prepareCom_event");
        cursor.beginElement(new QName(emlurl, "SelectEvent"));
        cursor.insertAttributeWithValue("eventName", "prepare_event");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "View"));
        cursor.beginElement(new QName(emlurl, "LengthView"));
        cursor.insertElementWithText(new QName(emlurl, "EventCount"), "1");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "Guard"));
        cursor.beginElement(new QName(fes, "Filter", "fes"));
        cursor.beginElement(new QName(fes, "PropertyIsGreaterThan", "fes"));
        cursor.insertElementWithText(new QName(fes, "ValueReference", "fes"), "prepare_event/doubleValue");
        cursor.insertElementWithText(new QName(fes, "Literal", "fes"), subscibeEventParams.getPrepareRepeatTimes().toString());
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "PropertyRestrictions"));
        cursor.dispose();
        // cursor

        //************response simplepattern 1 ****************
        cursor = responseSimplePattern1.newCursor();
        cursor.addToSelection();
        cursor.toFirstContentToken();
        cursor.insertAttributeWithValue("inputName", "input");
        cursor.insertAttributeWithValue("patternID", "response_stream");

        cursor.beginElement(new QName(emlurl, "SelectFunctions"));
        cursor.beginElement(new QName(emlurl, "SelectFunction"));
        cursor.insertAttributeWithValue("newEventName", "response_event");
        cursor.beginElement(new QName(emlurl, "SelectCount"));
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "View"));
        cursor.beginElement(new QName(emlurl, "TimeView"));
        cursor.insertAttributeWithValue("isBatch", "false");
        cursor.insertElementWithText(new QName(emlurl, "Duration"), "PT" + subscibeEventParams.getResponseDay() * 24 + subscibeEventParams.getResponseHour() + "H" + subscibeEventParams.getResponseMinute() + "M" + subscibeEventParams.getResponseSecond() + "S");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "Guard"));
        cursor.beginElement(new QName(fes, "Filter", "fes"));
        cursor.beginElement(new QName(fes, "PropertyIsGreaterThan", "fes"));
        cursor.insertElementWithText(new QName(fes, "ValueReference", "fes"), "input/doubleValue");
        cursor.beginElement(new QName(fes, "Literal", "fes"));
        cursor.beginElement(new QName(swe, "Quantity", "swe"));
        cursor.insertElementWithText(new QName(swe, "value", "swe"), subscibeEventParams.getResponseThreshold().toString());
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "PropertyRestrictions"));
        cursor.beginElement(new QName(emlurl, "PropertyRestriction"));
        cursor.insertElementWithText(new QName(emlurl, "name"), "observedProperty");
        cursor.insertElementWithText(new QName(emlurl, "value"), subscibeEventParams.getResponseObservation());
        cursor.toParent();
        cursor.beginElement(new QName(emlurl, "PropertyRestriction"));
        cursor.insertElementWithText(new QName(emlurl, "name"), "sensorID");
        cursor.insertElementWithText(new QName(emlurl, "value"), subscibeEventParams.getResponseSensor());
        cursor.dispose();
        //**************response simplepattern 2
        cursor = responseSimlePattern2.newCursor();
        cursor.addToSelection();
        cursor.toFirstContentToken();
        cursor.insertAttributeWithValue("inputName", "response_event");
        cursor.insertAttributeWithValue("patternID", "response_result_stream");

        cursor.beginElement(new QName(emlurl, "SelectFunctions"));
        cursor.beginElement(new QName(emlurl, "SelectFunction"));
        cursor.insertAttributeWithValue("newEventName", "responseCom_event");
        cursor.beginElement(new QName(emlurl, "SelectEvent"));
        cursor.insertAttributeWithValue("eventName", "response_event");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "View"));
        cursor.beginElement(new QName(emlurl, "LengthView"));
        cursor.insertElementWithText(new QName(emlurl, "EventCount"), "1");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "Guard"));
        cursor.beginElement(new QName(fes, "Filter", "fes"));
        cursor.beginElement(new QName(fes, "PropertyIsGreaterThan", "fes"));
        cursor.insertElementWithText(new QName(fes, "ValueReference", "fes"), "response_event/doubleValue");
        cursor.insertElementWithText(new QName(fes, "Literal", "fes"), subscibeEventParams.getResponseRepeatTimes().toString());
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "PropertyRestrictions"));
        cursor.dispose();

        //******************recovery simplepattern 1*************
        cursor = recoverySimplePattern1.newCursor();
        cursor.addToSelection();
        cursor.toFirstContentToken();
        cursor.insertAttributeWithValue("inputName", "input");
        cursor.insertAttributeWithValue("patternID", "recovery_stream");

        cursor.beginElement(new QName(emlurl, "SelectFunctions"));
        cursor.beginElement(new QName(emlurl, "SelectFunction"));
        cursor.insertAttributeWithValue("newEventName", "recovery_event");
        cursor.beginElement(new QName(emlurl, "SelectCount"));
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "View"));
        cursor.beginElement(new QName(emlurl, "TimeView"));
        cursor.insertAttributeWithValue("isBatch", "false");
        cursor.insertElementWithText(new QName(emlurl, "Duration"), "PT" + subscibeEventParams.getRecoveryDay() * 24 + subscibeEventParams.getRecoveryHour() + "H" + subscibeEventParams.getRecoveryMinute() + "M" + subscibeEventParams.getRecoverySecond() + "S");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "Guard"));
        cursor.beginElement(new QName(fes, "Filter", "fes"));
        cursor.beginElement(new QName(fes, "PropertyIsLessThan", "fes"));
        cursor.insertElementWithText(new QName(fes, "ValueReference", "fes"), "input/doubleValue");
        cursor.beginElement(new QName(fes, "Literal", "fes"));
        cursor.beginElement(new QName(swe, "Quantity", "swe"));
        cursor.insertElementWithText(new QName(swe, "value", "swe"), subscibeEventParams.getRecoveryThreshold().toString());
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "PropertyRestrictions"));
        cursor.beginElement(new QName(emlurl, "PropertyRestriction"));
        cursor.insertElementWithText(new QName(emlurl, "name"), "observedProperty");
        cursor.insertElementWithText(new QName(emlurl, "value"), subscibeEventParams.getRecoveryObservation());
        cursor.toParent();
        cursor.beginElement(new QName(emlurl, "PropertyRestriction"));
        cursor.insertElementWithText(new QName(emlurl, "name"), "sensorID");
        cursor.insertElementWithText(new QName(emlurl, "value"), subscibeEventParams.getRecoverySensor());
        cursor.dispose();

        //****************recovery simplepattern 2**********************
        cursor = recoverySimplePattern2.newCursor();
        cursor.addToSelection();
        cursor.toFirstContentToken();
        cursor.insertAttributeWithValue("inputName", "recovery_event");
        cursor.insertAttributeWithValue("patternID", "recovery_result_stream");

        cursor.beginElement(new QName(emlurl, "SelectFunctions"));
        cursor.beginElement(new QName(emlurl, "SelectFunction"));
        cursor.insertAttributeWithValue("newEventName", "recoveryCom_event");
        cursor.beginElement(new QName(emlurl, "SelectEvent"));
        cursor.insertAttributeWithValue("eventName", "recovery_event");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "View"));
        cursor.beginElement(new QName(emlurl, "LengthView"));
        cursor.insertElementWithText(new QName(emlurl, "EventCount"), "1");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "Guard"));
        cursor.beginElement(new QName(fes, "Filter", "fes"));
        cursor.beginElement(new QName(fes, "PropertyIsGreaterThan", "fes"));
        cursor.insertElementWithText(new QName(fes, "ValueReference", "fes"), "recovery_event/doubleValue");
        cursor.insertElementWithText(new QName(fes, "Literal", "fes"), subscibeEventParams.getRecoveryRepeatTimes().toString());
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "PropertyRestrictions"));
        cursor.dispose();

        //*********** prepare complext pattern ****************
        cursor = preparePattern.newCursor();
        cursor.addToSelection();
        cursor.toFirstContentToken();
        cursor.insertAttributeWithValue("patternID", "diagnosis_prepare_compare_stream");
        cursor.beginElement(new QName(emlurl, "SelectFunctions"));
        cursor.beginElement(new QName(emlurl, "SelectFunction"));
        cursor.insertAttributeWithValue("createCausality", "true");
        cursor.insertAttributeWithValue("newEventName", "diagnosis_prepare_compare_event");
        cursor.insertAttributeWithValue("outputName", "prepareOut");
        cursor.beginElement(new QName(emlurl, "NotifyOnSelect"));
        cursor.insertElementWithText(new QName(emlurl, "Message"), subscibeEventParams.getEventID() + ":prepare");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "StructuralOperator"));
        cursor.beginElement(new QName(emlurl, "BEFORE"));
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "FirstPattern"));
        cursor.insertElementWithText(new QName(emlurl, "PatternReference"), "diagnosis_result_stream");
        cursor.insertElementWithText(new QName(emlurl, "SelectFunctionNumber"), "0");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "SecondPattern"));
        cursor.insertElementWithText(new QName(emlurl, "PatternReference"), "prepare_result_stream");
        cursor.insertElementWithText(new QName(emlurl, "SelectFunctionNumber"), "0");

        //*****************response complex pattern *************
        cursor = responsePattern.newCursor();
        cursor.addToSelection();
        cursor.toFirstContentToken();
        cursor.insertAttributeWithValue("patternID", "diagnosis_prepare_response_stream");
        cursor.beginElement(new QName(emlurl, "SelectFunctions"));
        cursor.beginElement(new QName(emlurl, "SelectFunction"));
        cursor.insertAttributeWithValue("createCausality", "true");
        cursor.insertAttributeWithValue("newEventName", "diagnosis_prepare_response_event");
        cursor.insertAttributeWithValue("outputName", "responseOut");
        cursor.beginElement(new QName(emlurl, "NotifyOnSelect"));
        cursor.insertElementWithText(new QName(emlurl, "Message"), subscibeEventParams.getEventID() + ":response");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "StructuralOperator"));
        cursor.beginElement(new QName(emlurl, "BEFORE"));
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "FirstPattern"));
        cursor.insertElementWithText(new QName(emlurl, "PatternReference"), "diagnosis_prepare_compare_stream");
        cursor.insertElementWithText(new QName(emlurl, "SelectFunctionNumber"), "0");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "SecondPattern"));
        cursor.insertElementWithText(new QName(emlurl, "PatternReference"), "response_result_stream");
        cursor.insertElementWithText(new QName(emlurl, "SelectFunctionNumber"), "0");

        //***************recovery complex pattern ****************
        cursor = recoveryPattern.newCursor();
        cursor.addToSelection();
        cursor.toFirstContentToken();
        cursor.insertAttributeWithValue("patternID", "diagnosis_prepare_response_recovery_stream");
        cursor.beginElement(new QName(emlurl, "SelectFunctions"));
        cursor.beginElement(new QName(emlurl, "SelectFunction"));
        cursor.insertAttributeWithValue("createCausality", "true");
        cursor.insertAttributeWithValue("newEventName", "diagnosis_prepare_response_recovery_event");
        cursor.insertAttributeWithValue("outputName", "recoveryOut");
        cursor.beginElement(new QName(emlurl, "NotifyOnSelect"));
        cursor.insertElementWithText(new QName(emlurl, "Message"), subscibeEventParams.getEventID() + ":recovery");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "StructuralOperator"));
        cursor.beginElement(new QName(emlurl, "BEFORE"));
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "FirstPattern"));
        cursor.insertElementWithText(new QName(emlurl, "PatternReference"), "diagnosis_prepare_response_stream");
        cursor.insertElementWithText(new QName(emlurl, "SelectFunctionNumber"), "0");
        cursor.toSelection(0);
        cursor.toEndToken();
        cursor.beginElement(new QName(emlurl, "SecondPattern"));
        cursor.insertElementWithText(new QName(emlurl, "PatternReference"), "recovery_result_stream");
        cursor.insertElementWithText(new QName(emlurl, "SelectFunctionNumber"), "0");
        // cursor.beginElement();
        cursor.dispose();

//        XmlOptions options=new XmlOptions();
//        Map<String,String> nameSpace=new HashMap<String,String>();
//        nameSpace.put("http://www.opengis.net/fes/2.0","fes");
//        nameSpace.put("http://www.opengis.net/swe/1.0.1","swe");
//        options.setSaveSuggestedPrefixes(nameSpace);
//        options.setSaveAggressiveNamespaces();
//        options.setSavePrettyPrint();
//        //options.setCharacterEncoding("UTF-8")
//        request= emlDocument.xmlText();

        return emlDocument;
    }

    @Override
    public SubscibeEventParams getEventSpatialArea(SubscibeEventParams subscibeEventParams) {
        List<Sensor> sensorList = getEventSensorList(subscibeEventParams);
        double maxLat = -Double.MAX_VALUE;
        double maxLon = -Double.MAX_VALUE;
        double minLat = Double.MAX_VALUE;
        double minLon = Double.MIN_VALUE;

        for (int i=0;i<sensorList.size();i++) {
            double lat = sensorList.get(i).getLat();
            double lon = sensorList.get(i).getLon();
            if (lat > maxLat) {
                maxLat = lat;
            }
            if (lat < minLat) {
                minLat = lat;
            }
            if (lon > maxLon) {
                maxLon = lon;
            }
            if (lon < minLon) {
                minLon = lon;
            }
        }
        subscibeEventParams.setMaxLat(maxLat);
        subscibeEventParams.setMinLat(minLat);
        subscibeEventParams.setMaxLon(maxLon);
        subscibeEventParams.setMinLon(minLon);
        return subscibeEventParams;
    }

    @Override
    public List<Sensor> getEventSensorList(SubscibeEventParams subscibeEventParams) {
        List<Sensor> sensorList = new ArrayList<Sensor>();
        //获取所有的传感器信息
        List<Sensor> sensorsDiagnosis = sensorService.findObseredPropertyBySensorID(subscibeEventParams.getDiagnosisSensor());
        List<Sensor> sensorsPrepare = sensorService.findObseredPropertyBySensorID(subscibeEventParams.getPrepareSensor());
        List<Sensor> sensorsResponse = sensorService.findObseredPropertyBySensorID(subscibeEventParams.getResponseSensor());
        List<Sensor> sensorsRecovery = sensorService.findObseredPropertyBySensorID(subscibeEventParams.getRecoverySensor());

        sensorList.addAll(sensorsDiagnosis);
        sensorList.addAll(sensorsPrepare);
        sensorList.addAll(sensorsResponse);
        sensorList.addAll(sensorsRecovery);

        return sensorList;
    }

}
