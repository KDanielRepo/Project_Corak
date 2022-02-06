package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import utils.ScreenCaptureUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserInterface extends Application {

    private MenuBar menuBar;
    private CustomListener customListener;
    private BorderPane mainPane;
    private Scene mainScene;

    private int screenCaptureTimer;

    @Override
    public void start(Stage primaryStage){
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(796);
        primaryStage.setTitle("Project: Corak");
        initialize();

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void initialize(){
        customListener = new CustomListener();
        mainPane = new BorderPane();
        mainScene = new Scene(mainPane);

        createMenus();
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
            while (customListener.getPoint()==null){
                try{
                    System.out.println("sleep");
                    Thread.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //while (true){
                try{
                    //Thread.sleep(10000);
                    BufferedImage bufferedImage = ScreenCaptureUtils.createScreenCapture(customListener.getPoint().x, customListener.getPoint().y, 800, 600);
                    ScreenCaptureUtils.getResourcesImages(bufferedImage);
                }catch (Exception e){
                    e.printStackTrace();
                }
            //}
        });
        thread.start();
    }

    private void createMenus(){
        if(menuBar==null){
            menuBar = new MenuBar();
            Menu mainMenu = new Menu("test");
            MenuItem screenCaptureArea = new MenuItem("Set screen capture area");
            screenCaptureArea.setOnAction(e->{
                setScreenCaptureArea();
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
