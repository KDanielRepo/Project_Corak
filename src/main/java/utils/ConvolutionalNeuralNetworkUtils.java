package utils;

import java.util.Arrays;

public class ConvolutionalNeuralNetworkUtils {
    public static void maxPoolingDerivative(){

    }

    public static double[][] addPaddingToArray(double[][] array, int paddingSize){
        double[][] temp = new double[array.length+(paddingSize * 2)][array[0].length+(paddingSize * 2)];
        for (int i = 0; i < temp.length; i++) {
            Arrays.fill(temp[i], 0d);
        }
        for (int i = paddingSize; i < temp.length - paddingSize; i++) {
            if (temp[i].length - paddingSize - paddingSize >= 0){
                System.arraycopy(array[i - 1], paddingSize - 1, temp[i], paddingSize, temp[i].length - paddingSize - paddingSize);
            }
        }
        return temp;
    }

}
