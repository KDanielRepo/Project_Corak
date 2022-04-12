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

    public static void exportWeightsToFile(float[][] weights) {
        try {
            String exportString = "";
            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[i].length; j++) {
                    exportString = exportString.concat(weights[i][j] + ";");
                }
                exportString = exportString.concat("|");
            }
            File file = new File("./trainedWeights" + ZonedDateTime.now() + ".txt");
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
                AtomicInteger indexX = new AtomicInteger(0);
                AtomicInteger indexY = new AtomicInteger(0);
                Arrays.stream(scanner.nextLine().split("\\|")).forEach(line -> {
                    Arrays.stream(line.split(";")).forEach(flo -> {
                        //neuralNet.getWeights()[indexY.get()][indexX.get()] = Float.parseFloat(flo);
                        indexX.getAndIncrement();
                    });
                    indexX.set(0);
                    indexY.getAndIncrement();
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

    public static float meanSquareErrorDerivative(float[] answer, float[] value) {
        float derivative = 0f;
        for (int i = 0; i < answer.length; i++) {
            derivative += (answer[i] - value[i]);
        }
        return derivative;
    }

    public static float crossEntropyErrorDerivative(float[] answer, float[] value) {
        float derivative = 0f;
        for (int i = 0; i < answer.length; i++) {
            derivative += answer[i] * Math.log(value[i]);
        }
        return derivative;
    }

    public static float[] softmax(float[] neuronValues) {
        double[] temp = new double[neuronValues.length];
        float[] outputs = new float[neuronValues.length];
        for (int i = 0; i < neuronValues.length; i++) {
            temp[i] = neuronValues[i];
        }
        double total = Arrays.stream(temp).map(Math::exp).sum();
        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = (float) (Math.exp(neuronValues[i]) / total);
        }
        return outputs;
    }

    public static float[][] softmaxDerivative(float[] neuronValues) {
        float[][] derivatives = new float[neuronValues.length][neuronValues.length];

        for (int i = 0; i < derivatives.length; i++) {
            for (int j = 0; j < derivatives[i].length; j++) {
                if(i == j){
                    derivatives[i][j] = neuronValues[i] * (1 - neuronValues[i]);
                }else{
                    derivatives[i][j] = -neuronValues[i] * neuronValues[j];
                }
            }
        }
        return derivatives;
    }
}
