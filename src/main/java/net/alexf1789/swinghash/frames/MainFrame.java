package net.alexf1789.swinghash.frames;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.icons.FlatAnimatedIcon;

import net.alexf1789.swinghash.panels.InputPanel;
import net.alexf1789.swinghash.panels.MenuBar;
import net.alexf1789.swinghash.panels.OutputPanel;
import net.alexf1789.swinghash.services.Hasher;

/**
 * Main frame containing the input, verify and output panels and used by the user
 * to perform the standard hashing operations
 */
public class MainFrame extends JFrame {
    
    private Set<String> algorithms;
    private InputPanel inputPanel;
    private OutputPanel outputPanel;

    public MainFrame() {
        // let's set the core settings of the frame
        setTitle("SwingHash");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        defineBaseAlgorithms();
        createMenuBar();
        
        inputPanel = new InputPanel(true);
        outputPanel = new OutputPanel(algorithms);
        
        // let's set the layout settings
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // let's add the input panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.45;
        
        add(inputPanel, gbc);
        
        // let's add the output panel
        gbc.gridy++;
        add(outputPanel, gbc);
        
        // let's add the buttons
        JButton clearButton = new JButton("Clear");
        JButton computeButton = new JButton("Compute");
        
        clearButton.addActionListener(event -> clear());
        computeButton.addActionListener(event -> computeHashes());
        
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0.1;
        gbc.gridwidth = 1;
        gbc.gridy++;
        
        add(clearButton, gbc);
        
        gbc.gridx++;        
        add(computeButton, gbc);
        
        // let's define the size and set the frame as visible
        setSize(500, 500);
        setVisible(true);
    }
    
    /**
     * Defines the algorithms the hash will be computed by
     */
    private void defineBaseAlgorithms() {
        algorithms = new HashSet<String>(5);
        
        algorithms.add("MD5");
        algorithms.add("SHA-256");
        algorithms.add("SHA-512");
    }
    
    /**
     * Creates the menu bar for the frame and adds it
     */
    private void createMenuBar() {
        JMenuBar menuBar = new MenuBar()
                .withSubMenu("File", 'F', true)
                    .with(new JMenuItem("Compute"))
                        .performingAction(e -> computeHashes())
                    .done()
                    .with(new JMenuItem("Clear"))
                        .performingAction(e -> clear())
                    .done()
                .withSubMenu("View", 'V', true)
                    .with(new JMenuItem("File mode"))
                        .performingAction(e -> setToFileMode())
                    .done()
                    .with(new JMenuItem("Text mode"))
                        .performingAction(e -> setToTextMode())
                    .done()
                    .withSubMenu("Theme", 'T', false)
                        .with(new JMenuItem("Dark"))
                            .performingAction(e -> {
                                FlatDarculaLaf.setup();
                                FlatLaf.updateUI();
                            })
                        .done()
                        .with(new JMenuItem("Light"))
                            .performingAction(e -> {
                                FlatLightLaf.setup();
                                FlatLaf.updateUI();
                            })
                        .done()
                    .done()
                .withSubMenu("Help", 'H', true)
                    .with(new JMenuItem("About"))
                        .performingAction(e -> SwingUtilities.invokeLater(AboutFrame::new))
                    .done()
                .getJMenuBar();
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Sets the input view to text mode, meaning that no file chooser is shown and the resource that
     * will be produced is a StringResource
     */
    private void setToTextMode() {
        inputPanel.updateSelectionMode(true);
    }
    
    /**
     * Sets the input view to file mode, meaning that the file chooser is shown and the resource that
     * will be produced is a FileResource
     */
    private void setToFileMode() {
        inputPanel.updateSelectionMode(false);        
    }
    
    /**
     * Clears the input and output fields
     */
    private void clear() {
        inputPanel.clearFields();
        outputPanel.clearFields();
    }
    
    /**
     * Starts the hash computation and shows them in the output panel eventually highlighting the verify
     * and correct checksum text field
     */
    private void computeHashes() {
        try {

            Hasher hasher = new Hasher(algorithms, inputPanel.getResource());
            hasher.compute();
            
            outputPanel.updateResults(hasher);
            
            String expectedHash = inputPanel.getExpectedHash();
            if(expectedHash == null)
                return;
            
            String correctAlgo = hasher.validate(expectedHash);
            if(correctAlgo != null) {
                outputPanel.setCorrect(correctAlgo);
                inputPanel.updateHashCorrect();
            } else
                inputPanel.updateHashWrong();
            
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "The hashes could not be computed due to the following error: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
