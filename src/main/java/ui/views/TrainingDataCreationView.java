package ui.views;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ui.Profile;
import ui.abstracts.AbstractView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TrainingDataCreationView extends AbstractView {
    private static final String SELECT_IMAGE_BUTTON_NAME = "button1";
    private static final String SELECT_IMAGE_FOLDER_BUTTON_NAME = "button2";

    private VBox vBox;
    private HBox hBox;
    private Button selectImageButton;
    private Button selectImageFolderButton;
    private Canvas canvas;
    private GraphicsContext gc;
    private Profile profile;

    private Button clearSelectedCoordinates;
    private Button generateTrainingData;
    private ComboBox<String> label;


    public TrainingDataCreationView() {
        init();
    }

    private void init() {
        vBox = new VBox();
        hBox = new HBox();
        selectImageButton = new Button(SELECT_IMAGE_BUTTON_NAME);
        clearSelectedCoordinates = new Button("asd");
        generateTrainingData = new Button("sfg");
        label = new ComboBox<>();
        vBox.getChildren().addAll(label,clearSelectedCoordinates,generateTrainingData);
        selectImageButton.setOnAction(e -> {

        });
        selectImageFolderButton = new Button(SELECT_IMAGE_FOLDER_BUTTON_NAME);
        selectImageFolderButton.setOnAction(e -> {

        });
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        ZoomableView zoomableView = new ZoomableView(canvas);
        int gridSize = 32;//profile.getSelectedGridSize();
        canvas.setOnMouseClicked(e -> {
            gc.setFill(Color.RED);
            gc.fillRect(((int) (e.getX() / gridSize)) * gridSize, ((int) (e.getY() / gridSize)) * gridSize, gridSize, gridSize);
        });
        try {
            //divideImageToMatchGrid(ImageIO.read(new File("")));
            drawGridOnImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setCenter(zoomableView);
        this.setRight(vBox);
    }

    private void drawGridOnImage() {
        int gridSize = 32;//profile.getSelectedGridSize();
        int cellWidthNumber = (int) (canvas.getWidth() / gridSize);
        int cellHeightNumber = (int) (canvas.getHeight() / gridSize);
        System.out.println(cellWidthNumber);
        System.out.println(cellHeightNumber);

        gc.setStroke(Color.BLACK);
        for (int i = 0; i < cellWidthNumber; i++) {
            for (int j = 0; j < cellHeightNumber; j++) {
                gc.setLineWidth(2);
                gc.strokeLine(i * gridSize, 0, i * gridSize, canvas.getHeight());
                gc.strokeLine(0, j * gridSize, canvas.getWidth(), j * gridSize);
            }
        }
    }

    private void divideImageToMatchGrid(BufferedImage image) {
        //List<BufferedImage> images = new ArrayList<>();
        int gridSize = 32;//profile.getSelectedGridSize();
        int cellWidthNumber = (int) (canvas.getWidth() / gridSize);
        int cellHeightNumber = (int) (canvas.getHeight() / gridSize);

        for (int i = 0; i < cellWidthNumber; i++) {
            for (int j = 0; j < cellHeightNumber; j++) {
                gc.drawImage(SwingFXUtils.toFXImage(image.getSubimage(gridSize * i, (gridSize * i + gridSize), gridSize * j, (gridSize * j + gridSize)), null), gridSize * i, gridSize * j);
            }
        }
    }

    private void addCustomLabel() {
    }


}
