package yuan.flood.service;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import yuan.flood.dao.Entity.*;

import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.SensorObsProperty;
import yuan.flood.dao.Entity.UIEntity.SubscribeEventParamsDTO;
import yuan.flood.dao.IDao.IAlertFloodDao;
import yuan.flood.dao.IDao.IPredictWaterLevelResultDao;
import yuan.flood.service.IService.*;
import yuan.flood.service.function.BpDeep;
import yuan.flood.service.function.Normalization;
import yuan.flood.service.function.SendMail;
import yuan.flood.service.function.TestListener;
import yuan.flood.sos.DataTimeSeries;
import yuan.flood.sos.Decode;
import yuan.flood.sos.Encode;
import yuan.flood.until.FeatureUtil;
import yuan.flood.until.HttpMethods;
import yuan.flood.until.SOSSESConfig;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("preparePhaseServiceT")
public class PreparePhaseServiceT implements IPreparePhaseServiceT {
    @Autowired
    IEventService eventService;
    @Autowired
    ISensorService sensorService;
    @Autowired
    ISensorObsPropertyService sensorObsPropertyService;
    @Autowired
    private Encode encode;
    @Autowired
    private HttpMethods methods;
    @Autowired
    private Decode decode;
    @Autowired
    private IPredictWaterLevelResultDao predictWaterLevelResultDao;
    @Autowired
    private IAlertFloodDao alertFloodDao;

