package net.alexf1789.swinghash;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import net.alexf1789.swinghash.frames.MainFrame;
import net.alexf1789.swinghash.services.Settings;

public class Main {
    
    public final static String GITHUB_URL = "https://github.com/AlexF1789/SwingHash";
    public final static String ICON_PATH = "/resources/icon.png";

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        Settings settings = Settings.getSettings();
        
        // let's apply the FlatLaf look and feel to the application
        if(settings.isDarkTheme())
            FlatDarkLaf.setup();
        else
            FlatLightLaf.setup();
        
        // let's create the main application frame
        SwingUtilities.invokeAndWait(() -> new MainFrame(settings));
    }

}
