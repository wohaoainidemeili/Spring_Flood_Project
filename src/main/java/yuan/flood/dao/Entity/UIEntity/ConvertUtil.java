package yuan.flood.dao.Entity.UIEntity;

import yuan.flood.dao.Entity.ObservedProperty;
import yuan.flood.dao.Entity.Sensor;
import yuan.flood.dao.Entity.SubscibeEventParams;
import yuan.flood.dao.Entity.UIDTO.EventPhaseDTO;
import yuan.flood.dao.Entity.UIDTO.EventSensorPropertyDTO;
import yuan.flood.dao.Entity.UIDTO.SubscribeParamsDTO;
import yuan.flood.until.FeatureUtil;

import java.util.*;

public class ConvertUtil {
    public static SensorDTO getSensorDTOfromSensor(Sensor sensor) {
        if (sensor==null) return new SensorDTO();
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setSensorID(sensor.getSensorID());
        sensorDTO.setSensorName(sensor.getSensorName());
        sensorDTO.setLat(sensor.getLat());
        sensorDTO.setLon(sensor.getLon());

        if (sensor.getObservedProperties() == null) return sensorDTO;

        Iterator iterator = sensor.getObservedProperties().iterator();
        List<ObservedPropertyDTO> observedPropertyDTOS = new ArrayList<ObservedPropertyDTO>();
        while (iterator.hasNext()) {
           ObservedProperty observedProperty= (ObservedProperty) iterator.next();
            ObservedPropertyDTO observedPropertyDTO = getObservedPropertyDTOfromObservedProperty(observedProperty);
            observedPropertyDTOS.add(observedPropertyDTO);
        }
        sensorDTO.setObservedProperties(observedPropertyDTOS);
        return sensorDTO;
    }

    public static ObservedPropertyDTO getObservedPropertyDTOfromObservedProperty(ObservedProperty observedProperty) {
        if (observedProperty==null) return new ObservedPropertyDTO();
        ObservedPropertyDTO observedPropertyDTO = new ObservedPropertyDTO();
        observedPropertyDTO.setPropertyID(observedProperty.getPropertyID());
        observedPropertyDTO.setPropertyName(observedProperty.getPropertyName());
        observedPropertyDTO.setUnit(observedProperty.getUnit());
        return observedPropertyDTO;
    }

