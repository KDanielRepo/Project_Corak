package utils;

import java.util.Arrays;

public class MatrixUtils {

    public static double getMatrixValueFromIndex(double[][] matrix, int index){
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

    public static double[][] transpose(double[][] matrix){
        int m = matrix.length;
        int n = matrix[0].length;
        double[][] trans = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                trans[i][j] = matrix[j][i];
            }
        }
        return trans;
    }

    public static void add(double[][] matrixA, double[][] matrixB){
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[i].length; j++) {
                matrixA[i][j] += matrixB[i][j];
            }
        }
    }

    public static void multiply(double[][] matrixA, double[] matrixB){
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[i].length; j++) {
                matrixA[i][j] = matrixA[i][j] * matrixB[i];
            }
        }
    }

    public static double[] multiply(double[] matrixA, double scalar){
        double[] temp = new double[matrixA.length];
        for (int i = 0; i < matrixA.length; i++) {
            temp[i] = matrixA[i] * scalar;
        }
        return temp;
    }

    public static double[] multiArrayToSimpleArray(double[][][] matrix){
        int size = matrix.length*matrix[0].length*matrix[0][0].length+1;
        double[] array = new double[size];
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

    public static double[][][] simpleArrayToMultiArray(double[] matrix, int channels){
        int size = (matrix.length / 2) / channels;
        double[][][] array = new double[channels][size][size];
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

    public static double[][] getPartOfArray(double[][] matrix, int startX, int startY, int endX, int endY){
        double[][] result = new double[endY-startY][endX-startX];
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

    public static double convolution(double[][] matrix, double[][] secondMatrix){
        double value = 0d;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                value += matrix[i][j] * secondMatrix[i][j];
            }
        }
        return value;
    }

    public static int getArraySize(double[][] matrix){
        return matrix.length * matrix[0].length;
    }




    /*Assuming that given array is symmetrical*/
    public static double[] multiArrayToSimpleArray(double[][] matrix){
        double[] targetMatrix = new double[matrix.length*matrix.length];
        int index = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                targetMatrix[index] = matrix[i][j];
                index++;
            }
        }
        return targetMatrix;
    }

    public static int findMaxIndexFromArray(double[] array){
        int index = 0;
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < array.length; i++) {
            if(array[i] > max){
                max = array[i];
                index = i;
            }
        }
        return index;
    }

    public static double findMaxValueFromArray(double[] array){
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < array.length; i++) {
            if(array[i] > max){
                max = array[i];
            }
        }
        return max;
    }
}
