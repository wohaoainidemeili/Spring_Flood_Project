package yuan.flood.service.IService;

import yuan.flood.dao.Entity.PredictArrayResult;
import yuan.flood.dao.Entity.PredictWaterLevelResult;

import java.util.List;

public interface IPredictWaterLevelService {
    void savePredictWaterLevelResult(PredictArrayResult predictWaterLevelResult);

    PredictWaterLevelResult getPredictWaterLevelResultByTest(String hql);
    /**
     * 根据ses注册的ID来获取预测的最新数据
     * @param sesID
     * @return
     */
    List<PredictArrayResult> getLastPredictWaterLevelResultBySESID(String sesID);

}
