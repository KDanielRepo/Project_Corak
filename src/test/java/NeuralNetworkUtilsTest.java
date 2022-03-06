import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
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
}
