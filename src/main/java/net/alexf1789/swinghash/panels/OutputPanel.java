package net.alexf1789.swinghash.panels;

import com.formdev.flatlaf.FlatClientProperties;
import net.alexf1789.swinghash.models.HistoryHash;
import net.alexf1789.swinghash.services.Hasher;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Panel which contains the output results of the hash computation with a line for each
 * computed hash and its label
 */
public class OutputPanel extends JPanel {
    
    private HashMap<String, JTextField> hashes;
    
    public OutputPanel(Collection<String> hashes) {
        // let's create the textfields dictionary
        this.hashes = new HashMap<>(hashes.size());
        
        for(String hash : hashes) {
            JTextField textField = new JTextField();
            
            textField.setToolTipText(hash);
            //textField.setEnabled(false);
            
            this.hashes.put(hash, textField);
        }
        
        // let's create the border with the title
        setBorder(BorderFactory.createTitledBorder("Hashes"));
        
        // let's create the layout and its constraints
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.gridx = 0;
        gbc.gridy = -1;
        gbc.gridheight = 1;
        gbc.weighty = 1.0;
        
        for(Entry<String, JTextField> entry : this.hashes.entrySet()) {
            JLabel label = new JLabel(entry.getKey());
            
            // let's put the label in the next row at x position 0 taking 1 column with weight 0.1
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.weightx = 0.1;
            
            add(label, gbc);
            
            // let's put the textfield in the second column taking 0.9 space
            gbc.gridx++;
            gbc.gridwidth = 3;
            gbc.weightx = 0.9;
            
            add(entry.getValue(), gbc);
        }
    }
    
    /**
     * Clears the output text fields that normally show the result of the computation of the
     * resource hashes
     */
    public void clearFields() {
        hashes.values().parallelStream()
            .forEach(textField -> {
                textField.setText("");
                textField.putClientProperty(FlatClientProperties.OUTLINE, null);
            });
    }
    
    /**
     * Updates the panel with the computed hashes
     * 
     * @param hasher is the Hasher service instance corresponding to the current input
     * @throws InterruptedException if the process is interrupted while the hashes are being computed
     */
    public void updateResults(Hasher hasher) throws InterruptedException {
        for(Entry<String, String> hash : hasher.getHashesResult().entrySet()) {
            JTextField textField = this.hashes.get(hash.getKey());
            
            if(textField != null)
                textField.setText(hash.getValue());
        }
    }
    
    /**
     * Sets a text field representing a computed hash as correct
     * 
     * @param algorithm is the algorithm corresponding to the correct hash
     * @return true if the algorithm was found and the text field highlighted, false otherwise
     */
    public boolean setCorrect(String algorithm) {
        JTextField correctHash = this.hashes.get(algorithm);
        
        if(correctHash == null)
            return false;
        
        correctHash.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_SUCCESS);
        return true;
    }
    
    /**
     * Restores an Hasher from the history into the text fields
     * 
     * @param hasher is the HistoryHash to restore
     */
    public void restoreFromHistory(HistoryHash hasher) {
        // let's check if the hasher exists
        if(hasher == null)
            return;
        
        // let's populate the correct text fields
        hasher.getHashes().entrySet().parallelStream()
            .forEach(entry -> {
                JTextField textField = hashes.get(entry.getKey());
                
                if(textField != null)
                    textField.setText(entry.getValue());
            });
    }
    
}
