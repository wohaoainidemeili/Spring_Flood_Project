package yuan.flood.service;

import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.DetectedEvent;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.IDao.IDetectedEventDao;
import yuan.flood.dao.IDao.IEventDao;
import yuan.flood.service.IService.IEventService;
import yuan.flood.sos.DataTimeSeries;
import yuan.flood.sos.Decode;
import yuan.flood.sos.Encode;
import yuan.flood.sos.HighChartsDrawData;
import yuan.flood.until.HttpMethods;

import java.util.*;

/**
 * Created by Yuan on 2017/2/16.
 */
@Service
public class EventService implements IEventService{
    @Autowired
    private Decode decode;
    @Autowired
    private Encode encode;
    @Autowired
    private HttpMethods methods;
    @Autowired
    private IEventDao eventDao;
    @Autowired
    private IDetectedEventDao detectedEventDao;

    @Override
    public List<String> getAllEventIDs() {
        String findEventIDStr="select s.eventID from SubscibeEventParams s";
        String findEventCountStr="select count(*) from SubscibeEventParams s";
        List<Long> count=eventDao.find(findEventCountStr);
        Long countN=count.get(0);
        if (countN==0){
           return null;
        }else {
            List<String> eventIDs=eventDao.find(findEventIDStr);
            return  eventIDs;
        }
    }

    @Override
    public void saveSubscribeEvent(SubscibeEventParams subscibeEventParams) {
        eventDao.save(subscibeEventParams);
    }

    @Override
    public void saveDetectedEvent(DetectedEvent detectedEvent) {
        detectedEventDao.saveOrUpdate(detectedEvent);
    }

    @Override
    public Long getMaxEventOrder() {
        String findCountStr="select count(*) from SubscibeEventParams s";
        String findMaxOrderStr="select max(s.order) from SubscibeEventParams s";
        List<Long> count=eventDao.find(findCountStr);
        Long countN=count.get(0);
        if (countN==0){
            return 0L;
        }else {
           List<Long> maxorder= eventDao.find(findMaxOrderStr);
           return maxorder.get(0)+1;
        }
    }

