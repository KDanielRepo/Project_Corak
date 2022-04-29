package ui.menus;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ui.Profile;
import ui.views.MainAppView;
import ui.views.NeuralNetworkVisualizationView;
import ui.views.TrainingDataCreationView;

import java.util.Objects;

public class ViewMenu extends Menu {

    private NeuralNetworkVisualizationView neuralNetworkVisualizationView;
    private TrainingDataCreationView trainingDataCreationView;
    private MainAppView mainAppView;

    public ViewMenu(String name, BorderPane borderPane){
        super(name);
        initView();
        initItems(borderPane);
        if(Objects.isNull(borderPane.getCenter())){
            borderPane.setCenter(mainAppView);
        }
    }

    private void initItems(BorderPane borderPane){
        MenuItem mainAppMenuItem = new MenuItem("Main view");
        mainAppMenuItem.setOnAction(e->{
            borderPane.setCenter(mainAppView);
        });
        MenuItem neuralNetworkMenuItem = new MenuItem("Visualize neural net");
        neuralNetworkMenuItem.setOnAction(e->{
            borderPane.setCenter(neuralNetworkVisualizationView);
        });
        MenuItem trainingDataCreationMenuItem = new MenuItem("Training data view");
        trainingDataCreationMenuItem.setOnAction(e->{
            borderPane.setCenter(trainingDataCreationView);
        });
        this.getItems().addAll(neuralNetworkMenuItem,trainingDataCreationMenuItem);
    }

    private void initView(){
        neuralNetworkVisualizationView = new NeuralNetworkVisualizationView();
        trainingDataCreationView = new TrainingDataCreationView();
        mainAppView = new MainAppView();
    }
}
