package yuan.flood.mvc;

import yuan.flood.phase.function.BpDeep;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BpSinTest {
    public static void main(String[] args) throws IOException {
        double[][] p = loadData("D:\\硕士毕业论文\\湖北省水文数据\\BP神经网络测试\\p.csv");
        double[][] t = loadData("D:\\硕士毕业论文\\湖北省水文数据\\BP神经网络测试\\t.csv");
        double[][] ptest = loadData("D:\\硕士毕业论文\\湖北省水文数据\\BP神经网络测试\\ptest.csv");
        double[][] ttest = loadData("D:\\硕士毕业论文\\湖北省水文数据\\BP神经网络测试\\ttest.csv");

        double[] outMaxP = new double[p.length];
        double[] outMinP = new double[p.length];
        double[] outMaxT = new double[t.length];
        double[] outMinT = new double[t.length];

        double[][] pn = getNormalizationMatrix(p, outMaxP, outMinP);
        double[][] tn = getNormalizationMatrix(t, outMaxT, outMinT);


        BpDeep neuro = new BpDeep(new int[]{p.length,7,10,t.length}, 0.01, 0.8);
        for(int times=0;times<50000;times++){
            for (int l=0;l<p[0].length;l++) {
                double[] input=new double[p.length];
                double[] expectedOutput = new double[p.length];
                for (int h=0;h<p.length;h++) {
                    input[h] = pn[h][l];
                }
                for (int h=0;h<t.length;h++) {
                    expectedOutput[h] = tn[h][l];
                }
                neuro.train(input, expectedOutput);
            }
            //System.out.println("predict after training : "+neuro.predict(input));
        }
        //测试数据按照原有原则归一化
        double[][] p2n = getTramNormalizationMatrix(ptest, outMaxP, outMinP);
        double[][] result = new double[tn.length][p2n[0].length];
        for(int l=0;l<ptest[0].length;l++) {
            double[] inputTest = new double[ptest.length];
            for (int h=0;h<ptest.length;h++) {
                inputTest[h] = p2n[h][l];
            }
            double[] res = neuro.computeOut(inputTest);
            result[0][l] = res[0];
        }
        //将归一化数据解算返回

      double[][] resultT= getReverseNormalizationMatrix(result, outMaxT, outMinT);

       for (int i=0;i<resultT.length;i++) {
           for (int j=0;j<resultT[i].length;j++) {
               System.out.println(resultT[i][j] + "," + ttest[i][j]);
           }
       }

        }

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

    public static double[][] loadData(String filePath) throws IOException {
        //读取文件，构建训练数据
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        String lineValue;
        int lineNum = 0;
        int colNum = 0;
        List<String> values = new ArrayList<>();
        while ((lineValue=streamReader.readLine())!=null){
            values.add(lineValue);
        }
        String value= values.get(0);
        String[] eles = value.split(",");
        double[][] p = new double[values.size()][eles.length];
        for (int i=0;i<values.size();i++) {
            String[] elesValue = values.get(i).split(",");
            for (int j=0;j<elesValue.length;j++) {
                p[i][j] = Double.valueOf(elesValue[j]);
            }
        }
        return p;
    }
}
