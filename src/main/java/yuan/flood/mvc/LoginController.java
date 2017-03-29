package yuan.flood.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import yuan.flood.dao.Entity.DetectedEvent;
import yuan.flood.dao.Entity.User;
import yuan.flood.dao.IDao.IDetectedEventDao;
import yuan.flood.dao.IDao.IUserDao;
import yuan.flood.service.IService.IDecodeWNSEventService;
import yuan.flood.service.IService.IUserService;
import yuan.flood.service.DecodeWNSEventService;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Controller
public class LoginController {
	@Autowired
	private IUserService userService;
	@Autowired
	private IDecodeWNSEventService decodeWNSEventService;
	@RequestMapping(value = "/",method = RequestMethod.GET)
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
		return "login";
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

}