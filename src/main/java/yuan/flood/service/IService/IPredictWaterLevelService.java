package yuan.flood.service.IService;

import yuan.flood.dao.Entity.PredictWaterLevelResult;

public interface IPredictWaterLevelService {
    /**
     * 根据ses注册的ID来获取预测的最新数据
     * @param sesID
     * @return
     */
    PredictWaterLevelResult getLastPredictWaterLevelResultBySESID(String sesID);

}
