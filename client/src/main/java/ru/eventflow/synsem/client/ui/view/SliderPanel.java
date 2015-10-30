package ru.eventflow.synsem.client.ui.view;


import ru.eventflow.synsem.client.ui.Defaults;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class SliderPanel extends JPanel {

    public SliderPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(getTitle());
        titleLabel.setFont(Defaults.SMALL_FONT);
        titleLabel.setForeground(Color.GRAY);

        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.LINE_AXIS));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 3, 2));
        headingPanel.add(titleLabel);
        headingPanel.add(Box.createHorizontalGlue());
        headingPanel.add(Box.createRigidArea(new Dimension(0, 18)));

        List<AbstractButton> buttons = getButtons();
        if (buttons != null) {
            for (AbstractButton button : buttons) {
                headingPanel.add(button);
                headingPanel.add(Box.createRigidArea(new Dimension(1, 0)));
            }
        }

        add(headingPanel, BorderLayout.PAGE_START);
    }

    /**
     * This is called in the constructor
     */
    abstract public ImageIcon getIcon();

    /**
     * This is called in the constructor
     */
    abstract public String getTitle();

    /**
     * These buttons are displayed on the right of the slider panel header.
     * This is called in the constructor.
     */
    abstract public List<AbstractButton> getButtons();
}
