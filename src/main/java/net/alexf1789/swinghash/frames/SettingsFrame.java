package net.alexf1789.swinghash.frames;

import net.alexf1789.swinghash.panels.AlgoComboBox;
import net.alexf1789.swinghash.services.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SettingsFrame extends JFrame {
    
    private Settings settings;
    private boolean changed;
    private MainFrame mainFrame;

    // inputs
    private JCheckBox enableHistory;
    private JTextField historySize;
    private AlgoComboBox algorithms;

    public SettingsFrame(Settings settings, MainFrame mainFrame) {
        this.settings = settings;
        setUnchanged();
        this.mainFrame = mainFrame;
        
        // let's set the frame settings
        setTitle("Settings");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // let's add the window components
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0;

        // let's add the enable history checkbox
        gbc.weightx = 0.1;
        add(new JLabel("Enable history"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        enableHistory = new JCheckBox();
        enableHistory.addActionListener(action -> historyToggle());
        add(enableHistory, gbc);

        // let's add the history size
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        add(new JLabel("History size"), gbc);


        gbc.gridx = 1;
        gbc.weightx = 0.9;
        historySize = new JTextField();
        historySize.addFocusListener(new FocusAdapter() {
            private String oldText;

            @Override
            public void focusGained(FocusEvent e) {
                oldText = historySize.getText();
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(!historySize.getText().equals(oldText)) {
                    setChanged();

                    try {
                        settings.setHistorySize(Integer.parseInt(historySize.getText()));
                    } catch(NumberFormatException er) {
                        settings.setHistorySize(Settings.DEFAULT_HISTORY_SIZE);
                    }
                }
            }
        });

        add(historySize, gbc);

        // let's add the algorithms selection
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.1;
        add(new JLabel("Algorithms"), gbc);

        gbc.gridx++;
        gbc.weightx = 0.9;
        algorithms = new AlgoComboBox(this);

        add(algorithms, gbc);

        // let's add the buttons
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.5;

        JButton restoreButton = new JButton("Restore");
        restoreButton.addActionListener(event -> restoreFromSettings());

        add(restoreButton, gbc);

        gbc.gridx++;
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(event -> close());

        add(closeButton, gbc);

        // let's prepare and show the window
        restoreFromSettings();

        pack();
        setVisible(true);
    }

    /**
     * Shows the save the current changes dialog
     *
     * @return a boolean representing the user input (true for save, false for discard)
     */
    private boolean showSaveDialog() {
        return JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to save the current settings?",
                    "Save",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                ) == 0;
    }

    /**
     * Closes the settings window eventually reloading the main frame
     */
    private void close() {
        if(changed) {
            if(showSaveDialog())
                settings.save();

            mainFrame.reloadWindow();
        }

        dispose();
    }

    /**
     * Handles the history toggle state change
     */
    private void historyToggle() {
        setChanged();

        // let's update the UI
        if(enableHistory.isSelected()) {
            historySize.setEnabled(true);
        } else {
            historySize.setText("");
            historySize.setEnabled(false);
        }

        // let's update the settings
        settings.setHistoryEnabled(enableHistory.isSelected());

        try {
            settings.setHistorySize(Integer.parseInt(historySize.getText()));
        } catch(NumberFormatException e) {
            settings.setHistorySize(Settings.DEFAULT_HISTORY_SIZE);
        }
    }

    /**
     * Sets the changed flag, making appear a <i>do you want to save</i> popup before closing
     */
    public void setChanged() {
        changed = true;
    }

    /**
     * Unsets the changed flag
     */
    private void setUnchanged() {
        changed = false;
    }

    /**
     * Restores the visual commands from the settings previous (or actual value)
     */
    private void restoreFromSettings() {
        if(changed) {
            // to avoid reload at first run
            settings = Settings.reloadSettings();
            setUnchanged();
        }

        enableHistory.setSelected(settings.isHistoryEnabled());

        if(settings.isHistoryEnabled()) {
            historySize.setEnabled(true);
            historySize.setText(String.valueOf(settings.getHistorySize()));
        } else {
            historySize.setEnabled(false);
        }

    }
    
}
