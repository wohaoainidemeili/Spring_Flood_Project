package yuan.flood.mvc;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sun.org.apache.xpath.internal.operations.*;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.Entity.UIDTO.FloodResult;
import yuan.flood.dao.Entity.UIDTO.SubscribeParamsDTO;
import yuan.flood.dao.Entity.UIEntity.ConvertUtil;
import yuan.flood.dao.Entity.UIEntity.SubscribeEventParamsDTO;
import yuan.flood.dao.Entity.User;
import yuan.flood.service.IService.IEventService;
import yuan.flood.until.SessionNames;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Yuan on 2017/1/11.
 */
@Controller
public class EventManagerController {
    @Autowired
    private IEventService eventService;

    /**
     * get eventManager pages
     *
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/homePage", method = RequestMethod.GET)
    public String getHomePage(HttpServletRequest request, ModelMap modelMap) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            String userID = user.getUserID();
            modelMap.addAttribute("userName", userID);
            //get the info of all event ID
            List<String> eventIDs = eventService.getAllEventIDs();
            modelMap.addAttribute("eventIDs", eventIDs);
        } else {
            return "login";
        }
        return "eventManager";
    }

    @RequestMapping(value = "/getEventInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getSensorsInfo() {
        Map<String, Object> myMap = new HashMap<String, Object>();
        myMap.put("name", "sda");

        List<User> users = new ArrayList<User>();
        User user1 = new User();
        User user2 = new User();
        user1.setUserLonID(Long.valueOf(1));
        user1.setUserID("sda");
        user1.setPassWord("sadas");
        users.add(user1);
        user2.setUserLonID(Long.valueOf(2));
        user2.setUserID("sda");
        user2.setPassWord("sadas");
        users.add(user2);
        myMap.put("users", users);
        return myMap;
    }

    /**
     * 拉取所有的事件，不单单包括一个用户的
     *
     * @param request
     * @return
     */
    @CrossOrigin(value = "*")
    @RequestMapping(value = "/getAllEventParams", method = RequestMethod.POST)
    @ResponseBody
    public FloodResult<List<SubscribeEventParamsDTO>> getAllEventParams(HttpServletRequest request) {
        FloodResult<List<SubscribeEventParamsDTO>> floodResult = new FloodResult<>();
        floodResult.setFlag(true);
        List<SubscribeEventParamsDTO> subscribeEventParamsDTOS = new ArrayList<>();
        try {
            List<SubscibeEventParams> eventParams = eventService.getAllRegisteredEvent();

            if (eventParams == null || eventParams.isEmpty()) {
                floodResult.setFlag(true);
                floodResult.setMessage("无注册事件");
                floodResult.setObject(subscribeEventParamsDTOS);
                return floodResult;
            }
            for (int i = 0; i < eventParams.size(); i++) {
                subscribeEventParamsDTOS.add(ConvertUtil.getSubscribeEventParamsDTOfromSubscibeEventParams(eventParams.get(0)));
            }
        } catch (Exception e) {
            floodResult.setFlag(false);
            floodResult.setMessage("获取事件信息错误！");
        }
        floodResult.setObject(subscribeEventParamsDTOS);
        return floodResult;
    }

    @CrossOrigin(value = "*")
    @RequestMapping(value = "/getEventParamsByUserID", method = RequestMethod.POST)
    @ResponseBody
    public FloodResult<List<SubscribeEventParamsDTO>> getEventParamsByUserID(HttpServletRequest request){
        FloodResult<List<SubscribeEventParamsDTO>> floodResult = new FloodResult<>();
        floodResult.setFlag(true);
        List<SubscribeEventParamsDTO> subscribeEventParamsDTOS = new ArrayList<>();
        //获取用户session
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionNames.USER);
        if (user == null) {
            floodResult.setFlag(false);
            floodResult.setMessage("请先登录，才能查看洪涝事件订阅内容！");
            return floodResult;
        }

        try {
            List<SubscibeEventParams> eventParams = eventService.getRegisteredEventByUserID(user.getUserLonID());

            if (eventParams == null || eventParams.isEmpty()) {
                floodResult.setFlag(true);
                floodResult.setMessage("无注册事件");
                floodResult.setObject(subscribeEventParamsDTOS);
                return floodResult;
            }
            for (int i = 0; i < eventParams.size(); i++) {
                subscribeEventParamsDTOS.add(ConvertUtil.getSubscribeEventParamsDTOfromSubscibeEventParams(eventParams.get(0)));
            }
        } catch (Exception e) {
            floodResult.setFlag(false);
            floodResult.setMessage("获取事件信息错误！");
        }
        floodResult.setObject(subscribeEventParamsDTOS);
        return floodResult;
    }
}
