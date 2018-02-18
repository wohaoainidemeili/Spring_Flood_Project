package yuan.flood.dao.Entity;

import com.google.common.base.Strings;
import yuan.flood.until.FeatureUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 在数据库中存储预测的结果与真实水位数据，矩阵以逗号分隔为一维矩阵
 */
public class PredictWaterLevelResult implements Serializable{
    private Long id;
    /**
     * 训练数据
     */
    private double[][] trainDataMatrix;
    /**
     * 训练目标
     */
    private double[][] trainTargetMatrix;
    /**
     * 用于存储将要预测的输入数据
     */
    private double[][] predictDataMatrix;
    /**
     * 预测结果
     */
    private double[][] predictResultMatrix;
    /**
     * 时间序列，预测结果的时间序列
     */
    private double[] timeLonMatrix;
    private String trainTargetMatrixStr;
    private String timeLonMatrixStr;
    private String predictResultMatrixStr;
    private Date predictTime;
    private SubscibeEventParams subscibeEventParams;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double[][] getTrainDataMatrix() {
        return trainDataMatrix;
    }

    public void setTrainDataMatrix(double[][] trainDataMatrix) {
        this.trainDataMatrix = trainDataMatrix;
    }

    public double[][] getTrainTargetMatrix() {
        if (this.trainTargetMatrix==null) {
          List<String> list=FeatureUtil.getListFromString(this.getTrainTargetMatrixStr());
            double[][] trainData = new double[1][list.size()];
          for (int i=0;i<list.size();i++) {
              trainData[0][i] = Double.valueOf(list.get(i));
          }
            this.trainDataMatrix = trainData;
            return trainDataMatrix;
        }else {
            return trainTargetMatrix;
        }

    }

    public void setTrainTargetMatrix(double[][] trainTargetMatrix) {
        this.trainTargetMatrix = trainTargetMatrix;
        //将对应的matrix转为String
        if (this.trainTargetMatrix!=null) {
            List<String> list = new ArrayList<>();
            for (int i=0;i<trainTargetMatrix[0].length;i++) {
                list.add(String.valueOf(trainTargetMatrix[0][i]));
            }
           this.setTrainTargetMatrixStr(FeatureUtil.getStringFromList(list));
        }
    }

    public double[] getTimeLonMatrix() {
        if (this.timeLonMatrix==null){
            List<String> list=FeatureUtil.getListFromString(this.getTimeLonMatrixStr());
            double[] timeData = new double[list.size()];
            for (int i=0;i<list.size();i++) {
                timeData[i] = Double.valueOf(list.get(i));
            }
            this.timeLonMatrix = timeData;
            return timeLonMatrix;
        }else {
            return timeLonMatrix;
        }

    }

    public void setTimeLonMatrix(double[] timeLonMatrix) {
        this.timeLonMatrix = timeLonMatrix;
        //将对应的matrix转为String
        if (this.timeLonMatrixStr!=null) {
            List<String> list = new ArrayList<>();
            for (int i=0;i<timeLonMatrix.length;i++) {
                list.add(String.valueOf(timeLonMatrix[i]));
            }
            this.setTimeLonMatrixStr(FeatureUtil.getStringFromList(list));
        }
    }


    public double[][] getPredictResultMatrix() {
        if (this.predictResultMatrix==null) {
            List<String> list=FeatureUtil.getListFromString(this.getPredictResultMatrixStr());
            double[][] predictResult = new double[1][list.size()];
            for (int i=0;i<list.size();i++) {
                predictResult[0][i] = Double.valueOf(list.get(i));
            }
            this.predictResultMatrix = predictResult;
            return predictResultMatrix;
        }else {
            return predictResultMatrix;
        }
    }

    public void setPredictResultMatrix(double[][] predictResultMatrix) {
        this.predictResultMatrix = predictResultMatrix;

        //将对应的matrix转为String
        if (this.predictResultMatrix!=null) {
            List<String> list = new ArrayList<>();
            for (int i=0;i<predictResultMatrix[0].length;i++) {
                list.add(String.valueOf(predictResultMatrix[0][i]));
            }
            this.setPredictResultMatrixStr(FeatureUtil.getStringFromList(list));
        }
    }

    public double[][] getPredictDataMatrix() {
        return predictDataMatrix;
    }

    public void setPredictDataMatrix(double[][] predictDataMatrix) {
        this.predictDataMatrix = predictDataMatrix;
    }

    public String getTrainTargetMatrixStr() {
        return trainTargetMatrixStr;
    }

    public void setTrainTargetMatrixStr(String trainTargetMatrixStr) {
        this.trainTargetMatrixStr = trainTargetMatrixStr;
    }

    public String getTimeLonMatrixStr() {
        return timeLonMatrixStr;
    }

    public void setTimeLonMatrixStr(String timeLonMatrixStr) {
        this.timeLonMatrixStr = timeLonMatrixStr;
    }

    public String getPredictResultMatrixStr() {
        return predictResultMatrixStr;
    }

    public void setPredictResultMatrixStr(String predictResultMatrixStr) {
        this.predictResultMatrixStr = predictResultMatrixStr;
    }

    public Date getPredictTime() {
        return predictTime;
    }

    public void setPredictTime(Date predictTime) {
        this.predictTime = predictTime;
    }

    public SubscibeEventParams getSubscibeEventParams() {
        return subscibeEventParams;
    }

    public void setSubscibeEventParams(SubscibeEventParams subscibeEventParams) {
        this.subscibeEventParams = subscibeEventParams;
    }
}
