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
import javax.swing.SwingUtilities;

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
        
        clearButton.addActionListener(event -> {
            outputPanel.clearFields();
            inputPanel.clearFields();
        });
        
        computeButton.addActionListener(event -> {
            try {
                computeHashes();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
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
    
    private void defineBaseAlgorithms() {
        algorithms = new HashSet<String>(5);
        
        algorithms.add("MD5");
        algorithms.add("SHA-256");
        algorithms.add("SHA-512");
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new MenuBar()
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
    
    private void computeHashes() throws InterruptedException {
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
    }
    
}
