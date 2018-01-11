package yuan.flood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.ObservedProperty;
import yuan.flood.dao.IDao.IObsPropertyDao;
import yuan.flood.service.IService.IPropertyService;

import java.util.List;
@Service
public class PropertyService implements IPropertyService {
    @Autowired
    IObsPropertyDao propertyDao;
    @Override
    public ObservedProperty getPropertyByPropertyID(String propertyID) {
        String hql="from ObservedProperty s where s.propertyID='"+propertyID+"'";
        List<ObservedProperty> observedProperties = propertyDao.find(hql);
        if (observedProperties==null|| observedProperties.isEmpty()) return null;
        return observedProperties.get(0);
    }
}
