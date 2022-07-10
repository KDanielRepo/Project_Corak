import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.MatrixUtils;
import utils.NeuralNetworkUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class NeuralNetworkUtilsTest {

    @Test
    public void convertRGBImageToInputsTest() throws IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\Ithilgore\\Desktop\\rbgTest.png"));
        float[][][] arrayFromImage = NeuralNetworkUtils.convertRGBImageToInputs(image);
        System.out.println(Arrays.deepToString(arrayFromImage));
    }

    @Test
    public void convolutionTest(){
        float[][] matrix = new float[][]{{1,2,3,4},{2,3,4,5},{3,4,5,6},{4,5,6,7}};
        float[][] filter = new float[][]{{1,1},{1,1}};
        System.out.println(MatrixUtils.convolution(MatrixUtils.getPartOfArray(matrix,0,0,2,2),filter));
    }
}
