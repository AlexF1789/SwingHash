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
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // let's show the window
        pack();
        setVisible(true);
    }

    private void createTable() {
        HistoryTable historyTable = new HistoryTable(settings);
        table = new JTable(historyTable.getEntries(), historyTable.getColumns());
    }

}
