package yuan.flood.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yuan.flood.dao.Entity.DetectedEvent;
import yuan.flood.dao.Entity.PredictWaterLevelResult;
import yuan.flood.dao.Entity.User;
import yuan.flood.dao.IDao.IDetectedEventDao;
import yuan.flood.dao.IDao.IPredictWaterLevelResultDao;
import yuan.flood.dao.IDao.IUserDao;
import yuan.flood.service.IService.IDecodeWNSEventService;
import yuan.flood.service.IService.IPredictWaterLevelService;
import yuan.flood.service.IService.IUserService;
import yuan.flood.service.DecodeWNSEventService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Controller
public class LoginController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IPredictWaterLevelService predictWaterLevelService;
	@Autowired
	private IDecodeWNSEventService decodeWNSEventService;
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public String printWelcome() {
		//model.addAttribute("message", "Hello world!");
		//decodeWNSEventService.getEventFromWNS();
//		DetectedEvent detectedEvent=new DetectedEvent();
//		detectedEvent.setStartTimeLong(10L);
//		detectedEventDao.saveOrUpdate(detectedEvent);
		//decodeWNSEventService.getEventFromWNS();
		User user=new User();
		user.setUserID("sasa");
		user.setPassWord("sad");
		userService.saveUser(user);
//		PredictWaterLevelResult predictWaterLevelResult = new PredictWaterLevelResult();
//		predictWaterLevelResult.setTimeLonMatrixStr("1");
//		predictWaterLevelService.savePredictWaterLevelResult(predictWaterLevelResult);
//		PredictWaterLevelResult predictWaterLevelResults= predictWaterLevelService.getPredictWaterLevelResultByTest("from PredictWaterLevelResult p where p.timeLonMatrixStr='1'");

		return "/access";
	}
	@RequestMapping(value = "/check",method = RequestMethod.POST)
	@ResponseBody
	public boolean isLegealUser(@ModelAttribute(value = "userForm")User user,HttpServletRequest request){
		//justify the user is legeal or not
		boolean isLegeal= userService.isLegealUser(user);
		//if the user is exist , then set the session of user into servlet

		if (isLegeal){
			Enumeration em = request.getSession().getAttributeNames();
			while(em.hasMoreElements()){
				request.getSession().removeAttribute(em.nextElement().toString());
			}
			request.getSession().setAttribute("user",userService.getUser(user.getUserID()));
			//set the max active time is 30 minutes
			request.getSession().setMaxInactiveInterval(1800);
		}

		return isLegeal;
	}
	@CrossOrigin(value = "*",maxAge = 3600)
	@RequestMapping(value = "/api/user")
	@ResponseBody
	public List<User> testCors(){
		List<User> users = new ArrayList<User>();
		User user=new User();
		user.setUserID("1");
		user.setPassWord("sad");
		user.setUserLonID(132l);
		users.add(user);
		return users;
	}

}