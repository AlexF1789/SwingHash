package net.alexf1789.swinghash.services;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import net.alexf1789.swinghash.frames.MainFrame;
import net.alexf1789.swinghash.models.HistoryHash;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public class Settings {

    public static final int DEFAULT_HISTORY_SIZE = 5;
    
    @Expose
    private boolean darkTheme, textMode, enableHistory, enableFlatlaf;
    
    @Expose
    private Set<String> algorithms;
    
    @Expose
    private LinkedList<HistoryHash> history;
    
    @Expose
    private int historySize;

    private boolean alreadyFiltered = false;

    private static Settings settings;
    
    private Settings() {
        darkTheme = false;
        textMode = true;
        enableHistory = true;
        historySize = DEFAULT_HISTORY_SIZE;
        enableFlatlaf = true;

        resetAlgorithms();
        history = new LinkedList<>();
    }

    /**
     * Resets the current algorithms to the default ones
     */
    public void resetAlgorithms() {
        HashSet<String> algos = new HashSet<>(3);

        algos.add("SHA-256");
        algos.add("SHA-512");
        algos.add("MD5");

        algorithms = algos;
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
     * Reloads the settings from the disk
     *
     * @return an instance of Settings
     */
    public static Settings reloadSettings() {
        settings = null;
        return getSettings();
    }

    /**
     * Resets the settings to the default ones deleting the previous ones
     *
     * @return a new instance of Settings with all the properties
     *         set to the default values
     */
    public static Settings resetSettings() {
        try {
            Files.delete(getSavePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        settings = new Settings();
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
        return enableFlatlaf && darkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
        reloadFlatlaf();
    }

    /**
     * Returns the algorithms filtering them the first time the app is loaded
     *
     * @return the selected algorithms among the available ones
     */
    public Set<String> getAlgorithms() {
        if(!alreadyFiltered) {
            // the filtering is necessary since the JVM only ensures the SHA-256, SHA-512 and MD5 are implemented for sure
            Set<String> availableAlgos = getAllAlgorithms();

            algorithms = algorithms.stream()
                    .filter(availableAlgos::contains)
                    .collect(Collectors.toSet());

            alreadyFiltered = true;
        }

        return algorithms;
    }

    /**
     * Indicates wether the algorithm is selected or not
     *
     * @param algo is the algorithm to check
     * @return a boolean indicating whether the algo is selected or not
     */
    public boolean isAlgoSelected(String algo) {
        return algorithms.contains(algo);
    }

    /**
     * Selects a new algorithm
     *
     * @param algo is the algorithm to add
     */
    public void addSelectedAlgo(String algo) {
        algorithms.add(algo);
    }

    /**
     * Removes an algorithm from the selected ones
     *
     * @param algo is the algorithm to remove
     */
    public void removeSelectedAlgo(String algo) {
        algorithms.remove(algo);
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
     * @throws InterruptedException 
     */
    public void addToHistory(Hasher hash) throws InterruptedException {
        // let's check if the history is disabled or not
        if(!enableHistory)
            return;
        
        history.addFirst(new HistoryHash(hash));
        
        // let's check if the history size is not greater to the
        // wanted one
        if(history.size() > historySize)
            history.removeLast();
    }
    
    /**
     * Returns the last hash that was computed by the user
     * 
     * @return an HistoryHash or null if none
     */
    public HistoryHash getLastHashComputed() {
        // if the history is disabled we're not even trying to recover it
        if(!enableHistory)
            return null;
        
        return history.getFirst();
    }

    /**
     * Returns the maximum number of entries saved in the history
     *
     * @return the history maximum size
     */
    public int getHistorySize() {
        return historySize;
    }

    /**
     * Sets the size for the history, fallbacking on the defualt value of 5
     *
     * @param historySize is the history new size
     */
    public void setHistorySize(int historySize) {
        if(historySize == this.historySize)
            return;

        if(historySize > 0)
            this.historySize = historySize;
        else
            this.historySize = DEFAULT_HISTORY_SIZE;

        // let's trim the eventual records that go beyond the new size taking them
        // from the least recent
        if(history.size() > historySize) {
            for(int i=0; i<=history.size()-historySize; i++)
                history.removeLast();
        }
    }

    /**
     * Indicates wether the history is enabled
     *
     * @return a boolean indicating so
     */
    public boolean isHistoryEnabled() {
        return enableHistory;
    }

    /**
     * Sets the history to the value provided
     *
     * @param historyEnabled indicates if the history must be activated or deactivated
     */
    public void setHistoryEnabled(boolean historyEnabled) {
        this.enableHistory = historyEnabled;

        if(!historyEnabled)
            clearHistory();
    }

    /**
     * Returns the available algorithms for the platform
     *
     * @return a Set of String in which each of those is a supported algorithm
     */
    public static Set<String> getAllAlgorithms() {
        return Security.getAlgorithms("MessageDigest");
    }

    /**
     * Indicates whether the FlatLaf modern theme is enabled
     *
     * @return a boolean indicating so
     */
    public boolean isFlatlafEnabled() {
        return enableFlatlaf;
    }

    /**
     * Enables or disabled Flatlaf modern theme
     *
     * @param enableFlatlaf indicates whether the modern theme
     *                      has to be activated or deactivated
     */
    public void setEnableFlatlaf(boolean enableFlatlaf, MainFrame mainFrame) {
        this.enableFlatlaf = enableFlatlaf;

        // let's set the look and feel to the default one
        if(!enableFlatlaf && mainFrame != null) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(mainFrame);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(enableFlatlaf) {
            reloadFlatlaf();
        }
    }

    /**
     * Reloads flatlaf theme
     */
    public void reloadFlatlaf() {
        if(!enableFlatlaf)
            return;

        if(darkTheme)
            FlatDarculaLaf.setup();
        else
            FlatLightLaf.setup();

        FlatLaf.updateUI();
    }
}
