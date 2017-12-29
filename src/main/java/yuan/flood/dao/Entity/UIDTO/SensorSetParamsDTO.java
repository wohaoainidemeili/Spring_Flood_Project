package yuan.flood.dao.Entity.UIDTO;

import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.UIEntity.SensorDTO;

import java.util.List;

/**
 * 用于传感器数据集的记录
 */
public class SensorSetParamsDTO {
    public boolean flag;
    public List<SensorDTO> sensorList;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<SensorDTO> getSensorList() {
        return sensorList;
    }

    public void setSensorList(List<SensorDTO> sensorList) {
        this.sensorList = sensorList;
    }
}
