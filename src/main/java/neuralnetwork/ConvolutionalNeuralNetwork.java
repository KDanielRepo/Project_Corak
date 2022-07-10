package neuralnetwork;

import lombok.Getter;
import lombok.Setter;
import neuralnetwork.enums.ConvolutionalLayerType;
import neuralnetwork.enums.PaddingType;
import neuralnetwork.enums.PoolingType;
import neuralnetwork.validators.ConvolutionalNeuralNetValidator;
import utils.MatrixUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class ConvolutionalNeuralNetwork {
    private float[][][] inputs; //channels, y, x
    private float[][][][] filters; //layer, number, y, x
    private float[][][] poolingLayers; //layer, y, x
    private int[][][] poolingCoordinates; //layer, y, x
    private float[][][][] convolutionOutputs; //layer, channels, y, x
    private float[][] bias; //layer, value
    private NeuralNet neuralNet;
    private PoolingType poolingType;
    private PaddingType paddingType;
    private ConvolutionalNeuralNetValidator validator;
    private int stride = 1;
    private int filterSize;
    private int poolingSize;
    private int convolutionLayerNumber;
    private int poolingLayerNumber;
    private int activationLayerNumber;


    public ConvolutionalNeuralNetwork(int numberOfInputChannels, int filterSize, List<ConvolutionalLayerType> operationOrder, int[] neuralNetStructure) {
        convolutionLayerNumber = (int) operationOrder.stream().filter(o->ConvolutionalLayerType.CONVOLUTION == o).count();
        poolingLayerNumber = (int) operationOrder.stream().filter(o->ConvolutionalLayerType.POOLING == o).count();
        activationLayerNumber = (int) operationOrder.stream().filter(o->ConvolutionalLayerType.ACTIVATION == o).count();
        initRandomFilters(convolutionLayerNumber, numberOfInputChannels, filterSize);
        initBias(convolutionLayerNumber);
        initConvolutionOutputs(convolutionLayerNumber, numberOfInputChannels, operationOrder);
        initPoolingCoordinates(poolingLayerNumber);
        neuralNet = new NeuralNet(neuralNetStructure);
    }

    public void train(float learningRate) {
        validator.validate(this);
        setDefaultValuesIfNull();
    }

    private void setDefaultValuesIfNull() {
        if (Objects.isNull(poolingType)) {
            poolingType = PoolingType.MAX;
        }
        if (Objects.isNull(paddingType)) {
            paddingType = PaddingType.VALID;
        }
    }

    private void selectOperationByConvolutionalLayerType(List<ConvolutionalLayerType> operationOrder){
        int currentConvolutionLayer = 0;
        int currentPoolingLayer = 0;
        for (ConvolutionalLayerType operation : operationOrder){
            switch (operation){
                case CONVOLUTION:
                    convolution();
                    currentConvolutionLayer++;
                    break;
                case POOLING:
                    //selectOperationByPoolingType(currentPoolingLayer, );
                    currentPoolingLayer++;
                    break;
                case ACTIVATION:
                    break;
            }
        }
    }

    private void selectOperationByPoolingType(int layer, float[][] matrix){
        switch (poolingType){
            case MAX:
                maxPooling(layer, matrix);
            case AVERAGE:
                averagePooling(matrix);
                break;
            default:
               throw new RuntimeException();
        }
    }

    public void feedForward() {

    }

    private void backPropagation() {

    }

    private void convolution() {
        for (int i = 0; i < filters.length; i++) {//layers
            for (int j = 0; j < filters[i].length; j++) {//channels
                if (i == 0) {
                    for (int k = 0; k < getOutputSizeByPadding(inputs[j].length); k += stride) {
                        for (int l = 0; l < getOutputSizeByPadding(inputs[j][k].length); l += stride) {
                            convolutionOutputs[i][j][k][l] = MatrixUtils.convolution(MatrixUtils.getPartOfArray(inputs[j], l, k, filterSize, filterSize), filters[i][j]);
                        }
                    }
                } else {
                    for (int k = 0; k < getOutputSizeByPadding(convolutionOutputs[i][j].length); k += stride) {
                        for (int l = 0; l < getOutputSizeByPadding(convolutionOutputs[i - 1][j][k].length); l += stride) {
                            convolutionOutputs[i][j][k][l] = MatrixUtils.convolution(MatrixUtils.getPartOfArray(convolutionOutputs[i - 1][j], l, k, filterSize, filterSize), filters[i][j]);
                        }
                    }
                }
            }
        }
    }

    private int getOutputSizeByPadding(int size) {
        switch (paddingType) {
            case SAME: //otaczamy zerami
                return size;
            case VALID: //bez paddingu; rozmiar po konwolucji: gdy macierz wejściowa NxN a filtr MxM to po konwolucji rozmiar N-M+1xN-M+1
                return size - filterSize + 1;
            default:
                throw new IllegalStateException("Value not implemented: " + paddingType);
        }
    }

    private void initRandomFilters(int numberOfLayers, int numberOfInputChannels, int filterSize) {
        this.filterSize = filterSize;
        if (numberOfInputChannels != 1) {
            filters = new float[numberOfLayers][][][];
            for (int i = 0; i < filters.length; i++) {
                if (i == 0) {
                    float[][][] innerLayer = new float[numberOfInputChannels][filterSize][filterSize];
                    filters[i] = innerLayer;
                } else {
                    float[][][] innerLayer = new float[1][filterSize][filterSize];
                    filters[i] = innerLayer;
                }
            }
        } else {
            filters = new float[numberOfLayers][numberOfInputChannels][filterSize][filterSize];
        }
        for (int i = 0; i < filters.length; i++) {
            for (int j = 0; j < filters[i].length; j++) {
                for (int k = 0; k < filters[i][j].length; k++) {
                    for (int l = 0; l < filters[i][j][k].length; l++) {
                        filters[i][j][k][l] = ThreadLocalRandom.current().nextFloat();
                    }
                }
            }
        }
    }

    private void initBias(int numberOfLayers) {
        bias = new float[numberOfLayers][1];
        for (int i = 0; i < bias.length; i++) {
            Arrays.fill(bias[i], 1f);
        }
    }

    private void initConvolutionOutputs(int numberOfLayers, int numberOfInputChannels, List<ConvolutionalLayerType> operationOrder) {
        convolutionOutputs = new float[numberOfLayers][][][];
        if (numberOfInputChannels != 1) {
            for (int i = 0; i < convolutionOutputs.length; i++) {
                for(ConvolutionalLayerType operation : operationOrder){
                    switch (operation){
                        case CONVOLUTION:
                            break;
                        case POOLING:
                            break;
                    }
                }
                if (i == 0) {
                    int outputWidth = getOutputSizeByPadding(inputs[0][0].length);
                    int outputHeight = getOutputSizeByPadding(inputs[0].length);
                    float[][][] channels = new float[numberOfInputChannels][outputHeight][outputWidth];
                    convolutionOutputs[i] = channels;
                } else {
                    int outputWidth = getOutputSizeByPadding(inputs[0][0].length);
                    int outputHeight = getOutputSizeByPadding(inputs[0].length);
                    float[][][] channels = new float[1][outputHeight][outputWidth];
                    convolutionOutputs[i] = channels;
                }
            }
        } else {
            float[][][] channels = new float[1][][];

        }
    }

    private void initPoolingCoordinates(int layers){
        poolingCoordinates = new int[layers][][];
        for (int i = 0; i < poolingCoordinates.length; i++) {
            poolingCoordinates[i] = new int[1][1];
        }
    }

    public float maxPooling(int layer, float[][] matrix){
        float max = Float.NEGATIVE_INFINITY;
        int width = 0;
        int height = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if(max<matrix[i][j]){
                    max = matrix[i][j];
                    height = i;
                    width = j;
                }
            }
        }
        poolingCoordinates[layer][0] = new int[]{height};
        poolingCoordinates[layer][0][0] = width;
        return max;
    }

    public float averagePooling(float[][] matrix){
        float sum = 0f;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                sum += matrix[i][j];
            }
        }
        return sum/MatrixUtils.getArraySize(matrix);
    }


}
