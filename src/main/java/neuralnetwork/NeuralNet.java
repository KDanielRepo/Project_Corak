package neuralnetwork;

import lombok.Data;
import mnistreader.MnistMatrix;
import neuralnetwork.enums.ActivationFunctionType;
import neuralnetwork.enums.ErrorCalculationType;
import ui.UserInterface;
import utils.MatrixUtils;
import utils.NeuralNetworkUtils;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class NeuralNet {
    private float[] inputs;
    private float[][] outputs;
    private float[][][] weights;//warstwa,neuron,wagi_połączeń
    private float[][] bias;
    private float[][] errors;
    private float[][][] gradients; //warstwa,neuron,gradient wagi

    private int weightCount;

    private int[] structure; //wejscia -> x ukrytych -> wyjscia
    private UserInterface userInterface;
    private ErrorCalculationType errorCalculationType;
    private ActivationFunctionType activationFunctionType;
    private ActivationFunctionType outputActivationFunctionType;

    private boolean isUsedInCnn;

    public NeuralNet() {
        createNN();
        weightCount = 0;
    }

    public NeuralNet(int[] structure) {
        this.structure = structure;
        errorCalculationType = ErrorCalculationType.MEAN_SQUARE;
        activationFunctionType = ActivationFunctionType.SIGMOID;
        outputActivationFunctionType = ActivationFunctionType.SIGMOID;
        createNN();
    }

    public NeuralNet(byte[] inputs, boolean isUsedInCnn) {

    }

    public void createNN() {
        inputs = new float[structure[0]];
        weights = new float[structure.length - 1][][];
        outputs = new float[structure.length][];
        errors = new float[structure.length - 1][];
        gradients = new float[structure.length - 1][][];
        bias = new float[structure.length - 1][];
        for (int i = 0; i < structure.length; i++) {
            float[] a = new float[structure[i]];
            outputs[i] = a;
            if (i > 0) {
                float[] b = new float[structure[i]];
                float[] c = new float[structure[i]];
                bias[i - 1] = b;
                errors[i - 1] = c;
            }
        }
        for (int i = 0; i < structure.length - 1; i++) {
            float[][] b = new float[structure[i + 1]][];
            float[][] c = new float[structure[i + 1]][];
            weights[i] = b;
            gradients[i] = c;
            for (int j = 0; j < structure[i + 1]; j++) {
                float[] e = new float[structure[i]];
                c[j] = e;
                float[] d = new float[structure[i]];
                b[j] = d;
            }
        }

        initInputs();
        initRandomWeights();
        initOutputs();
        initGradients();
        initBias();
        initErrors();
    }

    public void feedForward() {
        for (int i = 0; i < inputs.length; i++) {
            outputs[0][i] = inputs[i];
        }
        for (int i = 0; i < structure.length - 1; i++) {
            weightedSum(i);
        }
    }

    public void weightedSum(int y) {
        float sum = 0f;
        for (int i = 0; i < weights[y].length; i++) {
            for (int j = 0; j < weights[y][i].length; j++) {
                sum += weights[y][i][j] * outputs[y][j];
                if (j == weights[y][i].length - 1) {
                    if(y == structure.length-1 && ActivationFunctionType.SOFTMAX == outputActivationFunctionType){
                        outputs[y + 1][i] = NeuralNetworkUtils.softmax((sum / weights[y].length) + bias[y][i], outputs[y]);
                    }
                    outputs[y + 1][i] = selectActivationFunction((sum / weights[y].length) + bias[y][i]);
                }
            }
            sum = 0f;
        }
    }

    private float selectActivationFunction(float sum) {
        switch (activationFunctionType) {
            case SIGMOID:
                return NeuralNetworkUtils.sigmoid(sum);
            case RELU:
                return NeuralNetworkUtils.relu(sum);
            case SELU:
                return NeuralNetworkUtils.selu(sum);
            case SOFTMAX:
                return 0;
            default:
                throw new IllegalStateException("Unexpected value: " + activationFunctionType);
        }
    }

    private float selectOutputActivationFunction(float sum) {
        switch (activationFunctionType) {
            case SIGMOID:
                return NeuralNetworkUtils.sigmoid(sum);
            case RELU:
                return NeuralNetworkUtils.relu(sum);
            case SELU:
                return NeuralNetworkUtils.selu(sum);
            case SOFTMAX:
                return 0;
            default:
                throw new IllegalStateException("Unexpected value: " + activationFunctionType);
        }
    }

    public float[] getOutputsFromLastLayer() {
        return outputs[outputs.length - 1];
    }

    private float calculateCostFunctionDerivative(float answer, float value) {
        switch (errorCalculationType) {
            case BASIC:
                return 0;//NeuralNetworkUtils.basicErrorDerivative(answer, value);
            case MEAN_SQUARE:
                return NeuralNetworkUtils.meanSquareErrorDerivative(answer, value);
            case CROSS_ENTROPY:
                return NeuralNetworkUtils.crossEntropyErrorDerivative(answer, value);
            default:
                throw new IllegalStateException("Unexpected value: " + errorCalculationType);
        }
    }

    private float calculateActivationFunctionDerivative(float value) {
        switch (activationFunctionType) {
            case SIGMOID:
                return NeuralNetworkUtils.sigmoidDerivative(value);
            case RELU:
                return NeuralNetworkUtils.reluDerivative(value);
            case SELU:
                return NeuralNetworkUtils.seluDerivative(value); //TODO dodać sprawdzenie czy ostatnia warstwa
            default:
                throw new IllegalStateException("Unexpected value: " + activationFunctionType);
        }
    }

    private float calculateOutputActivationFunctionDerivative(float value) {
        switch (outputActivationFunctionType) {
            case SIGMOID:
                return NeuralNetworkUtils.sigmoidDerivative(value);
            case RELU:
                return NeuralNetworkUtils.reluDerivative(value);
            case SELU:
                return NeuralNetworkUtils.seluDerivative(value); //TODO dodać sprawdzenie czy ostatnia warstwa
            default:
                throw new IllegalStateException("Unexpected value: " + outputActivationFunctionType);
        }
    }

    public void backPropagation(float learningRate, float[] answers) {
        calculateErrors(answers);
        calculateGradient(learningRate);
        adjustWeights();
        adjustBiases();
    }

    private void adjustBiases(){
        for (int i = 0; i < bias.length; i++) {
            for (int j = 0; j < bias[i].length; j++) {
                bias[i][j] -= errors[i][j];
            }
        }
    }

    private void calculateErrors(float[] answers) {
        for (int i = errors.length - 1; i >= 0; i--) {
            if (i == errors.length - 1) {
                for (int j = 0; j < errors[i].length; j++) {
                    if(ActivationFunctionType.SOFTMAX == outputActivationFunctionType && ErrorCalculationType.CROSS_ENTROPY == errorCalculationType){
                        errors[i][j] = getOutputsFromLastLayer()[j] - answers[j];
                    }else{
                        errors[i][j] = calculateCostFunctionDerivative(answers[j], getOutputsFromLastLayer()[j]) * calculateOutputActivationFunctionDerivative(getOutputsFromLastLayer()[j]);
                    }
                }
            } else {
                for (int j = 0; j < errors[i].length; j++) {
                    float temp = 0;
                    for (int k = 0; k < weights[i + 1].length; k++) {
                        temp += weights[i + 1][k][j] * errors[i + 1][k];
                    }
                    errors[i][j] = temp * calculateActivationFunctionDerivative(outputs[i + 1][j]);
                }
            }
        }
    }

    private void calculateGradient(float learningRate) {
        for (int i = gradients.length - 1; i >= 0; i--) {
            for (int j = 0; j < gradients[i].length; j++) {
                for (int k = 0; k < gradients[i][j].length; k++) {
                    gradients[i][j][k] = errors[i][j] * outputs[i][k] * learningRate;
                }
            }
        }
    }

    private void adjustWeights() {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] -= gradients[i][j][k];
                }
            }
        }
    }


    public void train(int epochSize, float learningRate, float[][] trainingData, float[][] answers) {
        for (int i = 0; i < epochSize; i++) {
            int random = ThreadLocalRandom.current().nextInt(0, 4);
            setInputValues(trainingData[random]);
            feedForward();
            backPropagation(learningRate, answers[random]);
        }
    }

    public void train(float learningRate, MnistMatrix[] trainingData) {
        for (int i = 0; i < trainingData.length; i++) {
            setInputValues(MatrixUtils.multiArrayToSimpleArray(trainingData[i].getData()));
            feedForward();
            backPropagation(learningRate, trainingData[i].getAnswer());
        }
    }


    public void initInputs() {
        Arrays.fill(inputs, 0f);
    }

    public void initRandomWeights() {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] = ThreadLocalRandom.current().nextFloat();
                }
            }
        }
    }

    public void initOutputs() {
        for (int i = 0; i < outputs.length; i++) {
            Arrays.fill(outputs[i], 0f);
        }
    }

    public void initErrors() {
        for (int i = 0; i < errors.length; i++) {
            Arrays.fill(errors[i], 0f);
        }
    }

    public void initBias() {
        for (int i = 0; i < bias.length; i++) {
            Arrays.fill(bias[i], 1f);
        }
    }

    public void initGradients() {
        for (int i = 0; i < gradients.length; i++) {
            for (int j = 0; j < gradients[i].length; j++) {
                for (int k = 0; k < gradients[i][j].length; k++) {
                    gradients[i][j][k] = 0f;
                }
            }
        }
    }

    public void setInputValues(float[] inputs) {
        for (int i = 0; i < inputs.length; i++) {
            this.inputs[i] = inputs[i];
        }
    }

    public int getWeightCount() {
        if (weightCount == 0) {
            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[i].length; j++) {
                    weightCount++;
                }
            }
        }
        return weightCount;
    }

    public void setWeights(float[][][] weights) {
        this.weights = weights;
    }

    public void setBias(float[][] bias) {
        this.bias = bias;
    }

    public int[] getStructure() {
        return structure;
    }

    public float[][] getOutputs() {
        return outputs;
    }

    /*public float[] getOutputValues() {
        return softmax(getOutputsFromLastLayer());
    }*/

    public void setOutputs(float[][] outputs) {
        this.outputs = outputs;
    }

    public float[][][] getWeights() {
        return weights;
    }

}
