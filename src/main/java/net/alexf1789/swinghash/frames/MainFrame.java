package net.alexf1789.swinghash.frames;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import net.alexf1789.swinghash.panels.InputPanel;

/**
 * Main frame containing the input, verify and output panels and used by the user
 * to perform the standard hashing operations
 */
public class MainFrame extends JFrame {
    
    private InputPanel inputPanel;

    public MainFrame() {
        // let's set the core settings of the frame
        setTitle("SwingHash");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        inputPanel = new InputPanel(true);
        
        // let's set the layout settings
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // let's add the components to the frame
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        add(inputPanel, gbc);
        
        // let's define the size and set the frame as visible
        pack();
        setVisible(true);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
    }
    
}
