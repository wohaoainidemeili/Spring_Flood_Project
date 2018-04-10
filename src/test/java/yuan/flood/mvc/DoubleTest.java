package yuan.flood.mvc;

import yuan.flood.dao.Entity.PredictWaterLevelResult;
import yuan.flood.phase.function.Normalization;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DoubleTest {
    public static void main(String[] args) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String test = simpleDateFormat.format(new Date());
        System.out.println(test);

        for (int i=0;i<3;i++) {
            double[][] testDouble = new double[2][2];
            testDouble[0][0] = 1.0;
            testDouble[1][0] = 1.2;
            testDouble[0][1] = 0.1;
            testDouble[1][1] = 1.3;


            double[] outmax = new double[testDouble.length];
            double[] outmin = new double[testDouble.length];

             double[] ces = new double[1];
            double[][] myce = tes().getTrainDataMatrix();
            double[][] ceshi= Normalization.getNormalizationMatrix(myce,outmax,outmin);

            int x=0;
        }
    }
    public static PredictWaterLevelResult tes() {
        double[][] test= new double[2][2];
        test[0][0] = 1.0;
        test[1][0] = 1.2;
        test[0][1] = 0.1;
        test[1][1] = 1.3;
        PredictWaterLevelResult predictWaterLevelResult = new PredictWaterLevelResult();
        predictWaterLevelResult.setTrainTargetMatrix(test);
        predictWaterLevelResult.setTrainDataMatrix(test);
        return predictWaterLevelResult;
    }
}
