package neuralnetwork;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvolutionalNeuralNet {//convolution > Max pooling > RELU > neuralNet

    //private int filterNumber;
    private float[][][] filterValue;

    public ConvolutionalNeuralNet(){
        filterValue = new float[3][3][3];
    }

    public ConvolutionalNeuralNet(int filterXSize, int filterYSize, int filterNumber){
        filterValue = new float[filterXSize][filterYSize][filterNumber];
    }

    public float[][] convolution(byte[][] byteArray, int filterNumber, int strideSize){//mnozenie wartosci filtrow * wartosci z obrazu -> suma tych wynikow to 1 wartosc
        float[][] convolutionArray = new float[byteArray.length-2][];

        for (int i = 0; i < convolutionArray.length; i++) {
            float[] temp = new float[byteArray[i].length-2];
            convolutionArray[i] = temp;
        }

        for (int i = 0; i < convolutionArray.length; i++) {
            for (int j = 0; j < convolutionArray[i].length; j++) {
                convolutionArray[i][j] = sumOfFilter(byteArray, filterNumber, i, j);
            }
        }
        return convolutionArray;
    }

    public float sumOfFilter(byte[][] byteArray, int filterNumber, int arrayOffsetY, int arrayOffsetX){
        float sum = 0;
        //System.out.println();
        for (int i = 0; i < filterValue[filterNumber].length; i++) {
            for (int j = 0; j < filterValue[filterNumber][i].length; j++) {
                //System.out.print(byteArray[i+arrayOffsetY][j+arrayOffsetX] * filterValue[filterNumber][i][j]+" ");
                sum += byteArray[i+arrayOffsetY][j+arrayOffsetX] * filterValue[filterNumber][i][j];
            }
            //System.out.println();
        }
        return sum;
    }

    /*private float[][] pooling(float[][] values, int poolingSize, int poolingStep){

    }*/

    private float reluActivation(float value){
        if(value > 0){
            return value;
        }else{
            return 0f;
        }
    }

}
