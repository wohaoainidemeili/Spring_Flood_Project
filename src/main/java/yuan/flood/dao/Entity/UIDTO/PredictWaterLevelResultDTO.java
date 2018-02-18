package yuan.flood.dao.Entity.UIDTO;

import yuan.flood.dao.Entity.SubscibeEventParams;

import java.util.Date;

public class PredictWaterLevelResultDTO {
    private Long id;
    private double[][] trainDataMatrix;
    private double[][] trainTargetMatrix;
    private double[] timeLonMatrix;
    private double[] predictResultMatrix;
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
        return trainTargetMatrix;
    }

    public void setTrainTargetMatrix(double[][] trainTargetMatrix) {
        this.trainTargetMatrix = trainTargetMatrix;
    }

    public double[] getTimeLonMatrix() {
        return timeLonMatrix;
    }

    public void setTimeLonMatrix(double[] timeLonMatrix) {
        this.timeLonMatrix = timeLonMatrix;
    }

    public double[] getPredictResultMatrix() {
        return predictResultMatrix;
    }

    public void setPredictResultMatrix(double[] predictResultMatrix) {
        this.predictResultMatrix = predictResultMatrix;
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
