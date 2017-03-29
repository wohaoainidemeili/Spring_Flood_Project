package yuan.flood.mvc;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yuan.flood.dao.Entity.ObservedProperty;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.User;
import yuan.flood.service.IService.ISensorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Yuan on 2017/2/7.
 */
@Controller
public class SubscribeEventController {

    @Autowired
    private ISensorService sensorService;
    @RequestMapping(method = RequestMethod.GET,value = "/subscribeEvent")
    public String getGetRegisterEventParameters(HttpServletRequest request,ModelMap modelMap){
       String[] sensors= (String[]) request.getSession().getAttribute("selectSensors");
        if (sensors!=null&&sensors.length>0){
           modelMap.addAttribute("sensorsOption", Arrays.asList(sensors));
        }else{

        }
        return "getRegisterEventParameters";
    }

    @RequestMapping(method = RequestMethod.POST,value = "/getObseervationProperty")
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

    @RequestMapping(method = RequestMethod.POST,value = "/getEventParameters")
    public void getEventParameters(@ModelAttribute(value = "EventForm") Object object){
        Object o=object;
    }
}
