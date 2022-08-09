import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.MatrixUtils;
import utils.NeuralNetworkUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class NeuralNetworkUtilsTest {

    @Test
    public void convertRGBImageToInputsTest() throws IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\Ithilgore\\Desktop\\rbgTest.png"));
        double[][][] arrayFromImage = NeuralNetworkUtils.convertRGBImageToInputs(image);
        System.out.println(Arrays.deepToString(arrayFromImage));
    }

    @Test
    public void convolutionTest(){
        double[][] matrix = new double[][]{{1,2,3,4},{2,3,4,5},{3,4,5,6},{4,5,6,7}};
        double[][] filter = new double[][]{{1,1},{1,1}};
        System.out.println(MatrixUtils.convolution(MatrixUtils.getPartOfArray(matrix,0,0,2,2),filter));
    }

    @Test
    public void softmaxTest(){
        double[] doubles = doubles();
        double[] a = new double[10];
        for (int i = 0; i < doubles.length; i++) {
            a[i]=NeuralNetworkUtils.softmax(doubles[i], doubles);
            System.out.println(a[i]);
        }
        double sum = 0d;
        for (int i = 0; i < a.length; i++) {
            sum+=a[i];
        }
        System.out.println(sum);
    }

    public double[] doubles(){
        double[] doubles = new double[10];
        doubles[0] = 0.0000001d;
        doubles[1] = 0.0000001d;
        doubles[2] = 0.0000001d;
        doubles[3] = 0.0000001d;
        doubles[4] = 0.0000001d;
        doubles[5] = 0.0000001d;
        doubles[6] = 0.0000001d;
        doubles[7] = 0.0000001d;
        doubles[8] = 0.0000001d;
        doubles[9] = 0.0000091d;
        return doubles;
    }
}
