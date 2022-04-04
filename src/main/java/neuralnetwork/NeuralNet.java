package neuralnetwork;

import ui.UserInterface;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class NeuralNet {
    private float[] inputs;
    private float[][] outputs;
    private float[][][] weights;//warstwa,neuron,wagi_połączeń
    private float[][] bias;//dodać do sumy ważonej
    private float[][] errors; //warstwa, blad neuronu

    private int weightCount;

    private static final float LAMBDA = 1.0507f;
    private static final float ALPHA = 1.6732f;
    private int[] structure; //wejscia -> x ukrytych -> wyjscia
    private UserInterface userInterface;

    public NeuralNet() {
        createNN();
        weightCount = 0;
    }

    public NeuralNet(int[] structure) {
        this.structure = structure;
        createNN();
    }

    public void createNN() {
        inputs = new float[structure[0]];
        weights = new float[structure.length - 1][][];
        outputs = new float[structure.length][];
        errors = new float[structure.length - 1][];
        bias = new float[structure.length - 1][];
        for (int i = 0; i < structure.length; i++) {
            float[] a = new float[structure[i]];
            outputs[i] = a;
            if (i > 0) {
                float[] c = new float[structure[i]];
                float[] b = new float[structure[i]];
                bias[i - 1] = c;
                errors[i - 1] = b;
            }
        }
        for (int i = 0; i < structure.length - 1; i++) {
            float[][] b = new float[structure[i]][];
            weights[i] = b;
            for (int j = 0; j < structure[i]; j++) {
                float[] c = new float[structure[i + 1]];
                b[j] = c;
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
        for (int i = 0; i < structure.length-1; i++) {
            weightedSum(i);
        }
        //outputs[outputs.length-1] = softmax(outputs[outputs.length-1]);
    }

    public void classificationFeedForward() {
        for (int i = 0; i < inputs.length; i++) {
            outputs[0][i] = inputs[i];
        }
        for (int i = 0; i < structure.length-1; i++) {
            weightedSum(i);
        }
        outputs[outputs.length-1] = softmax(outputs[outputs.length-1]);
    }

    public void weightedSum(int y) {
        float sum = 0f;
        int innerSize = weights[y][0].length;
        for (int i = 0; i < innerSize; i++) {
            for (int j = 0; j < weights[y].length; j++) {
                sum += weights[y][j][i] * outputs[y][j];
                if(j == weights[y].length-1){
                    outputs[y+1][i] = sigmoid(sum+bias[y][i]);
                }
            }
            sum = 0f;
        }
    }

    public void backPropagation(float[] answers) {
        for (int i = errors.length - 1; i >= 0; i--) {
            if (i == errors.length - 1) {
                for (int j = 0; j < errors[i].length; j++) {
                    errors[i][j] = answers[j] - outputs[i + 1][j];
                }
            } else {
                calculateError(i);
            }
        }
    }

    private void calculateError(int y) {
        float calculatedError = 0;
        for (int i = 0; i < weights[y+1].length; i++) {
            float sumOfWeights = sumWeightsInLayer(y+1);
            for (int j = 0; j < weights[y+1][i].length; j++) {
                calculatedError += (weights[y+1][i][j]/sumOfWeights) * errors[y+1][j];
                if(j == weights[y+1][i].length-1){
                    errors[y][i] = calculatedError;
                }
            }
            calculatedError = 0f;
        }
    }

    private float sumWeightsInLayer(int layer){
        float sum = 0;
        for (int i = 0; i < weights[layer].length; i++) {
            for (int j = 0; j < weights[layer][i].length; j++) {
                sum+= weights[layer][i][j];
            }
        }
        return sum;
    }

    public void calculateGradient(float learningRate) {
        for (int i = weights.length - 1; i >= 0; i--) {
            adjustWeights(learningRate, i);
        }
    }

    private void adjustWeights(float learningRate, int y) {
        for (int i = 0; i < weights[y].length; i++) {
            for (int j = 0; j < weights[y][i].length; j++) {
                float value = learningRate * errors[y][j] * (outputs[y + 1][j] * (1 - outputs[y + 1][j])) * outputs[y][i];
                weights[y][i][j] += value;
                if(j==weights[y][i].length-1){
                    bias[y][j] +=value;
                }

            }
        }
    }

    public float sigmoid(float sum) {
        return (float) (1 / (1 + Math.exp(-sum)));
    }

    public float derivativeSigmoid(float value) {
        return sigmoid(value) * (1 - sigmoid(value));
    }

    public float seluActivation(float sum) {
        if (sum <= 0) {
            return ((float) ((ALPHA * Math.exp(sum) - ALPHA) * LAMBDA));
        }
        return (sum * LAMBDA);

    }

    public float seluDerivativeFromGuess(float value) {
        if (value > 0) {
            return LAMBDA;
        }
        return (value + LAMBDA * ALPHA);
    }

    public float seluDerivative(float value) {
        if (value > 0) {
            return LAMBDA;
        }
        return (float) (LAMBDA * ALPHA * Math.exp(value));
    }

    public float[] softmax(float[] neuronValues) {
        double[] temp = new double[neuronValues.length];
        float[] outputs = new float[getOutputsFromLastLayer().length];
        for (int i = 0; i < neuronValues.length; i++) {
            temp[i] = neuronValues[i];
        }
        double total = Arrays.stream(temp).map(Math::exp).sum();
        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = (float) (Math.exp(getOutputsFromLastLayer()[i]) / total);
        }
        return outputs;
    }

    private float[] getOutputsFromLastLayer(){
        return outputs[outputs.length-1];
    }


    public void train(int epochSize, float learningRate, float[][] trainingData, String trainingDataName, float[][] answers, boolean classification) {
        for (int i = 0; i < epochSize; i++) {
            int random = ThreadLocalRandom.current().nextInt(0, 4);
            setInputValues(trainingData[random]);
            if(classification){
                classificationFeedForward();
            }else{
                feedForward();
            }
            //System.out.print("odpowiedź to: " + outputs[outputs.length - 1][0]);
            //System.out.println(" dla pytania : " + Arrays.toString(trainingData[random]));
            backPropagation(answers[random]);
            calculateGradient(learningRate);
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
            Arrays.fill(errors[i], 0f);
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

    public float[] getOutputValues(){
        return softmax(getOutputsFromLastLayer());
    }

    public void setOutputs(float[][] outputs) {
        this.outputs = outputs;
    }

    public float[][][] getWeights() {
        return weights;
    }
}
