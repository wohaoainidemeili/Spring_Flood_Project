package yuan.flood.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.Entity.User;
import yuan.flood.message.MessageRecieveThread;
import yuan.flood.service.DecodeWNSEventService;
import yuan.flood.service.IService.IDecodeWNSEventService;
import yuan.flood.service.IService.ISensorService;
import yuan.flood.service.IService.ISubScribeEventService;
import yuan.flood.ses.SESConnector;
import yuan.flood.until.ReadConfig;
import yuan.flood.until.SOSSESConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Yuan on 2017/1/12.
 */
@Controller
public class RegisterEventController {
    @Autowired
    private ISensorService sensorService;
    @Autowired
    private ReadConfig readConfig;

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
            modelMap.addAttribute("sensorList",sensors);
        }else {
            return "login";
        }
        return "registerEvent";
    }
    @RequestMapping(value = "/getSensorInfo",method = RequestMethod.POST)
    @ResponseBody
    public List<Sensor> getSensors(){
        readConfig.read();
        String sosUrl= SOSSESConfig.getSosurl();
        List<Sensor> sensors= sensorService.getSensors(sosUrl);
        //save sensor
        sensorService.saveSensorsAndObsProperty(sensors);
        return sensors;
    }

    @RequestMapping(value = "/getSelectSensors",method = RequestMethod.POST)
    @ResponseBody
    public boolean getSelectSenosors(HttpServletRequest request,String[] sensorIDs){
        if (sensorIDs!=null&&sensorIDs.length>0){
            HttpSession session=request.getSession();
            session.removeAttribute("selectSensors");
            session.setAttribute("selectSensors",sensorIDs);
            return true;
        }else{
            return false;
        }

    }
}
