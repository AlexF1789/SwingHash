package net.alexf1789.swinghash;

import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatLightLaf;

import net.alexf1789.swinghash.frames.MainFrame;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // let's apply the FlatLightLaf look and feel to the application
        FlatLightLaf.setup();
        
        // let's create the main application frame
        SwingUtilities.invokeLater(MainFrame::new);
    }

}
