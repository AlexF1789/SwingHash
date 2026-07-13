package net.alexf1789.swinghash.panels;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;

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
     * @param addToMain specifies if the submenu has to be added in the root level of MenuBar
     * @return the MenuBar itself to allow a fluent design
     */
    public MenuBar withSubMenu(String name, Character mnemonic, boolean addToMain) {
        JMenu submenu = new JMenu(name);
        
        if(mnemonic != null)
            submenu.setMnemonic(mnemonic);
        
        if(addToMain)
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
