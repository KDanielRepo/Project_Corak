package utils;

import java.util.Arrays;

public class MatrixUtils {

    public static float getMatrixValueFromIndex(float[][] matrix, int index){
        int counter = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if(index==counter){
                    return matrix[i][j];
                }
                counter++;
            }
        }
        return 0;
    }

    public static float[][] transpose(float[][] matrix){
        int m = matrix.length;
        int n = matrix[0].length;
        float[][] trans = new float[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                trans[i][j] = matrix[j][i];
            }
        }
        return trans;
    }

    public static void add(float[][] matrixA, float[][] matrixB){
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[i].length; j++) {
                matrixA[i][j] += matrixB[i][j];
            }
        }
    }

    public static void multiply(float[][] matrixA, float[] matrixB){
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[i].length; j++) {
                matrixA[i][j] = matrixA[i][j] * matrixB[i];
            }
        }
    }

    public static float multiply(float[] matrixA, float scalar){
        float value = 0;
        for (int i = 0; i < matrixA.length; i++) {
            value += matrixA[i] * scalar;
        }
        return value;
    }

    public static float[] multiArrayToSimpleArray(float[][][] matrix){
        int size = matrix.length*matrix[0].length*matrix[0][0].length+1;
        float[] array = new float[size];
        int index = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                for (int k = 0; k < matrix[i][j].length; k++) {
                    array[index] = matrix[i][j][k];
                    index++;
                }
            }
        }
        return array;
    }

    public static float[][][] simpleArrayToMultiArray(float[] matrix, int channels){
        int size = (matrix.length / 2) / channels;
        float[][][] array = new float[channels][size][size];
        int index = 0;
        for (int i = 0; i < channels; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    array[i][j][k] = matrix[index];
                    index++;
                }
            }
        }
        return array;
    }

    public static byte[][] getPartOfArray(byte[][] matrix, int startX, int startY, int endX, int endY){
        byte[][] result = new byte[endY-startY][endX-startX];
        int xIndex = 0;
        int yIndex = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if((i>= startY && i<endY) && (j>=startX && j<endX)){
                    result[yIndex][xIndex] = matrix[i][j];
                    xIndex++;
                    if(xIndex==result[0].length){
                        xIndex = 0;
                        yIndex++;
                    }
                }
            }
        }
        return result;
    }

    public static float[][] getPartOfArray(float[][] matrix, int startX, int startY, int endX, int endY){
        float[][] result = new float[endY-startY][endX-startX];
        int xIndex = 0;
        int yIndex = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if((i>= startY && i<endY) && (j>=startX && j<endX)){
                    result[yIndex][xIndex] = matrix[i][j];
                    xIndex++;
                    if(xIndex==result[0].length){
                        xIndex = 0;
                        yIndex++;
                    }
                }
            }
        }
        return result;
    }

    public static float convolution(float[][] matrix, float[][] secondMatrix){
        float value = 0f;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                value += matrix[i][j] * secondMatrix[i][j];
            }
        }
        return value;
    }

    public static int getArraySize(float[][] matrix){
        return matrix.length * matrix[0].length;
    }




    /*Assuming that given array is symmetrical*/
    public static float[] multiArrayToSimpleArray(float[][] matrix){
        float[] targetMatrix = new float[matrix.length*matrix.length];
        int index = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                targetMatrix[index] = matrix[i][j];
                index++;
            }
        }
        return targetMatrix;
    }
}
