package yuan.flood.mvc;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import jdk.nashorn.internal.parser.JSONParser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yuan.flood.dao.Entity.ObservedProperty;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.Entity.UIDTO.*;
import yuan.flood.dao.Entity.UIEntity.ConvertUtil;
import yuan.flood.dao.Entity.UIEntity.SensorDTO;
import yuan.flood.service.IService.IEventService;
import yuan.flood.service.IService.IPropertyService;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.sos.DataTimeSeries;
import yuan.flood.sos.HighChartsDrawData;
import yuan.flood.until.ReadConfig;
import yuan.flood.until.SOSSESConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuan on 2017/2/27.
 */
@Controller
public class SingleEventController {
    @Autowired
    private IPropertyService propertyService;
    @Autowired
    private IEventService eventService;
    @Autowired
    private ISensorService sensorService;

    @RequestMapping(value = "/showSingleEventInformation/{eventID}",method = RequestMethod.GET)
    public String getShowSingleEventInformationPage(@PathVariable String eventID,ModelMap modelMap,HttpServletRequest request){
        HttpSession session=request.getSession();
        session.removeAttribute("eventID");
        session.setAttribute("eventID",eventID);
        SubscibeEventParams params= eventService.getRegisteredEventParamsBySesid(eventID);
        modelMap.addAttribute("eventParams",params);
        return "showSingleEventInformation";
    }

    @ResponseBody
    @RequestMapping(value = "/getEventData",method = RequestMethod.POST)
    public String getEventDataForPlot(HttpServletRequest request){
        HttpSession session=request.getSession();
        String sesID= (String) session.getAttribute("eventID");
        String series= eventService.getDetectedEventBySESID(SOSSESConfig.getSosurl(), sesID);
        //create series json for draw;
        //demo:[{type:'areaspline',data:[29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]," +
       // "pointStart:1420070400000,pointIntervalUnit:'month',zoneAxis:'x',zones:[{value:1433116800000" +
        //       ",color:'#90ed7d'},{value:1435708800000,color:'#f15c80'},{value:1441065600000,color:'#90ed7d'}]}]

        return series;
    }


    /////////////////***********************洪涝事件传感器分布界面基本操作*************//

    /**
     * 获取单个事件的传感器分布信息，以及当前事件选择的属性信息
     * @param eventID
     * @return
     */
    @CrossOrigin(value = "*")
    @ResponseBody
    @RequestMapping(value = "/getEventSensorInfoJson", method = RequestMethod.POST)
    public FloodResult<SingleEventSensorDTO> getEventDifferSensor(@RequestBody String eventID) {
        FloodResult<SingleEventSensorDTO> floodResult = new FloodResult<SingleEventSensorDTO>();
        floodResult.setFlag(true);

        if (Strings.isNullOrEmpty(eventID)) {
            floodResult.setFlag(false);
            floodResult.setMessage("事件ID不可为空值！");
            return floodResult;
        }
        //当事件不为空值时，拿到存储的事件信息
        SubscibeEventParams params = eventService.getRegisteredEventParamsBySesid(eventID);
        //获取

        //根据四个传感器的ID，获取传感器信息
        List<Sensor> sensorsDiagnosis = sensorService.findObseredPropertyBySensorID(params.getDiagnosisSensor());
        List<Sensor> sensorsPrepare = sensorService.findObseredPropertyBySensorID(params.getPrepareSensor());
        List<Sensor> sensorsResponse = sensorService.findObseredPropertyBySensorID(params.getResponseSensor());
        List<Sensor> sensorsRecovery = sensorService.findObseredPropertyBySensorID(params.getRecoverySensor());

        ObservedProperty observedPropertyDiag = propertyService.getPropertyByPropertyID(params.getDiagnosisObservation());
        ObservedProperty observedPropertyPre = propertyService.getPropertyByPropertyID(params.getPrepareObservation());
        ObservedProperty observedPropertyRes = propertyService.getPropertyByPropertyID(params.getResponseObservation());
        ObservedProperty observedPropertyRec = propertyService.getPropertyByPropertyID(params.getRecoveryObservation());

        if (sensorsDiagnosis == null || sensorsDiagnosis.isEmpty()
                || sensorsPrepare == null || sensorsPrepare.isEmpty()
                || sensorsResponse == null || sensorsResponse.isEmpty()
                || sensorsRecovery == null || sensorsRecovery.isEmpty()) {
            floodResult.setFlag(false);
            floodResult.setMessage("传感器参数错误！事件中存在传感器ID无效！");
            return floodResult;
        }


        SingleEventSensorDTO singleEventSensorDTO = new SingleEventSensorDTO();
        singleEventSensorDTO.setDiagnosisSensor(ConvertUtil.getSensorDTOfromSensor(sensorsDiagnosis.get(0)));
        singleEventSensorDTO.setDiagnosisProperty(ConvertUtil.getObservedPropertyDTOfromObservedProperty(observedPropertyDiag));
        singleEventSensorDTO.setPrepareSensor(ConvertUtil.getSensorDTOfromSensor(sensorsPrepare.get(0)));
        singleEventSensorDTO.setPrepareProperty(ConvertUtil.getObservedPropertyDTOfromObservedProperty(observedPropertyPre));
        singleEventSensorDTO.setResponseSensor(ConvertUtil.getSensorDTOfromSensor(sensorsResponse.get(0)));
        singleEventSensorDTO.setResponseProperty(ConvertUtil.getObservedPropertyDTOfromObservedProperty(observedPropertyRes));
        singleEventSensorDTO.setRecoverySensor(ConvertUtil.getSensorDTOfromSensor(sensorsRecovery.get(0)));
        singleEventSensorDTO.setRecoveryProperty(ConvertUtil.getObservedPropertyDTOfromObservedProperty(observedPropertyRec));

        floodResult.setMessage("返回成功！");
        floodResult.setObject(singleEventSensorDTO);
        return floodResult;
    }


