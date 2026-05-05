package net.alexf1789.swinghash;

import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatLightLaf;

import net.alexf1789.swinghash.frames.MainFrame;

public class Main {
    
    public final static String GITHUB_URL = "https://github.com/AlexF1789/SwingHash";
    public final static String ICON_PATH = "/resources/icon.png";

    public static void main(String[] args) throws InterruptedException {
        // let's apply the FlatLightLaf look and feel to the application
        FlatLightLaf.setup();
        
        // let's create the main application frame
        SwingUtilities.invokeLater(MainFrame::new);
    }

}
