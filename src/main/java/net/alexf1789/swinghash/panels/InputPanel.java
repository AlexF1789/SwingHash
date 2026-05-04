package net.alexf1789.swinghash.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.SystemFileChooser;

import net.alexf1789.swinghash.models.FileResource;
import net.alexf1789.swinghash.models.Resource;
import net.alexf1789.swinghash.models.StringResource;

/**
 * Input panel containing the logic to determine the resource type to hash and the verify
 * text field to optionally verify it
 */
public class InputPanel extends JPanel {
    
    private JTextField textInput, verifyInput;
    private JLabel inputLabel, verifyLabel;
    private SystemFileChooser fileChooser;
    private JButton chooseFileButton;
    private boolean textMode;
    
    public InputPanel(boolean textMode) {
        // let's initialize the core components
        this.textMode = !textMode;
        initializeComponents();
        updateSelectionMode(textMode);
        
        verifyInput = new JTextField();
        verifyLabel = new JLabel("Verify");
        
        // let's initialize the layout
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // input label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        
        add(inputLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0.8;
        
        add(textInput, gbc);
        
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        add(chooseFileButton, gbc);
        
        // verify label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.1;
        
        add(verifyLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 0.9;
        
        add(verifyInput, gbc);
        
        // let's display the component
        setVisible(true);
    }
    
    /**
     * Initialize the components of the panel
     */
    private void initializeComponents() {
        textInput = new JTextField();
        
        fileChooser = new SystemFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        
        inputLabel = new JLabel();
        
        chooseFileButton = new JButton("Choose...");
        chooseFileButton.addActionListener(event -> choiceFile());
    }
    
    /**
     * Updates the panel to display the correct modality
     * @param textMode
     */
    public void updateSelectionMode(boolean textMode) {
        if(textMode == this.textMode)
            return;
        
        this.textMode = textMode;
        
        // let's empty the text field content
        textInput.setText("");
        
        // let's define the tooltip text and add it
        String tooltip = textMode ? "Text to hash" : "File path";
        
        textInput.setToolTipText(tooltip);
        inputLabel.setText(tooltip);
        
        // let's update the border
        setBorder(BorderFactory.createTitledBorder(textMode ? "Hash a text" : "Hash a file"));
        
        // let's create the choice file button
        this.chooseFileButton.setVisible(!textMode);
    }

    /**
     * Returns the input resource specified in the panel
     * 
     * @return a Resource which can be a String or a File
     */
    public Resource getResource() {
        String content = textInput.getText();
        return textMode ? new StringResource(content) : new FileResource(content);
    }
    
    /**
     * Shows the file chooser input and, if confirmed adds it as a path in the input field
     */
    private void choiceFile() {
        // let's show the file and check if the user pressed the OK button
        if(fileChooser.showOpenDialog(this) == SystemFileChooser.APPROVE_OPTION) {
            textInput.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * Clears the fields and resets the eventual highlighting of the verify field
     */
    public void clearFields() {
        textInput.setText("");
        verifyInput.setText("");

        verifyInput.putClientProperty(FlatClientProperties.OUTLINE, null);
    }
    
    /**
     * Returns the expected hash, meaning the one contained in the verify text input field
     * 
     * @return a String representing the hash the user expects, eventually null if none
     */
    public String getExpectedHash() {
        String expectedHash = this.verifyInput.getText();
        
        if(expectedHash.isEmpty())
            return null;
        
        return expectedHash;
    }
    
    /**
     * Updates the verify hash field to have a green border according to its being considered correct
     */
    public void updateHashCorrect() {
        verifyInput.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_SUCCESS);
    }
    
    /**
     * Updates the verify hash field to have a red border according to its being considered wrong
     */
    public void updateHashWrong() {
        verifyInput.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
    }
    
}
