package yuan.flood.dao.Entity.UIDTO;

import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.UIEntity.ObservedPropertyDTO;
import yuan.flood.dao.Entity.UIEntity.SensorDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件显示层，用于透出传感器位置基本信息，以及当前选择的传感器属性信息
 */
public class SingleEventSensorDTO {
    /**
     * 诊断阶段传感器以及选中的属性信息
     */
    private SensorDTO diagnosisSensor;
    private ObservedPropertyDTO diagnosisProperty;

    /**
     * 准备阶段
     */
    private SensorDTO prepareSensor;
    private ObservedPropertyDTO prepareProperty;

    /**
     * 响应阶段
     */
    private SensorDTO responseSensor;
    private ObservedPropertyDTO responseProperty;

    /**
     * 恢复阶段
     */
    private SensorDTO recoverySensor;
    private ObservedPropertyDTO recoveryProperty;

    /**
     * 存放用于预测的传感器信息
     */
    List<SensorDTO> sensors = new ArrayList<SensorDTO>();

    public SensorDTO getDiagnosisSensor() {
        return diagnosisSensor;
    }

    public void setDiagnosisSensor(SensorDTO diagnosisSensor) {
        this.diagnosisSensor = diagnosisSensor;
    }


    public SensorDTO getPrepareSensor() {
        return prepareSensor;
    }

    public void setPrepareSensor(SensorDTO prepareSensor) {
        this.prepareSensor = prepareSensor;
    }



    public SensorDTO getResponseSensor() {
        return responseSensor;
    }

    public void setResponseSensor(SensorDTO responseSensor) {
        this.responseSensor = responseSensor;
    }


    public SensorDTO getRecoverySensor() {
        return recoverySensor;
    }

    public void setRecoverySensor(SensorDTO recoverySensor) {
        this.recoverySensor = recoverySensor;
    }

    public List<SensorDTO> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDTO> sensors) {
        this.sensors = sensors;
    }

    public ObservedPropertyDTO getDiagnosisProperty() {
        return diagnosisProperty;
    }

    public void setDiagnosisProperty(ObservedPropertyDTO diagnosisProperty) {
        this.diagnosisProperty = diagnosisProperty;
    }

    public ObservedPropertyDTO getPrepareProperty() {
        return prepareProperty;
    }

    public void setPrepareProperty(ObservedPropertyDTO prepareProperty) {
        this.prepareProperty = prepareProperty;
    }

    public ObservedPropertyDTO getResponseProperty() {
        return responseProperty;
    }

    public void setResponseProperty(ObservedPropertyDTO responseProperty) {
        this.responseProperty = responseProperty;
    }

    public ObservedPropertyDTO getRecoveryProperty() {
        return recoveryProperty;
    }

    public void setRecoveryProperty(ObservedPropertyDTO recoveryProperty) {
        this.recoveryProperty = recoveryProperty;
    }
}
