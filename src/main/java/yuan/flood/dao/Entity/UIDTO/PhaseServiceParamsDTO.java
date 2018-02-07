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
    Map<String, String> params = new HashMap<>();
    List<SensorObsProperty> selectedSensor = new ArrayList<>();
}