    public static SubscribeEventParamsDTO getSubscribeEventParamsDTOfromSubscibeEventParams(SubscibeEventParams subscibeEventParams) {
        if (subscibeEventParams==null) return new SubscribeEventParamsDTO();
        SubscribeEventParamsDTO subscribeEventParamsDTO = new SubscribeEventParamsDTO();

        subscribeEventParamsDTO.setDiagnosisDay(subscibeEventParams.getDiagnosisDay());
        subscribeEventParamsDTO.setDiagnosisHour(subscibeEventParams.getPrepareHour());
        subscribeEventParamsDTO.setDiagnosisMinute(subscibeEventParams.getDiagnosisMinute());
        subscribeEventParamsDTO.setDiagnosisObservation(subscibeEventParams.getDiagnosisObservation());
        subscribeEventParamsDTO.setDiagnosisRepeatTimes(subscibeEventParams.getDiagnosisRepeatTimes());
        subscribeEventParamsDTO.setDiagnosisSecond(subscibeEventParams.getDiagnosisSecond());
        subscribeEventParamsDTO.setDiagnosisSensor(subscibeEventParams.getDiagnosisSensor());
        subscribeEventParamsDTO.setDiagnosisThreshold(subscibeEventParams.getDiagnosisThreshold());
        subscribeEventParamsDTO.setDiagnosisUnit(subscibeEventParams.getDiagnosisUnit());

        subscribeEventParamsDTO.setEventID(subscibeEventParams.getEventID());
        subscribeEventParamsDTO.setEventName(subscibeEventParams.getEventName());
        subscribeEventParamsDTO.setEventSesID(subscibeEventParams.getEventSesID());
        subscribeEventParamsDTO.setOrder(subscibeEventParams.getOrder());

        subscribeEventParamsDTO.setPrepareDay(subscibeEventParams.getPrepareDay());
        subscribeEventParamsDTO.setPrepareHour(subscibeEventParams.getPrepareHour());
        subscribeEventParamsDTO.setPrepareMinute(subscibeEventParams.getPrepareMinute());
        subscribeEventParamsDTO.setPrepareObservation(subscibeEventParams.getPrepareObservation());
        subscribeEventParamsDTO.setPrepareRepeatTimes(subscibeEventParams.getPrepareRepeatTimes());
        subscribeEventParamsDTO.setPrepareSecond(subscibeEventParams.getPrepareSecond());
        subscribeEventParamsDTO.setPrepareSensor(subscibeEventParams.getPrepareSensor());
        subscribeEventParamsDTO.setPrepareThreshold(subscibeEventParams.getPrepareThreshold());
        subscribeEventParamsDTO.setPrepareUnit(subscibeEventParams.getPrepareUnit());

        subscribeEventParamsDTO.setRecoveryDay(subscibeEventParams.getRecoveryDay());
        subscribeEventParamsDTO.setRecoveryHour(subscibeEventParams.getRecoveryHour());
        subscribeEventParamsDTO.setRecoveryMinute(subscibeEventParams.getRecoveryMinute());
        subscribeEventParamsDTO.setRecoveryObservation(subscibeEventParams.getRecoveryObservation());
        subscribeEventParamsDTO.setRecoveryRepeatTimes(subscibeEventParams.getRecoveryRepeatTimes());
        subscribeEventParamsDTO.setRecoverySecond(subscibeEventParams.getRecoverySecond());
        subscribeEventParamsDTO.setRecoverySensor(subscibeEventParams.getRecoverySensor());
        subscribeEventParamsDTO.setRecoveryThreshold(subscibeEventParams.getRecoveryThreshold());
        subscribeEventParamsDTO.setRecoveryUnit(subscribeEventParamsDTO.getDiagnosisUnit());

        subscribeEventParamsDTO.setResponseDay(subscibeEventParams.getResponseDay());
        subscribeEventParamsDTO.setResponseHour(subscibeEventParams.getResponseHour());
        subscribeEventParamsDTO.setResponseMinute(subscibeEventParams.getResponseMinute());
        subscribeEventParamsDTO.setResponseObservation(subscibeEventParams.getResponseObservation());
        subscribeEventParamsDTO.setResponseRepeatTimes(subscibeEventParams.getResponseRepeatTimes());
        subscribeEventParamsDTO.setResponseSecond(subscibeEventParams.getResponseSecond());
        subscribeEventParamsDTO.setResponseSensor(subscibeEventParams.getResponseSensor());
        subscribeEventParamsDTO.setResponseThreshold(subscibeEventParams.getResponseThreshold());
        subscribeEventParamsDTO.setResponseUnit(subscibeEventParams.getResponseUnit());

        subscribeEventParamsDTO.setMaxLat(subscibeEventParams.getMaxLat());
        subscribeEventParamsDTO.setMaxLon(subscibeEventParams.getMaxLon());
        subscribeEventParamsDTO.setMinLat(subscibeEventParams.getMinLat());
        subscribeEventParamsDTO.setMinLon(subscibeEventParams.getMinLon());
        subscribeEventParamsDTO.setEmail(subscibeEventParams.getEmail());

        subscribeEventParamsDTO.setUserDefineName(subscibeEventParams.getUserDefineName());
        subscribeEventParamsDTO.setFeatureMap(FeatureUtil.getFeatureFromString(subscibeEventParams.getFeatureString()));
        Map feature= subscribeEventParamsDTO.getFeatureMap();
        //计算sensorIDs并设置
        String sensorIDStrings = (String) feature.get(SubscribeEventParamsDTO.SENSOR_PROPERTY_IDS);
        List<String> listFromString = FeatureUtil.getListFromString(sensorIDStrings);
        subscribeEventParamsDTO.setSensorPropertyIDs(listFromString);

        return subscribeEventParamsDTO;

    }

