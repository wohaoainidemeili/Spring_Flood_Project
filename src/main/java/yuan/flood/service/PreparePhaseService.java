package yuan.flood.service;

import org.apache.xmlbeans.XmlException;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuan.flood.dao.Entity.*;
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

@Service
public class PreparePhaseService implements IPreparePhaseService {
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
    public void executeService(String sesID,Date date) {
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
        Map<SensorObsProperty, List<DataTimeSeries>> propertyDataMap = new HashMap<>();
        //当前时间
//        Date now = new Date();
        Date now = date;
        //当前时间前七分钟时间
        Date before = new Date(now.getTime() - 1000 * 10 * 60);
        for (int i=0;i<listFromString.size();i++) {
            //获取传感器属性的数据
            SensorObsProperty sensorObsProperty = sensorObsPropertyService.getSensorPropertyByID(Long.valueOf(listFromString.get(i)));
            String observationRequestXML = encode.getGetObservationByTimeXML(sensorObsProperty.getSensorID(), sensorObsProperty.getObservedPropertyID(), before, now);
            String responseXML= methods.sendPost(SOSSESConfig.getSosurl(), observationRequestXML);
            try {
                List<DataTimeSeries> dataTimeSeries= decode.decodeObservation(responseXML);
                //数据长度小于100，消除该数据
                if (dataTimeSeries.size() < 5) continue;
                Collections.sort(dataTimeSeries);

                usefulProperties.add(sensorObsProperty);
                //将时间序列数据，放置到map中
                propertyDataMap.put(sensorObsProperty, dataTimeSeries);
            } catch (XmlException e) {
                e.printStackTrace();
            }
        }
        //对齐传感器数据
        //第一步，计算当前时间点，在其他时间点是否能找到匹配的数据点
        List<DataTimeSeries> targetDataTimeSeries = new ArrayList<>();
        String targetObservationXML = encode.getGetObservationByTimeXML(targetSensorID, targetPropertyID, before, now);
        String targetResponseXML = methods.sendPost(SOSSESConfig.getSosurl(), targetObservationXML);
        try {
         targetDataTimeSeries = decode.decodeObservation(targetResponseXML);
         if (targetDataTimeSeries.size()<5) try {
             throw new Exception("水位数据数量无法达到训练要求");
         } catch (Exception e) {
             e.printStackTrace();
         }
            Collections.sort(targetDataTimeSeries);

        } catch (XmlException e) {
            e.printStackTrace();
        }
        //寻找距离目标时间最近的时间，形成时间序列
        double[][] test = null;

        /***********************
         * 数据对齐
         */
        List<DataTimeSeries> targetData = targetDataTimeSeries;
        Map<SensorObsProperty, List<DataTimeSeries>> data=propertyDataMap;
        Long startFirstTarget = targetData.get(targetData.size() - 1).getTimeLon();
        //计算矩阵的长度
        int rowLen = data.keySet().size();
        int colLen = targetData.size();

        Map<SensorObsProperty, Integer> preDataIndex = new HashMap<>();
        for (Map.Entry entry : data.entrySet()) {
            //寻找出列的最小长度
            List<DataTimeSeries> dataTimeSeries = (List<DataTimeSeries>) entry.getValue();
            Long startFirstOther = dataTimeSeries.get(dataTimeSeries.size() - 1).getTimeLon();
            Long startSecondOther = dataTimeSeries.get(dataTimeSeries.size() - 2).getTimeLon();

            //计算目标数组从倒数第一个还是第二个计数
            Long deltaFirst = Math.abs(startFirstTarget - startFirstOther);
            Long deltaSecond = Math.abs(startFirstTarget - startSecondOther);


            if (deltaFirst<deltaSecond) {
                preDataIndex.put((SensorObsProperty) entry.getKey(), 0);
                colLen = getMinValue(targetData.size(), dataTimeSeries.size());}

            else{
                preDataIndex.put((SensorObsProperty) entry.getKey(), 1);
                colLen = getMinValue(targetData.size(), dataTimeSeries.size() - 1);}

        }
        //形成数据的矩阵
        //预测数据
        double[] outMaxP = new double[rowLen];
        double[] outMinP = new double[rowLen];
        double[] outMaxT = new double[1];
        double[] outMinT = new double[1];

        double[][] trainDataMatrix = new double[rowLen][colLen-2];
        double[][] predictDataMatix = new double[rowLen][colLen];

        int rowCount = 0;
        for (Map.Entry entry : preDataIndex.entrySet()) {
            List<DataTimeSeries> dataTimeSeries = data.get(entry.getKey());
            int endIndex = (int) entry.getValue();
            List<DataTimeSeries> dataTimeSeries1 = dataTimeSeries.subList(dataTimeSeries.size() - endIndex - colLen, dataTimeSeries.size() - endIndex);
            for (int col=0;col<dataTimeSeries1.size()-2;col++) {
                trainDataMatrix[rowCount][col] = dataTimeSeries1.get(col).getDataValue();
            }
            for (int col=0;col<dataTimeSeries1.size();col++) {
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
        for (int i=1;i<targetData.size();i++) {
            sumTimeSpace = sumTimeSpace + targetData.get(i).getTimeLon() - targetData.get(i - 1).getTimeLon();
        }
        avgTimeSpace = sumTimeSpace / (targetData.size() - 1);
        timeMatrix[colLen - 2] = targetData.get(targetData.size() - 1).getTimeLon()+avgTimeSpace;
        timeMatrix[colLen - 1] = targetData.get(targetData.size() - 1).getTimeLon() + avgTimeSpace * 2;
        for (int i=targetData.size()-1;i>=2;i--) {
            timeMatrix[i-2] = targetData.get(i).getTimeLon();
            targetDataMatrix[0][i-2] = targetData.get(i).getDataValue();
        }


        /***
         * 结束
         */
        double[][] ptest = predictDataMatix;


        double[][] pn = Normalization.getNormalizationMatrix(trainDataMatrix, outMaxP, outMinP);
        double[][] tn = Normalization.getNormalizationMatrix(targetDataMatrix, outMaxT, outMinT);


//        BpDeep neuro = new BpDeep(new int[]{p.length,10,10,t.length}, 0.01, 0.8);
//        for(int times=0;times<50000;times++){
//            for (int l=0;l<p[0].length;l++) {
//                double[] input=new double[p.length];
//                double[] expectedOutput = new double[p.length];
//                for (int h=0;h<p.length;h++) {
//                    input[h] = pn[h][l];
//                }
//                for (int h=0;h<t.length;h++) {
//                    expectedOutput[h] = tn[h][l];
//                }
//                neuro.train(input, expectedOutput);
//            }
            //System.out.println("predict after training : "+neuro.predict(input));
//        }

        //运用neuroph工具计算BP神经网络训练
        NeuralNetwork neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,rowLen,7,10,1);
        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setLearningRate(0.001);
        backPropagation.setMaxIterations(50000);
        backPropagation.setMaxError(0.000000000001);

        DataSet trainDataSet = new DataSet(rowLen, 1);
        for (int l=0;l<pn[0].length;l++) {
            double[] input=new double[rowLen];
            double[] expectedOutput = new double[1];
            for (int h=0;h<pn.length;h++) {
                input[h] = pn[h][l];
            }
            for (int h=0;h<tn.length;h++) {
                expectedOutput[h] = tn[h][1];
            }
            trainDataSet.addRow(new DataSetRow(input, expectedOutput));
        }
        neuralNetwork.setLearningRule(backPropagation);
        LearningRule learningRule = neuralNetwork.getLearningRule();

        learningRule.addListener(new TestListener());
        neuralNetwork.learn(trainDataSet);


//        backPropagation.setNeuralNetwork(neuralNetwork);
//        backPropagation.learn(trainDataSet);

        //测试数据按照原有原则归一化
        double[][] p2n = Normalization.getTramNormalizationMatrix(ptest, outMaxP, outMinP);
        double[][] result = new double[tn.length][p2n[0].length];
        for(int l=0;l<ptest[0].length;l++) {
            double[] inputTest = new double[ptest.length];
            for (int h=0;h<ptest.length;h++) {
                inputTest[h] = p2n[h][l];
            }
            neuralNetwork.setInput(inputTest);
            neuralNetwork.calculate();
            double[] res = neuralNetwork.getOutput();
            result[0][l] = res[0];
        }
        //将归一化数据解算返回

        double[][] resultT= Normalization.getReverseNormalizationMatrix(result, outMaxT, outMinT);

//        //设置预测的订阅ID
//        predictWaterLevelResult.setSubscibeEventParams(subscibeEventParams);
//        //将预测结果存储到数据库中
//        predictWaterLevelResultDao.save(predictWaterLevelResult);

        //生成预警消息内容，并将预警消息存储到数据库中
        AlertFloodResult alertFloodResult = new AlertFloodResult();
        alertFloodResult.setTime(date);
        alertFloodResult.setSubscibeEventParams(subscibeEventParams);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String dateStr = simpleDateFormat.format(date);
        try {
            //create message
            StringBuffer message = new StringBuffer();
            message.append("您订阅的事件" + subscibeEventParams.getEventID() + "于" + dateStr + "进入准备阶段。\r\n");
            message.append("该事件的" + subscibeEventParams.getPrepareSensor() + "传感器观测值ֵ");
            message.append("在" + subscibeEventParams.getPrepareDay() + "天");
            message.append(subscibeEventParams.getPrepareHour() + "时");
            message.append(subscibeEventParams.getPrepareMinute() + "分");
            message.append(subscibeEventParams.getDiagnosisSecond() + "秒");
            message.append("内大于" + subscibeEventParams.getPrepareThreshold() + "m");
            message.append("出现次数超过" + subscibeEventParams.getPrepareRepeatTimes() + "次");
//            message.append("该水位站点预测水位未来两天结果为" + predictWaterLevelResult.getPredictResultMatrixStr());
            alertFloodResult.setMessage(message.toString());
            //email发送消息内容
            SendMail.send("wenying3413ying@126.com", "dwytam1314", subscibeEventParams.getEmail(), subscibeEventParams.getEventName() + "事件进入准备阶段", message.toString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //发送完数据，将信息写入
        alertFloodDao.save(alertFloodResult);
    }


    public int getMinValue(int value1, int value2) {
        if (value1 > value2) {
            return value2;
        } else return value1;
    }
}
