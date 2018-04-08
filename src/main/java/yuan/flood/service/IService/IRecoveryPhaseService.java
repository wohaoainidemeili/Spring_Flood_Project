package yuan.flood.service.IService;

import yuan.flood.dao.Entity.DetectedFullEvent;

import java.util.Date;

public interface IRecoveryPhaseService {
    void executeService(String sesID, Date date, DetectedFullEvent detectedFullEvent);
}
