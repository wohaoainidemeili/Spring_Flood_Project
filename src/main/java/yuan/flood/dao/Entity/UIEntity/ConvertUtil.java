package yuan.flood.dao.Entity.UIEntity;

import yuan.flood.dao.Entity.*;
import yuan.flood.dao.Entity.UIDTO.EventPhaseDTO;
import yuan.flood.dao.Entity.UIDTO.EventSensorPropertyDTO;
import yuan.flood.dao.Entity.UIDTO.SubscribeParamsDTO;
import yuan.flood.until.FeatureUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConvertUtil {
    public static SensorDTO getSensorDTOfromSensor(Sensor sensor) {
        if (sensor == null) return new SensorDTO();
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setSensorID(sensor.getSensorID());
        sensorDTO.setSensorName(sensor.getSensorName());
        sensorDTO.setLat(sensor.getLat());
        sensorDTO.setLon(sensor.getLon());

        if (sensor.getObservedProperties() == null) return sensorDTO;

        Iterator iterator = sensor.getObservedProperties().iterator();
        List<ObservedPropertyDTO> observedPropertyDTOS = new ArrayList<ObservedPropertyDTO>();
        while (iterator.hasNext()) {
            ObservedProperty observedProperty = (ObservedProperty) iterator.next();
            ObservedPropertyDTO observedPropertyDTO = getObservedPropertyDTOfromObservedProperty(observedProperty);
            observedPropertyDTOS.add(observedPropertyDTO);
        }
        sensorDTO.setObservedProperties(observedPropertyDTOS);
        return sensorDTO;
    }

    public static ObservedPropertyDTO getObservedPropertyDTOfromObservedProperty(ObservedProperty observedProperty) {
        if (observedProperty == null) return new ObservedPropertyDTO();
        ObservedPropertyDTO observedPropertyDTO = new ObservedPropertyDTO();
        observedPropertyDTO.setPropertyID(observedProperty.getPropertyID());
        observedPropertyDTO.setPropertyName(observedProperty.getPropertyName());
        observedPropertyDTO.setUnit(observedProperty.getUnit());
        return observedPropertyDTO;
    }

    public static SubscribeEventParamsDTO getSubscribeEventParamsDTOfromSubscibeEventParams(SubscibeEventParams subscibeEventParams) {
        if (subscibeEventParams == null) return new SubscribeEventParamsDTO();
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

        subscribeEventParamsDTO.setMaxError(subscibeEventParams.getMaxError());
        subscribeEventParamsDTO.setMaxIterations(subscibeEventParams.getMaxIterations());
        subscribeEventParamsDTO.setLearningRate(subscibeEventParams.getLearningRate());

        subscribeEventParamsDTO.setUserDefineName(subscibeEventParams.getUserDefineName());
        subscribeEventParamsDTO.setFeatureMap(FeatureUtil.getFeatureFromString(subscibeEventParams.getFeatureString()));
        Map feature = subscribeEventParamsDTO.getFeatureMap();
        //计算sensorIDs并设置
        String sensorIDStrings = (String) feature.get(SubscribeEventParamsDTO.SENSOR_PROPERTY_IDS);
        List<String> listFromString = FeatureUtil.getListFromString(sensorIDStrings);
        subscribeEventParamsDTO.setSensorPropertyIDs(listFromString);

        return subscribeEventParamsDTO;

    }

    public static SubscibeEventParams getSubscibeEventParamsfromSubscribeEventParamsDTO(SubscribeEventParamsDTO subscribeEventParamsDTO) {
        SubscibeEventParams subscibeEventParams = new SubscibeEventParams();
        if (subscribeEventParamsDTO == null) return subscibeEventParams;
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

        subscibeEventParams.setLearningRate(subscribeEventParamsDTO.getLearningRate());
        subscibeEventParams.setMaxIterations(subscribeEventParamsDTO.getMaxIterations());
        subscibeEventParams.setMaxError(subscribeEventParamsDTO.getMaxError());

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

    public static AlertFloodResultDTO getAlertFloodResultDTOFromAlertFloodResult(AlertFloodResult alertFloodResult) {
        AlertFloodResultDTO alertFloodResultDTO = new AlertFloodResultDTO();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String date = simpleDateFormat.format(alertFloodResult.getTime());
        alertFloodResultDTO.setTime(date);
        alertFloodResultDTO.setSubject(alertFloodResult.getSubject());
        alertFloodResultDTO.setMessage(alertFloodResult.getMessage());
        return alertFloodResultDTO;
    }

    public static PredictArrayResultDTO getPredictArrayResultDTOFromPredictArrayResult(PredictArrayResult predictArrayResult) {
        PredictArrayResultDTO predictArrayResultDTO = new PredictArrayResultDTO();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String date= simpleDateFormat.format(predictArrayResult.getPredictTime());
        predictArrayResultDTO.setPredictTime(date);
        predictArrayResultDTO.setSubject(predictArrayResult.getSubject());
        //计算误差，并计算画图的字符串

        //计算误差内容
        String targetDataStr = predictArrayResult.getTrainTargetMatrixStr();
        String predictTargetStr = predictArrayResult.getPredictResultMatrixStr();
        String timeLonStr = predictArrayResult.getTimeLonMatrixStr();

        List<List<Double>> targetDataListList = FeatureUtil.getListListDoubleFromString(targetDataStr);
        List<List<Double>> predictTargetListList = FeatureUtil.getListListDoubleFromString(predictTargetStr);
        //转换为List数组
        List<Double> targetData = targetDataListList.get(0);

        List<Double> predictTarget = predictTargetListList.get(0);
        List<String> timeLon = FeatureUtil.getListFromString(timeLonStr);

        //根据list求误差结果
        double error = 0d;
        for (int i = 0; i < targetData.size(); i++) {
            double delta = Double.valueOf(targetData.get(i)) - Double.valueOf(predictTarget.get(i));
            error = delta * delta + error;
        }
        error = Math.sqrt(error / targetData.size());
        predictArrayResultDTO.setPredictError(error);

        String plotResult;
        //形成highcharts的JSON数据
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        buffer.append("title: {");
        buffer.append("text: '洪涝过程统计模型传感器水位预测结果图'");
        buffer.append("},");
        buffer.append("legend: {");
        buffer.append("layout: 'vertical',");
        buffer.append("floating: true,");
        buffer.append("align: 'left',");
        buffer.append("verticalAlign: 'top',");
        buffer.append(" x: 90,");
        buffer.append("y: 45,");
        buffer.append("},");
        buffer.append("yAxis:{");
        buffer.append(" title: {");
        buffer.append("text: '水位'");
        buffer.append("}, labels: {");
        buffer.append("formatter: function() {");
        buffer.append(" return this.value +'m';");
        buffer.append(" }");
        buffer.append(" }},");
        buffer.append("xAxis: {");
        buffer.append("type: 'datetime',");
        buffer.append("labels: {");
        buffer.append("format: '{value:%y-%m-%d %H:%M}',");
        buffer.append(" align: 'right',");
        buffer.append("rotation: -30,");
        buffer.append("},");
        buffer.append("categories:");
        buffer.append("[");
        if (timeLon == null || timeLon.size() == 0) buffer.append("]},series:[]}");
        else {
            //加入时间数据
            for (int i = 0; i < timeLon.size() - 1; i++) {
                buffer.append(timeLon.get(i) + ",");
            }
            buffer.append(timeLon.get(timeLon.size() - 1) + "]},");
            buffer.append("series:[{name:'真实水位',data:[");
            for (int i = 0; i < targetData.size() - 1; i++) {
                buffer.append(targetData.get(i) + ",");
            }
            buffer.append(targetData.get(targetData.size() - 1) + "],color: '#0000FF'},{name:'预测水位',data:[");
            for (int i = 0; i < predictTarget.size() - 1; i++) {
                buffer.append(predictTarget.get(i) + ",");
            }
            buffer.append(predictTarget.get(predictTarget.size() - 1) + "],color:'#FF0000'}]}");

        }
        plotResult = buffer.toString();
        predictArrayResultDTO.setPlotRes(plotResult);
        //{
//                title: {
//            text: '汉江水位预测结果图',
//        },
//        legend: {
//            layout: 'vertical',
//                    backgroundColor: '#FFFFFF',
//                    floating: true,
//                    align: 'left',
//                    verticalAlign: 'top',
//                    x: 90,
//                    y: 45,
//        },
//        yAxis:{
//            title: {
//                text: '水位'
//            }, labels: {
//                formatter: function() {
//                    return this.value +'m';
//                }
//            }},
//        xAxis: {
//            type: 'datetime',
//                    labels: {
//                format: '{value:%y-%m-%d %H:%M}',
//                        align: 'right',
//                        rotation: -30,
//            },
//            categories:[1523082795070,1523082855070,1523082915070,1523082975071,1523083035071,1523083095070,1523083155071,1523083215071,1523083275072,1523083335073,1523083395072,1523083455074]
//        },
//        series: [{name:'ceshi',
//                data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4],
//            color: '#FF0000'
//        },{name:'ceshi1',
//                data: [112.9, 12.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1],
//            color: '#0000FF'
//        }]
//}
        return predictArrayResultDTO;
    }

    public static StatisticFloodResultDTO getStatisticFloodResultDTOFromStatisticFloodResult(StatisticFloodResult statisticFloodResult) {
        StatisticFloodResultDTO statisticFloodResultDTO = new StatisticFloodResultDTO();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        String startTime = simpleDateFormat.format(statisticFloodResult.getStartTime());
        String endTime = simpleDateFormat.format(statisticFloodResult.getEndTime());
        String diagnosisStartTime = simpleDateFormat.format(statisticFloodResult.getDiagnosisStartTime());
        String prepareStartTime = simpleDateFormat.format(statisticFloodResult.getPrepareStartTime());
        String responseStartTime = simpleDateFormat.format(statisticFloodResult.getResponseStartTime());
        String recoveryStartTime = simpleDateFormat.format(statisticFloodResult.getRecoveryStartTime());
        String recoveryEndTime = simpleDateFormat.format(statisticFloodResult.getRecoveryEndTime());
        String statisticTime = simpleDateFormat.format(statisticFloodResult.getStatisticTime());
        String maxWaterLevelTime = simpleDateFormat.format(statisticFloodResult.getMaxWaterLevelTime());

        statisticFloodResultDTO.setStartTime(startTime);
        statisticFloodResultDTO.setEndTime(endTime);
        statisticFloodResultDTO.setDiagnosisStartTime(diagnosisStartTime);
        statisticFloodResultDTO.setPrepareStartTime(prepareStartTime);
        statisticFloodResultDTO.setResponseStartTime(responseStartTime);
        statisticFloodResultDTO.setRecoveryStartTime(recoveryStartTime);
        statisticFloodResultDTO.setRecoveryEndTime(recoveryEndTime);

        statisticFloodResultDTO.setMaxWaterLevel(statisticFloodResult.getMaxWaterLevel());
        statisticFloodResultDTO.setMaxWaterLevelTime(maxWaterLevelTime);
        statisticFloodResultDTO.setStatisticTime(statisticTime);
        double prepareHour = new BigDecimal(statisticFloodResult.getPrepareDuration() * 1.0 / 3600000).setScale(3,RoundingMode.UP).doubleValue();
        double responseHour = new BigDecimal(statisticFloodResult.getResponseDuration() * 1.0 / 3600000).setScale(3, RoundingMode.UP).doubleValue();
        double recovery = new BigDecimal(statisticFloodResult.getRecoveryDuration() * 1.0 / 3600000).setScale(3, RoundingMode.UP).doubleValue();
        statisticFloodResultDTO.setPrepareDuration(prepareHour);
        statisticFloodResultDTO.setResponseDuration(responseHour);
        statisticFloodResultDTO.setRecoveryDuration(recovery);

        return statisticFloodResultDTO;
    }
}
