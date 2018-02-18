package yuan.flood.mvc;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.SensorObsProperty;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.Entity.UIDTO.*;
import yuan.flood.dao.Entity.UIEntity.ConvertUtil;
import yuan.flood.dao.Entity.UIEntity.SensorDTO;
import yuan.flood.dao.Entity.User;
import yuan.flood.message.MessageRecieveThread;
import yuan.flood.service.DecodeWNSEventService;
import yuan.flood.service.IService.IDecodeWNSEventService;
import yuan.flood.service.IService.ISensorObsPropertyService;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.service.IService.ISubScribeEventService;
import yuan.flood.ses.SESConnector;
import yuan.flood.until.ReadConfig;
import yuan.flood.until.SOSSESConfig;
import yuan.flood.until.SessionNames;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Created by Yuan on 2017/1/12.
 */
@Controller
public class RegisterEventController {
    @Autowired
    private ISensorService sensorService;
    @Autowired
    private ReadConfig readConfig;
    @Autowired
    private ISensorObsPropertyService sensorObsPropertyService;

//    @Autowired
   // private SESConnector sesConnector;
  //  @Autowired
   // private ISubScribeEventService subscirbeEventService;
    @RequestMapping(value = "/registerEvent",method = RequestMethod.GET)
    public String getRegisterEvent(HttpServletRequest request,ModelMap modelMap){
        //decodeWNSEventService.saveTest();
       // decodeWNSEventService.saveEventFromWNS();
        HttpSession session=request.getSession();
        User user= (User) session.getAttribute("user");
        if (user!=null){
            String userID= user.getUserID();
            modelMap.addAttribute("userName",userID);

            readConfig.read();
            //SubscibeEventParams subscibeEventParams=new SubscibeEventParams();
            //sesConnector.subscribeEvent(subscirbeEventService.createSubscirbeEvent(subscibeEventParams));
            String sosUrl= SOSSESConfig.getSosurl();
            List<Sensor> sensors= sensorService.getSensors(sosUrl);
//            sensors.get(1).setSensorName("上沙兰（二）水文观测站");
//            sensors.get(1).setLat(26.9333);
//            sensors.get(1).setLon(114.8);
            modelMap.addAttribute("sensorList", sensors);
        }else {
            return "login";
        }
        return "registerEvent";
    }

    /**
     * 获取所有的传感器信息
     * @return
     */
    @CrossOrigin(value = "*")
    @RequestMapping(value = "/getSensorInfo",method = RequestMethod.POST)
    @ResponseBody
    public List<Sensor> getSensors(){
        readConfig.read();
        String sosUrl= SOSSESConfig.getSosurl();
        List<Sensor> sensors= sensorService.getSensors(sosUrl);
        //save sensor
        sensorService.saveSensorsAndObsProperty(sensors);
//        sensors.get(1).setSensorName("上沙兰（二）水文观测站");
//        sensors.get(1).setLat(26.9333);
//        sensors.get(1).setLon(114.8);
        return sensors;
    }

    /**
     * 用于上传订阅事件选择的传感器
     * @param request
     * @return
     */
    @CrossOrigin(value = "*")
    @RequestMapping(value = "/getSelectSensors",method = RequestMethod.POST)
    @ResponseBody
    public boolean getSelectSenosors(HttpServletRequest request){
        String[] sensorIDs= (String[]) request.getParameterValues("sensorIDs");
        if (sensorIDs!=null&&sensorIDs.length>0){
            HttpSession session=request.getSession();
            session.removeAttribute("selectSensors");
            session.setAttribute("selectSensors",sensorIDs);
            return true;
        }else{
            return false;
        }
    }

    @CrossOrigin(value = "*")
    @RequestMapping(value = "/getSelectSensorsJson",method = RequestMethod.POST)
    @ResponseBody
    public boolean getSelectSenosorsJson(HttpServletRequest request, HttpServletResponse response, @RequestBody String[] sensorIDs) throws IOException {
//        if (sensorIDs==null||sensorIDs.length==0) {response.sendRedirect("error");return false;}
        if (sensorIDs!=null&&sensorIDs.length>0){
            HttpSession session=request.getSession();
            session.removeAttribute("selectSensors");
            session.setAttribute("selectSensors",sensorIDs);
//            response.sendRedirect("simpleSubscribeEvnt");
            return true;
        }else{
            return false;
        }
    }

