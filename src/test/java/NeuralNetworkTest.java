import neuralnetwork.NeuralNet;
import neuralnetwork.enums.ActivationFunctionType;
import neuralnetwork.enums.ErrorCalculationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class NeuralNetworkTest {
    private NeuralNet neuralNet;

    @Test
    public void feedForwardTest(){
        long start1 = System.nanoTime();
        createNeuralNet2R();
        float[] inputs = new float[]{0,1};
        float[][][] weights = new float[][][]{ {{-8.759594f,-10.508968f},{11.164163f,8.439672f}}, {{-9.27132f},{11.488307f}}};
        neuralNet.setWeights(weights);
        neuralNet.setInputValues(inputs);
        neuralNet.feedForward();
        long end1 = System.nanoTime();
        System.out.println(end1-start1);
        for (int i = 0; i < neuralNet.getStructure()[neuralNet.getStructure().length-1]; i++) {
            System.out.println(neuralNet.getOutputs()[neuralNet.getStructure().length-1][i]);
        }
    }

    @Test
    public void backPropagationTest(){
        createNeuralNet2R();
        float[] inputs = new float[]{1,1};
        float[] answers = new float[]{0};
        float[][][] weights = new float[][][]{ {{0.5f,1f},{0.3f,1f}}, {{0.8f},{0.7f}}};
        neuralNet.setWeights(weights);
        neuralNet.setInputValues(inputs);
        neuralNet.feedForward();
        System.out.println("predictions: ");
        for (int i = 0; i < neuralNet.getOutputs()[neuralNet.getOutputs().length-1].length; i++) {
            System.out.print(neuralNet.getOutputs()[neuralNet.getOutputs().length-1][i]+"       ");
        }
        System.out.println();
        System.out.println("______________________");
        //neuralNet.backPropagation(answers);
    }

    @Test
    public void calculateGradientTest(){
        createNeuralNet2R();
        float[] inputs = new float[]{1,1};
        float[] answers = new float[]{0};
        float[][][] weights = new float[][][]{ {{0.5f,1f},{0.3f,1f}}, {{0.8f},{0.7f}}};
        neuralNet.setWeights(weights);
        neuralNet.setInputValues(inputs);
        neuralNet.feedForward();
        neuralNet.backPropagation(0.1f, answers);
        //neuralNet.calculateGradient(0.1f);
    }

    @Test
    public void trainTest(){
        createNeuralNet2R();
        float[][] data = new float[][]{{1,1},{0,1},{1,0},{0,0}};
        float[][] answers = new float[][]{{0},{1},{1},{0}};
        //float[][][] weights = new float[][][]{ {{0.5f,1f},{0.3f,1f}}, {{0.8f},{0.7f}}};
        //neuralNetRefactor.setWeights(weights);
        neuralNet.train(50000, 0.1f, data, "test", answers, false);

        neuralNet.setInputValues(data[0]);
        neuralNet.feedForward();
        System.out.println("wynik to: "+ neuralNet.getOutputs()[neuralNet.getOutputs().length-1][0]);
        System.out.println("_______________________________");

        neuralNet.setInputValues(data[1]);
        neuralNet.feedForward();
        System.out.println("wynik to: "+ neuralNet.getOutputs()[neuralNet.getOutputs().length-1][0]);
        System.out.println("_______________________________");

        neuralNet.setInputValues(data[2]);
        neuralNet.feedForward();
        System.out.println("wynik to: "+ neuralNet.getOutputs()[neuralNet.getOutputs().length-1][0]);
        System.out.println("_______________________________");

        neuralNet.setInputValues(data[3]);
        neuralNet.feedForward();
        System.out.println("wynik to: "+ neuralNet.getOutputs()[neuralNet.getOutputs().length-1][0]);
        System.out.println("_______________________________");
        System.out.println("wagi to: "+ Arrays.deepToString(neuralNet.getWeights()));
    }

    @Test
    public void classificationGuessTest(){
        createClassificationNeuralNet();
        float[][] trainingData = new float[][]{};
        float[][] answers = new float[][]{};
        neuralNet.train(50000,0.1f,trainingData,"training",answers,true);
    }

    private void createNeuralNet2R(){
        int[] structure = new int[]{2,10,1};

        //float[] inputs = new float[]{0.10f, 0.20f, 0.30f, 0.40f};
        neuralNet = new NeuralNet(structure);
        //neuralNet.setInputValues(inputs);
    }

    private void createClassificationNeuralNet(){
        int[] structure = new int[]{144,16,16,16,4};
        neuralNet = new NeuralNet(structure);
        neuralNet.setOutputActivationFunctionType(ActivationFunctionType.SOFTMAX);
        neuralNet.setErrorCalculationType(ErrorCalculationType.CROSS_ENTROPY);

    }
}
