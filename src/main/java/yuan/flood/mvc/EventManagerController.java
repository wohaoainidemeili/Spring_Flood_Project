package yuan.flood.mvc;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sun.org.apache.xpath.internal.operations.*;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import yuan.flood.dao.Entity.User;
import yuan.flood.service.IService.IEventService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuan on 2017/1/11.
 */
@Controller
public class EventManagerController {
    @Autowired
    private IEventService eventService;
    /**
     * get eventManager pages
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/homePage",method = RequestMethod.GET)
    public String getHomePage(HttpServletRequest request,ModelMap modelMap){
        HttpSession session=request.getSession();
        User user= (User) session.getAttribute("user");
        if (user!=null){
            String userID= user.getUserID();
            modelMap.addAttribute("userName",userID);
            //get the info of all event ID
            List<String> eventIDs= eventService.getAllEventIDs();
            modelMap.addAttribute("eventIDs",eventIDs);
        }else {
            return "login";
        }
        return "eventManager";
    }

    @RequestMapping(value = "/getEventInfo",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getSensorsInfo(){
        Map<String,Object> myMap=new HashMap<String, Object>();
        myMap.put("name","sda");

        List<User> users=new ArrayList<User>();
        User user1=new User();
        User user2=new User();
        user1.setUserLonID(Long.valueOf(1));
        user1.setUserID("sda");
        user1.setPassWord("sadas");
        users.add(user1);
        user2.setUserLonID(Long.valueOf(2));
        user2.setUserID("sda");
        user2.setPassWord("sadas");
        users.add(user2);
        myMap.put("users",users);
        return myMap ;
    }

}
