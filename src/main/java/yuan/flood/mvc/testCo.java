package yuan.flood.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yuan.flood.service.IService.*;
import yuan.flood.service.PreparePhaseService;
import yuan.flood.sos.DataTimeSeries;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Yuan on 2017/2/11.
 */
@Controller
public class testCo {

    @Autowired
    private IPreparePhaseServiceT preparePhaseServiceT;
    @RequestMapping(value = "test")
    public String test() {
        return "test";
    }

    @ResponseBody
    @RequestMapping(value = "/sad", method = RequestMethod.GET)
    public String dataTimeSeries() {
        String res = null;
        res = "[{type:'areaspline',data:[29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]," +
                "pointStart:1420070400000,pointIntervalUnit:'month',zoneAxis:'x',zones:[{value:1433116800000" +
                ",color:'#90ed7d'},{value:1435708800000,color:'#f15c80'},{value:1441065600000,color:'#90ed7d'}]}]";
        return res;
    }


    @CrossOrigin(value = "*")
    @ResponseBody
    @RequestMapping(value = "/rand")
    public Integer getRandom() {
        Random random = new Random();
        return random.nextInt();
    }

    @CrossOrigin(value = "*")
    @ResponseBody
    @RequestMapping(value = "/sadJson")
    public String dataSadTest() {
        String res = "[{\"type\":\"areaspline\",\"data\":[29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]," +
                "\"pointStart\":1420070400000,\"pointIntervalUnit\":\"month\",\"zoneAxis\":\"x\",\"zones\":[{\"value\":1433116800000" +
                ",\"color\":\"#90ed7d\"},{\"value\":1435708800000,\"color\":\"#f15c80\"},{\"value\":1441065600000,\"color\":\"#90ed7d\"}]}]";
        return res;
    }

    @CrossOrigin(value = "*")
    @RequestMapping(value = "/testPrepare")
    public void getTestPrepare() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            Date date=simpleDateFormat.parse("2018-04-03 15:32:04.681");
            preparePhaseServiceT.executeService("Event0",date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
}
