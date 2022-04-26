package ui.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import ui.CustomListener;
import ui.Profile;
import utils.ScreenCaptureUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainMenu extends Menu {
    private Profile profile;
    private int screenCaptureTimer;
    private CustomListener customListener;

    public MainMenu(String name, Profile profile, CustomListener customListener){
        super(name);
        //this.profile = profile;
        this.customListener = customListener;
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
        this.getItems().add(screenCaptureArea);
        this.getItems().add(setScreenCaptureTimer);
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
                    BufferedImage bufferedImage = ScreenCaptureUtils.createScreenCapture(0, 0, 800, 600, true, profile);
                    //List<BufferedImage> imageList = ScreenCaptureUtils.getResourcesImages(bufferedImage,index);
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
}
