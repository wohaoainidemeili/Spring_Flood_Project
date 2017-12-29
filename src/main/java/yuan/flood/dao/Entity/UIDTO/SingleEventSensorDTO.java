package yuan.flood.dao.Entity.UIDTO;

import yuan.flood.dao.Entity.Sensor;

/**
 * 事件显示层，用于透出传感器位置基本信息，以及当前选择的传感器属性信息
 */
public class SingleEventSensorDTO {
    /**
     * 诊断阶段传感器以及选中的属性信息
     */
    private Sensor diagnosisSensor;
    private String diagnosisPropertyID;

    /**
     * 准备阶段
     */
    private Sensor prepareSensor;
    private String preparePropertyID;

    /**
     * 响应阶段
     */
    private Sensor responseSensor;
    private String responsePropertyID;

    /**
     * 恢复阶段
     */
    private Sensor recoverySensor;
    private String recoveryPropertyID;

    public Sensor getDiagnosisSensor() {
        return diagnosisSensor;
    }

    public void setDiagnosisSensor(Sensor diagnosisSensor) {
        this.diagnosisSensor = diagnosisSensor;
    }

    public String getDiagnosisPropertyID() {
        return diagnosisPropertyID;
    }

    public void setDiagnosisPropertyID(String diagnosisPropertyID) {
        this.diagnosisPropertyID = diagnosisPropertyID;
    }

    public Sensor getPrepareSensor() {
        return prepareSensor;
    }

    public void setPrepareSensor(Sensor prepareSensor) {
        this.prepareSensor = prepareSensor;
    }

    public String getResponsePropertyID() {
        return responsePropertyID;
    }

    public void setResponsePropertyID(String responsePropertyID) {
        this.responsePropertyID = responsePropertyID;
    }

    public String getPreparePropertyID() {
        return preparePropertyID;
    }

    public void setPreparePropertyID(String preparePropertyID) {
        this.preparePropertyID = preparePropertyID;
    }

    public Sensor getResponseSensor() {
        return responseSensor;
    }

    public void setResponseSensor(Sensor responseSensor) {
        this.responseSensor = responseSensor;
    }

    public Sensor getRecoverySensor() {
        return recoverySensor;
    }

    public void setRecoverySensor(Sensor recoverySensor) {
        this.recoverySensor = recoverySensor;
    }

    public String getRecoveryPropertyID() {
        return recoveryPropertyID;
    }

    public void setRecoveryPropertyID(String recoveryPropertyID) {
        this.recoveryPropertyID = recoveryPropertyID;
    }
}
