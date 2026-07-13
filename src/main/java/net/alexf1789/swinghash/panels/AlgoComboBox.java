package net.alexf1789.swinghash.panels;

import net.alexf1789.swinghash.frames.SettingsFrame;
import net.alexf1789.swinghash.services.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class AlgoComboBox extends JPanel {

    private Settings settings;
    private SettingsFrame settingsFrame;
    private JComboBox<String> comboBox;

    public AlgoComboBox(SettingsFrame settingsFrame) {
        this.settings = Settings.getSettings();
        this.settingsFrame = settingsFrame;

        resetAlgos();
        setRenderer();

        add(comboBox);
    }

    /**
     * Resets the algorithms shown in the combobox
     */
    private void resetAlgos() {
        Set<String> availableAlgos = Settings.getAllAlgorithms();
        String[] allAlgos = new String[availableAlgos.size()];

        comboBox = new JComboBox<>(availableAlgos.toArray(allAlgos));
    }

    /**
     * Sets the custom renderer of the combobox to match the list of checkboxes model
     */
    private void setRenderer() {
        comboBox.setRenderer(new ListCellRenderer<String>() {
            private JCheckBox checkBox = new JCheckBox();

            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                if(value != null) {
                    checkBox.setText(value);
                    checkBox.setSelected(settings.isAlgoSelected(value));
                }

                return checkBox;
            }
        });

        comboBox.addActionListener(e -> {
            String algo = (String) comboBox.getSelectedItem();

            if(algo != null) {
                settingsFrame.setChanged();

                if(settings.isAlgoSelected(algo))
                    settings.removeSelectedAlgo(algo);
                else
                    settings.addSelectedAlgo(algo);

                comboBox.repaint();
            }
        });
    }

}
