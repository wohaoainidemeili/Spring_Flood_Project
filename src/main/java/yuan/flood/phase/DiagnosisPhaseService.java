package yuan.flood.phase;

import org.springframework.beans.factory.annotation.Autowired;
import yuan.flood.service.IService.IEventService;

import javax.xml.crypto.Data;
import java.util.Date;

public class DiagnosisPhaseService implements IPhaseService{

    @Autowired
    private IEventService eventService;
    @Override
    public void executeService(String sesID, Date date,Object object) {
        //do nothing
     eventService.getAllRegisteredEvent();
        System.out.println("My diagnosis service has run");

    }
}
