package ui.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import ui.Profile;
import ui.TrainingDataCoordinates;
import ui.abstracts.AbstractView;
import utils.ScreenCaptureUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TrainingDataCreationView extends AbstractView {
    private static final String SELECT_IMAGE_BUTTON_NAME = "Select image";
    private static final String SELECT_IMAGE_FOLDER_BUTTON_NAME = "Select images folder";
    private static final String START_ADD_COORDINATES_BUTTON_NAME = "Start adding coordinates";
    private static final String STOP_ADD_COORDINATES_BUTTON_NAME = "Stop adding coordinates";
    private static final String ADD_NEW_LABEL_BUTTON_NAME = "Add new label";
    private static final String EDIT_EXISTING_LABEL_BUTTON_NAME = "Edit selected label";
    private static final String GENERATE_TRAINING_DATA_BUTTON_NAME = "Generate training data";
    private int lineWidth = 1;
    private int gridSize = 1;

    private VBox toolBar;
    private HBox hBox;

    private Button selectImageButton;
    private Button selectImageFolderButton;
    private Button addCoordinatesButton;
    private Button addNewLabelButton;
    private Button editExistingLabelButton;
    private Button clearSelectedCoordinatesButton;
    private Button generateTrainingDataButton;

    private Canvas background;
    private Canvas foreground;
    private Canvas labelForeground;
    private GraphicsContext gc;
    private GraphicsContext gcp;
    private GraphicsContext labelGc;


    private ComboBox<String> labelSelection;
    private ComboBox<TrainingDataCoordinates> existingLabelSelection;
    private ObservableList<String> labelSelectionList;
    private ObservableList<TrainingDataCoordinates> existingLabelSelectionList;
    private ZoomableView zoomableView;

    private Map<TrainingDataCoordinates, String> trainingDataCoordinatesMap;
    private List<Node> nodes;

    int startX;
    int startY;
    int endX;
    int endY;
    boolean addingCoordinates;


    public TrainingDataCreationView(Profile profile) {
        init(profile);
    }

    private void init(Profile profile) {
        trainingDataCoordinatesMap = new HashMap<>();
        initControls(profile);
        initControlsLogic(profile);
        initGraphics(profile);

        this.setCenter(zoomableView);
        this.setRight(toolBar);
    }

    private void initControls(Profile profile) {
        toolBar = new VBox();
        hBox = new HBox();

        selectImageButton = new Button(SELECT_IMAGE_BUTTON_NAME);
        selectImageFolderButton = new Button(SELECT_IMAGE_FOLDER_BUTTON_NAME);
        clearSelectedCoordinatesButton = new Button("asd");
        generateTrainingDataButton = new Button(GENERATE_TRAINING_DATA_BUTTON_NAME);
        addCoordinatesButton = new Button(START_ADD_COORDINATES_BUTTON_NAME);
        addNewLabelButton = new Button(ADD_NEW_LABEL_BUTTON_NAME);
        editExistingLabelButton = new Button(EDIT_EXISTING_LABEL_BUTTON_NAME);

        if (!profile.getTrainingDataLabels().isEmpty()) {
            labelSelectionList = FXCollections.observableArrayList(profile.getTrainingDataLabels());
        } else {
            labelSelectionList = FXCollections.observableArrayList();
        }
        existingLabelSelectionList = FXCollections.observableArrayList();
        labelSelection = new ComboBox<>(labelSelectionList);
        existingLabelSelection = new ComboBox<>(existingLabelSelectionList);
        toolBar.getChildren().addAll(labelSelection, clearSelectedCoordinatesButton, generateTrainingDataButton,
                selectImageButton, selectImageFolderButton, addCoordinatesButton, addNewLabelButton, existingLabelSelection);

    }

    private void initControlsLogic(Profile profile) {
        selectImageButton.setOnAction(e -> {
            try {
                JFileChooser jFileChooser = new JFileChooser();
                FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Images", "jpg", "png");
                jFileChooser.setCurrentDirectory(new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources"));
                jFileChooser.setFileFilter(fileNameExtensionFilter);
                int ret = jFileChooser.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = jFileChooser.getSelectedFile();
                    Image backgroundImage = SwingFXUtils.toFXImage(ImageIO.read(file), null);
                    gc.drawImage(backgroundImage, 0, 0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        selectImageFolderButton.setOnAction(e -> {

        });

        addCoordinatesButton.setOnAction(e -> {
            addingCoordinates = !addingCoordinates;
            if (addingCoordinates) {
                addCoordinatesButton.setText(STOP_ADD_COORDINATES_BUTTON_NAME);
                zoomableView.setPannable(false);
            } else {
                addCoordinatesButton.setText(START_ADD_COORDINATES_BUTTON_NAME);
                zoomableView.setPannable(true);
                TrainingDataCoordinates trainingDataCoordinates = new TrainingDataCoordinates(startX, startY, endX, endY);
                trainingDataCoordinatesMap.put(trainingDataCoordinates, trainingDataCoordinates.toString());
                existingLabelSelection.getItems().add(trainingDataCoordinates);
                gcp.setStroke(Color.RED);
                gcp.strokeRect(startX, startY, endX - startX, endY - startY);
                labelGc.clearRect(0, 0, profile.getSelectedAppWidth(), profile.getSelectedAppHeight());
            }
        });

        addNewLabelButton.setOnAction(e -> {
            TextInputDialog addNewLabelDialog = new TextInputDialog();
            addNewLabelDialog.setTitle("Add new label");
            addNewLabelDialog.setHeaderText("Add new label");
            addNewLabelDialog.setContentText("Add new label for training data: ");
            Optional<String> result = addNewLabelDialog.showAndWait();
            result.ifPresent(label -> {
                labelSelection.getItems().add(label);
                profile.getTrainingDataLabels().add(label);
            });
        });

        editExistingLabelButton.setOnAction(e -> {
            TextInputDialog editLabelDialog = new TextInputDialog();
            editLabelDialog.setTitle("Edit label");
            editLabelDialog.setHeaderText("Edit label");
            editLabelDialog.setContentText("Edit selected label (" + labelSelection.getSelectionModel().getSelectedItem() + ") for training data: ");
            Optional<String> result = editLabelDialog.showAndWait();
            result.ifPresent(label -> {
                labelSelection.getItems().add(label);
                profile.getTrainingDataLabels().add(label);
            });

        });
    }

    private void initGraphics(Profile profile) {
        nodes = new ArrayList<>();
        gridSize = profile.getSelectedGridSize();

        background = new Canvas(profile.getSelectedAppWidth(), profile.getSelectedAppHeight());
        foreground = new Canvas(profile.getSelectedAppWidth(), profile.getSelectedAppHeight());
        labelForeground = new Canvas(profile.getSelectedAppWidth(), profile.getSelectedAppHeight());

        gc = background.getGraphicsContext2D();
        gcp = foreground.getGraphicsContext2D();
        labelGc = labelForeground.getGraphicsContext2D();

        nodes.add(background);
        nodes.add(foreground);
        nodes.add(labelForeground);
        try {
            Image backgroundImage = SwingFXUtils.toFXImage(ScreenCaptureUtils.createScreenCapture(0, 0, profile.getSelectedAppWidth(), profile.getSelectedAppHeight(), false, null), null);
            zoomableView = new ZoomableView(nodes);
            background.toBack();
            foreground.toFront();
            labelForeground.toFront();
            gc.drawImage(backgroundImage, 0, 0);
            drawGridOnImage();
            labelForeground.setOnDragDetected(e -> {
                if (addingCoordinates) {
                    startX = ((int) (e.getX() / gridSize)) * gridSize;
                    startY = ((int) (e.getY() / gridSize)) * gridSize;
                }
            });
            labelForeground.setOnMouseDragged(e -> {
                if (addingCoordinates) {
                    labelGc.clearRect(0, 0, profile.getSelectedAppWidth(), profile.getSelectedAppHeight());
                    //drawGridOnImage();
                    endX = ((int) (e.getX() / gridSize)) * gridSize;
                    endY = ((int) (e.getY() / gridSize)) * gridSize;
                    int w = endX - startX;
                    int h = endY - startY;
                    labelGc.setStroke(Color.RED);
                    labelGc.strokeRect(startX, startY, w, h);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawGridOnImage() {
        int cellWidthNumber = (int) ((foreground.getWidth()) / gridSize);
        int cellHeightNumber = (int) ((foreground.getHeight()) / gridSize);

        gcp.setStroke(Color.BLACK);
        for (int i = 0; i <= cellWidthNumber; i++) {
            for (int j = 0; j <= cellHeightNumber; j++) {
                gcp.setLineWidth(lineWidth);
                gcp.strokeLine(i * gridSize, 0, i * gridSize, foreground.getHeight() - (foreground.getHeight() - gridSize * cellHeightNumber));
                gcp.strokeLine(0, j * gridSize, foreground.getWidth() - (foreground.getWidth() - gridSize * cellWidthNumber), j * gridSize);
            }
        }
    }

    /*private void initImageView() {
        toolBar = new VBox();
        hBox = new HBox();
        selectImageButton = new Button(SELECT_IMAGE_BUTTON_NAME);
        clearSelectedCoordinates = new Button("asd");
        generateTrainingData = new Button("sfg");
        label = new ComboBox<>();
        toolBar.getChildren().addAll(label,clearSelectedCoordinates,generateTrainingData);
        selectImageButton.setOnAction(e -> {

        });
        selectImageFolderButton = new Button(SELECT_IMAGE_FOLDER_BUTTON_NAME);
        selectImageFolderButton.setOnAction(e -> {

        });
        //canvas = new Canvas(800, 600);
        //gc = canvas.getGraphicsContext2D();
        int gridSize = 32;//profile.getSelectedGridSize();
        try {
            Image backgroundImage = SwingFXUtils.toFXImage(ScreenCaptureUtils.createScreenCapture(0,0,800,600,false,null),null);
            ImageView imageView = new ImageView(backgroundImage);
            zoomableView = new ZoomableView(imageView);
            //gc.drawImage(backgroundImage,0,0);
            //divideImageToMatchGrid(ImageIO.read(new File("")));
            drawGridOnImage(imageView, zoomableView);
            zoomableView.setOnMouseClicked(e -> {
                if(MouseButton.PRIMARY == e.getButton()){
                    int x = ((int) (e.getX() / gridSize)) * gridSize;
                    int y = ((int) (e.getY() / gridSize)) * gridSize;
                    Rectangle rectangle = new Rectangle(((int) (e.getX() / gridSize)) * gridSize, ((int) (e.getY() / gridSize)) * gridSize, gridSize, gridSize);
                    rectangle.setFill(Color.RED);
                    WritableImage wi=new WritableImage(backgroundImage.getPixelReader(),(int)backgroundImage.getWidth(),(int)backgroundImage.getHeight());
                    PixelWriter pw=wi.getPixelWriter();
                    pw.setColor(x,y,new Color(0,0,0,1));
                    imageView.setImage(wi);
                    //imageView.getChildren().add(rectangle);
                    //gc.setFill(Color.RED);
                    //gc.fillRect(((int) (e.getX() / gridSize)) * gridSize, ((int) (e.getY() / gridSize)) * gridSize, gridSize, gridSize);
                }else if(MouseButton.SECONDARY == e.getButton()){
                    *//*zoomableView.getChildren().remo
                    gc.clearRect(((int) (e.getX() / gridSize)) * gridSize, ((int) (e.getY() / gridSize)) * gridSize, gridSize, gridSize);*//*
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setCenter(zoomableView);
        this.setRight(toolBar);
    }*/

    private void drawGridOnImageView(ImageView imageView, ZoomableView zoomableView) {
        int gridSize = 32;//profile.getSelectedGridSize();
        int cellWidthNumber = (int) ((imageView.getImage().getWidth()) / gridSize);
        int cellHeightNumber = (int) ((imageView.getImage().getHeight()) / gridSize);
        System.out.println(cellWidthNumber);
        System.out.println(cellHeightNumber);

        //gc.setStroke(Color.BLACK);
        for (int i = 0; i <= cellWidthNumber; i++) {
            for (int j = 0; j <= cellHeightNumber; j++) {
                Line vertical = new Line(i * gridSize, 0, i * gridSize, imageView.getImage().getHeight() - (imageView.getImage().getHeight() - gridSize * cellHeightNumber));
                vertical.setFill(null);
                vertical.setStroke(Color.BLACK);
                vertical.setStrokeWidth(1);
                Line horizontal = new Line(0, j * gridSize, imageView.getImage().getWidth() - (imageView.getImage().getWidth() - gridSize * cellWidthNumber), j * gridSize);
                horizontal.setFill(null);
                horizontal.setStroke(Color.BLACK);
                horizontal.setStrokeWidth(1);
                zoomableView.getChildren().add(vertical);
                zoomableView.getChildren().add(horizontal);

                //gc.setLineWidth(1);
                //gc.strokeLine(i * gridSize, 0, i * gridSize, imageView.getImage().getHeight()-(imageView.getImage().getHeight()-gridSize*cellHeightNumber));
                //gc.strokeLine(0, j * gridSize, imageView.getImage().getWidth()-(imageView.getImage().getWidth()-gridSize*cellWidthNumber), j * gridSize);
            }
        }
    }

    /*private void divideImageToMatchGrid(BufferedImage image) {
        //List<BufferedImage> images = new ArrayList<>();
        int gridSize = 32;//profile.getSelectedGridSize();
        int cellWidthNumber = (int) (canvas.getWidth() / gridSize);
        int cellHeightNumber = (int) (canvas.getHeight() / gridSize);

        for (int i = 0; i < cellWidthNumber; i++) {
            for (int j = 0; j < cellHeightNumber; j++) {
                gc.drawImage(SwingFXUtils.toFXImage(image.getSubimage(gridSize * i, (gridSize * i + gridSize), gridSize * j, (gridSize * j + gridSize)), null), gridSize * i, gridSize * j);
            }
        }
    }*/

    private void addCustomLabel() {
    }


}