    @CrossOrigin(value = "*")
    @RequestMapping(value = "/getSelectPropertiesJson ", method = RequestMethod.POST)
    @ResponseBody
    public FloodResult<Boolean> getSelectPropertiesJson(HttpServletRequest request, @RequestBody List<SensorObsProperty> properties) {
        FloodResult<Boolean> floodResult = new FloodResult<>();
        floodResult.setFlag(true);
        HttpSession session = request.getSession();
        if (properties != null && !properties.isEmpty()) {
            //获取SensorProperty的ID信息
            List<SensorObsProperty> sensorObsProperties = new ArrayList<>();
            for (SensorObsProperty sensorObsProperty : properties) {
                SensorObsProperty fullSensorProperty = sensorObsPropertyService.getSensorPropertyIDBySensor(sensorObsProperty);
                if (fullSensorProperty==null) {
                    floodResult.setFlag(false);
                    floodResult.setMessage("属性设置错误！无法找到对应的传感器属性！");
                    floodResult.setObject(false);
                    return floodResult;
                }else {
                    sensorObsProperties.add(fullSensorProperty);
                }
            }
            session.removeAttribute(SessionNames.SELECT_PROPERTYIES);
            session.setAttribute(SessionNames.SELECT_PROPERTYIES,sensorObsProperties);
            return floodResult;
        } else {
            floodResult.setFlag(false);
            floodResult.setMessage("属性错误！属性集不可为空！");
            floodResult.setObject(false);
            return floodResult;
        }
    }
    /**
     * 用于生成React订阅界面的Json数据，实时更新
     */
    @CrossOrigin(value = "*")
    @RequestMapping(value = "/getSubScribeJson")
    @ResponseBody
    public SubscribeParamsDTO getSubScribeDTO(HttpServletRequest request){

       //创建基本的返回数据内容
        SubscribeParamsDTO subscribeParamsDTO=new SubscribeParamsDTO();
        SensorSetParamsDTO sensorSetParamsDTO=new SensorSetParamsDTO();
        EventParamsDTO eventParamsDTO=new EventParamsDTO();
        PhaseServiceParamsDTO phaseServiceParamsDTO = new PhaseServiceParamsDTO();

        //如果session缓存中不包含选中的传感器ID，直接返回结果
        HttpSession session=request.getSession();
        String[] sensorIDs= (String[]) session.getAttribute("selectSensors");
        SubscibeEventParams subscibeEventParams = (SubscibeEventParams) session.getAttribute(SessionNames.SETTED_EVENT_PARAMS);
        List<SensorObsProperty> sensorObsProperties = (List<SensorObsProperty>) session.getAttribute(SessionNames.SELECT_PROPERTYIES);

        if (sensorIDs==null||sensorIDs.length==0) {
            subscribeParamsDTO.setDataset(sensorSetParamsDTO);
            subscribeParamsDTO.setEvent(eventParamsDTO);
            subscribeParamsDTO.setService(phaseServiceParamsDTO);
            return subscribeParamsDTO;
        }
        //如果当前包含sensors的缓存，则进行数据的修改
        sensorSetParamsDTO.setFlag(true);
        List<SensorDTO> sensorDTOList = new ArrayList<SensorDTO>();
        for (int i=0;i<sensorIDs.length;i++) {
            List<Sensor> sensors = sensorService.findObseredPropertyBySensorID(sensorIDs[i]);
            if (sensors.isEmpty() || sensors == null) {
                continue;
            }
            for (int j=0;j<sensors.size();j++) {
                sensorDTOList.add(ConvertUtil.getSensorDTOfromSensor(sensors.get(j)));
            }

        }

        sensorSetParamsDTO.setSensorList(sensorDTOList);

        //再获取订阅参数
       if (subscibeEventParams==null){
           subscribeParamsDTO.setDataset(sensorSetParamsDTO);
           subscribeParamsDTO.setEvent(eventParamsDTO);
           subscribeParamsDTO.setService(phaseServiceParamsDTO);
           return subscribeParamsDTO;
       }

       //订阅参数缓存不为空，则进行数据设置
        eventParamsDTO.setFlag(true);
        eventParamsDTO.setParams(ConvertUtil.getSubscribeEventParamsDTOfromSubscibeEventParams(subscibeEventParams));

        //查询服务参数配置中，选中的传感器是否为空
        if (sensorObsProperties == null || sensorObsProperties.isEmpty()) {
            subscribeParamsDTO.setDataset(sensorSetParamsDTO);
            subscribeParamsDTO.setEvent(eventParamsDTO);
            subscribeParamsDTO.setService(phaseServiceParamsDTO);
            return subscribeParamsDTO;
        }
        //加载服务参数部分
        phaseServiceParamsDTO.setFlag(true);
        phaseServiceParamsDTO.setSelectedProperty(sensorObsProperties);
        phaseServiceParamsDTO.setOtherParams(new HashMap<>());

        subscribeParamsDTO.setDataset(sensorSetParamsDTO);
        subscribeParamsDTO.setEvent(eventParamsDTO);
        subscribeParamsDTO.setService(phaseServiceParamsDTO);
        return subscribeParamsDTO;
    }

}
