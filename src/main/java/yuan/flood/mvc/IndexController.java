package yuan.flood.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping(value = "/")
    public String getIndex(){
        return "index";
    }
    @RequestMapping(value = "/show")
    public String getShow(){
        return "index";
    }

    @RequestMapping(value = "/tab")
    public String getTab(){
        return "index";
    }
}
