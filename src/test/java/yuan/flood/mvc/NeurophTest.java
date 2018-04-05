package yuan.flood.mvc;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import yuan.flood.service.function.TestListener;

import java.util.Arrays;

public class NeurophTest implements LearningEventListener {
    public static void main(String[] args) {
       new NeurophTest().learnXor();
    }
    public void learnXor()
    {
        MultiLayerPerceptron neuralNetwork = new MultiLayerPerceptron(TransferFunctionType.TANH,2,3,1);
        BackPropagation backPropagation = new BackPropagation();
        backPropagation.setLearningRate(0.01);
        backPropagation.setMaxIterations(50000);
        backPropagation.setMaxError(0.00001);

        DataSet trainDataSet = new DataSet(2, 1);
        trainDataSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
        trainDataSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
        trainDataSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
        trainDataSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{1}));
        neuralNetwork.setLearningRule(backPropagation);

        LearningRule learningRule = neuralNetwork.getLearningRule();

        learningRule.addListener(new TestListener());
        System.out.println("XOR训练神经网络");
        neuralNetwork.learn(trainDataSet);


        neuralNetwork.setInput(1,1);
        neuralNetwork.calculate();
        double[] networkOutput= neuralNetwork.getOutput();
        System.out.println(Arrays.toString(networkOutput));

    }

    @Override
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        if (event.getEventType() != LearningEvent.Type.LEARNING_STOPPED) {
            System.out.println(bp.getCurrentIteration() + ".iterotion:" + bp.getTotalNetworkError());
        }
    }
}
