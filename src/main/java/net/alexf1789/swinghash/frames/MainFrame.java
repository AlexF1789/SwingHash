package net.alexf1789.swinghash.frames;

import net.alexf1789.swinghash.Main;
import net.alexf1789.swinghash.models.HistoryHash;
import net.alexf1789.swinghash.panels.InputPanel;
import net.alexf1789.swinghash.panels.MenuBar;
import net.alexf1789.swinghash.panels.OutputPanel;
import net.alexf1789.swinghash.services.Hasher;
import net.alexf1789.swinghash.services.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

/**
 * Main frame containing the input, verify and output panels and used by the user
 * to perform the standard hashing operations
 */
public class MainFrame extends JFrame {
    
    private InputPanel inputPanel;
    private OutputPanel outputPanel;
    private Settings settings;

    public MainFrame(Settings settings, boolean reloadLastHash) {
        this.settings = settings;
        settings.reloadFlatlaf();
        
        // let's set the core settings of the frame
        setTitle("SwingHash");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setApplicationIcon();
        
        createMenuBar();
        
        inputPanel = new InputPanel(settings.isTextMode());
        outputPanel = new OutputPanel(settings.getAlgorithms());
        
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
        
        // let's save the settings before closing
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                settings.save();
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                settings.save();
            }
        });
        
        // let's define the size and set the frame as visible
        pack();
        Dimension currentSize = getSize();
        currentSize.width = 500;
        
        setSize(currentSize);
        setVisible(true);

        if(reloadLastHash)
            restoreLastHash();
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
                    .with(new JMenuItem("Restore last"))
                        .performingAction(e -> restoreLastHash())
                    .done()
                    .with(new JMenuItem("Settings"))
                        .performingAction(e -> SwingUtilities.invokeLater(() -> new SettingsFrame(settings, this)))
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
                            .performingAction(e -> settings.setDarkTheme(true))
                        .done()
                        .with(new JMenuItem("Light"))
                            .performingAction(e -> settings.setDarkTheme(false))
                        .done()
                    .done()
                    .with(new JMenuItem("History"))
                        .performingAction(e -> SwingUtilities.invokeLater(() -> new HistoryFrame(settings)))
                    .done()
                .withSubMenu("Help", 'H', true)
                    .with(new JMenuItem("About"))
                        .performingAction(e -> SwingUtilities.invokeLater(AboutFrame::new))
                    .done()
                    .with(new JMenuItem("Reload"))
                        .performingAction(e -> reloadWindow())
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
        settings.setTextMode(true);
    }
    
    /**
     * Sets the input view to file mode, meaning that the file chooser is shown and the resource that
     * will be produced is a FileResource
     */
    private void setToFileMode() {
        inputPanel.updateSelectionMode(false);
        settings.setTextMode(false);
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
            // let's create the hasher instance and compute the hashes in an explicit way
            Hasher hasher = new Hasher(settings.getAlgorithms(), inputPanel.getResource(), inputPanel.getExpectedHash());
            hasher.compute();
            
            // let's update the output panel and the chronology
            outputPanel.updateResults(hasher);
            settings.addToHistory(hasher);
            
            // if hasher has not expected hash let's return immediately
            if(!hasher.hasExpectedHash())
                return;
            
            // let's validate the input according to its being correct
            // in respect to the computed hashes
            String correctAlgo = hasher.validate();
            if(correctAlgo != null) {
                outputPanel.setCorrect(correctAlgo);
                inputPanel.updateHashCorrect();

                // if Flatlaf is not enabled we have to show a notification
                if(!settings.isFlatlafEnabled())
                    showCorrectHashNotification(correctAlgo);

            } else {
                inputPanel.updateHashWrong();

                // if Flatlaf is not enabled we have to show a notification
                if(!settings.isFlatlafEnabled())
                    showWrongHashNotification();

            }
            
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "The hashes could not be computed due to the following error: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows the user that the hash computed with the specified algorithm matches the provided one
     *
     * @param algo is the algorithm matching the specified one
     */
    private void showCorrectHashNotification(String algo) {
        JOptionPane.showMessageDialog(
            this,
            String.format("The hash computed with the algorithm %s matches the specified one!", algo),
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Shows the user that none of the computed hashes match the provided one
     */
    private void showWrongHashNotification() {
        JOptionPane.showMessageDialog(
            this,
            "No algorithm provided the specified hash!",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Informs the user that the computed hash matches the provided one, but it's not shown since
     * settings have changed in the meantime
     *
     * @param algo is the algorithm matching the specified hash
     */
    private void showNotPresentAlgoNotification(String algo) {
        JOptionPane.showMessageDialog(
            this,
            String.format("The hash computed with the algorithm %s matches the specified one! Anyway it's not shown becase the configuration changed in the meantime", algo),
            "Warning",
            JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Sets the application icon in the system trail
     */
    private void setApplicationIcon() {
        URL iconUrl = this.getClass().getResource(Main.ICON_PATH);
        
        if(iconUrl == null)
            return;
        
        ImageIcon icon = new ImageIcon(iconUrl);
        setIconImage(icon.getImage());
    }
    
    /**
     * Restores the last hash computed view in the application
     */
    private void restoreLastHash() {
        HistoryHash lastHash = settings.getLastHashComputed();
        
        // if there is no hash let's not do anything
        if(lastHash == null)
            return;
        
        // let's restore the state of input and output panel
        inputPanel.restoreFromHistory(lastHash);
        outputPanel.restoreFromHistory(lastHash);
        
        if(lastHash.getExpected() == null)
            return;
        
        // let's highlight the correct hash or wrong input
        String correctAlgo = lastHash.getCorrectAlgo();
        
        // if the correct algorithm was not found let's show it in the input
        if(correctAlgo == null) {
            inputPanel.updateHashWrong();

            if(!settings.isFlatlafEnabled())
                showWrongHashNotification();

            return;
        }
        
        // if we can highlight the algorithm let's put it in green
        if(outputPanel.setCorrect(correctAlgo)) {
            inputPanel.updateHashCorrect();

            if(!settings.isFlatlafEnabled())
                showCorrectHashNotification(correctAlgo);

            return;
        }
        
        // otherwise let's show an error and highlight in orange the input with a tooltip
        inputPanel.updateHashWarning(correctAlgo);
        showNotPresentAlgoNotification(correctAlgo);
    }

    /**
     * Reloads the main frame by opening a new one and closing this instance
     */
    public void reloadWindow() {
        SwingUtilities.invokeLater(() -> new MainFrame(Settings.reloadSettings(), true));
        dispose();
    }
    
}
