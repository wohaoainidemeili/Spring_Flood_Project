package yuan.flood.message;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import yuan.flood.service.DecodeWNSEventService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by Yuan on 2017/2/17.
 */
public class FloodDecodeServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
       MessageRecieveThread messageRecieveThread=new MessageRecieveThread();
        messageRecieveThread.start();

    }
}
