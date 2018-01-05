package yuan.flood.mvc;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.Entity.UIDTO.FloodResult;
import yuan.flood.dao.Entity.UIDTO.SingleEventDTO;
import yuan.flood.dao.Entity.UIDTO.SingleEventSensorDTO;
import yuan.flood.dao.Entity.UIEntity.ConvertUtil;
import yuan.flood.service.IService.IEventService;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.sos.DataTimeSeries;
import yuan.flood.sos.HighChartsDrawData;
import yuan.flood.until.ReadConfig;
import yuan.flood.until.SOSSESConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuan on 2017/2/27.
 */
@Controller
public class SingleEventController {
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
        //根据四个传感器的ID，获取传感器信息
        List<Sensor> sensorsDiagnosis = sensorService.findObseredPropertyBySensorID(params.getDiagnosisSensor());
        List<Sensor> sensorsPrepare = sensorService.findObseredPropertyBySensorID(params.getPrepareSensor());
        List<Sensor> sensorsResponse = sensorService.findObseredPropertyBySensorID(params.getResponseSensor());
        List<Sensor> sensorsRecovery = sensorService.findObseredPropertyBySensorID(params.getRecoverySensor());

        if (sensorsDiagnosis == null || sensorsDiagnosis.isEmpty()
                || sensorsPrepare == null || sensorsPrepare.isEmpty()
                || sensorsResponse == null || sensorsResponse.isEmpty()
                || sensorsRecovery == null || sensorsRecovery.isEmpty()) {
            floodResult.setFlag(false);
            floodResult.setMessage("传感器参数错误！事件中存在传感器ID无效！");
            return floodResult;
        }

        SingleEventSensorDTO singleEventSensorDTO = new SingleEventSensorDTO();
        singleEventSensorDTO.setDiagnosisSensor(sensorsDiagnosis.get(0));
        singleEventSensorDTO.setDiagnosisPropertyID(params.getDiagnosisObservation());
        singleEventSensorDTO.setPrepareSensor(sensorsPrepare.get(0));
        singleEventSensorDTO.setPreparePropertyID(params.getPrepareObservation());
        singleEventSensorDTO.setResponseSensor(sensorsResponse.get(0));
        singleEventSensorDTO.setResponsePropertyID(params.getResponseObservation());
        singleEventSensorDTO.setRecoverySensor(sensorsRecovery.get(0));
        singleEventSensorDTO.setRecoveryPropertyID(params.getRecoveryObservation());

        floodResult.setMessage("返回成功！");
        floodResult.setObject(singleEventSensorDTO);
        return floodResult;
    }


    @CrossOrigin(value = "*")
    @ResponseBody
    @RequestMapping(value = "/getEventDataJson", method = RequestMethod.POST)
    public String getEventDataForPlotJson(HttpServletRequest request, @RequestBody String eventID) {
       String series = eventService.getDetectedDataInJsonBySESID(SOSSESConfig.getSosurl(), eventID);

//        String  res="[{\"type\":\"areaspline\",\"data\":[29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]," +
//                "\"pointStart\":1420070400000,\"pointIntervalUnit\":\"month\",\"zoneAxis\":\"x\",\"zones\":[{\"value\":1433116800000" +
//                ",\"color\":\"#90ed7d\"},{\"value\":1435708800000,\"color\":\"#f15c80\"},{\"value\":1441065600000,\"color\":\"#90ed7d\"}]}]";
////        String res = "[{\"type\":\"name\"}]";
        return series;
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

        if (sensorsDiagnosis == null || sensorsDiagnosis.isEmpty()
                || sensorsPrepare == null || sensorsPrepare.isEmpty()
                || sensorsResponse == null || sensorsResponse.isEmpty()
                || sensorsRecovery == null || sensorsRecovery.isEmpty()) {
            floodResult.setFlag(false);
            floodResult.setMessage("传感器参数错误！事件中存在传感器ID无效！");
            return floodResult;
        }
        SingleEventDTO singleEventDTO = new SingleEventDTO();
        singleEventDTO.setParams(ConvertUtil.getSubscribeEventParamsDTOfromSubscibeEventParams(params));

        SingleEventSensorDTO singleEventSensorDTO = new SingleEventSensorDTO();
        singleEventSensorDTO.setDiagnosisSensor(sensorsDiagnosis.get(0));
        singleEventSensorDTO.setDiagnosisPropertyID(params.getDiagnosisObservation());
        singleEventSensorDTO.setPrepareSensor(sensorsPrepare.get(0));
        singleEventSensorDTO.setPreparePropertyID(params.getPrepareObservation());
        singleEventSensorDTO.setResponseSensor(sensorsResponse.get(0));
        singleEventSensorDTO.setResponsePropertyID(params.getResponseObservation());
        singleEventSensorDTO.setRecoverySensor(sensorsRecovery.get(0));
        singleEventSensorDTO.setRecoveryPropertyID(params.getRecoveryObservation());

        singleEventDTO.setSensors(singleEventSensorDTO);
        floodResult.setMessage("返回成功！");
        floodResult.setObject(singleEventDTO);
        return floodResult;
    }


    /**
     * 获取服务的地址，及当前事件的状态,以及执行最新的服务
     * @param eventID
     * @return
     */
    public FloodResult<String> getServiceUrl(@RequestBody String eventID) {
        String url = "http://www.myflood.com/phase/diagnosis";
        String message;
        return null;
    }
}
