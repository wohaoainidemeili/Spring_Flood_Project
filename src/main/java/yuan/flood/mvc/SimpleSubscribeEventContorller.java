package yuan.flood.mvc;

import com.google.common.base.Strings;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yuan.flood.dao.Entity.ObservedProperty;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.Entity.UIDTO.SubscribeParamsDTO;
import yuan.flood.dao.Entity.UIEntity.ConvertUtil;
import yuan.flood.dao.Entity.UIEntity.SubscribeEventParamsDTO;
import yuan.flood.service.IService.IEventService;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.service.IService.ISubScribeEventService;
import yuan.flood.ses.SESConnector;
import yuan.flood.until.SessionNames;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Yuan on 2017/2/15.
 */
@Controller
public class SimpleSubscribeEventContorller {
    @Autowired
    private ISensorService sensorService;
    @Autowired
     private SESConnector sesConnector;
      @Autowired
     private ISubScribeEventService subscirbeEventService;
    @Autowired
    private IEventService eventService;
    @CrossOrigin(value = "*")
    @RequestMapping(value = "/simpleSubscribeEvnt",method = RequestMethod.GET)
    public String getSimpleSubscribeEvent(HttpServletRequest request,ModelMap modelMap){
        String[] sensors= (String[]) request.getSession().getAttribute("selectSensors");
        if (sensors!=null&&sensors.length>0){
            modelMap.addAttribute("sensorsOption", Arrays.asList(sensors));
        }else{

        }
        return "getSubscribeEventParameters";
    }
    @RequestMapping(method = RequestMethod.POST,value = "/getSimpleObseervationProperty")
    @ResponseBody
    public Map<String,Object> getObservationProperty(@ModelAttribute(value = "selectedSensor")String sensorID){
        Map<String,Object> property_unit=new HashMap<String, Object>();
        List<String> properties=new ArrayList<String>();
        List<String> units=new ArrayList<String>();
        List<Sensor> sensors= sensorService.findObseredPropertyBySensorID(sensorID);
        for (ObservedProperty observedProperty:sensors.get(0).getObservedProperties()){
            properties.add(observedProperty.getPropertyID());
            units.add(observedProperty.getUnit());
        }
        property_unit.put("properties",properties);
        property_unit.put("units",units);
        return property_unit;
    }

