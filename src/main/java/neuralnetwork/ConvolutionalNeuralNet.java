package neuralnetwork;

import lombok.Getter;
import lombok.Setter;
import datareaders.mnistreader.MnistMatrix;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class ConvolutionalNeuralNet {//convolution > RELU > Max pooling > neuralNet

    //private int filterNumber;
    private double[][][] inputs;
    private double[][][][] filterValue;//layer,amount in layer, y, x
    private NeuralNet fullyConnectedNLayer;

    public ConvolutionalNeuralNet(){
        filterValue = new double[3][3][3][3];
    }

    public ConvolutionalNeuralNet(int depth, int filterNumber, int filterYSize, int filterXSize, int[] neuralNetStructure){
        filterValue = new double[depth][filterNumber][filterYSize][filterXSize];
        initRandomFilterValues();
        fullyConnectedNLayer = new NeuralNet(neuralNetStructure);
    }

    public void train(double learningRate, MnistMatrix[] trainingData){
        inputs = new double[1][][];
        for (int i = 0; i < trainingData.length; i++) {
            inputs[0] = trainingData[i].getData();
            feedForward(inputs);
            backPropagation();
        }
    }

    private void feedForward(double[][][] inputs){

    }

    private void backPropagation(){

    }

    public double[][][] convolution(byte[][][] byteArray, int layer, int strideSize){//depth,width,lenght of data
        double[][][] convolutionArray = new double[filterValue[layer].length][byteArray[0].length-2][byteArray[0][0].length-2];

        for (int i = 0; i < filterValue[layer].length; i++) {
            for (int j = 0; j < byteArray[0].length-2; j++) {
                for (int k = 0; k < byteArray[0][j].length-2; k++) {
                    convolutionArray[i][j][k] = sumOfFilter(byteArray, layer, i,j,k);
                }
            }
        }
        return convolutionArray;
    }

    public double[][][] convolution(double[][][] byteArray, int layer, int strideSize){//depth,width,lenght of data
        double[][][] convolutionArray = new double[filterValue[layer].length][byteArray[0].length-2][byteArray[0][0].length-2];

        for (int i = 0; i < filterValue[layer].length; i++) {
            for (int j = 0; j < byteArray[0].length-2; j+=strideSize) {
                for (int k = 0; k < byteArray[0][j].length-2; k+=strideSize) {
                    convolutionArray[i][j][k] = sumOfFilter(byteArray, layer, i,j,k);
                }
            }
        }
        return convolutionArray;
    }

    public double sumOfFilter(byte[][][] byteArray, int layer, int filterNumber, int yOffset, int xOffset){
        double sum = 0;
        for (int j = 0; j < filterValue[layer][filterNumber].length; j++) {//y
            for (int k = 0; k < filterValue[layer][filterNumber][j].length; k++) {//x
                for (int i = 0; i < byteArray.length; i++) {
                    sum += filterValue[layer][filterNumber][j][k] * byteArray[i][j+yOffset][k+xOffset];
                }
            }
        }
        return sum;
    }

    public double sumOfFilter(double[][][] byteArray, int layer, int filterNumber, int yOffset, int xOffset){
        double sum = 0;
        for (int j = 0; j < filterValue[layer][filterNumber].length; j++) {//y
            for (int k = 0; k < filterValue[layer][filterNumber][j].length; k++) {//x
                for (int i = 0; i < byteArray.length; i++) {
                    sum += filterValue[layer][filterNumber][j][k] * byteArray[i][j+yOffset][k+xOffset];
                }
            }
        }
        return sum;
    }

    public double[][][] pooling(double[][][] byteArray, int poolingSize){
        double[][][] poolingArray = new double[byteArray.length][byteArray[0].length/poolingSize][byteArray[0][0].length/poolingSize];

        for (int i = 0; i < poolingArray.length; i++) {
            for (int j = 0; j < poolingArray[i].length; j++) {
                for (int k = 0; k < poolingArray[i][j].length; k++) {
                    poolingArray[i][j][k] = maxPool(byteArray[i], j, k, poolingSize);
                }
            }
        }
        return poolingArray;
    }

    private double maxPool(double[][] values, int arrayOffsetY, int arrayOffsetX, int poolingSize){
        double max = Double.NEGATIVE_INFINITY;
        for (int i = arrayOffsetY*poolingSize; i < arrayOffsetY*poolingSize+poolingSize; i++) {
            for (int j = arrayOffsetX*poolingSize; j < arrayOffsetX*poolingSize+poolingSize; j++) {
                //System.out.print(values[i][j]+"     ");
                if(max<values[i][j]){
                    max = values[i][j];
                }
            }
            //System.out.println();
        }
        return max;
    }

    private void initRandomFilterValues(){
        for (int i = 0; i < filterValue.length; i++) {
            for (int j = 0; j < filterValue[i].length; j++) {
                for (int k = 0; k < filterValue[i][j].length; k++) {
                    for (int l = 0; l < filterValue[i][j][k].length; l++) {
                        filterValue[i][j][k][l] = ThreadLocalRandom.current().nextDouble();
                    }
                }
            }
        }
    }

    /*public double[][] pooling(double[][] values, int poolingSize, int poolingStep){
        double[][] poolingArray = new double[values.length/poolingSize][];

        for (int i = 0; i < poolingArray.length; i++) {
            double[] temp = new double[values[i].length/2];
            poolingArray[i] = temp;
        }

        for (int i = 0; i < poolingArray.length; i++) {
            for (int j = 0; j < poolingArray[i].length; j++) {
                poolingArray[i][j] = maxPool(values, i, j, poolingSize);
            }
        }
        return poolingArray;
    }

    private double maxPool(double[][] values, int arrayOffsetY, int arrayOffsetX, int poolingSize){
        double max = double.NEGATIVE_INFINITY;
        for (int i = arrayOffsetY*poolingSize; i < arrayOffsetY*poolingSize+poolingSize; i++) {
            for (int j = arrayOffsetX*poolingSize; j < arrayOffsetX*poolingSize+poolingSize; j++) {
                //System.out.print(values[i][j]+"     ");
                if(max<values[i][j]){
                    max = values[i][j];
                }
            }
            //System.out.println();
        }
        return max;
    }*/
}
