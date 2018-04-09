package yuan.flood.service.IService;

import yuan.flood.dao.Entity.AlertFloodResult;

import java.util.List;

public interface IAlertFloodService {
    List<AlertFloodResult> findAlertFloodResultByEventID(String eventID);
}