    @CrossOrigin(value = "*")
    @RequestMapping(method = RequestMethod.POST,value = "/subscribe")
    @ResponseBody
    public boolean getSubscribeParamsAndSubscribeEvent(HttpServletRequest request){

        SubscibeEventParams subscibeEventParams=new SubscibeEventParams();
        subscibeEventParams.setDiagnosisSensor(request.getParameter("diagnosisSensor"));
        subscibeEventParams.setDiagnosisObservation(request.getParameter("diagnosisObservation"));
        subscibeEventParams.setDiagnosisDay(Integer.valueOf(request.getParameter("diagnosisDay")));
        subscibeEventParams.setDiagnosisHour(Integer.valueOf(request.getParameter("diagnosisHour")));
        subscibeEventParams.setDiagnosisMinute(Integer.valueOf(request.getParameter("diagnosisMinute")));
        subscibeEventParams.setDiagnosisSecond(Integer.valueOf(request.getParameter("diagnosisSecond")));
        subscibeEventParams.setDiagnosisRepeatTimes(Integer.valueOf(request.getParameter("diagnosisRepeatTimes")));
        subscibeEventParams.setDiagnosisThreshold(Double.valueOf(request.getParameter("diagnosisThreshold")));
        subscibeEventParams.setDiagnosisUnit(request.getParameter("diagnosisUnit"));

        subscibeEventParams.setPrepareSensor(request.getParameter("prepareSensor"));
        subscibeEventParams.setPrepareObservation(request.getParameter("prepareObservation"));
        subscibeEventParams.setPrepareThreshold(Double.valueOf(request.getParameter("prepareThreshold")));
        subscibeEventParams.setPrepareDay(Integer.valueOf(request.getParameter("prepareDay")));
        subscibeEventParams.setPrepareHour(Integer.valueOf(request.getParameter("prepareHour")));
        subscibeEventParams.setPrepareMinute(Integer.valueOf(request.getParameter("prepareMinute")));
        subscibeEventParams.setPrepareSecond(Integer.valueOf(request.getParameter("prepareSecond")));
        subscibeEventParams.setPrepareRepeatTimes(Integer.valueOf(request.getParameter("prepareRepeatTimes")));
        subscibeEventParams.setPrepareUnit(request.getParameter("prepareUnit"));

        subscibeEventParams.setResponseSensor(request.getParameter("responseSensor"));
        subscibeEventParams.setResponseObservation(request.getParameter("responseObservation"));
        subscibeEventParams.setResponseThreshold(Double.valueOf(request.getParameter("responseThreshold")));
        subscibeEventParams.setResponseDay(Integer.valueOf(request.getParameter("responseDay")));
        subscibeEventParams.setResponseHour(Integer.valueOf(request.getParameter("responseHour")));
        subscibeEventParams.setResponseMinute(Integer.valueOf(request.getParameter("responseMinute")));
        subscibeEventParams.setResponseSecond(Integer.valueOf(request.getParameter("responseSecond")));
        subscibeEventParams.setResponseRepeatTimes(Integer.valueOf(request.getParameter("responseRepeatTimes")));
        subscibeEventParams.setResponseUnit(request.getParameter("responseUnit"));

        subscibeEventParams.setRecoverySensor(request.getParameter("recoverySensor"));
        subscibeEventParams.setRecoveryObservation(request.getParameter("recoveryObservation"));
        subscibeEventParams.setRecoveryThreshold(Double.valueOf(request.getParameter("recoveryThreshold")));
        subscibeEventParams.setRecoveryDay(Integer.valueOf(request.getParameter("recoveryDay")));
        subscibeEventParams.setRecoveryHour(Integer.valueOf(request.getParameter("recoveryHour")));
        subscibeEventParams.setRecoveryMinute(Integer.valueOf(request.getParameter("recoveryMinute")));
        subscibeEventParams.setRecoverySecond(Integer.valueOf(request.getParameter("recoverySecond")));
        subscibeEventParams.setRecoveryRepeatTimes(Integer.valueOf(request.getParameter("recoveryRepeatTimes")));
        subscibeEventParams.setRecoveryUnit(request.getParameter("recoveryUnit"));

        //get the max order of event
        Long ID= eventService.getMaxEventOrder();
        subscibeEventParams.setOrder(ID);
        subscibeEventParams.setEventID("Event"+ID);
        subscibeEventParams.setEventName("Event"+ID);
        // suscribe ses
        String sesID= sesConnector.subscribeEvent(subscirbeEventService.createSubscirbeEvent(subscibeEventParams));
        subscibeEventParams.setEventSesID(sesID);
        eventService.saveSubscribeEvent(subscibeEventParams);
        //insert the ses params to db

        return true;
    }

