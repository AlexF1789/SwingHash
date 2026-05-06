package net.alexf1789.swinghash.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;

public class Settings {
    
    private boolean darkTheme, textMode, disableHistory;
    private Set<String> algorithms;
    private LinkedList<Hasher> history;
    private int historySize;
    
    private static Settings settings;
    
    private Settings() {
        darkTheme = false;
        textMode = true;
        disableHistory = false;
        historySize = 5;
        
        algorithms = new HashSet<>(3);
        history = new LinkedList<>();
        
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
        
        // let's retrieve and check if the directory in which we'll save the settings exists
        // and, in case it doesn't, let's create it
        Path settingsPath = getSavePath();
        
        try {
            Files.createDirectories(getDirectorySavePath());
        } catch (IOException e) {
            System.err.println("Error in creating the directories to save the settings");
        }
        
        // let's check if the Settings file exists, if it doesn't
        // let's create the instance and return it
        if(!Files.exists(settingsPath))
            return settings = new Settings();
        
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(settingsPath.toAbsolutePath().toString()))) {
            settings = new Gson().fromJson(bufferedReader, Settings.class);
        } catch(Exception e) {
            settings = new Settings();
            System.err.println("Error in reading settings!");
            e.printStackTrace();
        }
        
        return settings;
    }
    
    /**
     * Saves the current settings in the system
     */
    public void save() {
        Path settingsPath = getSavePath();

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(settingsPath.toAbsolutePath().toString()))) {
            new Gson().toJson(settings, bufferedWriter);
        } catch(Exception e) {
           System.err.println("Error in saving the settings!");
           e.printStackTrace();
        }
    }

    /**
     * Returns the settings file save path
     * 
     * @return a Path which represents the save path of the settings file
     */
    private static Path getSavePath() {
        return Paths.get(System.getProperty("user.home"),  ".SwingHash", "settings.json");
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

    public boolean isTextMode() {
        return textMode;
    }

    public void setTextMode(boolean textMode) {
        this.textMode = textMode;
    }

    public void clearHistory() {
        history = new LinkedList<>();
    }
    
    /**
     * Adds a computed hash to the history, watch out: it removes the elements
     * not fitting anymore according to the specified size
     * 
     * @param hash is the Hash to add to the history
     */
    public void addToHistory(Hasher hash) {
        // let's check if the history is disabled or not
        if(disableHistory)
            return;
        
        history.addFirst(hash);
        
        // let's check if the history size is not greater to the
        // wanted one
        if(history.size() > historySize)
            history.removeLast();
    }
    
    /**
     * Returns the last hash that was computed by the user
     * 
     * @return an Hasher or null if none
     */
    public Hasher getLastHashComputed() {
        // if the history is disabled we're not even trying to recover it
        if(disableHistory)
            return null;
        
        return history.getFirst();
    }

    public int getHistorySize() {
        return historySize;
    }

    public void setHistorySize(int historySize) {
        this.historySize = historySize;
    }

    public boolean isDisableHistory() {
        return disableHistory;
    }

    public void setDisableHistory(boolean disableHistory) {
        this.disableHistory = disableHistory;
    }

}
