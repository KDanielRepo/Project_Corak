import neuralnetwork.ConvolutionalNeuralNet;
import neuralnetwork.NeuralNet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.MatrixUtils;
import utils.NeuralNetworkUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(JUnit4.class)
public class ConvolutionalNeuralNetTest {

    private float[][][][] filter;
    private float[][][] values;

    @Test
    public void convolutionTest() {
        byte[][][] bytes = new byte[3][5][5];
        createFilter();

        //System.out.println("przed: ");
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < bytes[i].length; j++) {
                for (int k = 0; k < bytes[i][j].length; k++) {
                    bytes[i][j][k] = (byte) (i+j+k);
                }
            }
        }
        long start1 = System.nanoTime();
        ConvolutionalNeuralNet neuralNet = new ConvolutionalNeuralNet();
        neuralNet.setFilterValue(filter);
        float[][][] a = neuralNet.convolution(bytes, 0, 1);
        long end1 = System.nanoTime();
        System.out.println(end1 - start1);
        //System.out.println("______________________________________________");
        //System.out.println("po: ");


        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                for (int k = 0; k < a[i][j].length; k++) {
                    System.out.print(a[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    @Test
    public void maxPoolTest() {
        createValues();
        ConvolutionalNeuralNet neuralNet = new ConvolutionalNeuralNet();
        long start1 = System.nanoTime();
        float[][][] after = neuralNet.pooling(values, 2);
        long end1 = System.nanoTime();
        System.out.println(end1 - start1);

    }

    @Test
    public void finalTest() throws IOException {
        long start1 = System.nanoTime();
        createFinalFilter();
        ConvolutionalNeuralNet convolutionalNeuralNet = new ConvolutionalNeuralNet();
        convolutionalNeuralNet.setFilterValue(filter);

        //NeuralNet neuralNet = new NeuralNet();


        BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\Ithilgore\\Desktop\\rbgTest.png"));

        float[][][][] test = new float[filter.length][][][];
        float[][][][] testPool = new float[filter.length][][][];
        for (int i = 0; i < filter.length; i++) {
            if(i==0){
                byte[][][] a = new byte[1][][];
                a[0] = NeuralNetworkUtils.convertBinaryImageToInputs(bufferedImage);
                test[i] = convolutionalNeuralNet.convolution(a, i,1);

            }else{
                test[i] = convolutionalNeuralNet.convolution(testPool[i-1], i,1);
            }
            for (int j = 0; j < test[i].length; j++) {
                for (int k = 0; k < test[i][j].length; k++) {
                    for (int l = 0; l < test[i][j][k].length; l++) {
                        test[i][j][k][l] = convolutionalNeuralNet.reluActivation(test[i][j][k][l]);
                    }
                }
            }
            testPool[i] = convolutionalNeuralNet.pooling(test[i],2);
        }
        //System.out.println(Arrays.deepToString(testPool[filter.length-1]));
        float[] x = MatrixUtils.multiArrayToSimpleArray(testPool[filter.length-1]);

        int[] structure = new int[]{x.length, 90, 45, 20, 10};
        NeuralNet neuralNet = new NeuralNet(structure);
        neuralNet.setInputValues(x);
        neuralNet.feedForward();

        long end1 = System.nanoTime();
        System.out.println(Arrays.toString(neuralNet.getOutputValues()));
        System.out.println(end1 - start1);
    }

    private void createFilter() {
        filter = new float[1][3][3][3];
        for (int i = 1; i < filter[0].length+1; i++) {
            for (int j = 1; j < filter[0][i-1].length+1; j++) {
                for (int k = 1; k < filter[0][i-1][j-1].length+1; k++) {
                    filter[0][i-1][j-1][k-1] = i*j+k;
                }
            }
        }
    }

    private void createFinalFilter() {
        filter = new float[2][8][3][3];
        for (int i = 0; i < filter.length; i++) {
            for (int j = 0; j < filter[i].length; j++) {
                for (int k = 0; k < filter[i][j].length; k++) {
                    for (int l = 0; l < filter[i][j][k].length; l++) {
                        switch (ThreadLocalRandom.current().nextInt(0, 2)) {
                            case 0:
                                filter[i][j][k][l] = -1;
                                break;
                            case 1:
                                filter[i][j][k][l] = 0.1f;
                                break;
                            case 2:
                                filter[i][j][k][l] = 1;
                                break;
                        }
                    }
                }
            }
        }
    }

    private void createValues() {
        values = new float[3][8][8];
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                for (int k = 0; k < values[i][j].length; k++) {
                    values[i][j][k] = ThreadLocalRandom.current().nextInt(-5, 5);
                }
            }
        }
    }
}