    /**
     * 构建Session，返回订阅事件ID?
     * @param request
     * @return
     */
    @CrossOrigin(value = "*")
    @RequestMapping(method = RequestMethod.POST,value = "/subscribeToSession")
    @ResponseBody
    public boolean setSubscribeParamsSession(HttpServletRequest request){

        SubscibeEventParams subscibeEventParams=new SubscibeEventParams();
        subscibeEventParams.setDiagnosisSensor(request.getParameter("diagnosisSensor"));
        subscibeEventParams.setDiagnosisObservation(request.getParameter("diagnosisObservation"));
        subscibeEventParams.setDiagnosisDay(Integer.valueOf(request.getParameter("diagnosisDay")));
        subscibeEventParams.setDiagnosisHour(Integer.valueOf(request.getParameter("diagnosisHour")));
        subscibeEventParams.setDiagnosisMinute(Integer.valueOf(request.getParameter("diagnosisMinute")));
        subscibeEventParams.setDiagnosisSecond(Integer.valueOf(request.getParameter("diagnosisSecond")));
        subscibeEventParams.setDiagnosisRepeatTimes(Integer.valueOf(request.getParameter("diagnosisRepeatTimes")));
        subscibeEventParams.setDiagnosisThreshold(Double.valueOf(request.getParameter("diagnosisThreshold")));
        subscibeEventParams.setDiagnosisUnit(request.getParameter("diagnosisUnit"));

        subscibeEventParams.setPrepareSensor(request.getParameter("prepareSensor"));
        subscibeEventParams.setPrepareObservation(request.getParameter("prepareObservation"));
        subscibeEventParams.setPrepareThreshold(Double.valueOf(request.getParameter("prepareThreshold")));
        subscibeEventParams.setPrepareDay(Integer.valueOf(request.getParameter("prepareDay")));
        subscibeEventParams.setPrepareHour(Integer.valueOf(request.getParameter("prepareHour")));
        subscibeEventParams.setPrepareMinute(Integer.valueOf(request.getParameter("prepareMinute")));
        subscibeEventParams.setPrepareSecond(Integer.valueOf(request.getParameter("prepareSecond")));
        subscibeEventParams.setPrepareRepeatTimes(Integer.valueOf(request.getParameter("prepareRepeatTimes")));
        subscibeEventParams.setPrepareUnit(request.getParameter("prepareUnit"));

        subscibeEventParams.setResponseSensor(request.getParameter("responseSensor"));
        subscibeEventParams.setResponseObservation(request.getParameter("responseObservation"));
        subscibeEventParams.setResponseThreshold(Double.valueOf(request.getParameter("responseThreshold")));
        subscibeEventParams.setResponseDay(Integer.valueOf(request.getParameter("responseDay")));
        subscibeEventParams.setResponseHour(Integer.valueOf(request.getParameter("responseHour")));
        subscibeEventParams.setResponseMinute(Integer.valueOf(request.getParameter("responseMinute")));
        subscibeEventParams.setResponseSecond(Integer.valueOf(request.getParameter("responseSecond")));
        subscibeEventParams.setResponseRepeatTimes(Integer.valueOf(request.getParameter("responseRepeatTimes")));
        subscibeEventParams.setResponseUnit(request.getParameter("responseUnit"));

        subscibeEventParams.setRecoverySensor(request.getParameter("recoverySensor"));
        subscibeEventParams.setRecoveryObservation(request.getParameter("recoveryObservation"));
        subscibeEventParams.setRecoveryThreshold(Double.valueOf(request.getParameter("recoveryThreshold")));
        subscibeEventParams.setRecoveryDay(Integer.valueOf(request.getParameter("recoveryDay")));
        subscibeEventParams.setRecoveryHour(Integer.valueOf(request.getParameter("recoveryHour")));
        subscibeEventParams.setRecoveryMinute(Integer.valueOf(request.getParameter("recoveryMinute")));
        subscibeEventParams.setRecoverySecond(Integer.valueOf(request.getParameter("recoverySecond")));
        subscibeEventParams.setRecoveryRepeatTimes(Integer.valueOf(request.getParameter("recoveryRepeatTimes")));
        subscibeEventParams.setRecoveryUnit(request.getParameter("recoveryUnit"));

        //get the max order of event
        Long ID= eventService.getMaxEventOrder();
        subscibeEventParams.setOrder(ID);
        subscibeEventParams.setEventID("Event"+ID);
        subscibeEventParams.setEventName("Event"+ID);
        // suscribe ses
//        String sesID= sesConnector.subscribeEvent(subscirbeEventService.createSubscirbeEvent(subscibeEventParams));
//        subscibeEventParams.setEventSesID(sesID);
//        eventService.saveSubscribeEvent(subscibeEventParams);
        //insert the ses params to db

        //create subscribeEventParams session
        HttpSession session = request.getSession();
        session.removeAttribute(SessionNames.SETTED_EVENT_PARAMS);
        session.setAttribute(SessionNames.SETTED_EVENT_PARAMS, subscibeEventParams);

        return true;
    }

    /**
     * 上传操作，上传事件的标识，根据Session获取信息，并绑定邮箱，事件别名等，在SES中注册事件
     */
    @CrossOrigin(value = "*")
    @RequestMapping(method = RequestMethod.POST, value = "/subscribeWithSession")
    @ResponseBody
    public boolean getSubscribeEventRegiistered(HttpServletRequest request, @RequestBody SubscibeEventParams params) {
//        SubscibeEventParams subscibeEventParams = ConvertUtil.getSubscibeEventParamsfromSubscribeEventParamsDTO(params);
        String sesID = sesConnector.subscribeEvent(subscirbeEventService.createSubscirbeEvent(params));
        if (Strings.isNullOrEmpty(sesID)) return false;

        //注册完成后入库，清理缓存

        params.setEventSesID(sesID);
        eventService.saveSubscribeEvent(params);

        HttpSession session = request.getSession();
        session.removeAttribute(SessionNames.SELECT_SENSORS);
        session.removeAttribute(SessionNames.SETTED_EVENT_PARAMS);
        return true;
    }
}
