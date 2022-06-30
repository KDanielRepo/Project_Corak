import mnistreader.MnistDataReader;
import mnistreader.MnistMatrix;
import neuralnetwork.NeuralNet;
import neuralnetwork.enums.ActivationFunctionType;
import neuralnetwork.enums.ErrorCalculationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.MatrixUtils;
import utils.NeuralNetworkUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(JUnit4.class)
public class NeuralNetworkTest {
    private NeuralNet neuralNet;

    @Test
    public void feedForwardTest(){
        long start1 = System.nanoTime();
        createNeuralNet2R();
        float[] inputs = new float[]{1,1};
        float[][][] weights = new float[][][]{ {{4.11264f,-6.235361f},{-6.546086f,8.805063f}}, {{10.010825f},{11.4009905f}}};
        neuralNet.setWeights(weights);
        neuralNet.setInputValues(inputs);
        neuralNet.feedForward();
        System.out.println(neuralNet.getOutputsFromLastLayer()[0]);
        /*long end1 = System.nanoTime();
        System.out.println(end1-start1);
        for (int i = 0; i < neuralNet.getStructure()[neuralNet.getStructure().length-1]; i++) {
            System.out.println(neuralNet.getOutputs()[neuralNet.getStructure().length-1][i]);
        }*/
    }

    @Test
    public void weightedSumTest(){
        createNeuralNetWeighted();
        float[] inputs = new float[]{3,5,7};
        float[][][] weightsNew = new float[][][]{{{0.5f,0.3f,0.2f},{0.6f,0.7f,0.8f},{0.3f,0.4f,0.5f},{0.2f,0.3f,0.4f}},{{0.5f,0.6f,0.7f,0.8f},{0.5f,0.6f,0.7f,0.8f},{0.5f,0.6f,0.7f,0.8f},{0.5f,0.6f,0.7f,0.8f}},{{0.5f,0.6f,0.7f,0.8f},{0.5f,0.6f,0.7f,0.8f}}};
        float[][][] weightsOld = new float[][][]{{},{},{}};
        neuralNet.setWeights(weightsNew);
        neuralNet.setInputValues(inputs);
        neuralNet.feedForward();
        System.out.println(neuralNet.getOutputsFromLastLayer()[0]);
    }

    @Test
    public void backPropagationTest(){
        createNeuralNet2R();
        float[] inputs = new float[]{1,1};
        float[] answers = new float[]{0};
        float[][][] weights = new float[][][]{ {{-1.1182877f,8.255283f},{-1.1086226f,8.264524f}}, {{13.6292515f},{8.268224f}}};
        float[][] bias = new float[][]{{1.0f, -3.3691354f},{-12.8256035f}};
        //float[][][] weights = new float[][][]{ {{0.5f,1f},{0.3f,1f}}, {{0.8f},{0.7f}}};
        neuralNet.setWeights(weights);
        neuralNet.setBias(bias);
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
        neuralNet.setErrorCalculationType(ErrorCalculationType.MEAN_SQUARE);
        neuralNet.setActivationFunctionType(ActivationFunctionType.SIGMOID);
        float[][] data = new float[][]{{1,1},{0,1},{1,0},{0,0}};
        float[][] answers = new float[][]{{0},{1},{1},{0}};
        //float[][][] weights = new float[][][]{ {{0.5f,1f},{0.3f,1f}}, {{0.8f},{0.7f}}};
        //neuralNetRefactor.setWeights(weights);
        double start1 = System.nanoTime();
        neuralNet.train(50000, 0.1f, data, answers);
        double end1 = System.nanoTime();
        System.out.println("time: "+((end1-start1) / 1000000000));
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
        System.out.println("bias to: " +Arrays.deepToString(neuralNet.getBias()));
    }

