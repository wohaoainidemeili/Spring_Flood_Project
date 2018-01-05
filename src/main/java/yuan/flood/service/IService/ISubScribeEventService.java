package yuan.flood.service.IService;

import net.opengis.eml.x001.EMLDocument;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.SubscibeEventParams;

import java.util.List;

/**
 * Created by Yuan on 2017/2/14.
 */
public interface ISubScribeEventService {
    public EMLDocument createSubscirbeEvent(SubscibeEventParams subscibeEventParams);

    public SubscibeEventParams getEventSpatialArea(SubscibeEventParams subscibeEventParams);

    public List<Sensor> getEventSensorList(SubscibeEventParams subscibeEventParams);

}
