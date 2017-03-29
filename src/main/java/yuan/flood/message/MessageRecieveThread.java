package yuan.flood.message;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import yuan.flood.dao.Entity.DetectedEvent;
import yuan.flood.dao.IDao.IDetectedEventDao;
import yuan.flood.dao.IDao.IEventDao;

/**
 * Created by Yuan on 2017/2/21.
 */
public class MessageRecieveThread extends Thread {
    private static org.apache.log4j.Logger logger= org.apache.log4j.Logger.getLogger(MessageRecieveThread.class);
    @Autowired
    private IDetectedEventDao detectedEventDao;
    @Override
    public void run() {
        DetectedEvent detectedEvent=new DetectedEvent();
        detectedEvent.setStartTimeLong(108L);
        detectedEventDao.save(detectedEvent);
        logger.info("sadas");
    }
}