    @CrossOrigin(value = "*")
    @ResponseBody
    @RequestMapping(value = "/getEventDataJson", method = RequestMethod.POST)
    public FloodResult<String> getEventDataForPlotJson(HttpServletRequest request, @RequestBody String eventID) {
        FloodResult<String> floodResult = new FloodResult<>();
        floodResult.setFlag(true);

        if (Strings.isNullOrEmpty(eventID)){
            floodResult.setFlag(false);
            floodResult.setMessage("eventID不可为空");
        }
       String series = "[]";
        try {
            series = eventService.getDetectedDataInJsonBySESID(SOSSESConfig.getSosurl(), eventID);
        } catch (Exception e) {
            floodResult.setFlag(false);
            floodResult.setMessage(e.getMessage());
        }
        floodResult.setObject(series);

        return floodResult;
    }

    @CrossOrigin(value = "*")
    @ResponseBody
    @RequestMapping(value = "/getPropertyDataJson", method = RequestMethod.POST)
    public FloodResult<String> getSinglePropertyData(HttpServletRequest request, @RequestBody EventSensorPropertyDTO eventSensorPropertyDTO) {
        FloodResult<String> floodResult = new FloodResult<>();
        floodResult.setFlag(true);

        String sesEventID = eventSensorPropertyDTO.getEventID();
        String sensorID = eventSensorPropertyDTO.getSensorID();
        String propertyID = eventSensorPropertyDTO.getPropertyID();
        if (Strings.isNullOrEmpty(sesEventID) || Strings.isNullOrEmpty(sensorID) || Strings.isNullOrEmpty(propertyID)) {
            floodResult.setFlag(false);
            floodResult.setMessage("参数输入错误！");
        }
        String res = "";
        try {
          res= eventService.getSingleDataInJsonByEventSensorProperty(SOSSESConfig.getSosurl(), sesEventID, sensorID, propertyID);
        } catch (Exception e) {
            floodResult.setFlag(false);
            floodResult.setMessage(e.getMessage());
        }
//        res="[{type:'areaspline',data:[29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]," +
//                "pointStart:1420070400000,pointIntervalUnit:'month',zoneAxis:'x',zones:[{value:1433116800000" +
//                ",color:'#90ed7d'},{value:1435708800000,color:'#f15c80'},{value:1441065600000,color:'#90ed7d'}]}]";

        floodResult.setObject(res);

        return floodResult;
    }



