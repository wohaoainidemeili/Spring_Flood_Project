package yuan.flood.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.service.IService.IEventService;
import yuan.flood.sos.DataTimeSeries;
import yuan.flood.sos.HighChartsDrawData;
import yuan.flood.until.ReadConfig;
import yuan.flood.until.SOSSESConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Yuan on 2017/2/27.
 */
@Controller
public class SingleEventController {
    @Autowired
    private IEventService eventService;
    @RequestMapping(value = "/showSingleEventInformation/{eventID}",method = RequestMethod.GET)
    public String getShowSingleEventInformationPage(@PathVariable String eventID,ModelMap modelMap,HttpServletRequest request){
        HttpSession session=request.getSession();
        session.removeAttribute("eventID");
        session.setAttribute("eventID",eventID);
        SubscibeEventParams params= eventService.getRegisteredEventParamsBySesid(eventID);
        modelMap.addAttribute("eventParams",params);
        return "showSingleEventInformation";
    }

    @ResponseBody
    @RequestMapping(value = "/getEventData",method = RequestMethod.POST)
    public String getEventDataForPlot(HttpServletRequest request){
        HttpSession session=request.getSession();
        String sesID= (String) session.getAttribute("eventID");
        String series= eventService.getDetectedEventBySESID(SOSSESConfig.getSosurl(), sesID);
        //create series json for draw;
        //demo:[{type:'areaspline',data:[29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]," +
       // "pointStart:1420070400000,pointIntervalUnit:'month',zoneAxis:'x',zones:[{value:1433116800000" +
        //       ",color:'#90ed7d'},{value:1435708800000,color:'#f15c80'},{value:1441065600000,color:'#90ed7d'}]}]

        return series;
    }

}
