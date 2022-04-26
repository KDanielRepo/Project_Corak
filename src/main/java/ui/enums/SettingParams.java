package ui.enums;

import lombok.Getter;

@Getter
public enum SettingParams {
    LAST_SELECTED_PROFILE("last_selected_profile");

    private String value;

    SettingParams(String value){
        this.value = value;
    }
}
