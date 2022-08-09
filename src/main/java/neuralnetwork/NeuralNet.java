package neuralnetwork;

import lombok.Data;
import datareaders.mnistreader.MnistMatrix;
import neuralnetwork.enums.ActivationFunctionType;
import neuralnetwork.enums.ErrorCalculationType;
import org.apache.commons.collections4.CollectionUtils;
import ui.UserInterface;
import utils.MatrixUtils;
import utils.NeuralNetworkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class NeuralNet {
    private double[] inputs;
    private double[][] outputs;
    private double[][][] weights;//warstwa,neuron,wagi_połączeń
    private double[][] bias;
    private double[][] errors;
    private double[][][] gradients; //warstwa,neuron,gradient wagi

    private int weightCount;

    private int[] structure; //wejscia -> x ukrytych -> wyjscia
    private UserInterface userInterface;
    private ErrorCalculationType errorCalculationType;
    private ActivationFunctionType activationFunctionType;
    private ActivationFunctionType outputActivationFunctionType;

    private boolean isUsedInCnn;
    private Integer threadNumber;

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
        inputs = new double[structure[0]];
        weights = new double[structure.length - 1][][];
        outputs = new double[structure.length][];
        errors = new double[structure.length - 1][];
        gradients = new double[structure.length - 1][][];
        bias = new double[structure.length - 1][];
        for (int i = 0; i < structure.length; i++) {
            double[] a = new double[structure[i]];
            outputs[i] = a;
            if (i > 0) {
                double[] b = new double[structure[i]];
                double[] c = new double[structure[i]];
                bias[i - 1] = b;
                errors[i - 1] = c;
            }
        }
        for (int i = 0; i < structure.length - 1; i++) {
            double[][] b = new double[structure[i + 1]][];
            double[][] c = new double[structure[i + 1]][];
            weights[i] = b;
            gradients[i] = c;
            for (int j = 0; j < structure[i + 1]; j++) {
                double[] e = new double[structure[i]];
                c[j] = e;
                double[] d = new double[structure[i]];
                b[j] = d;
            }
        }

        initInputs();
        initRandomWeights();
        initOutputs();
        initGradients();
        initBias();
        initErrors();
        initThreads();
    }

    private void initThreads() {
        if (Objects.isNull(threadNumber)) {
            threadNumber = 1;
        }
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
        double sum = 0f;
        List<CalculationThread> threads = new ArrayList<>();
        if (y == 0) {
            for (AtomicInteger i = new AtomicInteger(0); i.get() < threadNumber; i.getAndIncrement()) {
                /*if((i.get()+1)*( weights[y].length/threadNumber) > weights[y].length){

                }*/
                CalculationThread calculationThread = new CalculationThread(i.get() * (weights[y].length / threadNumber), ((i.get() + 1) * (weights[y].length / threadNumber)), weights, outputs, bias, y, outputActivationFunctionType);
                threads.add(calculationThread);
                //calculationThread.run();
            }
            if (CollectionUtils.isNotEmpty(threads)) {
                threads.forEach(t -> {
                    t.run();
                });
            }
        } else {
            for (int i = 0; i < weights[y].length; i++) {
                for (int j = 0; j < weights[y][i].length; j++) {
                    sum += weights[y][i][j] * outputs[y][j];
                    if (j == weights[y][i].length - 1) {
                        if (y == outputs.length - 2 && ActivationFunctionType.SOFTMAX == outputActivationFunctionType) {
                            outputs[y + 1][i] = NeuralNetworkUtils.softmax(((sum / weights[y].length) + bias[y][i]), outputs[y + 1]);
                        }
                        outputs[y + 1][i] = selectActivationFunction((sum / weights[y].length) + bias[y][i]);
                    }
                }
                sum = 0f;
            }
        }
    }

    private double selectActivationFunction(double sum) {
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

    private double selectOutputActivationFunction(double sum) {
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

    public double[] getOutputsFromLastLayer() {
        return outputs[outputs.length - 1];
    }

    private double calculateCostFunctionDerivative(double answer, double value) {
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

    private double calculateActivationFunctionDerivative(double value) {
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

    private double calculateOutputActivationFunctionDerivative(double value) {
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

    public void backPropagation(double learningRate, double[] answers) {
        calculateErrors(answers);
        calculateGradient(learningRate);
        adjustWeights();
        adjustBiases();
    }

    private void adjustBiases() {
        for (int i = 0; i < bias.length; i++) {
            for (int j = 0; j < bias[i].length; j++) {
                bias[i][j] -= errors[i][j];
            }
        }
    }

    private void calculateErrors(double[] answers) {
        for (int i = errors.length - 1; i >= 0; i--) {
            if (i == errors.length - 1) {
                for (int j = 0; j < errors[i].length; j++) {
                    if (ActivationFunctionType.SOFTMAX == outputActivationFunctionType && ErrorCalculationType.CROSS_ENTROPY == errorCalculationType) {
                        errors[i][j] = getOutputsFromLastLayer()[j] - answers[j];
                    } else {
                        errors[i][j] = calculateCostFunctionDerivative(answers[j], getOutputsFromLastLayer()[j]) * calculateOutputActivationFunctionDerivative(getOutputsFromLastLayer()[j]);
                    }
                }
            } else {
                for (int j = 0; j < errors[i].length; j++) {
                    double temp = 0;
                    for (int k = 0; k < weights[i + 1].length; k++) {
                        temp += weights[i + 1][k][j] * errors[i + 1][k];
                    }
                    errors[i][j] = temp * calculateActivationFunctionDerivative(outputs[i + 1][j]);
                }
            }
        }
    }

    private void calculateGradient(double learningRate) {
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


    public void train(int epochSize, double learningRate, double[][] trainingData, double[][] answers) {
        for (int i = 0; i < epochSize; i++) {
            int random = ThreadLocalRandom.current().nextInt(0, 4);
            setInputValues(trainingData[random]);
            feedForward();
            backPropagation(learningRate, answers[random]);
        }
    }

    public void train(double learningRate, MnistMatrix[] trainingData) {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < trainingData.length; j++) {
                int rand = ThreadLocalRandom.current().nextInt(0, trainingData.length);
                setInputValues(MatrixUtils.multiArrayToSimpleArray(trainingData[rand].getData()));
                feedForward();
                backPropagation(learningRate, trainingData[rand].getAnswer());
            }
        }
    }


    public void initInputs() {
        Arrays.fill(inputs, 0f);
    }

    public void initRandomWeights() {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                for (int k = 0; k < weights[i][j].length; k++) {
                    weights[i][j][k] = ThreadLocalRandom.current().nextDouble();
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

    public void setInputValues(double[] inputs) {
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

    public void setWeights(double[][][] weights) {
        this.weights = weights;
    }

    public void setBias(double[][] bias) {
        this.bias = bias;
    }

    public int[] getStructure() {
        return structure;
    }

    public double[][] getOutputs() {
        return outputs;
    }

    /*public double[] getOutputValues() {
        return softmax(getOutputsFromLastLayer());
    }*/

    public void setOutputs(double[][] outputs) {
        this.outputs = outputs;
    }

    public double[][][] getWeights() {
        return weights;
    }

    public int printValuesFromOutputWithPercentages() {
        return MatrixUtils.findMaxIndexFromArray(outputs[outputs.length - 1]);
    }

}
