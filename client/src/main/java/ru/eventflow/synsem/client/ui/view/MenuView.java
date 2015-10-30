package ru.eventflow.synsem.client.ui.view;

import javax.swing.*;

public class MenuView extends JMenuBar {

    public static final String ITEM_FORMAT = "%-20s";
    private static final ImageIcon PLACEHOLDER_ICON = icon("placeholder.png");
    private final JMenuItem exitMenuItem;
    private final JMenuItem aboutMenuItem;


    public MenuView() {
        setBorder(BorderFactory.createEmptyBorder());

        JMenu fileMenu = new JMenu("File");
        addDisabledMenuItem(fileMenu, "Settings");
        exitMenuItem = addMenuItem(fileMenu, "Exit");
        add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        addDisabledMenuItem(editMenu, "Undo", icon("undo.png"));
        addDisabledMenuItem(editMenu, "Undo", icon("redo.png"));
        editMenu.add(new JSeparator());
        addDisabledMenuItem(editMenu, "Cut");
        addDisabledMenuItem(editMenu, "Copy");
        addDisabledMenuItem(editMenu, "Paste");
        addDisabledMenuItem(editMenu, "Delete");
        add(editMenu);

        // like in CoqIDE
        JMenu navigationMenu = new JMenu("Navigation");
        addDisabledMenuItem(navigationMenu, "Forward", icon("forward.png"));
        addDisabledMenuItem(navigationMenu, "Backward", icon("backward.png"));
        addDisabledMenuItem(navigationMenu, "Go to");
        addDisabledMenuItem(navigationMenu, "Start");
        addDisabledMenuItem(navigationMenu, "End");
        addDisabledMenuItem(navigationMenu, "Interrupt");
        addDisabledMenuItem(navigationMenu, "Hide");
        add(navigationMenu);

        JMenu tacticsMenu = new JMenu("Tactics");
        addDisabledMenuItem(tacticsMenu, "simpl");
        addDisabledMenuItem(tacticsMenu, "reflexivity");
        addDisabledMenuItem(tacticsMenu, "admit");
        add(tacticsMenu);

        JMenu helpMenu = new JMenu("Help");
        addDisabledMenuItem(helpMenu, "Manual");
        helpMenu.add(new JSeparator());
        aboutMenuItem = addMenuItem(helpMenu, "About");
        add(helpMenu);
    }

    private static JMenuItem addMenuItem(JMenu menu, String title, ImageIcon imageIcon, boolean enabled) {
        JMenuItem menuItem = new JMenuItem(String.format(ITEM_FORMAT, title), imageIcon);
        menuItem.setEnabled(enabled);
        menu.add(menuItem);
        return menuItem;
    }

    private static ImageIcon icon(String name) {
        return new ImageIcon(ClassLoader.getSystemResource("images/" + name));
    }

    private static JMenuItem addMenuItem(JMenu menu, String title) {
        return addMenuItem(menu, title, PLACEHOLDER_ICON, true);
    }

    private static JMenuItem addDisabledMenuItem(JMenu menu, String title) {
        return addMenuItem(menu, title, PLACEHOLDER_ICON, false);
    }

    private static JMenuItem addDisabledMenuItem(JMenu menu, String title, ImageIcon imageIcon) {
        return addMenuItem(menu, title, imageIcon, false);
    }

    public JMenuItem getExitMenuItem() {
        return exitMenuItem;
    }

    public JMenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }

}
