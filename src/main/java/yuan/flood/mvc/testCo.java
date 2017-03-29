package yuan.flood.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import yuan.flood.sos.DataTimeSeries;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuan on 2017/2/11.
 */
@Controller
public class testCo {
    @RequestMapping(value = "test")
    public String test()
    {
        return "test";
    }
    @ResponseBody
    @RequestMapping(value = "/sad",method = RequestMethod.GET)
    public String dataTimeSeries() {
        String res=null;
        res="[{type:'areaspline',data:[29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]," +
                "pointStart:1420070400000,pointIntervalUnit:'month',zoneAxis:'x',zones:[{value:1433116800000" +
                ",color:'#90ed7d'},{value:1435708800000,color:'#f15c80'},{value:1441065600000,color:'#90ed7d'}]}]";
        return res;
    }
}
