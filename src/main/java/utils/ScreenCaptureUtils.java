package utils;

import gamedata.enums.Resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScreenCaptureUtils {
    public static BufferedImage createScreenCapture(int x, int y, int width, int height, int index, boolean save) throws IOException, AWTException {
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

        BufferedImage bufferedImage = robot.createScreenCapture(screenBounds);
        if(save){
            saveImage(bufferedImage, "./src/main/resources/screens/testCapture"+index+".jpg");
        }

        return bufferedImage;
    }

    public static List<BufferedImage> getResourcesImages(BufferedImage image, int index, boolean save) throws IOException {
        List<BufferedImage> bufferedImageList = new ArrayList<>();
        int xOffset = 7;
        int nextOffset = 26;
        int previousWidth = 0;
        for (Resources resources : Resources.values()) {
            BufferedImage resourceImage = applyBlackAndWhiteFilter(image.getSubimage(xOffset + nextOffset + previousWidth, 578, 54, 16));
            if(save){
                saveImage(resourceImage, resources.name() + index + ".jpg");
            }
            bufferedImageList.add(resourceImage);
            previousWidth += 78;
            xOffset += 6;
        }
        return bufferedImageList;
    }

    public static void extractDigitsFromResourceImage(BufferedImage image) throws IOException {
        int x = 0;
        int y = 0;
        byte[][] bytes = NeuralNetworkUtils.convertBinaryImageToInputs(image);
        byte[][][] imageArray = new byte[8][11][7];
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < bytes[i].length; j++) {
                //pierszy bialy x i pierwszy bialy y, biorę y z x i x z y i odejmuje po 1 pixelu z obu; nastepnie kazda cyfra 2 px odstepu x
                if (bytes[i][j] == 1 && y == 0) {
                    y = i - 1;
                    break;
                }
            }
        }
        for (int i = 0; i < bytes[0].length; i++) {
            for (int j = 0; j < bytes.length; j++) {
                if (bytes[j][i] == 1 && x == 0) {
                    x = i - 1;
                    break;
                }
            }
        }
        int index = 0;
        while (x + 7 < bytes[0].length) {
            imageArray[index] = MatrixUtils.getPartOfArray(bytes, x, y, x + 7, y + 11);
            BufferedImage bufferedImage = image.getSubimage(x, y, 7, 11);
            File output = new File("number" + index + ".jpg");
            ImageIO.write(bufferedImage, "jpg", output);
            x += 7;
            index++;
        }
        /*System.out.println(" x i y to od : "+x+" "+y);
        System.out.println(" x i y to do : "+(x+7)+" "+(y+11));

        for (int i = 0; i < imageArray.length; i++) {
            for (int j = 0; j < imageArray[i].length; j++) {
                for (int k = 0; k < imageArray[i][j].length; k++) {
                    System.out.print(imageArray[i][j][k]);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }*/
    }

    public static void extractSegmentsFromOverWorld(BufferedImage image) throws IOException {
        List<BufferedImage> subImages = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 18; j++) {
                BufferedImage subImage = image.getSubimage(((j * 32) + 8), ((i * 32) + 8), 32, 32);
                subImages.add(subImage);
            }
        }
        int index = 0;
        for (BufferedImage image1 : subImages) {
            File output = new File("sub" + index + ".jpg");
            ImageIO.write(image1, "jpg", output);
            index++;
        }
    }//Moze ale watpie ze sie przyda

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

    public static void saveImage(BufferedImage bufferedImage, String name) throws IOException {
        File output = new File(name);
        ImageIO.write(bufferedImage, "jpg", output);
    }

}
