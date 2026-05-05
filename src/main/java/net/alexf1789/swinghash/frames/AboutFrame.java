package net.alexf1789.swinghash.frames;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.alexf1789.swinghash.Main;

public class AboutFrame extends JFrame {
    
    private JLabel title, description, icon, javaVersion, bytecodeVersion;
    private JButton seeGithub, closeButton;
    
    public AboutFrame() {
        setTitle("About");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        defineComponents();
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        if(icon != null) {
            gbc.gridwidth = 1;
            add(icon, gbc);
            
            gbc.gridx++;
        } else
            gbc.gridwidth = 2;
        
        add(title, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(description, gbc);
        
        
        JLabel javaVersionTitle = new JLabel("Running on:", JLabel.LEFT);
        javaVersionTitle.setFont(javaVersionTitle.getFont().deriveFont(Font.BOLD));
        gbc.gridy++;
        gbc.gridwidth = 1; 
        add(javaVersionTitle, gbc);
        
        gbc.gridx++;
        add(javaVersion, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(seeGithub, gbc);
        
        gbc.gridx++;
        add(closeButton, gbc);
        
        pack();
        setVisible(true);
    }
    
    /**
     * Defines the core components used in the frame
     */
    private void defineComponents() {
        URL iconUrl = getClass().getResource(Main.ICON_PATH);
        
        if(iconUrl != null) {
            ImageIcon appIcon = new ImageIcon(iconUrl);
            icon = new JLabel(appIcon, JLabel.RIGHT);
        } else
            icon = null;
        
        title = new JLabel("SwingHash", JLabel.LEFT);
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        
        description = new JLabel("A simple program to compute hashes in Java", JLabel.LEFT);
        
        seeGithub = new JButton("GitHub");
        closeButton = new JButton("Close");
        
        seeGithub.addActionListener(event -> {
            try {
                Desktop desktop = Desktop.getDesktop();
                URI githubUrl = new URI(Main.GITHUB_URL);
                
                desktop.browse(githubUrl);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                            this,
                            "An error occurred while trying to open the GitHub repository at "+Main.GITHUB_URL,
                            "Erorr",
                            JOptionPane.ERROR
                        );
            }
        });
        
        closeButton.addActionListener(event -> dispose());
        
        javaVersion = new JLabel(System.getProperty("java.version"), JLabel.LEFT);
    }
    
}
