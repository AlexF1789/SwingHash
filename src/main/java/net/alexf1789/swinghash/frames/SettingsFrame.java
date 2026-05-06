package net.alexf1789.swinghash.frames;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.alexf1789.swinghash.services.Settings;

public class SettingsFrame extends JFrame {
    
    private Settings settings;
    
    public SettingsFrame(Settings settings) {
        this.settings = settings;
        
        // let's set the frame settings
        setTitle("Settings");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // let's add the save settings behavior on closing
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(showSaveDialog())
                    settings.save();
            }
        });
        
        setSize(300, 300);
        setVisible(true);
    }
    
    private boolean showSaveDialog() {
        return JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to save the current settings?",
                    "Save",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                ) == 1;
    }
    
}
