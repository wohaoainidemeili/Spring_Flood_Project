package yuan.flood.dao.Entity.UIDTO;

import yuan.flood.dao.Entity.SensorObsProperty;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阶段服务配置参数
 */
public class PhaseServiceParamsDTO {
    private boolean flag;
    private List<SensorObsProperty> selectedProperty = new ArrayList<>();
    private Map<String, String> otherParams = new HashMap<>();

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<SensorObsProperty> getSelectedProperty() {
        return selectedProperty;
    }

    public void setSelectedProperty(List<SensorObsProperty> selectedProperty) {
        this.selectedProperty = selectedProperty;
    }

    public Map<String, String> getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(Map<String, String> otherParams) {
        this.otherParams = otherParams;
    }
}
