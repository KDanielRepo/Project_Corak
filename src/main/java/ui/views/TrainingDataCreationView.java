package ui.views;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.abstracts.AbstractView;
import ui.abstracts.AbstractZoomableView;

public class TrainingDataCreationView extends AbstractZoomableView{
    private static final String SELECT_IMAGE_BUTTON_NAME = "button1";
    private static final String SELECT_IMAGE_FOLDER_BUTTON_NAME = "button2";

    private VBox vBox;
    private HBox hBox;
    private Button selectImageButton;
    private Button selectImageFolderButton;


    public TrainingDataCreationView(){
        init();
    }

    private void init(){
        vBox = new VBox();
        hBox = new HBox();
        selectImageButton = new Button(SELECT_IMAGE_BUTTON_NAME);
        selectImageButton.setOnAction(e->{

        });
        selectImageFolderButton = new Button(SELECT_IMAGE_FOLDER_BUTTON_NAME);
        selectImageFolderButton.setOnAction(e->{

        });
    }

}
