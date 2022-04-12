package neuralnetwork;

import lombok.Data;
import lombok.Getter;
import neuralnetwork.enums.ActivationFunctionType;
import neuralnetwork.enums.ErrorCalculationType;
import ui.UserInterface;
import utils.NeuralNetworkUtils;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class NeuralNet {
    private float[] inputs;
    private float[][] outputs;
    private float[][][] weights;//warstwa,neuron,wagi_połączeń, !--WAZNE!!! : ilość wag oznacza ilosc neuronow w warstwie i+1 !!!--!
    private float[][] bias;//dodać do sumy ważonej
    private float[][][] errors; //warstwa,neuron, gradient wagi

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
        errors = new float[structure.length - 1][][];
        bias = new float[structure.length - 1][];
        for (int i = 0; i < structure.length; i++) {
            float[] a = new float[structure[i]];
            outputs[i] = a;
            if (i > 0) {
                float[] b = new float[structure[i]];
                bias[i - 1] = b;
            }
        }
        for (int i = 0; i < structure.length - 1; i++) {
            float[][] b = new float[structure[i]][];
            float[][] c = new float[structure[i]][];
            weights[i] = b;
            errors[i] = c;
            for (int j = 0; j < structure[i]; j++) {
                float[] d = new float[structure[i + 1]];
                float[] e = new float[structure[i + 1]];
                b[j] = d;
                c[j] = e;
            }
        }

        initInputs();
        initRandomWeights();
        initOutputs();
        initErrors();
        initBias();
    }

    public void feedForward() {
        for (int i = 0; i < inputs.length; i++) {
            outputs[0][i] = inputs[i];
        }
        for (int i = 0; i < structure.length - 1; i++) {
            weightedSum(i);
        }
    }

    public void classificationFeedForward() {
        for (int i = 0; i < inputs.length; i++) {
            outputs[0][i] = inputs[i];
        }
        for (int i = 0; i < structure.length - 1; i++) {
            weightedSum(i);
        }
        outputs[outputs.length - 1] = NeuralNetworkUtils.softmax(outputs[outputs.length - 1]);
    }

    public void weightedSum(int y) {
        float sum = 0f;
        int innerSize = weights[y][0].length;
        for (int i = 0; i < innerSize; i++) {
            for (int j = 0; j < weights[y].length; j++) {
                sum += weights[y][j][i] * outputs[y][j];
                if (j == weights[y].length - 1) {
                    outputs[y + 1][i] = selectActivationFunction(sum + bias[y][i]);
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

    /*private float selectErrorCalculationType(float answer, float value) {
        switch (errorCalculationType) {
            case BASIC:
                return NeuralNetworkUtils.basicError(answer, value);
            case MEAN_SQUARE:
                return NeuralNetworkUtils.meanSquareError(answer, value);
            default:
                throw new IllegalStateException("Unexpected value: " + errorCalculationType);
        }
    }*/

    public float[] getOutputsFromLastLayer() {
        return outputs[outputs.length - 1];
    }

    private float calculateCostFunctionDerivative(float[] answer, float[] value) {
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

    private float calculateWeightedSumDerivative(int layer, int neuron) {
        return outputs[layer][neuron];
    }

    public void backPropagation(float learningRate, float[] answers) {
        float activationFunctionDerivative;
        float outputActivationFunctionDerivative = 0f;
        float costFunctionDerivative = calculateCostFunctionDerivative(answers, getOutputsFromLastLayer());
        float weightedSumDerivative;

        for (int i = errors.length - 1; i >= 0; i--) {
            for (int j = 0; j < errors[i].length; j++) {
                weightedSumDerivative = calculateWeightedSumDerivative(i, j);
                for (int k = 0; k < errors[i][j].length; k++) {
                    activationFunctionDerivative = calculateActivationFunctionDerivative(outputs[i+1][k]);
                    outputActivationFunctionDerivative = NeuralNetworkUtils.softmaxDerivative(getOutputsFromLastLayer());
                    float currentActivation = i == errors.length-1 ? outputActivationFunctionDerivative : activationFunctionDerivative;
                    errors[i][j][k] = learningRate * costFunctionDerivative * currentActivation * weightedSumDerivative;
                    weights[i][j][k] += errors[i][j][k];
                    if (k == errors[i][j].length - 1) {
                        bias[i][k] += learningRate*costFunctionDerivative*currentActivation;
                    }
                }
            }
        }
    }


    public void train(int epochSize, float learningRate, float[][] trainingData, String trainingDataName, float[][] answers, boolean classification) {
        for (int i = 0; i < epochSize; i++) {
            int random = ThreadLocalRandom.current().nextInt(0, 4);
            setInputValues(trainingData[random]);
            if (classification) {
                classificationFeedForward();
            } else {
                feedForward();
            }
            backPropagation(learningRate, answers[random]);
            //System.out.print("odpowiedź to: " + outputs[outputs.length - 1][0]);
            //System.out.println(" dla pytania : " + Arrays.toString(trainingData[random]));
            //backPropagation -> calculateCost -> calculateGradients(calculateWeightDerivative, calculateActivationFunctionDerivative, calculateWeightedSumDerivative) -> adjustWeights
            //backPropagation(answers[random]);
            //calculateGradient(learningRate);
            //System.out.println("________________________________________________");
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

    public void initBias() {
        for (int i = 0; i < bias.length; i++) {
            Arrays.fill(bias[i], 1f);
        }
    }

    public void initErrors() {
        for (int i = 0; i < errors.length; i++) {
            for (int j = 0; j < errors[i].length; j++) {
                for (int k = 0; k < errors[i][j].length; k++) {
                    errors[i][j][k] = 0f;
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

    /*private void calculateError(int y) {
        float calculatedError = 0;
        for (int i = 0; i < weights[y + 1].length; i++) {
            float sumOfWeights = sumWeightsInLayer(y + 1);
            for (int j = 0; j < weights[y + 1][i].length; j++) {
                calculatedError += (weights[y + 1][i][j] / sumOfWeights) * errors[y + 1][j];
                if (j == weights[y + 1][i].length - 1) {
                    errors[y][i] = calculatedError;
                }
            }
            calculatedError = 0f;
        }
    }*/

    /*private float sumWeightsInLayer(int layer) {
        float sum = 0;
        for (int i = 0; i < weights[layer].length; i++) {
            for (int j = 0; j < weights[layer][i].length; j++) {
                sum += weights[layer][i][j];
            }
        }
        return sum;
    }*/

    /*public void calculateGradient(float learningRate) {
        for (int i = weights.length - 1; i >= 0; i--) {
            adjustWeights(learningRate, i);
        }
    }*/

    /*private void adjustWeights(float learningRate, int y) {
        for (int i = 0; i < weights[y].length; i++) {
            for (int j = 0; j < weights[y][i].length; j++) {
                float value = learningRate * errors[y][j] * (outputs[y + 1][j] * (1 - outputs[y + 1][j])) * outputs[y][i];
                weights[y][i][j] += value;
                if (j == weights[y][i].length - 1) {
                    bias[y][j] += value;
                }

            }
        }
    }*/
}
