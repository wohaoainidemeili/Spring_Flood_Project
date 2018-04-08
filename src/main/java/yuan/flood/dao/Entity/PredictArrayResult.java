package yuan.flood.dao.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PredictArrayResult implements Serializable {
    private Long id;
    /**
     * 训练数据
     */
    private List<List<Double>> trainDataMatrix = new ArrayList<>();
    /**
     * 训练目标
     */
    private List<List<Double>> trainTargetMatrix=new ArrayList<>();;
    /**
     * 用于存储将要预测的输入数据
     */
    private List<List<Double>> predictDataMatrix=new ArrayList<>();;
    /**
     * 预测结果
     */
    private List<List<Double>> predictResultMatrix=new ArrayList<>();;
    /**
     * 时间序列，预测结果的时间序列
     */
    private Long[] timeLonMatrix;

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

    public List<List<Double>> getTrainDataMatrix() {
        return trainDataMatrix;
    }

    public void setTrainDataMatrix(List<List<Double>> trainDataMatrix) {
        this.trainDataMatrix = trainDataMatrix;
    }

    public List<List<Double>> getTrainTargetMatrix() {
        return trainTargetMatrix;
    }

    public void setTrainTargetMatrix(List<List<Double>> trainTargetMatrix) {
        this.trainTargetMatrix = trainTargetMatrix;
    }

    public List<List<Double>> getPredictDataMatrix() {
        return predictDataMatrix;
    }

    public void setPredictDataMatrix(List<List<Double>> predictDataMatrix) {
        this.predictDataMatrix = predictDataMatrix;
    }

    public List<List<Double>> getPredictResultMatrix() {
        return predictResultMatrix;
    }

    public void setPredictResultMatrix(List<List<Double>> predictResultMatrix) {
        this.predictResultMatrix = predictResultMatrix;
    }

    public Long[] getTimeLonMatrix() {
        return timeLonMatrix;
    }

    public void setTimeLonMatrix(Long[] timeLonMatrix) {
        this.timeLonMatrix = timeLonMatrix;
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
