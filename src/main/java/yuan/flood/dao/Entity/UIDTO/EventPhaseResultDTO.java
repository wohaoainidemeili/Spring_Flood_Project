package yuan.flood.dao.Entity.UIDTO;

import yuan.flood.dao.Entity.StatisticFloodResult;
import yuan.flood.dao.Entity.UIEntity.AlertFloodResultDTO;
import yuan.flood.dao.Entity.UIEntity.PredictArrayResultDTO;
import yuan.flood.dao.Entity.UIEntity.StatisticFloodResultDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 向UI层输出洪涝各个阶段的信息内容
 */
public class EventPhaseResultDTO {
    List<AlertFloodResultDTO> alert;
    List<PredictArrayResultDTO> predict;
    List<StatisticFloodResultDTO> statistic;

    public List<AlertFloodResultDTO> getAlert() {
        return alert;
    }

    public void setAlert(List<AlertFloodResultDTO> alert) {
        this.alert = alert;
    }

    public List<PredictArrayResultDTO> getPredict() {
        return predict;
    }

    public void setPredict(List<PredictArrayResultDTO> predict) {
        this.predict = predict;
    }

    public List<StatisticFloodResultDTO> getStatistic() {
        return statistic;
    }

    public void setStatistic(List<StatisticFloodResultDTO> statistic) {
        this.statistic = statistic;
    }
}