    @Override
    public String getDetectedEventBySESID(String url,String sesid) {
        String seriesRes=null;
        List<DataTimeSeries> dataTimeSeries=null;
        //get different sensor;
        SubscibeEventParams subscibeEventParams= getRegisteredEventParamsBySesid(sesid);
        String diagnosissSensorID=subscibeEventParams.getDiagnosisSensor();
        String prepareSensorID=subscibeEventParams.getPrepareSensor();
        String responseSensorID=subscibeEventParams.getResponseSensor();
        String recoverySensorID=subscibeEventParams.getRecoverySensor();
        List<String> differSensors=new ArrayList<String>();
        differSensors.add(diagnosissSensorID);
        if (!differSensors.contains(prepareSensorID)){
            differSensors.add(prepareSensorID);
        }
        if (!differSensors.contains(responseSensorID)){
            differSensors.add(responseSensorID);
        }
        if (!differSensors.contains(recoverySensorID)){
            differSensors.add(recoverySensorID);
        }
        //try show different sensor observation,if the subscribe contains different sensors
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("[");
        int Count=0;
        for(String SensorID:differSensors){
            if (Count==0)
                stringBuilder.append("{");
            else stringBuilder.append(",{");
            Count++;
            String observationRequestXML= encode.getGetObservationXML(subscibeEventParams.getDiagnosisSensor(), subscibeEventParams.getDiagnosisObservation());
            String responseXML= methods.sendPost(url, observationRequestXML);
            try {
               //get data form observation
                dataTimeSeries= decode.decodeObservation(responseXML);
                if (dataTimeSeries==null||dataTimeSeries.size()==0) {
                      stringBuilder.append("}");
                    continue;
                }
                //get event form event database
                String findStr="from DetectedEvent d where d.event.eventID='"+sesid+"' order by d.startTimeLong";
                List events= detectedEventDao.find(findStr);

                //get different type of information
                List<DetectedEvent> differEvents= getTimeChange(events);
                //get all type information

                //create series json using the data
                stringBuilder.append("name:"+"'"+SensorID+"',"+ "type: 'areaspline',"
                        +"zoneAxis: 'x',"
                        +" tooltip: {shared: true,useHTML: true, headerFormat: '<small>{point.key}</small><br/><table>'" +
                        ",pointFormat: '<tr><td style=\"color: {series.color}\">{series.name}: </td>"
                +"<td style=\"text-align: right\"><b>{point.y}m</b></td></tr><br/>" +
                        "<tr><td style=\"color: {series.color}\">EventType: </td><td style=\"text-align: right\"><b>{point.className}</b></td></tr>',footerFormat: '</table>',valueDecimals: 2},");
                //formating data array and zones array
                //add data
                stringBuilder.append("data:[");
                getAllObservationEventType(differEvents,dataTimeSeries);
                for (int i=0;i<dataTimeSeries.size()-1;i++){
                    StringBuilder databuilderStr=new StringBuilder();
                    databuilderStr.append("{x:"+dataTimeSeries.get(i).getTimeLon()
                            +",y:"+dataTimeSeries.get(i).getDataValue()+",className:"+"'"+dataTimeSeries.get(i).getEventType()+"'},");
                    stringBuilder.append(databuilderStr);

                }
                StringBuilder lastDatStr=new StringBuilder();
                lastDatStr.append("{x:"+dataTimeSeries.get(dataTimeSeries.size()-1).getTimeLon()
                        +",y:"+dataTimeSeries.get(dataTimeSeries.size()-1).getDataValue()+",className:"+"'"+dataTimeSeries.get(dataTimeSeries.size()-1).getEventType()+"'}");
                stringBuilder.append(lastDatStr);
                stringBuilder.append("],");
                //add zones color diagnosis:green'#90ed7d',prepare:yellow '#f7a35c',response:red'#f15c80',recovery:blue #91e8e1,noEvent,white'#ffffff'
                stringBuilder.append("zones: [");
                for (int i=0;i<differEvents.size();i++){
                    StringBuilder zonebuilderStr=new StringBuilder();
                    Long zoneValue= differEvents.get(i).getStartTimeLong()-1*60*1000;
                    //create zone
                    if (i==0)
                        if (zoneValue>dataTimeSeries.get(0).getTimeLon())
                           zonebuilderStr.append("{value:"+zoneValue+", color: '#7cb5ec'},");
                        else zonebuilderStr.append("{value:"+zoneValue+", color: '#91e8e1'},");
                    else if (differEvents.get(i-1).getEventTypeName().equals("diagnosis")){
                        zonebuilderStr.append("{value:"+zoneValue+", color: '#91e8e1'},");
                    }else if (differEvents.get(i-1).getEventTypeName().equals("prepare")){
                        zonebuilderStr.append("{value:"+zoneValue+",color:'#f7a35c'},");
                    }else if (differEvents.get(i-1).getEventTypeName().equals("response")){
                            zonebuilderStr.append("{value:"+zoneValue+",color:'#f15c80'},");
                        }else if (differEvents.get(i-1).getEventTypeName().equals("recovery")) {
                        zonebuilderStr.append("{value:" + zoneValue + ",color:'#90ed7d'},");
                    }
                    stringBuilder.append(zonebuilderStr);

                }
                StringBuilder zonebuilderStr1=new StringBuilder();
                zonebuilderStr1.append("{ color: '#7cb5ec'}");
                stringBuilder.append(zonebuilderStr1);
                stringBuilder.append("]");
                stringBuilder.append("}");
            } catch (XmlException e) {
                e.printStackTrace();
            }
        }
        stringBuilder.append("]");
        String series= stringBuilder.toString();

        return series;
    }

    @Override
    public SubscibeEventParams getRegisteredEventParamsBySesid(String sesid) {
        String findStr="from SubscibeEventParams s where s.eventID='"+sesid+"'";
        List events= eventDao.find(findStr);
        if (events==null||events.isEmpty()) return null;
        return (SubscibeEventParams)events.get(0);
    }

    /**
     * get all seperated event time
     * @param events
     * @return
     */
    public List<DetectedEvent> getTimeChange(List<DetectedEvent> events){
        if (events==null||events.size()==0) return null;
        List<DetectedEvent> eventPart=new ArrayList<DetectedEvent>();
        String lastEventType=events.get(0).getEventTypeName();
        eventPart.add(events.get(0));
        for (int i=1;i<events.size();i++){
            String currentEventType=events.get(i).getEventTypeName();
            if (currentEventType!=lastEventType||i==events.size()-1){
                eventPart.add(events.get(i));
            }
            lastEventType=currentEventType;
        }
        //若最后一个事件与之前的事件类型一致，则将该事件类型记录

        return eventPart;
    }

