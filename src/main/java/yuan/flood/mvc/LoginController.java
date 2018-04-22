package yuan.flood.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import yuan.flood.dao.Entity.DetectedEvent;
import yuan.flood.dao.Entity.PredictWaterLevelResult;
import yuan.flood.dao.Entity.UIDTO.FloodResult;
import yuan.flood.dao.Entity.User;
import yuan.flood.dao.IDao.IDetectedEventDao;
import yuan.flood.dao.IDao.IPredictWaterLevelResultDao;
import yuan.flood.dao.IDao.IUserDao;
import yuan.flood.service.IService.IDecodeWNSEventService;
import yuan.flood.service.IService.IPredictWaterLevelService;
import yuan.flood.service.IService.IUserService;
import yuan.flood.service.DecodeWNSEventService;
import yuan.flood.until.SessionNames;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String printWelcome() {
		//model.addAttribute("message", "Hello world!");
		//decodeWNSEventService.getEventFromWNS();
//		DetectedEvent detectedEvent=new DetectedEvent();
//		detectedEvent.setStartTimeLong(10L);
//		detectedEventDao.saveOrUpdate(detectedEvent);
		//decodeWNSEventService.getEventFromWNS();
		User user = new User();
		user.setUserID("sasa");
		user.setPassWord("sad");
		userService.saveUser(user);
//		PredictWaterLevelResult predictWaterLevelResult = new PredictWaterLevelResult();
//		predictWaterLevelResult.setTimeLonMatrixStr("1");
//		predictWaterLevelService.savePredictWaterLevelResult(predictWaterLevelResult);
//		PredictWaterLevelResult predictWaterLevelResults= predictWaterLevelService.getPredictWaterLevelResultByTest("from PredictWaterLevelResult p where p.timeLonMatrixStr='1'");

		return "/access";
	}

	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public boolean isLegealUser(@ModelAttribute(value = "userForm") User user, HttpServletRequest request) {
		//justify the user is legeal or not
		boolean isLegeal = userService.isLegealUser(user);
		//if the user is exist , then set the session of user into servlet

		if (isLegeal) {
			Enumeration em = request.getSession().getAttributeNames();
			while (em.hasMoreElements()) {
				request.getSession().removeAttribute(em.nextElement().toString());
			}
			request.getSession().setAttribute("user", userService.getUser(user.getUserID()));
			//set the max active time is 30 minutes
			request.getSession().setMaxInactiveInterval(1800);
		}

		return isLegeal;
	}

	@CrossOrigin(value = "*", maxAge = 3600)
	@RequestMapping(value = "/api/user")
	@ResponseBody
	public List<User> testCors() {
		List<User> users = new ArrayList<User>();
		User user = new User();
		user.setUserID("1");
		user.setPassWord("sad");
		user.setUserLonID(132l);
		users.add(user);
		return users;
	}


	/**
	 * 用户判断当前是否有用户登录到系统中，登录则返回当前的用户名
	 * @param request
	 * @return
	 */
	@CrossOrigin(value = "*")
	@RequestMapping(value = "/getCurrentUser", method = RequestMethod.POST)
	@ResponseBody
	public FloodResult<String> getCurrentUser(HttpServletRequest request) {
		FloodResult<String> floodResult = new FloodResult<>();
		floodResult.setFlag(true);
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(SessionNames.USER);
		if (user==null){
			floodResult.setFlag(false);
			floodResult.setMessage("请先登录！");
			return floodResult;
		}
		floodResult.setObject(user.getUserID());
		return floodResult;
	}

	/**
	 * 登录操作，如果是则验证通过，并加入session
	 * @param user
	 * @param request
	 * @return
	 */
	@CrossOrigin(value = "*")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public FloodResult<Boolean> getIsRegisteredUser(@RequestBody User user, HttpServletRequest request) {
		FloodResult<Boolean> floodResult = new FloodResult<>();
		floodResult.setFlag(true);
		boolean isLegeal = userService.isLegealUser(user);
		if (!isLegeal) {
			floodResult.setFlag(false);
			floodResult.setMessage("当前" + user.getUserID() + "用户不存在");
			return floodResult;
		}
		//将用户加入缓存

		Enumeration em = request.getSession().getAttributeNames();
		while (em.hasMoreElements()) {
			request.getSession().removeAttribute(em.nextElement().toString());
		}
		request.getSession().setAttribute(SessionNames.USER, userService.getUser(user.getUserID()));
		//set the max active time is 30 minutes
		request.getSession().setMaxInactiveInterval(1800);
		floodResult.setObject(isLegeal);

		return floodResult;
	}

	/**
	 * 用户注册操作，完成注册
	 * @param user
	 * @param request
	 * @return
	 */
	@CrossOrigin(value = "*")
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	@ResponseBody
	public FloodResult<Boolean> registerUser(@RequestBody User user, HttpServletRequest request) {
		FloodResult<Boolean> floodResult = new FloodResult<>();
		floodResult.setFlag(true);
		if (user==null||user.getUserID()==null){
			floodResult.setFlag(false);
			floodResult.setMessage("错误的用户注册信息");
			return floodResult;
		}

		//确定当前用户ID是否已经注册
		User repeatUser = userService.getUser(user.getUserID());
		if (repeatUser!=null){
			floodResult.setFlag(false);
			floodResult.setMessage("该用户名已注册！");
			return floodResult;
		}

		//注册用户
		userService.saveUser(user);

		floodResult.setObject(true);
		return floodResult;

	}
}