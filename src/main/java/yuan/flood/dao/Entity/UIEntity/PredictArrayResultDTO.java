package yuan.flood.dao.Entity.UIEntity;

import java.util.Date;
import java.util.List;

public class PredictArrayResultDTO {
    private int key;
    private String predictTime;
    private String subject;
    /**
     * highcharts 画图需要的数据字符串
     */
    private String plotRes;
    /**
     * 计算均方根误差 RMSE
     */
    private double predictError;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getPredictTime() {
        return predictTime;
    }

    public void setPredictTime(String predictTime) {
        this.predictTime = predictTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPlotRes() {
        return plotRes;
    }

    public void setPlotRes(String plotRes) {
        this.plotRes = plotRes;
    }

    public double getPredictError() {
        return predictError;
    }

    public void setPredictError(double predictError) {
        this.predictError = predictError;
    }
}
