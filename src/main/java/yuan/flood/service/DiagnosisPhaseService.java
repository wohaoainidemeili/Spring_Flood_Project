package yuan.flood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.service.IService.IEventService;
import yuan.flood.service.IService.IPhaseService;

import java.util.Date;
@Service
public class DiagnosisPhaseService implements IPhaseService {

    @Autowired
    private IEventService eventService;
    @Override
    public void executeService(String sesID, Date date,Object object) {
        //do nothing
     eventService.getAllRegisteredEvent();
        System.out.println("My diagnosis service has run");

    }
}
