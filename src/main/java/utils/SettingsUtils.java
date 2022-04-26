package utils;

import ui.Profile;
import ui.Settings;
import ui.enums.ProfileParams;
import ui.enums.SettingParams;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingsUtils {

    private static final String LINE_SEPARATOR = ":";
    private static final int NUMBER_OF_PARAMS = 1;
    private static final String DEFAULT_PROFILE = "default_profile";
    private static final String NEW_LINE = "\n";

    public static Settings parseSettings(){
        Settings settings = new Settings();
        try{
            File file = new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources\\settings.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Map<String, String> settingsParameters = new HashMap<>();
            bufferedReader.lines().forEach(line->{
                String[] paramValueArray = line.split(LINE_SEPARATOR);
                if(Objects.isNull(paramValueArray) || paramValueArray.length<1){
                    throw new RuntimeException();
                }
                String param = paramValueArray[0];
                String value = paramValueArray[1];
                settingsParameters.put(param, value);
            });
            if(settingsParameters.entrySet().size()<NUMBER_OF_PARAMS){
                throw new RuntimeException();
            }
            for (SettingParams value : SettingParams.values()) {
                if(Objects.isNull(settingsParameters.get(value.getValue()))){
                    throw new RuntimeException();
                }
            }
            settings.setLastSelectedProfile(settingsParameters.get(SettingParams.LAST_SELECTED_PROFILE.getValue()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return settings;
    }

    public static Profile parseProfile(File profileFile){
        Profile profile = new Profile();
        try{
            FileReader fileReader = new FileReader(profileFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Map<String, String> profileParameters = new HashMap<>();
            bufferedReader.lines().forEach(line->{
                String[] paramValueArray = line.split(LINE_SEPARATOR);
                if(Objects.isNull(paramValueArray) || paramValueArray.length<1){
                    throw new RuntimeException();
                }
                String param = paramValueArray[0];
                String value = paramValueArray[1];
                profileParameters.put(param, value);
            });
            profile.setProfileName(profileParameters.get(ProfileParams.PROFILE_NAME.getValue()));
            profile.setSelectedAppWidth(Integer.parseInt(profileParameters.get(ProfileParams.APP_WIDTH.getValue())));
            profile.setSelectedAppHeight(Integer.parseInt(profileParameters.get(ProfileParams.APP_HEIGHT.getValue())));
            profile.setSelectedGridSize(Integer.parseInt(profileParameters.get(ProfileParams.GRID_SIZE.getValue())));
        }catch (Exception e){
            e.printStackTrace();
        }
        return profile;
    }

    public static Profile getProfileFromSettings(Settings settings){
        File file = null;
        try{
            if(settings.getLastSelectedProfile().equals(DEFAULT_PROFILE)){
                file = new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources\\profiles\\default_profile.txt");
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(SettingParams.LAST_SELECTED_PROFILE.getValue()+":"+DEFAULT_PROFILE+NEW_LINE);
                bufferedWriter.write(ProfileParams.APP_WIDTH.getValue()+":"+"800"+NEW_LINE);
                bufferedWriter.write(ProfileParams.APP_HEIGHT.getValue()+":"+"600"+NEW_LINE);
                bufferedWriter.write(ProfileParams.GRID_SIZE.getValue()+":"+"32"+NEW_LINE);
                bufferedWriter.flush();
                fileWriter.close();
            }else{
                file = new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources\\profiles\\"+settings.getLastSelectedProfile()+".txt");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return parseProfile(file);
    }

    public static void saveSettingsOnExit(){

    }

    public static void saveProfile(){

    }
}
