package ui;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Profile {
    private static final int DEFAULT_APP_WIDTH = 800;
    private static final int DEFAULT_APP_HEIGHT = 600;

    private String profileName;

    private Integer selectedAppWidth;
    private Integer selectedAppHeight;
    private Integer selectedGridSize;
    private List<String> trainingDataLabels = new ArrayList<>();

    public Integer getSelectedAppWidth() {
        if(Objects.nonNull(selectedAppWidth)){
            return selectedAppWidth;
        }else{
            return DEFAULT_APP_WIDTH;
        }
    }

    public Integer getSelectedAppHeight() {
        if(Objects.nonNull(selectedAppHeight)){
            return selectedAppHeight;
        }else{
            return DEFAULT_APP_HEIGHT;
        }
    }
}
