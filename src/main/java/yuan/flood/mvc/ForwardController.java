package yuan.flood.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/web")
public class ForwardController {
    @RequestMapping(value = "**")
    public void forwardToClient(HttpServletRequest request, HttpServletResponse response){
      try {
          request.getRequestDispatcher("../homePage").forward(request,response);
      } catch (ServletException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }
    }

}
