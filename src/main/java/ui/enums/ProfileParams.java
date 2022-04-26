package ui.enums;

import lombok.Getter;

@Getter
public enum  ProfileParams {
    APP_WIDTH("app_width"),
    APP_HEIGHT("app_height"),
    PROFILE_NAME("profile_name"),
    GRID_SIZE("grid_size");

    private String value;

    ProfileParams(String value) {
        this.value = value;
    }
}