    @Override
    public void executeService(String sesID, Date date) {
        //得到参数
        SubscibeEventParams subscibeEventParams = eventService.getRegisteredEventParamsBySesid(sesID);

        //加载当前需要预测的属性，也就是，当前的response属性
        String targetSensorID = subscibeEventParams.getResponseSensor();
        String targetPropertyID = subscibeEventParams.getResponseObservation();

        //计算消息内容，发送消息
        //获取预测需要的属性数据
        String featureString = subscibeEventParams.getFeatureString();
        Map<String, String> featureFromString = FeatureUtil.getFeatureFromString(featureString);
        String ids = featureFromString.get(SubscribeEventParamsDTO.SENSOR_PROPERTY_IDS);
        List<String> listFromString = FeatureUtil.getListFromString(ids);
        //将ID转为LONG,并查询获取所有的属性，当前可用的传感器属性
        List<SensorObsProperty> usefulProperties = new ArrayList<>();
        Map<SensorObsProperty, List<DataTimeSeries>> propertyDataMap = new LinkedHashMap<>();
        //当前时间
//        Date now = new Date();
        Date now = new Date();
        //当前时间前七分钟时间
        Integer trainLength = SOSSESConfig.getPredictrainlength();
        Date before = new Date(now.getTime() - 1000 * trainLength * 60);
        List<String> sensorNameList = new ArrayList<>();
        List<String> propertyNameList = new ArrayList<>();
        for (int i = 0; i < listFromString.size(); i++) {
            //获取传感器属性的数据
            SensorObsProperty sensorObsProperty = sensorObsPropertyService.getSensorPropertyByID(Long.valueOf(listFromString.get(i)));
//                System.out.println("yuan:"+sensorObsProperty.getSensorName() + ":" + sensorObsProperty.getPropertyName());
            sensorNameList.add(sensorObsProperty.getSensorName());
            propertyNameList.add(sensorObsProperty.getPropertyName());
            String observationRequestXML = encode.getGetObservationByTimeXML(sensorObsProperty.getSensorID(), sensorObsProperty.getObservedPropertyID(), before, now);
            String responseXML = methods.sendPost(SOSSESConfig.getSosurl(), observationRequestXML);
            try {
                List<DataTimeSeries> dataTimeSeries = decode.decodeObservation(responseXML);
                //数据长度小于100，消除该数据
                if (dataTimeSeries.size() < listFromString.size()) continue;
                Collections.sort(dataTimeSeries);

                usefulProperties.add(sensorObsProperty);
                //将时间序列数据，放置到map中
                propertyDataMap.put(sensorObsProperty, dataTimeSeries);
            } catch (XmlException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < sensorNameList.size(); i++) {
            System.out.println(sensorNameList.get(i) + ":" + propertyNameList.get(i));
        }
        //对齐传感器数据
        //第一步，计算当前时间点，在其他时间点是否能找到匹配的数据点
        List<DataTimeSeries> targetDataTimeSeries = new ArrayList<>();
        String targetObservationXML = encode.getGetObservationByTimeXML(targetSensorID, targetPropertyID, before, now);
        String targetResponseXML = methods.sendPost(SOSSESConfig.getSosurl(), targetObservationXML);
        try {
            targetDataTimeSeries = decode.decodeObservation(targetResponseXML);
            if (targetDataTimeSeries.size() < listFromString.size()) try {
                throw new Exception("水位数据数量无法达到训练要求");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Collections.sort(targetDataTimeSeries);

        } catch (XmlException e) {
            e.printStackTrace();
        }
        //寻找距离目标时间最近的时间，形成时间序列
        PredictArrayResult predictArrayResult = getArrayUseFulTime(targetDataTimeSeries, propertyDataMap);
        List<List<Double>> trainDataMatrix = predictArrayResult.getTrainDataMatrix();
        List<List<Double>> targetDataMatrix = predictArrayResult.getTrainTargetMatrix();
        List<List<Double>> predictDataMatrix = predictArrayResult.getPredictDataMatrix();
        int targetRows = targetDataMatrix.size();
        int trainRows = trainDataMatrix.size();
        //预测数据
        List<Double> outMaxP = new ArrayList<>();
        List<Double> outMinP = new ArrayList<>();
        List<Double> outMaxT = new ArrayList<>();
        List<Double> outMinT = new ArrayList<>();

        double[][] pn = getNormalizationMatrix(trainDataMatrix, outMaxP, outMinP);
        double[][] tn = getNormalizationMatrix(targetDataMatrix, outMaxT, outMinT);


        int firstLayerNum = (int)Math.floor(Math.sqrt(trainRows + targetRows));
        int secondLayerNum = firstLayerNum + 3;
        //运用neuroph工具计算BP神经网络训练
        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.TANH, trainRows, firstLayerNum, secondLayerNum, targetRows);
        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setLearningRate(subscibeEventParams.getLearningRate());
        backPropagation.setMaxIterations(subscibeEventParams.getMaxIterations());
        backPropagation.setMaxError(subscibeEventParams.getMaxError());
        DataSet trainDataSet = new DataSet(trainRows, 1);
        for (int l = 0; l < pn[0].length; l++) {
            double[] input = new double[trainRows];
            double[] expectedOutput = new double[targetRows];
            for (int h = 0; h < pn.length; h++) {
                input[h] = pn[h][l];
            }
            for (int h = 0; h < tn.length; h++) {
                expectedOutput[h] = tn[h][l];
            }
            trainDataSet.addRow(new DataSetRow(input, expectedOutput));
        }
        neuralNetwork.setLearningRule(backPropagation);
        LearningRule learningRule = neuralNetwork.getLearningRule();

        learningRule.addListener(new TestListener());
        neuralNetwork.learn(trainDataSet);

        //测试数据按照原有原则归一化
        double[][] p2n = getTramNormalizationMatrix(predictDataMatrix, outMaxP, outMinP);
        double[][] result = new double[tn.length][p2n[0].length];
        for (int i = 0; i < tn.length; i++) {
            for (int j = 0; j < p2n[0].length; j++) {
                result[i][j] = 0;
            }
        }
        List<Double> resultRowArray = new ArrayList<>();
        for (int l = 0; l < p2n[0].length; l++) {
            double[] inputTest = new double[p2n.length];
            for (int h = 0; h < p2n.length; h++) {
                inputTest[h] = p2n[h][l];
            }
            neuralNetwork.setInput(inputTest);
            neuralNetwork.calculate();
            double[] res = neuralNetwork.getOutput();
            result[0][l] = res[0];
            resultRowArray.add(res[0]);
        }
        List<List<Double>> resultMatrix = new ArrayList<>();
        resultMatrix.add(resultRowArray);
        //将归一化数据解算返回
        double[][] resultT = getReverseNormalizationMatrix(resultMatrix, outMaxT, outMinT);
        //设置预测的时间点
        predictArrayResult.setPredictTime(now);

        List<List<Double>> predictResultMatrix = new ArrayList<>();
        for (int i = 0; i < resultT.length; i++) {
            List<Double> predictResultArray = new ArrayList<>();
            for (int j = 0; j < resultT[i].length; j++) {
                predictResultArray.add(resultT[i][j]);
            }
            predictResultMatrix.add(predictResultArray);
        }

        //将数组转换为String形式
        List<String> timeLonStrList = new ArrayList<>();
        Long[] timeLonMatrix = predictArrayResult.getTimeLonMatrix();
        for (int i = 0; i < timeLonMatrix.length; i++) {
            timeLonStrList.add(String.valueOf(timeLonMatrix[i]));
        }
        String timeLonListStr = FeatureUtil.getStringFromList(timeLonStrList);
        predictArrayResult.setTimeLonMatrixStr(timeLonListStr);
        predictArrayResult.setTrainTargetMatrixStr(FeatureUtil.getStringFromListListDouble(targetDataMatrix));
        predictArrayResult.setPredictResultMatrixStr(FeatureUtil.getStringFromListListDouble(predictResultMatrix));

        //设置预测的订阅ID
        predictArrayResult.setSubscibeEventParams(subscibeEventParams);
        predictArrayResult.setSubject("准备阶段");
        //将预测结果存储到数据库中
        predictWaterLevelResultDao.save(predictArrayResult);

        //生成预警消息内容，并将预警消息存储到数据库中
        AlertFloodResult alertFloodResult = new AlertFloodResult();
        alertFloodResult.setTime(now);
        alertFloodResult.setSubscibeEventParams(subscibeEventParams);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dateStr = simpleDateFormat.format(date);
        try {
            //create message
            StringBuffer message = new StringBuffer();
            message.append("您订阅的事件\"" + subscibeEventParams.getUserDefineName() + "\"于" + dateStr + "进入准备阶段。\r\n");
            message.append("该事件的" + subscibeEventParams.getPrepareSensor() + "传感器观测值ֵ");
            message.append("在" + subscibeEventParams.getPrepareDay() + "天");
            message.append(subscibeEventParams.getPrepareHour() + "时");
            message.append(subscibeEventParams.getPrepareMinute() + "分");
            message.append(subscibeEventParams.getPrepareSecond() + "秒");
            message.append("内大于" + subscibeEventParams.getPrepareThreshold() + "m");
            message.append("出现次数达到或超过" + subscibeEventParams.getPrepareRepeatTimes() + "次。\r\n");
            message.append("该水位站点预测水位未来两天结果为" + predictArrayResult.getPredictResultMatrixStr());
            alertFloodResult.setMessage(message.toString());
            alertFloodResult.setSubject(subscibeEventParams.getUserDefineName() + "事件进入准备阶段");
            //email发送消息内容
            SendMail.send("wenying3413ying@126.com", "dwytam1314", subscibeEventParams.getEmail(), subscibeEventParams.getUserDefineName() + "事件进入准备阶段", message.toString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //发送完数据，将信息写入
        alertFloodDao.save(alertFloodResult);
    }

    /**
     * 根据输入目标数据，与辅助预测数据，计算数据对齐后的矩阵，同时需要构建时间序列，
     *
     * @param targetData
     * @param data
     * @return
     */
    public PredictWaterLevelResult getUseFulTime(List<DataTimeSeries> targetData, Map<SensorObsProperty, List<DataTimeSeries>> data) {
        PredictWaterLevelResult predictWaterLevelResult = new PredictWaterLevelResult();
        //找到最近的时间，按照时间排序
        //计算开始和结束
        //获取最小的时间，和最大的时间
        Long startFirstTarget = targetData.get(targetData.size() - 1).getTimeLon();
        //计算矩阵的长度
        int rowLen = data.keySet().size();
        int colLen = targetData.size();

        Map<SensorObsProperty, Integer> preDataIndex = new LinkedHashMap<>();
        for (Map.Entry entry : data.entrySet()) {
            //寻找出列的最小长度
            List<DataTimeSeries> dataTimeSeries = (List<DataTimeSeries>) entry.getValue();
            Long startFirstOther = dataTimeSeries.get(dataTimeSeries.size() - 1).getTimeLon();
            Long startSecondOther = dataTimeSeries.get(dataTimeSeries.size() - 2).getTimeLon();

            //计算目标数组从倒数第一个还是第二个计数
            Long deltaFirst = Math.abs(startFirstTarget - startFirstOther);
            Long deltaSecond = Math.abs(startFirstTarget - startSecondOther);


            if (deltaFirst < deltaSecond) {
                preDataIndex.put((SensorObsProperty) entry.getKey(), 0);
                colLen = getMinValue(targetData.size(), dataTimeSeries.size());
            } else {
                preDataIndex.put((SensorObsProperty) entry.getKey(), 1);
                colLen = getMinValue(targetData.size(), dataTimeSeries.size() - 1);
            }

        }
        //形成数据的矩阵
        double[][] trainDataMatrix = new double[rowLen][colLen - 2];
        double[][] predictDataMatix = new double[rowLen][colLen];
        int rowCount = 0;
        for (Map.Entry entry : preDataIndex.entrySet()) {
            List<DataTimeSeries> dataTimeSeries = data.get(entry.getKey());
            int endIndex = (int) entry.getValue();
            List<DataTimeSeries> dataTimeSeries1 = dataTimeSeries.subList(dataTimeSeries.size() - endIndex - colLen, dataTimeSeries.size() - endIndex);
            for (int col = 0; col < dataTimeSeries1.size() - 2; col++) {
                trainDataMatrix[rowCount][col] = dataTimeSeries1.get(col).getDataValue();
            }
            for (int col = 0; col < dataTimeSeries1.size(); col++) {
                predictDataMatix[rowCount][col] = dataTimeSeries1.get(col).getDataValue();
            }
            rowCount++;
        }
        //根据target数据，形成时间序列（计算两个预测值的时间序列）
        Long[] timeMatrix = new Long[colLen];
        double[][] targetDataMatrix = new double[1][colLen - 2];
        //计算时间平均间隔，根据平均间隔，计算后两个预测结果的时间
        Long avgTimeSpace = 0l;
        Long sumTimeSpace = 0l;
        for (int i = 1; i < targetData.size(); i++) {
            sumTimeSpace = sumTimeSpace + targetData.get(i).getTimeLon() - targetData.get(i - 1).getTimeLon();
        }
        avgTimeSpace = sumTimeSpace / (targetData.size() - 1);
        timeMatrix[colLen - 2] = targetData.get(targetData.size() - 1).getTimeLon() + avgTimeSpace;
        timeMatrix[colLen - 1] = targetData.get(targetData.size() - 1).getTimeLon() + avgTimeSpace * 2;
        for (int i = targetData.size() - 1; i >= 2; i--) {
            timeMatrix[i - 2] = targetData.get(i).getTimeLon();
            targetDataMatrix[0][i - 2] = targetData.get(i).getDataValue();
        }

        //输出
        predictWaterLevelResult.setTrainDataMatrix(trainDataMatrix);
        predictWaterLevelResult.setTrainTargetMatrix(targetDataMatrix);
        predictWaterLevelResult.setTimeLonMatrix(timeMatrix);
        predictWaterLevelResult.setPredictDataMatrix(predictDataMatix);
        return predictWaterLevelResult;
    }

    public PredictArrayResult getArrayUseFulTime(List<DataTimeSeries> targetData, Map<SensorObsProperty, List<DataTimeSeries>> data) {
        PredictArrayResult predictArrayResult = new PredictArrayResult();
        //找到最近的时间，按照时间排序
        //计算开始和结束
        //获取最小的时间，和最大的时间
        Long startFirstTarget = targetData.get(targetData.size() - 1).getTimeLon();
        //计算矩阵的长度
        int rowLen = data.keySet().size();
        int colLen = targetData.size();

        Map<SensorObsProperty, Integer> preDataIndex = new LinkedHashMap<>();
        for (Map.Entry entry : data.entrySet()) {
            //寻找出列的最小长度
            List<DataTimeSeries> dataTimeSeries = (List<DataTimeSeries>) entry.getValue();
            Long startFirstOther = dataTimeSeries.get(dataTimeSeries.size() - 1).getTimeLon();
            Long startSecondOther = dataTimeSeries.get(dataTimeSeries.size() - 2).getTimeLon();

            //计算目标数组从倒数第一个还是第二个计数
            Long deltaFirst = Math.abs(startFirstTarget - startFirstOther);
            Long deltaSecond = Math.abs(startFirstTarget - startSecondOther);


            if (deltaFirst < deltaSecond) {
                preDataIndex.put((SensorObsProperty) entry.getKey(), 0);
                colLen = getMinValue(targetData.size(), dataTimeSeries.size());
            } else {
                preDataIndex.put((SensorObsProperty) entry.getKey(), 1);
                colLen = getMinValue(targetData.size(), dataTimeSeries.size() - 1);
            }

        }
        //形成数据的矩阵 colen-2
        List<List<Double>> trainDataMatrix = new ArrayList<>();
        List<List<Double>> predictDataMatix = new ArrayList<>();

        for (Map.Entry entry : preDataIndex.entrySet()) {
            List<DataTimeSeries> dataTimeSeries = data.get(entry.getKey());
            int endIndex = (int) entry.getValue();
            List<DataTimeSeries> dataTimeSeries1 = dataTimeSeries.subList(dataTimeSeries.size() - endIndex - colLen, dataTimeSeries.size() - endIndex);
            List<Double> trainRowArray = new ArrayList<>();
            for (int col = 0; col < dataTimeSeries1.size() - 2; col++) {
                trainRowArray.add(dataTimeSeries1.get(col).getDataValue());
            }
            trainDataMatrix.add(trainRowArray);
            List<Double> predictRowArray = new ArrayList<>();
            for (int col = 0; col < dataTimeSeries1.size(); col++) {
                predictRowArray.add(dataTimeSeries1.get(col).getDataValue());
            }
            predictDataMatix.add(predictRowArray);
        }
        //根据target数据，形成时间序列（计算两个预测值的时间序列）
        Long[] timeMatrix = new Long[colLen];
        List<List<Double>> targetDataMatrix = new ArrayList<>();
        //计算时间平均间隔，根据平均间隔，计算后两个预测结果的时间
        Long avgTimeSpace = 0l;
        Long sumTimeSpace = 0l;
        for (int i = 1; i < targetData.size(); i++) {
            sumTimeSpace = sumTimeSpace + targetData.get(i).getTimeLon() - targetData.get(i - 1).getTimeLon();
        }
        avgTimeSpace = sumTimeSpace / (targetData.size() - 1);
        timeMatrix[colLen - 2] = targetData.get(targetData.size() - 1).getTimeLon() + avgTimeSpace;
        timeMatrix[colLen - 1] = targetData.get(targetData.size() - 1).getTimeLon() + avgTimeSpace * 2;
        List<Double> targetRowArray = new ArrayList<>();
        for (int i = 2; i < targetData.size(); i++) {
            timeMatrix[i - 2] = targetData.get(i).getTimeLon();
            targetRowArray.add(targetData.get(i).getDataValue());
        }
        targetDataMatrix.add(targetRowArray);
        predictArrayResult.setTrainDataMatrix(trainDataMatrix);
        predictArrayResult.setTrainTargetMatrix(targetDataMatrix);
        predictArrayResult.setPredictDataMatrix(predictDataMatix);
        predictArrayResult.setTimeLonMatrix(timeMatrix);
        return predictArrayResult;
    }

    public int getMinValue(int value1, int value2) {
        if (value1 > value2) {
            return value2;
        } else return value1;
    }

    /**
     * 归一化数据，并得到最大值最小值，以及归一化后的数据归一化到（0,1）
     * 按行归一化，每行归一化0-1
     *
     * @param inputMatrix
     * @param outMaxValue
     * @param outMinValue
     * @return
     */
    public double[][] getNormalizationMatrix(List<List<Double>> inputMatrix, List<Double> outMaxValue, List<Double> outMinValue) {
        if (inputMatrix == null || inputMatrix.size() == 0) return null;
        //找到最大最小值
        for (int i = 0; i < inputMatrix.size(); i++) {
            double minValue = Double.MAX_VALUE;
            double maxValue = -Double.MAX_VALUE;
            for (int j = 0; j < inputMatrix.get(i).size(); j++) {
                Double currentValue = inputMatrix.get(i).get(j);
                if (currentValue > maxValue) {
                    maxValue = currentValue;
                }
                if (currentValue < minValue) {
                    minValue = currentValue;
                }
            }
            outMaxValue.add(maxValue);
            outMinValue.add(minValue);
        }
        //归一化
        double[][] result = new double[inputMatrix.size()][inputMatrix.get(0).size()];

        for (int i = 0; i < result.length; i++) {
            double maxValue = outMaxValue.get(i);
            double minValue = outMinValue.get(i);
            if (minValue == maxValue) {
                for (int j = 0; j < result[i].length; j++) {
                    result[i][j] = 1;
                }
            } else {
                for (int j = 0; j < result[i].length; j++) {
                    result[i][j] = (inputMatrix.get(i).get(j) - minValue) / (maxValue - minValue);
                }
            }
        }
        return result;
    }

    /**
     * 反向归一化，从（0,1）解算得到原始数据
     *
     * @param normalizedMatrix
     * @param maxValueArray    每一行的最大值
     * @param minValueArray    每一行的最小值
     * @return
     */
    public double[][] getReverseNormalizationMatrix(List<List<Double>> normalizedMatrix, List<Double> maxValueArray, List<Double> minValueArray) {
        if (normalizedMatrix == null || normalizedMatrix.size() == 0) return null;

        double[][] result = new double[normalizedMatrix.size()][normalizedMatrix.get(0).size()];

        for (int i = 0; i < normalizedMatrix.size(); i++) {
            double maxValue = maxValueArray.get(i);
            double minValue = minValueArray.get(i);
            if (maxValue == minValue) {
                for (int j = 0; j < normalizedMatrix.get(i).size(); j++) {
                    result[i][j] = minValue;
                }
            } else {
                for (int j = 0; j < normalizedMatrix.get(i).size(); j++) {
                    result[i][j] = normalizedMatrix.get(i).get(j) * (maxValue - minValue) + minValue;
                }
            }
        }

        return result;
    }

    /**
     * 根据原有的归一化规则，归一化数据，数据范围可能不在（0,1）
     *
     * @param inputMatrix
     * @param maxValueArray
     * @param minValueArray
     * @return
     */
    public double[][] getTramNormalizationMatrix(List<List<Double>> inputMatrix, List<Double> maxValueArray, List<Double> minValueArray) {
        if (inputMatrix == null || inputMatrix.size() == 0) return null;
        double[][] result = new double[inputMatrix.size()][inputMatrix.get(0).size()];
        for (int i = 0; i < inputMatrix.size(); i++) {
            double maxValue = maxValueArray.get(i);
            double minValue = minValueArray.get(i);
            if (maxValue == minValue) {
                for (int j = 0; j < inputMatrix.get(i).size(); j++) {
                    result[i][j] = 1;
                }
            } else {
                for (int j = 0; j < inputMatrix.get(i).size(); j++) {
                    result[i][j] = (inputMatrix.get(i).get(j) - minValue) / (maxValue - minValue);
                }
            }
        }
        return result;
    }


}


