package ui;

import lombok.Data;

@Data
public class Profile {
    private static final int DEFAULT_APP_WIDTH = 800;
    private static final int DEFAULT_APP_HEIGHT = 600;

    private String profileName;

    private int selectedAppWidth;
    private int selectedAppHeight;
    private int selectedGridSize;


}
