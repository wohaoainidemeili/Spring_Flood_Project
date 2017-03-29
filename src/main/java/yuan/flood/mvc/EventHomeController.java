package yuan.flood.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Yuan on 2017/2/27.
 */
@Controller
public class EventHomeController {
    @RequestMapping(value = "eventHomePage",method = RequestMethod.GET)
    public String getEventHomePage(){
        return "homePage";
    }
}
