package yuan.flood.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import yuan.flood.service.IService.IDecodeWNSEventService;

/**
 * Created by Yuan on 2017/2/26.
 */
@Controller
public class EventAcceptController {
    @Autowired
    private IDecodeWNSEventService decodeWNSEventService;
    private static boolean isStartLisener=false;
    @RequestMapping(value = "getRecieveEventPage",method = RequestMethod.GET)
    public String getRecieveEventPage(){
        return "recieveEventPage";
    }
    @RequestMapping(value = "startWNSLisener",method = RequestMethod.POST)
    public void startWNSLisener(){
        if (!isStartLisener) {
            isStartLisener=true;
            decodeWNSEventService.saveEventFromWNS();
        }
    }
}