    /**
     * get event type of different observation
     * @param events
     * @param dataTimeSeries
     * @return
     */
    public void getAllObservationEventType(List<DetectedEvent> events,List<DataTimeSeries> dataTimeSeries){
        if (events==null||events.size()==0){
            return;
        }
       for (int i=0;i<dataTimeSeries.size();i++){
           Long obsTime=dataTimeSeries.get(i).getTimeLon();
           //if before subscribe the eventtype will be noevent
           if (obsTime<=events.get(0).getStartTimeLong()-1*60*1000)
               dataTimeSeries.get(i).setEventType("NoEvent");
           else if (obsTime>events.get(events.size()-1).getStartTimeLong()-1*60*1000){
               dataTimeSeries.get(i).setEventType("NoEvent");
           }
               else{
                   int currentJ = 0;
                   while (currentJ < events.size() - 1) {
                       //get the low and high time of a event and move forward 1 min
                       Long lowTime = events.get(currentJ).getStartTimeLong() - 1 * 60 * 1000;
                       Long highTime = events.get(currentJ + 1).getStartTimeLong() - 1 * 60 * 1000;

                       if (obsTime >= lowTime && obsTime <= highTime) {
                           dataTimeSeries.get(i).setEventType(events.get(currentJ).getEventTypeName());
                           break;
                       } else{
                           currentJ++;
                       }

                   }
               }
       }
        return ;
    }

    @Override
    public String getDetectedDataInJsonBySESID(String url, String sesid) {
        String seriesRes=null;
        List<DataTimeSeries> dataTimeSeries=null;
        //get different sensor;
        SubscibeEventParams subscibeEventParams= getRegisteredEventParamsByEventSesID(sesid);
        String diagnosissSensorID=subscibeEventParams.getDiagnosisSensor();
        String prepareSensorID=subscibeEventParams.getPrepareSensor();
        String responseSensorID=subscibeEventParams.getResponseSensor();
        String recoverySensorID=subscibeEventParams.getRecoverySensor();
        List<String> differSensors=new ArrayList<String>();
        differSensors.add(diagnosissSensorID);
        if (!differSensors.contains(prepareSensorID)){
            differSensors.add(prepareSensorID);
        }
        if (!differSensors.contains(responseSensorID)){
            differSensors.add(responseSensorID);
        }
        if (!differSensors.contains(recoverySensorID)){
            differSensors.add(recoverySensorID);
        }
        //try show different sensor observation,if the subscribe contains different sensors
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("[");
        int Count=0;
        for(String SensorID:differSensors){
            if (Count==0)
                stringBuilder.append("{");
            else stringBuilder.append(",{");
            Count++;
            String observationRequestXML= encode.getGetObservationXML(subscibeEventParams.getDiagnosisSensor(), subscibeEventParams.getDiagnosisObservation());
            String responseXML= methods.sendPost(url, observationRequestXML);
            try {
                //get data form observation
                dataTimeSeries= decode.decodeObservation(responseXML);
                if (dataTimeSeries==null||dataTimeSeries.size()==0) {
                    stringBuilder.append("}");
                    continue;
                }
                //get event form event database
                String findStr="from DetectedEvent d where d.event.eventID='"+sesid+"' order by d.startTimeLong";
                List events= detectedEventDao.find(findStr);

                //get different type of information
                List<DetectedEvent> differEvents= getTimeChange(events);
                //get all type information

                //create series json using the data
                stringBuilder.append("name:"+"'"+SensorID+"',"+ "type: 'areaspline',"
                        +"zoneAxis: 'x',"
                        +" tooltip: {shared: true,useHTML: true, headerFormat: '<small>{point.key}</small><br/><table>'" +
                        ",pointFormat: '<tr><td style=\"color: {series.color}\">{series.name}: </td>"
                        +"<td style=\"text-align: right\"><b>{point.y}m</b></td></tr><br/>" +
                        "<tr><td style=\"color: {series.color}\">EventType: </td><td style=\"text-align: right\"><b>{point.className}</b></td></tr>',footerFormat: '</table>',valueDecimals: 2},");
                //formating data array and zones array
                //add data
                stringBuilder.append("data:[");
                getAllObservationEventType(differEvents,dataTimeSeries);
                for (int i=0;i<dataTimeSeries.size()-1;i++){
                    StringBuilder databuilderStr=new StringBuilder();
                    databuilderStr.append("{x:"+dataTimeSeries.get(i).getTimeLon()
                            +",y:"+dataTimeSeries.get(i).getDataValue()+",className:"+"'"+dataTimeSeries.get(i).getEventType()+"'},");
                    stringBuilder.append(databuilderStr);

                }
                StringBuilder lastDatStr=new StringBuilder();
                lastDatStr.append("{x:"+dataTimeSeries.get(dataTimeSeries.size()-1).getTimeLon()
                        +",y:"+dataTimeSeries.get(dataTimeSeries.size()-1).getDataValue()+",className:"+"'"+dataTimeSeries.get(dataTimeSeries.size()-1).getEventType()+"'}");
                stringBuilder.append(lastDatStr);
                stringBuilder.append("],");
                //add zones color diagnosis:green'#90ed7d',prepare:yellow '#f7a35c',response:red'#f15c80',recovery:blue #91e8e1,noEvent,white'#ffffff'
                stringBuilder.append("zones: [");
                for (int i=0;i<differEvents.size();i++){
                    StringBuilder zonebuilderStr=new StringBuilder();
                    Long zoneValue= differEvents.get(i).getStartTimeLong()-1*60*1000;
                    //create zone
                    if (i==0)
                        if (zoneValue>dataTimeSeries.get(0).getTimeLon())
                            zonebuilderStr.append("{value:"+zoneValue+", color: '#7cb5ec'},");
                        else zonebuilderStr.append("{value:"+zoneValue+", color: '#91e8e1'},");
                    else if (differEvents.get(i-1).getEventTypeName().equals("diagnosis")){
                        zonebuilderStr.append("{value:"+zoneValue+", color: '#91e8e1'},");
                    }else if (differEvents.get(i-1).getEventTypeName().equals("prepare")){
                        zonebuilderStr.append("{value:"+zoneValue+",color:'#f7a35c'},");
                    }else if (differEvents.get(i-1).getEventTypeName().equals("response")){
                        zonebuilderStr.append("{value:"+zoneValue+",color:'#f15c80'},");
                    }else if (differEvents.get(i-1).getEventTypeName().equals("recovery")) {
                        zonebuilderStr.append("{value:" + zoneValue + ",color:'#90ed7d'},");
                    }
                    stringBuilder.append(zonebuilderStr);

                }
                StringBuilder zonebuilderStr1=new StringBuilder();
                zonebuilderStr1.append("{ color: '#7cb5ec'}");
                stringBuilder.append(zonebuilderStr1);
                stringBuilder.append("]");
                stringBuilder.append("}");
            } catch (XmlException e) {
                e.printStackTrace();
            }
        }
        stringBuilder.append("]");
        String series= stringBuilder.toString();

        return series;
    }

