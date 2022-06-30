package utils;

import neuralnetwork.NeuralNet;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class NeuralNetworkUtils {
    private static final float LAMBDA = 1.0507f;
    private static final float ALPHA = 1.6732f;
    private static final String WEIGHT_SEPARATOR = ";";
    private static final String NEURON_SEPARATOR = "|";
    private static final String LAYER_SEPARATOR = "/";
    private static final String BIAS_AND_WEIGHT_SEPARATOR = "%";
    private static final String BIAS_LAYER_SEPARATOR = "@";
    private static final String BIAS_WEIGHT_SEPARATOR = "$";

    public static byte[][] convertBinaryImageToInputs(BufferedImage image) {
        byte[][] array = new byte[image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int clr = image.getRGB(i, j);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;
                if (red > 20 || green > 20 || blue > 20) {
                    array[j][i] = 1;
                } else {
                    array[j][i] = 0;
                }
            }
        }
        return array;
    }

    public static float[][][] convertRGBImageToInputs(BufferedImage image) {
        float[][][] array = new float[3][image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int clr = image.getRGB(i, j);
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;
                array[0][i][j] = (float) red / 255;
                array[1][i][j] = (float) green / 255;
                array[2][i][j] = (float) blue / 255;
            }
        }
        return array;
    }

    public static void exportWeightsAndBiasesToFile(float[][][] weights, float[][] biases) {
        try {
            String exportString = "";
            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[i].length; j++) {
                    for (int k = 0; k < weights[i][j].length; k++) {
                        exportString = exportString.concat(weights[i][j][k] + WEIGHT_SEPARATOR);
                    }
                    exportString = exportString.concat(NEURON_SEPARATOR);
                }
                exportString = exportString.concat(LAYER_SEPARATOR);
            }
            exportString = exportString.concat(BIAS_AND_WEIGHT_SEPARATOR);
            for (int i = 0; i < biases.length; i++) {
                for (int j = 0; j < biases[i].length; j++) {
                    exportString = exportString.concat(biases[i][j] + BIAS_WEIGHT_SEPARATOR);
                }
                exportString = exportString.concat(BIAS_LAYER_SEPARATOR);
            }
            File file = new File("trainedWeights.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(exportString);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void importWeightsIntoNeuralNet(NeuralNet neuralNet) {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Texts", "txt");
            jFileChooser.setCurrentDirectory(new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources"));
            jFileChooser.setFileFilter(fileNameExtensionFilter);
            int ret = jFileChooser.showOpenDialog(null);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                Scanner scanner = new Scanner(file);
                AtomicInteger indexLayer = new AtomicInteger(0);
                AtomicInteger indexNeuron = new AtomicInteger(0);
                AtomicInteger indexWeight = new AtomicInteger(0);
                AtomicInteger weightsAndBiasesIndex = new AtomicInteger(0);
                AtomicInteger biasLayerIndex = new AtomicInteger(0);
                AtomicInteger biasIndex = new AtomicInteger(0);
                Arrays.stream(scanner.nextLine().split("%")).forEach(weightsAndBiases->{
                    if (weightsAndBiasesIndex.get() == 0){
                        Arrays.stream(weightsAndBiases.split("/")).forEach(layer->{
                            Arrays.stream(layer.split("\\|")).forEach(neuron->{
                                Arrays.stream(neuron.split(";")).forEach(weight->{
                                    neuralNet.getWeights()[indexLayer.get()][indexNeuron.get()][indexWeight.get()] = Float.parseFloat(weight);
                                    indexWeight.getAndIncrement();
                                });
                                indexWeight.set(0);
                                indexNeuron.getAndIncrement();
                            });
                            indexWeight.set(0);
                            indexNeuron.set(0);
                            indexLayer.getAndIncrement();
                        });
                        weightsAndBiasesIndex.getAndIncrement();
                    }else{
                        Arrays.stream(weightsAndBiases.split("@")).forEach(biasLayer->{
                            Arrays.stream(biasLayer.split("\\$")).forEach(bias->{
                                neuralNet.getBias()[biasLayerIndex.get()][biasIndex.get()] = Float.parseFloat(bias);
                                biasIndex.getAndIncrement();
                            });
                            biasIndex.set(0);
                            biasLayerIndex.getAndIncrement();
                        });
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static float sigmoid(float sum) {
        return (float) (1 / (1 + Math.exp(-sum)));
    }

    public static float sigmoidDerivative(float value) {
        return value * (1 - value);
    }

    public static float relu(float sum) {
        return sum > 0 ? sum : 0;
    }

    public static float reluDerivative(float value) {
        return value > 0 ? 1 : 0;
    }

    public static float derivativeSigmoid(float value) {
        return sigmoid(value) * (1 - sigmoid(value));
    }

    public static float selu(float sum) {
        if (sum <= 0) {
            return ((float) ((ALPHA * Math.exp(sum) - ALPHA) * LAMBDA));
        }
        return (sum * LAMBDA);

    }

    public static float seluDerivativeFromGuess(float value) {
        if (value > 0) {
            return LAMBDA;
        }
        return (value + LAMBDA * ALPHA);
    }

    public static float seluDerivative(float value) {
        if (value > 0) {
            return LAMBDA;
        }
        return (float) (LAMBDA * ALPHA * Math.exp(value));
    }

    public static float basicError(float answer, float value) {
        return answer - value;
    }

    public static float basicErrorDerivative(float answer, float value) {
        return 1;
    }

    public static float meanSquareError(float answer, float value) {
        return (float) Math.pow(answer - value, 2) / 2;
    }

    public static float meanSquareErrorDerivative(float answer, float value) {
        return (value - answer);
    }

    public static float crossEntropyErrorDerivative(float answer, float value) {
        return (float) (answer * Math.log(value));
    }

    public static float softmax(float value, float[] neuronValues) {
        float total = 0f;
        for (int i = 0; i < neuronValues.length; i++) {
            total+=Math.exp(neuronValues[i]);
        }
        return (float)(Math.exp(value)/total);
    }

    public static float softmaxDerivative(float[] neuronValues) {
        //float[][] derivatives = new float[neuronValues.length][neuronValues.length];
        float derivative = 0f;
        for (int i = 0; i < neuronValues.length; i++) {
            for (int j = 0; j < neuronValues.length; j++) {
                if(i == j){
                    derivative += neuronValues[i] * (1 - neuronValues[i]);
                }else{
                    derivative += -neuronValues[i] * neuronValues[j];
                }
            }
        }
        return derivative;
    }
}
