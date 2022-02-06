package utils;

import gamedata.Resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScreenCaptureUtils {
    public static BufferedImage createScreenCapture(int x, int y, int width, int height) throws IOException, AWTException {
        System.out.println("creating capture");
        Robot robot = new Robot();
        Rectangle screenBounds = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();

        screenBounds.x = x;
        screenBounds.y = y;
        screenBounds.width = width;
        screenBounds.height = height;
        System.out.println("capture complete");
        return robot.createScreenCapture(screenBounds);
    }

    public static List<BufferedImage> getResourcesImages(BufferedImage image) {
        List<BufferedImage> bufferedImageList = new ArrayList<>();
        int xOffset = 7;
        int nextOffset = 26;
        int previousWidth = 0;
        for (Resources resources : Resources.values()) {
            BufferedImage test = applyBlackAndWhiteFilter(image.getSubimage(xOffset + nextOffset + previousWidth, 578, 54, 16));
            byte[][] bytes = NeuralNetworkUtils.convertBinaryImageToInputs(test);
            /*for (int i = 0; i < bytes.length; i++) {
                for (int j = 0; j < bytes[i].length; j++) {
                    System.out.print(bytes[i][j]);
                }
                System.out.println();
            }
            System.out.println(resources.name());*/
            /*File output = new File(resources.name()+"test.jpg");
            ImageIO.write(test, "jpg", output);*/
            bufferedImageList.add(test);
            previousWidth += 78;
            xOffset += 6;
        }
        return bufferedImageList;
    }

    public static BufferedImage applyBlackAndWhiteFilter(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics2D = result.createGraphics();
        graphics2D.drawImage(image, 0, 0, Color.WHITE, null);
        graphics2D.dispose();

        return result;
    }

    public static Point selectAreaForCapture() {
        return MouseInfo.getPointerInfo().getLocation();
    }

}
