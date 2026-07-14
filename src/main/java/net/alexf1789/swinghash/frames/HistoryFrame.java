package net.alexf1789.swinghash.frames;

import net.alexf1789.swinghash.services.HistoryTable;
import net.alexf1789.swinghash.services.Settings;

import javax.swing.*;
import java.awt.*;

public class HistoryFrame extends JFrame {

    private Settings settings;
    private JTable table;

    public HistoryFrame(Settings settings) {
        this.settings = settings;
        createTable();

        // let's set the window option
        setTitle("History");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // let's set the layout and the elements
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.weightx = 1.0;
        gbc.weighty = 0.9;
        gbc.gridwidth = 3;
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, gbc);

        gbc.gridy++;
        gbc.weighty = 0.1;
        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearHistory());

        add(clearButton, gbc);

        gbc.gridx = 2;
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, gbc);

        // let's show the window
        pack();
        setVisible(true);
    }

    private void createTable() {
        HistoryTable historyTable = new HistoryTable(settings);
        table = new JTable(historyTable.getEntries(), historyTable.getColumns());
    }

    private void clearHistory() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "By pressing yes you will permanently delete the history.",
            "Are you sure?",
            JOptionPane.YES_NO_OPTION
        );

        if(result == 0) {
            settings.clearHistory();
            settings.save();
            reloadWindow();
        }
    }

    private void reloadWindow() {
        SwingUtilities.invokeLater(() -> new HistoryFrame(Settings.reloadSettings()));
        dispose();
    }


}
