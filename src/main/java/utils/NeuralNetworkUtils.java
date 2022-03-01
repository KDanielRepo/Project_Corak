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
    public static byte[][] convertBinaryImageToInputs(BufferedImage image){
        byte[][] array = new byte[image.getHeight()][image.getWidth()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int clr = image.getRGB(i, j);
                int red =   (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue =   clr & 0x000000ff;
                if(red > 20 || green > 20 || blue > 20){
                    array[j][i] = 1;
                }else{
                    array[j][i] = 0;
                }
            }
        }
        return array;
    }

    public static void exportWeightsToFile(float[][] weights){
        try {
            String exportString = "";
            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[i].length; j++) {
                    exportString = exportString.concat(weights[i][j]+";");
                }
                exportString = exportString.concat("|");
            }
            File file = new File("./trainedWeights"+ ZonedDateTime.now() +".txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(exportString);
            fileWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void importWeightsIntoNeuralNet(NeuralNet neuralNet){
        try {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Texts","txt");
            jFileChooser.setCurrentDirectory(new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources"));
            jFileChooser.setFileFilter(fileNameExtensionFilter);
            int ret = jFileChooser.showOpenDialog(null);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                Scanner scanner = new Scanner(file);
                AtomicInteger indexX = new AtomicInteger(0);
                AtomicInteger indexY = new AtomicInteger(0);
                Arrays.stream(scanner.nextLine().split("\\|")).forEach(line->{
                    Arrays.stream(line.split(";")).forEach(flo->{
                        //neuralNet.getWeights()[indexY.get()][indexX.get()] = Float.parseFloat(flo);
                        indexX.getAndIncrement();
                    });
                    indexX.set(0);
                    indexY.getAndIncrement();
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
