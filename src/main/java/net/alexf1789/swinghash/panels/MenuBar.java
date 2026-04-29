package net.alexf1789.swinghash.panels;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar {

    private JMenuBar menuBar;
    private LinkedList<JMenuItem> items;
    
    /**
     * Creates a MenuBar fluent generator
     */
    public MenuBar() {
        menuBar = new JMenuBar();
        items = new LinkedList<JMenuItem>();
    }
    
    /**
     * Adds hierarchically an element to the MenuBar
     * 
     * @param element is the element to add
     * @return the MenuBar itself to allow a fluent design
     */
    public MenuBar with(JMenuItem element) {
        items.addFirst(element);
        
        return this;
    }
    
    /**
     * Adds a submenu from the specified name and mnemonic
     * 
     * @param name is the submenu name
     * @param mnemonic is the mnemonic characted (null for none)
     * @return the MenuBar itself to allow a fluent design
     */
    public MenuBar withSubMenu(String name, Character mnemonic) {
        JMenu submenu = new JMenu(name);
        
        if(mnemonic != null)
            submenu.setMnemonic(mnemonic);
        
        menuBar.add(submenu);
        items.addFirst(submenu);
        
        return this;
    }
    
    /**
     * Associates an action to the last added item
     * 
     * @param actionListener is the ActionListener to associate to the last element added
     * @return the MenuBar itself to allow a fluent design
     */
    public MenuBar performingAction(ActionListener actionListener) {
        items.getFirst().addActionListener(actionListener);
        return this;
    }
    
    /**
     * Marks the current element as done and selects the previously added one
     * 
     * @return the MenuBar itself to allow a fluent design
     */
    public MenuBar done() {
        JMenuItem menuItem = items.pop();
        items.getFirst().add(menuItem);
        
        return this;
    }
    
    /**
     * Returns the finally added JMenuBar
     * 
     * @return the JMenuBar
     */
    public JMenuBar getJMenuBar() {
        return menuBar;
    }
    
}
