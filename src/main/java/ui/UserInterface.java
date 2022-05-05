package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import neuralnetwork.CoracsBrain;
import ui.menus.MainMenu;
import ui.menus.ViewMenu;
import ui.views.MainAppView;
import ui.views.TrainingDataCreationView;
import utils.SettingsUtils;

import java.util.*;

public class UserInterface extends Application {

    private MenuBar menuBar;
    private CustomListener customListener;
    private BorderPane mainPane;
    private Scene mainScene;
    private CoracsBrain coracsBrain;
    private Settings settings;
    private Profile profile;

    private MainAppView mainAppView;
    private TrainingDataCreationView trainingDataCreationView;

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Project: Corak");
        initialize();
        primaryStage.setMinWidth(profile.getSelectedAppWidth());
        primaryStage.setMinHeight(profile.getSelectedAppHeight());
        primaryStage.setAlwaysOnTop(false);
        primaryStage.setScene(mainScene);
        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e->{
            System.out.println("saving");
            SettingsUtils.saveProfile(settings, profile);
        });
    }

    private void initialize(){
        customListener = new CustomListener();
        mainPane = new BorderPane();
        settings = SettingsUtils.parseSettings();
        profile = SettingsUtils.getProfileFromSettings(settings);
        //mainPane.setOpacity(1);
        //mainPane.setPickOnBounds(false);
        //mainPane.setMouseTransparent(true);
        mainScene = new Scene(mainPane, Color.TRANSPARENT);
        //mainScene.getRoot().setStyle("-fx-background-color: transparent");
        /*Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(2f);*/
        //mainPane.setCenter(canvas);
        createMenus();
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
        if(Objects.isNull(menuBar)){
            menuBar = new MenuBar();

            MainMenu mainMenu = new MainMenu("test", profile, customListener);
            ViewMenu viewMenu = new ViewMenu("view", mainPane, profile);

            menuBar.getMenus().addAll(mainMenu, viewMenu);
        }

        mainPane.setTop(menuBar);
    }
}
