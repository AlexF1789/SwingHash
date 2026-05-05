package net.alexf1789.swinghash.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Settings implements Serializable {
    
    private boolean darkTheme;
    private Set<String> algorithms;
    
    private static Settings settings;
    
    private Settings() {
        darkTheme = false;
        algorithms = new HashSet<>(3);
        
        algorithms.add("SHA-256");
        algorithms.add("SHA-512");
        algorithms.add("MD5");
    }
    
    /**
     * Gets or creates a Settings instance
     * 
     * @return a Settings instance
     */
    public static Settings getSettings() {
        if(settings != null)
            return settings;
        
        Path settingsPath = getSavePath();
        
        try {
            Files.createDirectories(getDirectorySavePath());
        } catch (IOException e) {
            System.err.println("Error in creating the directories to save the settings");
        }
        
        if(!Files.exists(settingsPath))
            settings = new Settings();
        
        try(FileInputStream settingsFile = new FileInputStream(settingsPath.toString())) {
            ObjectInputStream ois = new ObjectInputStream(settingsFile);
            settings = (Settings) ois.readObject();
        } catch(Exception e) {
            settings = new Settings();
        }
        
        return settings;
    }
    
    /**
     * Saves the current settings in the system
     */
    public void save() {
        Path settingsPath = getSavePath();

        try(FileOutputStream settingsFile = new FileOutputStream(settingsPath.toString())) {
            ObjectOutputStream oos = new ObjectOutputStream(settingsFile);
            oos.writeObject(settings);
        } catch(Exception e) {
           System.err.println("Error in saving the settings!");
        }
    }

    /**
     * Returns the settings file save path
     * 
     * @return a Path which represents the save path of the settings file
     */
    private static Path getSavePath() {
        return Paths.get(System.getProperty("user.home"),  ".SwingHash", "settings.swh");
    }
    
    /**
     * Returns the settings file directory save path
     * 
     * @return a Path which represents the directory in which we'll save the settings file
     */
    private static Path getDirectorySavePath() {
        return Paths.get(System.getProperty("user.home"),  ".SwingHash");
    }
    
    public boolean isDarkTheme() {
        return darkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }

    public Set<String> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(Set<String> algorithms) {
        this.algorithms = algorithms;
    }

}
