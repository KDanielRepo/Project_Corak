import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import utils.NeuralNetworkUtils;
import utils.ScreenCaptureUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
public class ScreenCaptureUtilsTest {

    @Test
    public void grayScaleTest() throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources\\mm55.png"));
        BufferedImage subimage = bufferedImage.getSubimage(7, 577, 80, 17);
        BufferedImage test = new BufferedImage(subimage.getWidth(), subimage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics2D = test.createGraphics();
        graphics2D.drawImage(subimage, 0, 0, Color.WHITE, null);
        graphics2D.dispose();

        File output = new File("test.jpg");
        ImageIO.write(test, "jpg", output);
        byte[][] a = NeuralNetworkUtils.convertBinaryImageToInputs(test);

    }

    @Test
    public void extractSegmentsFromOverWorldTest() throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources\\screens\\sur 4.png"));
        ScreenCaptureUtils.extractSegmentsFromOverWorld(bufferedImage);
    }
}