    @Override
    public String getSingleDataInJsonByEventSensorProperty(String url, String sesid, String SensorID, String propertyID) {

        String seriesRes=null;
        List<DataTimeSeries> dataTimeSeries=null;
        //get different sensor;
        SubscibeEventParams subscibeEventParams= getRegisteredEventParamsBySesid(sesid);
        String observationRequestXML= encode.getGetObservationXML(SensorID, propertyID);
        String responseXML= methods.sendPost(url, observationRequestXML);
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append("{");
        try {
            //get data form observation
            dataTimeSeries= decode.decodeObservation(responseXML);
            if (dataTimeSeries==null||dataTimeSeries.size()==0) {
                stringBuilder.append("}");
                stringBuilder.append("]");
                return stringBuilder.toString();
            }

            //get event form event database
            String findStr="from DetectedEvent d where d.event.eventSesID='"+sesid+"' order by d.startTimeLong";
            List events= detectedEventDao.find(findStr);

            //get different type of information
            List<DetectedEvent> differEvents= getTimeChange(events);
            //get all type information

            //create series json using the data
            stringBuilder.append("name:"+"'"+SensorID+"',"+ "type: 'areaspline',"
                    +"zoneAxis: 'x',"
                    +" tooltip: {shared: true,useHTML: true, headerFormat: '<small>{point.key}</small><br/><table>'" +
                    ",pointFormat: '<tr><td style=\"color: {series.color}\">{series.name}: </td>"
                    +"<td style=\"text-align: right\"><b>{point.y}m</b></td></tr><br/>" +
                    "<tr><td style=\"color: {series.color}\">EventType: </td><td style=\"text-align: right\"><b>{point.className}</b></td></tr>',footerFormat: '</table>',valueDecimals: 2},");
            //formating data array and zones array
            //add data
            stringBuilder.append("data:[");
            getAllObservationEventType(differEvents,dataTimeSeries);
            for (int i=0;i<dataTimeSeries.size()-1;i++){
                StringBuilder databuilderStr=new StringBuilder();
                databuilderStr.append("{x:"+dataTimeSeries.get(i).getTimeLon()
                        +",y:"+dataTimeSeries.get(i).getDataValue()+",className:"+"'"+dataTimeSeries.get(i).getEventType()+"'},");
                stringBuilder.append(databuilderStr);

            }
            StringBuilder lastDatStr=new StringBuilder();
            lastDatStr.append("{x:"+dataTimeSeries.get(dataTimeSeries.size()-1).getTimeLon()
                    +",y:"+dataTimeSeries.get(dataTimeSeries.size()-1).getDataValue()+",className:"+"'"+dataTimeSeries.get(dataTimeSeries.size()-1).getEventType()+"'}");
            stringBuilder.append(lastDatStr);
            stringBuilder.append("],");
            //add zones color diagnosis:green'#90ed7d',prepare:yellow '#f7a35c',response:red'#f15c80',recovery:blue #91e8e1,noEvent,white'#ffffff'
            stringBuilder.append("zones: [");
            if (differEvents!=null&&differEvents.size()!=0)
            for (int i=0;i<differEvents.size();i++){
                StringBuilder zonebuilderStr=new StringBuilder();
                Long zoneValue= differEvents.get(i).getStartTimeLong()-1*60*1000;
                //create zone
                if (i==0)
                    if (zoneValue>dataTimeSeries.get(0).getTimeLon())
                        zonebuilderStr.append("{value:"+zoneValue+", color: '#7cb5ec'},");
                    else zonebuilderStr.append("{value:"+zoneValue+", color: '#91e8e1'},");
                else if (differEvents.get(i-1).getEventTypeName().equals("diagnosis")){
                    zonebuilderStr.append("{value:"+zoneValue+", color: '#91e8e1'},");
                }else if (differEvents.get(i-1).getEventTypeName().equals("prepare")){
                    zonebuilderStr.append("{value:"+zoneValue+",color:'#f7a35c'},");
                }else if (differEvents.get(i-1).getEventTypeName().equals("response")){
                    zonebuilderStr.append("{value:"+zoneValue+",color:'#f15c80'},");
                }else if (differEvents.get(i-1).getEventTypeName().equals("recovery")) {
                    zonebuilderStr.append("{value:" + zoneValue + ",color:'#90ed7d'},");
                }
                stringBuilder.append(zonebuilderStr);

            }
            StringBuilder zonebuilderStr1=new StringBuilder();
            zonebuilderStr1.append("{ color: '#7cb5ec'}");
            stringBuilder.append(zonebuilderStr1);
            stringBuilder.append("]");
            stringBuilder.append("}");
        } catch (XmlException e) {
            e.printStackTrace();
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /**
     * 该方法使用在SES中已经注册的sesid作为标识进行查询
     * @param eventSesID
     * @return
     */
    @Override
    public SubscibeEventParams getRegisteredEventParamsByEventSesID(String eventSesID) {
        String findStr = "from SubscibeEventParams s where s.eventSesID='" + eventSesID + "'";
        List events= eventDao.find(findStr);
        if (events==null||events.isEmpty()) return null;
        return (SubscibeEventParams)events.get(0);
    }

    @Override
    public List<SubscibeEventParams> getAllRegisteredEvent() {
        String findStr = "from SubscibeEventParams s";
        List events= eventDao.find(findStr);
        if (events==null||events.isEmpty()) return null;
        return events;
    }

    @Override
    public String getLatestEventType(String sesID) {
        String maxHql = "from DetectedEvent d where d.startTimeLong=(select max(de.startTimeLong) from DetectedEvent de where de.event.eventID='" + sesID + "')";
        List<DetectedEvent> detectEvents = detectedEventDao.find(maxHql);
        if (detectEvents == null || detectEvents.isEmpty()) {
            return null;
        }
        return detectEvents.get(0).getEventTypeName();
    }

    @Override
    public List<SubscibeEventParams> getRegisteredEventByUserID(Long userLonID) {
        String findStr = "from SubscibeEventParams s where s.user='" + userLonID + "'";
        List events= eventDao.find(findStr);
        if (events==null||events.isEmpty()) return null;
        return events;
    }


}
