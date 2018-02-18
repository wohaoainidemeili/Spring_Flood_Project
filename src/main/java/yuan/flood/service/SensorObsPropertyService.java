package yuan.flood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.SensorObsProperty;
import yuan.flood.dao.IDao.ISensorObsPropertyDao;
import yuan.flood.service.IService.ISensorObsPropertyService;

import java.util.List;

@Service
public class SensorObsPropertyService implements ISensorObsPropertyService {
    @Autowired
    private ISensorObsPropertyDao sensorObsPropertyDao;
    @Override
    public List getSensorPropertyList(List<Long> ids) {
        return null;
    }

    @Override
    public SensorObsProperty getSensorPropertyByID(Long id) {
        String countHql = "select count(*) from SensorObsProperty s";
        String hql = "from SensorObsProperty s where s.id=" + id;
        List countList = sensorObsPropertyDao.find(countHql);
        Long count = (Long) countList.get(0);
        if (count!=0) {
            List result = sensorObsPropertyDao.find(hql);
            if (result != null &&!result.isEmpty()) {
                return (SensorObsProperty) result.get(0);
            }
        }
        return null;
    }

    @Override
    public SensorObsProperty getSensorPropertyIDBySensor(SensorObsProperty sensorObsProperty) {
        String countHql = "select count(*) from SensorObsProperty s";
        String hql = "from SensorObsProperty s where s.sensorID='" + sensorObsProperty.getSensorID() + "' and s.observedPropertyID='" + sensorObsProperty.getObservedPropertyID() + "'";
        List countList = sensorObsPropertyDao.find(countHql);
        Long count = (Long) countList.get(0);
        if (count!=0) {
            List result = sensorObsPropertyDao.find(hql);
            if (result != null &&!result.isEmpty()) {
                return (SensorObsProperty) result.get(0);
            }
        }
        return null;
    }

    @Override
    public boolean getIsSensorProperty(String sensorID, String propertyID) {
        String hql = "from SensorObsProperty s where s.sensorID='" + sensorID + "' and s.propertyID='" + propertyID + "'";
        List result = sensorObsPropertyDao.find(hql);
        if (result == null || result.isEmpty()) {
            return false;
        }
        return true;
    }
}