    public static SubscibeEventParams getSubscibeEventParamsfromSubscribeEventParamsDTO(SubscribeEventParamsDTO subscribeEventParamsDTO) {
        SubscibeEventParams subscibeEventParams = new SubscibeEventParams();
       if (subscribeEventParamsDTO==null) return subscibeEventParams;
        subscibeEventParams.setDiagnosisDay(subscribeEventParamsDTO.getDiagnosisDay());
        subscibeEventParams.setDiagnosisHour(subscribeEventParamsDTO.getPrepareHour());
        subscibeEventParams.setDiagnosisMinute(subscribeEventParamsDTO.getDiagnosisMinute());
        subscibeEventParams.setDiagnosisObservation(subscribeEventParamsDTO.getDiagnosisObservation());
        subscibeEventParams.setDiagnosisRepeatTimes(subscribeEventParamsDTO.getDiagnosisRepeatTimes());
        subscibeEventParams.setDiagnosisSecond(subscribeEventParamsDTO.getDiagnosisSecond());
        subscibeEventParams.setDiagnosisSensor(subscribeEventParamsDTO.getDiagnosisSensor());
        subscibeEventParams.setDiagnosisThreshold(subscribeEventParamsDTO.getDiagnosisThreshold());
        subscibeEventParams.setDiagnosisUnit(subscribeEventParamsDTO.getDiagnosisUnit());

        subscibeEventParams.setEventID(subscribeEventParamsDTO.getEventID());
        subscibeEventParams.setEventName(subscribeEventParamsDTO.getEventName());
        subscibeEventParams.setEventSesID(subscribeEventParamsDTO.getEventSesID());
        subscibeEventParams.setOrder(subscribeEventParamsDTO.getOrder());

        subscibeEventParams.setPrepareDay(subscribeEventParamsDTO.getPrepareDay());
        subscibeEventParams.setPrepareHour(subscribeEventParamsDTO.getPrepareHour());
        subscibeEventParams.setPrepareMinute(subscribeEventParamsDTO.getPrepareMinute());
        subscibeEventParams.setPrepareObservation(subscribeEventParamsDTO.getPrepareObservation());
        subscibeEventParams.setPrepareRepeatTimes(subscribeEventParamsDTO.getPrepareRepeatTimes());
        subscibeEventParams.setPrepareSecond(subscribeEventParamsDTO.getPrepareSecond());
        subscibeEventParams.setPrepareSensor(subscribeEventParamsDTO.getPrepareSensor());
        subscibeEventParams.setPrepareThreshold(subscribeEventParamsDTO.getPrepareThreshold());
        subscibeEventParams.setPrepareUnit(subscribeEventParamsDTO.getPrepareUnit());

        subscibeEventParams.setRecoveryDay(subscribeEventParamsDTO.getRecoveryDay());
        subscibeEventParams.setRecoveryHour(subscribeEventParamsDTO.getRecoveryHour());
        subscibeEventParams.setRecoveryMinute(subscribeEventParamsDTO.getRecoveryMinute());
        subscibeEventParams.setRecoveryObservation(subscribeEventParamsDTO.getRecoveryObservation());
        subscibeEventParams.setRecoveryRepeatTimes(subscribeEventParamsDTO.getRecoveryRepeatTimes());
        subscibeEventParams.setRecoverySecond(subscribeEventParamsDTO.getRecoverySecond());
        subscibeEventParams.setRecoverySensor(subscribeEventParamsDTO.getRecoverySensor());
        subscibeEventParams.setRecoveryThreshold(subscribeEventParamsDTO.getRecoveryThreshold());
        subscibeEventParams.setRecoveryUnit(subscibeEventParams.getDiagnosisUnit());

        subscibeEventParams.setResponseDay(subscribeEventParamsDTO.getResponseDay());
        subscibeEventParams.setResponseHour(subscribeEventParamsDTO.getResponseHour());
        subscibeEventParams.setResponseMinute(subscribeEventParamsDTO.getResponseMinute());
        subscibeEventParams.setResponseObservation(subscribeEventParamsDTO.getResponseObservation());
        subscibeEventParams.setResponseRepeatTimes(subscribeEventParamsDTO.getResponseRepeatTimes());
        subscibeEventParams.setResponseSecond(subscribeEventParamsDTO.getResponseSecond());
        subscibeEventParams.setResponseSensor(subscribeEventParamsDTO.getResponseSensor());
        subscibeEventParams.setResponseThreshold(subscribeEventParamsDTO.getResponseThreshold());
        subscibeEventParams.setResponseUnit(subscribeEventParamsDTO.getResponseUnit());

        subscibeEventParams.setMaxLat(subscribeEventParamsDTO.getMaxLat());
        subscibeEventParams.setMaxLon(subscribeEventParamsDTO.getMaxLon());
        subscibeEventParams.setMinLat(subscribeEventParamsDTO.getMinLat());
        subscibeEventParams.setMinLon(subscribeEventParamsDTO.getMinLon());
        subscibeEventParams.setEmail(subscribeEventParamsDTO.getEmail());

        subscibeEventParams.setUserDefineName(subscribeEventParamsDTO.getUserDefineName());
        String stringFromList = FeatureUtil.getStringFromList(subscribeEventParamsDTO.getSensorPropertyIDs());
        subscribeEventParamsDTO.getFeatureMap().put(SubscribeEventParamsDTO.SENSOR_PROPERTY_IDS, stringFromList);
        subscibeEventParams.setFeatureString(FeatureUtil.getStringFromFeature(subscribeEventParamsDTO.getFeatureMap()));
        return subscibeEventParams;
    }

    public static EventSensorPropertyDTO getEventSensorPropertyDTOFromSensorAndProperty(Sensor sensor, ObservedProperty property) {
        EventSensorPropertyDTO eventSensorPropertyDTO = new EventSensorPropertyDTO();
        eventSensorPropertyDTO.setSensorID(sensor.getSensorID());
        eventSensorPropertyDTO.setSensorName(sensor.getSensorName());
        eventSensorPropertyDTO.setLat(sensor.getLat());
        eventSensorPropertyDTO.setLon(sensor.getLon());
        eventSensorPropertyDTO.setPropertyID(property.getPropertyID());
        eventSensorPropertyDTO.setPropertyName(property.getPropertyName());
        eventSensorPropertyDTO.setUnit(property.getUnit());
        return eventSensorPropertyDTO;
    }
}
