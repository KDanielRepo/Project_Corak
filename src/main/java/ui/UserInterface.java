package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import neuralnetwork.ConvolutionalNeuralNet;
import neuralnetwork.CoracsBrain;
import utils.ScreenCaptureUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UserInterface extends Application {

    private MenuBar menuBar;
    private CustomListener customListener;
    private BorderPane mainPane;
    private Scene mainScene;
    private CoracsBrain coracsBrain;
    private int index;

    private int screenCaptureTimer;

    @Override
    public void start(Stage primaryStage){
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.setMaxWidth(800);
        primaryStage.setMaxHeight(600);
        primaryStage.setTitle("Project: Corak");
        initialize();
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(mainScene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }

    private void initialize(){
        customListener = new CustomListener();
        mainPane = new BorderPane();
        //mainPane.setOpacity(1);
        //mainPane.setPickOnBounds(false);
        //mainPane.setMouseTransparent(true);
        mainScene = new Scene(mainPane, Color.TRANSPARENT);
        mainScene.getRoot().setStyle("-fx-background-color: transparent");
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(2f);
        mainPane.setCenter(canvas);
        index = 589;
        //test(gc);
        createMenus();
    }

    private void test(GraphicsContext gc){
        Thread thread = new Thread(()->{
            while (true){
                try{
                    Thread.sleep(3000);
                    int x = ThreadLocalRandom.current().nextInt(0, 1260);
                    int y = ThreadLocalRandom.current().nextInt(0, 776);
                    gc.strokeRect(x,y, 20, 20);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void setScreenCaptureArea(){
        Toolkit.getDefaultToolkit().addAWTEventListener(
                customListener, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
        try {
            Thread.sleep(screenCaptureTimer*1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame();
        customListener.setJFrame(frame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void createCaptures(){
        Thread thread = new Thread(() -> {
            /*while (customListener.getPoint()==null){
                try{
                    System.out.println("sleep");
                    Thread.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }*/
            while (true){
                try{
                    Thread.sleep(20000);
                    BufferedImage bufferedImage = ScreenCaptureUtils.createScreenCapture(0, 0, 800, 600, index, true);
                    //List<BufferedImage> imageList = ScreenCaptureUtils.getResourcesImages(bufferedImage,index);
                    index++;
                    //ScreenCaptureUtils.extractDigitsFromResourceImage(imageList.get(imageList.size()-1));
                    //coracsBrain = new CoracsBrain();
                    //coracsBrain.setConvolutionalNeuralNet(new ConvolutionalNeuralNet());


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /*private void createResourceImagesDataFromSelectedScreenShot(){
        try {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Images","png");
            jFileChooser.setCurrentDirectory(new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources"));
            jFileChooser.setFileFilter(fileNameExtensionFilter);
            int ret = jFileChooser.showOpenDialog(null);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                BufferedImage image = ImageIO.read(file);
                ScreenCaptureUtils.getResourcesImages(image,index);
                index++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    private void createMenus(){
        if(menuBar==null){
            menuBar = new MenuBar();
            Menu mainMenu = new Menu("test");
            MenuItem screenCaptureArea = new MenuItem("Set screen capture area");
            screenCaptureArea.setOnAction(e->{
                //setScreenCaptureArea();
                createCaptures();
            });
            MenuItem setScreenCaptureTimer = new MenuItem("Set screen capture timer");
            setScreenCaptureTimer.setOnAction(e->{
                TextInputDialog structureDialog = new TextInputDialog();
                structureDialog.setTitle("Set screen capture timer");
                structureDialog.setHeaderText("Set screen capture time delay in seconds: ");
                structureDialog.setContentText("Insert delay: ");
                structureDialog.showAndWait().ifPresent(result->{
                    screenCaptureTimer = Integer.parseInt(result);
                });
            });
            mainMenu.getItems().add(screenCaptureArea);
            mainMenu.getItems().add(setScreenCaptureTimer);
            menuBar.getMenus().add(mainMenu);
        }

        mainPane.setTop(menuBar);
    }
}