    /**
     * 获取事件的基本数据，定义根据事件在SES中注册的ID获取，传感器信息，参数信息，获取事件的元数据信息
     */
    @CrossOrigin(value = "*")
    @ResponseBody
    @RequestMapping(value = "/getEventBaseData", method = RequestMethod.POST)
    public FloodResult<SingleEventDTO> getSingleEventDTO(@RequestBody String eventSesID) {
        FloodResult<SingleEventDTO> floodResult = new FloodResult<SingleEventDTO>();
        floodResult.setFlag(true);
        if (Strings.isNullOrEmpty(eventSesID)) {
            floodResult.setFlag(false);
            floodResult.setMessage("事件ID不可为空值！");
            return floodResult;
        }
        //当事件不为空值时，拿到存储的事件信息
        SubscibeEventParams params = eventService.getRegisteredEventParamsByEventSesID(eventSesID);
        if (params==null){
            floodResult.setFlag(false);
            floodResult.setMessage("事件ID无效，不存在该事件！");
            return floodResult;
        }
        //根据四个传感器的ID，获取传感器信息
        List<Sensor> sensorsDiagnosis = sensorService.findObseredPropertyBySensorID(params.getDiagnosisSensor());
        List<Sensor> sensorsPrepare = sensorService.findObseredPropertyBySensorID(params.getPrepareSensor());
        List<Sensor> sensorsResponse = sensorService.findObseredPropertyBySensorID(params.getResponseSensor());
        List<Sensor> sensorsRecovery = sensorService.findObseredPropertyBySensorID(params.getRecoverySensor());

        ObservedProperty observedPropertyDiag = propertyService.getPropertyByPropertyID(params.getDiagnosisObservation());
        ObservedProperty observedPropertyPre = propertyService.getPropertyByPropertyID(params.getPrepareObservation());
        ObservedProperty observedPropertyRes = propertyService.getPropertyByPropertyID(params.getResponseObservation());
        ObservedProperty observedPropertyRec = propertyService.getPropertyByPropertyID(params.getRecoveryObservation());

        if (sensorsDiagnosis == null || sensorsDiagnosis.isEmpty()
                || sensorsPrepare == null || sensorsPrepare.isEmpty()
                || sensorsResponse == null || sensorsResponse.isEmpty()
                || sensorsRecovery == null || sensorsRecovery.isEmpty()) {
            floodResult.setFlag(false);
            floodResult.setMessage("传感器参数错误！事件中存在传感器ID无效！");
            return floodResult;
        }
        //获取不重复的传感器
        String diagnosissSensorID=params.getDiagnosisSensor();
        String prepareSensorID=params.getPrepareSensor();
        String responseSensorID=params.getResponseSensor();
        String recoverySensorID=params.getRecoverySensor();
        List<String> differSensors=new ArrayList<String>();
        List<SensorDTO> sensorDTOS = new ArrayList<SensorDTO>();
        differSensors.add(diagnosissSensorID);
        sensorDTOS.add(ConvertUtil.getSensorDTOfromSensor(sensorsDiagnosis.get(0)));
        if (!differSensors.contains(prepareSensorID)){
            differSensors.add(prepareSensorID);
            sensorDTOS.add(ConvertUtil.getSensorDTOfromSensor(sensorsPrepare.get(0)));
        }
        if (!differSensors.contains(responseSensorID)){
            differSensors.add(responseSensorID);
            sensorDTOS.add(ConvertUtil.getSensorDTOfromSensor(sensorsResponse.get(0)));
        }
        if (!differSensors.contains(recoverySensorID)){
            differSensors.add(recoverySensorID);
            sensorDTOS.add(ConvertUtil.getSensorDTOfromSensor(sensorsRecovery.get(0)));
        }
        //获取不重复的传感器+属性
        List<String> sensorAndObsStr = new ArrayList<>();
        List<EventSensorPropertyDTO> eventSensorPropertyDTOS = new ArrayList<>();
        sensorAndObsStr.add(diagnosissSensorID + params.getDiagnosisObservation());
        eventSensorPropertyDTOS.add(ConvertUtil.getEventSensorPropertyDTOFromSensorAndProperty(sensorsDiagnosis.get(0), observedPropertyDiag));
        if (!sensorAndObsStr.contains(prepareSensorID+params.getPrepareObservation())){
            eventSensorPropertyDTOS.add(ConvertUtil.getEventSensorPropertyDTOFromSensorAndProperty(sensorsPrepare.get(0), observedPropertyPre));
            sensorAndObsStr.add(prepareSensorID + params.getPrepareObservation());
        }
        if (!sensorAndObsStr.contains(responseSensorID+params.getResponseObservation())){
            eventSensorPropertyDTOS.add(ConvertUtil.getEventSensorPropertyDTOFromSensorAndProperty(sensorsResponse.get(0), observedPropertyRes));
            sensorAndObsStr.add(responseSensorID + params.getResponseObservation());
        }
        if (!sensorAndObsStr.contains(recoverySensorID+params.getRecoveryObservation())){
            eventSensorPropertyDTOS.add(ConvertUtil.getEventSensorPropertyDTOFromSensorAndProperty(sensorsRecovery.get(0), observedPropertyRec));
            sensorAndObsStr.add(recoverySensorID + params.getRecoveryObservation());
        }

        SingleEventDTO singleEventDTO = new SingleEventDTO();
        singleEventDTO.setParams(ConvertUtil.getSubscribeEventParamsDTOfromSubscibeEventParams(params));

        SingleEventSensorDTO singleEventSensorDTO = new SingleEventSensorDTO();
        singleEventSensorDTO.setDiagnosisSensor(ConvertUtil.getSensorDTOfromSensor(sensorsDiagnosis.get(0)));
        singleEventSensorDTO.setDiagnosisProperty(ConvertUtil.getObservedPropertyDTOfromObservedProperty(observedPropertyDiag));
        singleEventSensorDTO.setPrepareSensor(ConvertUtil.getSensorDTOfromSensor(sensorsPrepare.get(0)));
        singleEventSensorDTO.setPrepareProperty(ConvertUtil.getObservedPropertyDTOfromObservedProperty(observedPropertyPre));
        singleEventSensorDTO.setResponseSensor(ConvertUtil.getSensorDTOfromSensor(sensorsResponse.get(0)));
        singleEventSensorDTO.setResponseProperty(ConvertUtil.getObservedPropertyDTOfromObservedProperty(observedPropertyRes));
        singleEventSensorDTO.setRecoverySensor(ConvertUtil.getSensorDTOfromSensor(sensorsRecovery.get(0)));
        singleEventSensorDTO.setRecoveryProperty(ConvertUtil.getObservedPropertyDTOfromObservedProperty(observedPropertyRec));

        singleEventDTO.setDataset(singleEventSensorDTO);
        singleEventDTO.setSensors(sensorDTOS);
        singleEventDTO.setPropertys(eventSensorPropertyDTOS);

        floodResult.setMessage("返回成功！");
        floodResult.setObject(singleEventDTO);
        return floodResult;
    }


    /**
     * 获取服务的地址，及当前事件的状态,以及执行最新的服务
     * @param eventID
     * @return
     */
    @CrossOrigin(value = "*")
    @ResponseBody
    @RequestMapping(value = "/getEventStatus", method = RequestMethod.POST)
    public FloodResult<String> getServiceMessage(@RequestBody String eventID) {
        FloodResult<String> floodResult = new FloodResult<>();
        floodResult.setFlag(true);

        if (Strings.isNullOrEmpty(eventID)){
            floodResult.setFlag(false);
            floodResult.setMessage("事件ID不可为空");
            return floodResult;
        }

        SubscibeEventParams subscibeEventParams = eventService.getRegisteredEventParamsByEventSesID(eventID);
        if (subscibeEventParams==null){
            floodResult.setFlag(false);
            floodResult.setMessage("无法查找到当前事件");
            return floodResult;
        }

        //根据eventID查找当前状态
        String ID = subscibeEventParams.getEventID();
        //返回当前状态
        String latestType = eventService.getLatestEventType(ID);

        if (Strings.isNullOrEmpty(latestType)) {
            floodResult.setObject("无事件状态o ");
        }else {
            floodResult.setObject(latestType);
        }
        return floodResult;
    }

}
