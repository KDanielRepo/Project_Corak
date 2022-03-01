package neuralnetwork;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvolutionalNeuralNet {//convolution > RELU > Max pooling > neuralNet

    //private int filterNumber;
    private float[][][][] filterValue;//layer,amount in layer, y, x
    private NeuralNet fullyConnectedNLayer;

    public ConvolutionalNeuralNet(){
        filterValue = new float[3][3][3][3];
    }

    public ConvolutionalNeuralNet(int depth, int filterNumber, int filterYSize, int filterXSize, int[] neuralNetStructure){
        filterValue = new float[depth][filterNumber][filterYSize][filterXSize];
        fullyConnectedNLayer = new NeuralNet(neuralNetStructure);
    }

    public float[][][] convolution(byte[][][] byteArray, int layer, int strideSize){//depth,width,lenght of data
        float[][][] convolutionArray = new float[filterValue[layer].length][byteArray[0].length-2][byteArray[0][0].length-2];

        for (int i = 0; i < filterValue[layer].length; i++) {
            for (int j = 0; j < byteArray[0].length-2; j++) {
                for (int k = 0; k < byteArray[0][j].length-2; k++) {
                    convolutionArray[i][j][k] = sumOfFilter(byteArray, layer, i,j,k);
                }
            }
        }
        return convolutionArray;
    }

    public float[][][] convolution(float[][][] byteArray, int layer, int strideSize){//depth,width,lenght of data
        float[][][] convolutionArray = new float[filterValue[layer].length][byteArray[0].length-2][byteArray[0][0].length-2];

        for (int i = 0; i < filterValue[layer].length; i++) {
            for (int j = 0; j < byteArray[0].length-2; j+=strideSize) {
                for (int k = 0; k < byteArray[0][j].length-2; k+=strideSize) {
                    convolutionArray[i][j][k] = sumOfFilter(byteArray, layer, i,j,k);
                }
            }
        }
        return convolutionArray;
    }

    public float sumOfFilter(byte[][][] byteArray, int layer, int filterNumber, int yOffset, int xOffset){
        float sum = 0;
        for (int j = 0; j < filterValue[layer][filterNumber].length; j++) {//y
            for (int k = 0; k < filterValue[layer][filterNumber][j].length; k++) {//x
                for (int i = 0; i < byteArray.length; i++) {
                    sum += filterValue[layer][filterNumber][j][k] * byteArray[i][j+yOffset][k+xOffset];
                }
            }
        }
        return sum;
    }

    public float sumOfFilter(float[][][] byteArray, int layer, int filterNumber, int yOffset, int xOffset){
        float sum = 0;
        for (int j = 0; j < filterValue[layer][filterNumber].length; j++) {//y
            for (int k = 0; k < filterValue[layer][filterNumber][j].length; k++) {//x
                for (int i = 0; i < byteArray.length; i++) {
                    sum += filterValue[layer][filterNumber][j][k] * byteArray[i][j+yOffset][k+xOffset];
                }
            }
        }
        return sum;
    }

    public float[][][] pooling(float[][][] byteArray, int poolingSize){
        float[][][] poolingArray = new float[byteArray.length][byteArray[0].length/poolingSize][byteArray[0][0].length/poolingSize];

        for (int i = 0; i < poolingArray.length; i++) {
            for (int j = 0; j < poolingArray[i].length; j++) {
                for (int k = 0; k < poolingArray[i][j].length; k++) {
                    poolingArray[i][j][k] = maxPool(byteArray[i], j, k, poolingSize);
                }
            }
        }
        return poolingArray;
    }

    private float maxPool(float[][] values, int arrayOffsetY, int arrayOffsetX, int poolingSize){
        float max = Float.NEGATIVE_INFINITY;
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

    /*public float[][] pooling(float[][] values, int poolingSize, int poolingStep){
        float[][] poolingArray = new float[values.length/poolingSize][];

        for (int i = 0; i < poolingArray.length; i++) {
            float[] temp = new float[values[i].length/2];
            poolingArray[i] = temp;
        }

        for (int i = 0; i < poolingArray.length; i++) {
            for (int j = 0; j < poolingArray[i].length; j++) {
                poolingArray[i][j] = maxPool(values, i, j, poolingSize);
            }
        }
        return poolingArray;
    }

    private float maxPool(float[][] values, int arrayOffsetY, int arrayOffsetX, int poolingSize){
        float max = Float.NEGATIVE_INFINITY;
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

    public float reluActivation(float value){
        if(value > 0){
            return value;
        }else{
            return 0f;
        }
    }

}
