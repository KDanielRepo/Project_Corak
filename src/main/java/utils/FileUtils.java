package utils;

import neuralnetwork.Dataset;
import ui.Profile;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileUtils {
    public static Dataset importDatasetFromFile(){
        Dataset dataset = new Dataset();
        try {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Texts", "txt");
            jFileChooser.setCurrentDirectory(new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources"));
            jFileChooser.setFileFilter(fileNameExtensionFilter);
            int ret = jFileChooser.showOpenDialog(null);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                Scanner scanner = new Scanner(file);
                AtomicInteger indexX = new AtomicInteger(0);
                AtomicInteger indexY = new AtomicInteger(0);
                Arrays.stream(scanner.nextLine().split(",")).forEach(line -> {
                    Arrays.stream(line.split(";")).forEach(flo -> {
                        dataset.getImageValues()[indexY.get()][indexX.get()] = Float.parseFloat(flo);
                        indexX.getAndIncrement();
                    });
                    indexX.set(0);
                    indexY.getAndIncrement();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Profile importSettingsFromFile(String profileName) throws Exception {//TODO zmienic Wszędzie sciezki w file
        File file = new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources\\profiles\\"+profileName);
        if(!file.exists()){
            throw new Exception();
        }
        Profile profile = new Profile();
        return null;
    }
    public static List<String> getListOfExistingProfiles(){
        List<String> names = new ArrayList<>();
        File folder = new File("C:\\Users\\Daniel\\IdeaProjects\\Project_Corak\\src\\main\\resources\\profiles");
        File[] files = folder.listFiles();
        if(Objects.nonNull(files)){
            for (File file : files) {
                names.add(file.getName());
            }
        }
        return names;
    }
}
