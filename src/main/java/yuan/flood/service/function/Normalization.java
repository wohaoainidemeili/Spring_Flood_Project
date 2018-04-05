package yuan.flood.service.function;

public class Normalization {
    /**
     * 归一化数据，并得到最大值最小值，以及归一化后的数据归一化到（0,1）
     * 按行归一化，每行归一化0-1
     * @param inputMatrix
     * @param outMaxValue
     * @param outMinValue
     * @return
     */
    public static double[][] getNormalizationMatrix(double[][] inputMatrix,double[] outMaxValue,double[] outMinValue) {
        if (inputMatrix==null||inputMatrix.length==0) return null;
        //找到最大最小值
        for (int i=0;i<inputMatrix.length;i++) {
            double minValue = Double.MAX_VALUE;
            double maxValue = -Double.MAX_VALUE;
            for (int j=0;j<inputMatrix[i].length;j++) {
                if (inputMatrix[i][j] > maxValue) {
                    maxValue = inputMatrix[i][j];
                }
                if (inputMatrix[i][j] < minValue) {
                    minValue = inputMatrix[i][j];
                }
            }
            outMaxValue[i] = maxValue;
            outMinValue[i] = minValue;
        }
        //归一化
        double[][] result = inputMatrix;
        for (int i=0;i<inputMatrix.length;i++) {
            double maxValue = outMaxValue[i];
            double minValue = outMinValue[i];
            if (minValue==maxValue) {
                for (int j=0;j<inputMatrix[i].length;j++) {
                    result[i][j] = 1;
                }
            }else {
                for (int j = 0; j < inputMatrix[i].length; j++) {
                    result[i][j] = (inputMatrix[i][j] - minValue) / (maxValue - minValue);
                }
            }
        }
        return result;
    }

    /**
     * 反向归一化，从（0,1）解算得到原始数据
     * @param normalizedMatrix
     * @param maxValueArray 每一行的最大值
     * @param minValueArray 每一行的最小值
     * @return
     */
    public static double[][] getReverseNormalizationMatrix(double[][] normalizedMatrix, double[] maxValueArray, double[] minValueArray) {
        if (normalizedMatrix==null||normalizedMatrix.length==0) return null;

        double[][] result = normalizedMatrix;

        for (int i=0;i<normalizedMatrix.length;i++) {
            double maxValue = maxValueArray[i];
            double minValue = minValueArray[i];
            if (maxValue==minValue){
                for (int j=0;j<normalizedMatrix[i].length;j++)
                {
                    result[i][j] = minValue;
                }
            }else {
                for (int j = 0; j < normalizedMatrix[i].length; j++) {
                    result[i][j] = normalizedMatrix[i][j] * (maxValue - minValue) + minValue;
                }
            }
        }

        return result;
    }

    /**
     * 根据原有的归一化规则，归一化数据，数据范围可能不在（0,1）
     * @param inputMatrix
     * @param maxValueArray
     * @param minValueArray
     * @return
     */
    public static double[][] getTramNormalizationMatrix(double[][] inputMatrix, double[] maxValueArray, double[] minValueArray) {
        if (inputMatrix==null||inputMatrix.length==0) return null;
        double[][] result = inputMatrix;
        for (int i=0;i<inputMatrix.length;i++) {
            double maxValue = maxValueArray[i];
            double minValue = minValueArray[i];
            if (maxValue==minValue) {
                for (int j = 0; j < inputMatrix[i].length; j++) {
                    result[i][j] = 1;
                }
            }else {
                for (int j = 0; j < inputMatrix[i].length; j++) {
                    result[i][j] = (inputMatrix[i][j] - minValue) / (maxValue - minValue);
                }
            }
        }
        return result;
    }
}