    @Test
    public void classificationGuessTest(){
        createClassificationNeuralNet();
        neuralNet.setErrorCalculationType(ErrorCalculationType.MEAN_SQUARE);
        neuralNet.setActivationFunctionType(ActivationFunctionType.SIGMOID);
        float[][] data = new float[][]{{1,1},{0,1},{1,0},{0,0}};
        float[][] answers = new float[][]{{0},{1},{1},{0}};
        //float[][][] weights = new float[][][]{ {{0.5f,1f},{0.3f,1f}}, {{0.8f},{0.7f}}};
        //neuralNetRefactor.setWeights(weights);
        double start1 = System.nanoTime();
        neuralNet.train(50000, 0.1f, data, answers);
        double end1 = System.nanoTime();
        System.out.println("time: "+((end1-start1) / 1000000000));
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
        System.out.println("bias to: " +Arrays.deepToString(neuralNet.getBias()));
    }

    @Test
    public void mnistDatasetTest() throws IOException {
        MnistMatrix[] trainingSet = MnistDataReader.readData("C:\\Users\\Daniel\\Downloads\\train-images.idx3-ubyte", "C:\\Users\\Daniel\\Downloads\\train-labels.idx1-ubyte");
        MnistMatrix[] testSet = MnistDataReader.readData("C:\\Users\\Daniel\\Downloads\\t10k-images.idx3-ubyte", "C:\\Users\\Daniel\\Downloads\\t10k-labels.idx1-ubyte");
        createMnistNeuralNet();
        double start1 = System.nanoTime();
        neuralNet.train(0.2f, trainingSet);
        double end1 = System.nanoTime();
        System.out.println("time: "+((end1-start1) / 1000000000));
        NeuralNetworkUtils.exportWeightsAndBiasesToFile(neuralNet.getWeights(), neuralNet.getBias());
        for (int i = 0; i < 10; i++) {
            int random = ThreadLocalRandom.current().nextInt(0,testSet.length);
            neuralNet.setInputValues(MatrixUtils.multiArrayToSimpleArray(testSet[random].getData()));
            neuralNet.feedForward();
            System.out.println(Arrays.toString(neuralNet.getOutputs()[neuralNet.getOutputs().length-1]));
            System.out.println(testSet[random].getLabel());

        }
    }

    @Test
    public void importWeightsTest() throws IOException {
        MnistMatrix[] testSet = MnistDataReader.readData("C:\\Users\\Daniel\\Downloads\\t10k-images.idx3-ubyte", "C:\\Users\\Daniel\\Downloads\\t10k-labels.idx1-ubyte");
        createMnistNeuralNet();
        NeuralNetworkUtils.importWeightsIntoNeuralNet(neuralNet);
        for (int i = 0; i < 10; i++) {
            int random = ThreadLocalRandom.current().nextInt(0,testSet.length);
            neuralNet.setInputValues(MatrixUtils.multiArrayToSimpleArray(testSet[random].getData()));
            neuralNet.feedForward();
            System.out.println(Arrays.toString(neuralNet.getOutputs()[neuralNet.getOutputs().length-1]));
            System.out.println(testSet[random].getLabel());

        }
    }

    private void createNeuralNet2R(){
        int[] structure = new int[]{2,2,1};

        //float[] inputs = new float[]{0.10f, 0.20f, 0.30f, 0.40f};
        neuralNet = new NeuralNet(structure);
        //neuralNet.setInputValues(inputs);
    }

    private void createNeuralNetWeighted(){
        int[] structure = new int[]{3,4,4,2};

        //float[] inputs = new float[]{0.10f, 0.20f, 0.30f, 0.40f};
        neuralNet = new NeuralNet(structure);
        //neuralNet.setInputValues(inputs);
    }

    private void createClassificationNeuralNet(){
        int[] structure = new int[]{4096,16,16,6};
        neuralNet = new NeuralNet(structure);
        neuralNet.setActivationFunctionType(ActivationFunctionType.SIGMOID);
        neuralNet.setOutputActivationFunctionType(ActivationFunctionType.SOFTMAX);
        neuralNet.setErrorCalculationType(ErrorCalculationType.CROSS_ENTROPY);

    }

    private void createMnistNeuralNet(){
        int[] structure = new int[]{784,32,10};
        //MnistDataReader.readData();
        neuralNet = new NeuralNet(structure);
        neuralNet.setActivationFunctionType(ActivationFunctionType.SIGMOID);
        neuralNet.setOutputActivationFunctionType(ActivationFunctionType.SOFTMAX);
        neuralNet.setErrorCalculationType(ErrorCalculationType.CROSS_ENTROPY);
    }
}
