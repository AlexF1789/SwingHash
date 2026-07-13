package net.alexf1789.swinghash.services;

import net.alexf1789.swinghash.models.HistoryHash;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HistoryTable {

    private List<HistoryHash> historyHashes;
    private String[] columns;
    private String[][] entries;

    public HistoryTable(Settings settings) {
        historyHashes = new ArrayList<>(settings.getHistory());
        determineColumns();
        determineEntries();
    }

    /**
     * Determines the columns for the table model
     */
    private void determineColumns() {
        Set<String> algos = historyHashes.stream()
            .map(HistoryHash::getHashes)
            .map(Map::keySet)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());

        columns = new String[algos.size() + 1];
        columns[0] = "Resource";

        int i = 1;
        for(String algo : algos)
            columns[i++] = algo;
    }

    /**
     * Determines the entries of the matrix
     */
    private void determineEntries() {
        entries = new String[historyHashes.size()][columns.length];

        // for each file let's create the sub-array containing all the fields
        for(int i=0; i<historyHashes.size(); i++) {
            entries[i] = new String[columns.length];
            entries[i][0] = historyHashes.get(i).getResource();

            Map<String, String> hashes = historyHashes.get(i).getHashes();

            // for each algo let's populate the table
            for(int j=1; j<columns.length; j++)
                entries[i][j] = hashes.get(columns[j]);
        }
    }

    public String[] getColumns() {
        return columns;
    }

    public String[][] getEntries() {
        return entries;
    }

}
