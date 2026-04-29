package net.alexf1789.swinghash.frames;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarculaLaf;

import net.alexf1789.swinghash.panels.InputPanel;
import net.alexf1789.swinghash.panels.MenuBar;

/**
 * Main frame containing the input, verify and output panels and used by the user
 * to perform the standard hashing operations
 */
public class MainFrame extends JFrame {
    
    private Set<String> algorithms;
    private InputPanel inputPanel;

    public MainFrame() {
        // let's set the core settings of the frame
        setTitle("SwingHash");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        defineBaseAlgorithms();
        createMenuBar();
        
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
        setSize(500, 500);
        setVisible(true);
    }
    
    private void defineBaseAlgorithms() {
        algorithms = new HashSet<String>(5);
        
        algorithms.add("MD5");
        algorithms.add("SHA-256");
        algorithms.add("SHA-512");
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new MenuBar()
                .withSubMenu("File", 'F')
                    .with(new JCheckBoxMenuItem("MD5", algorithms.contains("MD5")))
                    .done()
                .withSubMenu("View", 'V')
                    .with(new JMenuItem("File"))
                        .performingAction(e -> setToFileMode())
                    .done()
                    .with(new JMenuItem("Text"))
                        .performingAction(e -> setToTextMode())
                    .done()
                .withSubMenu("Help", 'H')
                    .with(new JMenuItem("About"))
                        .performingAction(e -> SwingUtilities.invokeLater(AboutFrame::new))
                    .done()
                .getJMenuBar();
        
        setJMenuBar(menuBar);
    }
    
    private void setToTextMode() {
        inputPanel.updateSelectionMode(true);
    }
    
    private void setToFileMode() {
        inputPanel.updateSelectionMode(false);        
    }
    
}
