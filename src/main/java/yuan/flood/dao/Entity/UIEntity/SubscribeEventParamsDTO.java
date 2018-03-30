package yuan.flood.dao.Entity.UIEntity;

import yuan.flood.dao.Entity.SensorObsProperty;
import yuan.flood.until.FeatureUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscribeEventParamsDTO {
    public final static String SENSOR_PROPERTY_IDS = "SensorPropertyIDs";
    public final static String EMAIL_ADDRESS = "EmailAddress";
    private String eventID;
    //for create the name of subevent in current event
    private Long order;
    //userdefined eventName
    private String eventName;
    //the attribute for detect event
    //only a threshold for diagnosis
    private String diagnosisSensor;
    private String diagnosisObservation;
    private Integer diagnosisDay;
    private Integer diagnosisHour;
    private Integer diagnosisMinute;
    private Integer diagnosisSecond;
    private Integer diagnosisRepeatTimes;
    private Double diagnosisThreshold;
    private String diagnosisUnit;
    //threshold and repeat times in a period for prepare
    private String prepareSensor;
    private String prepareObservation;
    private Integer prepareDay;
    private Integer prepareHour;
    private Integer prepareMinute;
    private Integer prepareSecond;
    private Integer prepareRepeatTimes;
    private Double prepareThreshold;
    private String prepareUnit;
    //threshold and repeat times in a period for response
    private String responseSensor;
    private String responseObservation;
    private Integer responseDay;
    private Integer responseHour;
    private Integer responseMinute;
    private Integer responseSecond;
    private Integer responseRepeatTimes;
    private Double responseThreshold;
    private String responseUnit;

    //threshold and repeat times in a period for recovery
    private  String recoverySensor;
    private String recoveryObservation;
    private Integer recoveryDay;
    private Integer recoveryHour;
    private Integer recoveryMinute;
    private Integer recoverySecond;
    private Integer recoveryRepeatTimes;
    private Double recoveryThreshold;
    private String recoveryUnit;
    //after subscribe the event we get eventSesID
    private String eventSesID;

    //空间位置
    private double maxLat;
    private double minLat;
    private double maxLon;
    private double minLon;

    private String userDefineName;
    private String email;

    // 服务参数
    private List<String> sensorPropertyIDs = new ArrayList<>();
    private Map<String, String> featureMap = new HashMap<>();

    public String getUserDefineName() {
        return userDefineName;
    }

    public void setUserDefineName(String userDefineName) {
        this.userDefineName = userDefineName;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDiagnosisSensor() {
        return diagnosisSensor;
    }

    public void setDiagnosisSensor(String diagnosisSensor) {
        this.diagnosisSensor = diagnosisSensor;
    }

    public String getDiagnosisObservation() {
        return diagnosisObservation;
    }

    public void setDiagnosisObservation(String diagnosisObservation) {
        this.diagnosisObservation = diagnosisObservation;
    }

    public Integer getDiagnosisDay() {
        return diagnosisDay;
    }

    public void setDiagnosisDay(Integer diagnosisDay) {
        this.diagnosisDay = diagnosisDay;
    }

    public Integer getDiagnosisHour() {
        return diagnosisHour;
    }

    public void setDiagnosisHour(Integer diagnosisHour) {
        this.diagnosisHour = diagnosisHour;
    }

    public Integer getDiagnosisMinute() {
        return diagnosisMinute;
    }

    public void setDiagnosisMinute(Integer diagnosisMinute) {
        this.diagnosisMinute = diagnosisMinute;
    }

    public Integer getDiagnosisSecond() {
        return diagnosisSecond;
    }

    public void setDiagnosisSecond(Integer diagnosisSecond) {
        this.diagnosisSecond = diagnosisSecond;
    }

    public Integer getDiagnosisRepeatTimes() {
        return diagnosisRepeatTimes;
    }

    public void setDiagnosisRepeatTimes(Integer diagnosisRepeatTimes) {
        this.diagnosisRepeatTimes = diagnosisRepeatTimes;
    }

    public Double getDiagnosisThreshold() {
        return diagnosisThreshold;
    }

    public void setDiagnosisThreshold(Double diagnosisThreshold) {
        this.diagnosisThreshold = diagnosisThreshold;
    }

    public String getDiagnosisUnit() {
        return diagnosisUnit;
    }

    public void setDiagnosisUnit(String diagnosisUnit) {
        this.diagnosisUnit = diagnosisUnit;
    }

    public String getPrepareSensor() {
        return prepareSensor;
    }

    public void setPrepareSensor(String prepareSensor) {
        this.prepareSensor = prepareSensor;
    }

    public String getPrepareObservation() {
        return prepareObservation;
    }

    public void setPrepareObservation(String prepareObservation) {
        this.prepareObservation = prepareObservation;
    }

    public Integer getPrepareDay() {
        return prepareDay;
    }

    public void setPrepareDay(Integer prepareDay) {
        this.prepareDay = prepareDay;
    }

    public Integer getPrepareHour() {
        return prepareHour;
    }

    public void setPrepareHour(Integer prepareHour) {
        this.prepareHour = prepareHour;
    }

    public Integer getPrepareMinute() {
        return prepareMinute;
    }

    public void setPrepareMinute(Integer prepareMinute) {
        this.prepareMinute = prepareMinute;
    }

    public Integer getPrepareSecond() {
        return prepareSecond;
    }

    public void setPrepareSecond(Integer prepareSecond) {
        this.prepareSecond = prepareSecond;
    }

    public Integer getPrepareRepeatTimes() {
        return prepareRepeatTimes;
    }

    public void setPrepareRepeatTimes(Integer prepareRepeatTimes) {
        this.prepareRepeatTimes = prepareRepeatTimes;
    }

    public Double getPrepareThreshold() {
        return prepareThreshold;
    }

    public void setPrepareThreshold(Double prepareThreshold) {
        this.prepareThreshold = prepareThreshold;
    }

    public String getPrepareUnit() {
        return prepareUnit;
    }

    public void setPrepareUnit(String prepareUnit) {
        this.prepareUnit = prepareUnit;
    }

    public String getResponseSensor() {
        return responseSensor;
    }

    public void setResponseSensor(String responseSensor) {
        this.responseSensor = responseSensor;
    }

    public String getResponseObservation() {
        return responseObservation;
    }

    public void setResponseObservation(String responseObservation) {
        this.responseObservation = responseObservation;
    }

    public Integer getResponseDay() {
        return responseDay;
    }

    public void setResponseDay(Integer responseDay) {
        this.responseDay = responseDay;
    }

    public Integer getResponseHour() {
        return responseHour;
    }

    public void setResponseHour(Integer responseHour) {
        this.responseHour = responseHour;
    }

    public Integer getResponseMinute() {
        return responseMinute;
    }

    public void setResponseMinute(Integer responseMinute) {
        this.responseMinute = responseMinute;
    }

    public Integer getResponseSecond() {
        return responseSecond;
    }

    public void setResponseSecond(Integer responseSecond) {
        this.responseSecond = responseSecond;
    }

    public Integer getResponseRepeatTimes() {
        return responseRepeatTimes;
    }

    public void setResponseRepeatTimes(Integer responseRepeatTimes) {
        this.responseRepeatTimes = responseRepeatTimes;
    }

    public Double getResponseThreshold() {
        return responseThreshold;
    }

    public void setResponseThreshold(Double responseThreshold) {
        this.responseThreshold = responseThreshold;
    }

    public String getResponseUnit() {
        return responseUnit;
    }

    public void setResponseUnit(String responseUnit) {
        this.responseUnit = responseUnit;
    }

    public String getRecoverySensor() {
        return recoverySensor;
    }

    public void setRecoverySensor(String recoverySensor) {
        this.recoverySensor = recoverySensor;
    }

    public String getRecoveryObservation() {
        return recoveryObservation;
    }

    public void setRecoveryObservation(String recoveryObservation) {
        this.recoveryObservation = recoveryObservation;
    }

    public Integer getRecoveryDay() {
        return recoveryDay;
    }

    public void setRecoveryDay(Integer recoveryDay) {
        this.recoveryDay = recoveryDay;
    }

    public Integer getRecoveryHour() {
        return recoveryHour;
    }

    public void setRecoveryHour(Integer recoveryHour) {
        this.recoveryHour = recoveryHour;
    }

    public Integer getRecoveryMinute() {
        return recoveryMinute;
    }

    public void setRecoveryMinute(Integer recoveryMinute) {
        this.recoveryMinute = recoveryMinute;
    }

    public Integer getRecoverySecond() {
        return recoverySecond;
    }

    public void setRecoverySecond(Integer recoverySecond) {
        this.recoverySecond = recoverySecond;
    }

    public Integer getRecoveryRepeatTimes() {
        return recoveryRepeatTimes;
    }

    public void setRecoveryRepeatTimes(Integer recoveryRepeatTimes) {
        this.recoveryRepeatTimes = recoveryRepeatTimes;
    }

    public Double getRecoveryThreshold() {
        return recoveryThreshold;
    }

    public void setRecoveryThreshold(Double recoveryThreshold) {
        this.recoveryThreshold = recoveryThreshold;
    }

    public String getRecoveryUnit() {
        return recoveryUnit;
    }

    public void setRecoveryUnit(String recoveryUnit) {
        this.recoveryUnit = recoveryUnit;
    }

    public String getEventSesID() {
        return eventSesID;
    }

    public void setEventSesID(String eventSesID) {
        this.eventSesID = eventSesID;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMaxLon() {
        return maxLon;
    }

    public void setMaxLon(double maxLon) {
        this.maxLon = maxLon;
    }

    public double getMinLon() {
        return minLon;
    }

    public void setMinLon(double minLon) {
        this.minLon = minLon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getSensorPropertyIDs() {
        //从Map中获取
//        String IDStr = this.featureMap.get(SENSOR_PROPERTY_IDS);
//        List<String> idList = FeatureUtil.getListFromString(IDStr);
//        return idList;
        return sensorPropertyIDs;
    }

    public void setSensorPropertyIDs(List<String> sensorPropertyIDs) {
        this.sensorPropertyIDs = sensorPropertyIDs;
//        String stringFromList = FeatureUtil.getStringFromList(sensorPropertyIDs);
//        this.featureMap.put(SENSOR_PROPERTY_IDS, stringFromList);
    }

    public Map<String, String> getFeatureMap() {
        return featureMap;
    }

    public void setFeatureMap(Map<String, String> featureMap) {
        this.featureMap = featureMap;
    }

}
