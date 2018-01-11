package yuan.flood.service.IService;

import yuan.flood.dao.Entity.ObservedProperty;

public interface IPropertyService {
    public ObservedProperty getPropertyByPropertyID(String propertyID);
}
