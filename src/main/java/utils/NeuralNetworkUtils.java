package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

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
}
