package utils;

import neuralnetwork.NeuralNet;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class NeuralNetworkUtils {
    private static final double LAMBDA = 1.0507f;
    private static final double ALPHA = 1.6732f;
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
                int red = (clr & 0x00df0000) >> 16;
                int green = (clr & 0x0000df00) >> 8;
                int blue = clr & 0x000000df;
                if (red > 20 || green > 20 || blue > 20) {
                    array[j][i] = 1;
                } else {
                    array[j][i] = 0;
                }
            }
        }
        return array;
    }

    public static double[][][] convertRGBImageToInputs(BufferedImage image) {
        double[][][] array = new double[3][image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int clr = image.getRGB(i, j);
                int red = (clr & 0x00df0000) >> 16;
                int green = (clr & 0x0000df00) >> 8;
                int blue = clr & 0x000000df;
                array[0][i][j] = (double) red / 255;
                array[1][i][j] = (double) green / 255;
                array[2][i][j] = (double) blue / 255;
            }
        }
        return array;
    }

    public static void exportWeightsAndBiasesToFile(double[][][] weights, double[][] biases) {
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
                Arrays.stream(scanner.nextLine().split("%")).forEach(weightsAndBiases -> {
                    if (weightsAndBiasesIndex.get() == 0) {
                        Arrays.stream(weightsAndBiases.split("/")).forEach(layer -> {
                            Arrays.stream(layer.split("\\|")).forEach(neuron -> {
                                Arrays.stream(neuron.split(";")).forEach(weight -> {
                                    neuralNet.getWeights()[indexLayer.get()][indexNeuron.get()][indexWeight.get()] = Double.parseDouble(weight);
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
                    } else {
                        Arrays.stream(weightsAndBiases.split("@")).forEach(biasLayer -> {
                            Arrays.stream(biasLayer.split("\\$")).forEach(bias -> {
                                neuralNet.getBias()[biasLayerIndex.get()][biasIndex.get()] = Double.parseDouble(bias);
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

    public static double sigmoid(double sum) {
        return (double) (1 / (1 + Math.exp(-sum)));
    }

    public static double sigmoidDerivative(double value) {
        return value * (1 - value);
    }

    public static double relu(double sum) {
        return sum > 0 ? sum : 0;
    }

    public static double reluDerivative(double value) {
        return value > 0 ? 1 : 0;
    }

    public static double derivativeSigmoid(double value) {
        return sigmoid(value) * (1 - sigmoid(value));
    }

    public static double selu(double sum) {
        if (sum <= 0) {
            return ((double) ((ALPHA * Math.exp(sum) - ALPHA) * LAMBDA));
        }
        return (sum * LAMBDA);

    }

    public static double seluDerivativeFromGuess(double value) {
        if (value > 0) {
            return LAMBDA;
        }
        return (value + LAMBDA * ALPHA);
    }

    public static double seluDerivative(double value) {
        if (value > 0) {
            return LAMBDA;
        }
        return (double) (LAMBDA * ALPHA * Math.exp(value));
    }

    public static double basicError(double answer, double value) {
        return answer - value;
    }

    public static double basicErrorDerivative(double answer, double value) {
        return 1;
    }

    public static double meanSquareError(double answer, double value) {
        return (double) Math.pow(answer - value, 2) / 2;
    }

    public static double meanSquareErrorDerivative(double answer, double value) {
        return (value - answer);
    }

    public static double crossEntropyErrorDerivative(double answer, double value) {
        return answer * Math.log(value);
    }

    public static double softmax(double value, double[] neuronValues) {
        double total = 0d;
        for (int i = 0; i < neuronValues.length; i++) {
            total += Math.exp(neuronValues[i]);
        }
        return (Math.exp(value) / total);
    }

    public static double softmaxDerivative(double[] neuronValues, int index) {
        //double[][] derivatives = new double[neuronValues.length][neuronValues.length];
        double derivative = 0d;
        for (int j = 0; j < neuronValues.length; j++) {
            if (index == j) {
                derivative = neuronValues[j] * (1 - neuronValues[j]);
            } else {
                derivative = -neuronValues[j] * neuronValues[j];
            }
        }
        return derivative;
    }
}
