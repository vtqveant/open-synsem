package ru.eventflow.synsem.client.ui.view;

import javax.swing.*;
import java.awt.*;

public class ContainerView extends JPanel {

    private final JLabel placeholderLabel;

    public ContainerView() {
        setLayout(new BorderLayout());

        placeholderLabel = new JLabel("Nothing yet");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        placeholderLabel.setVerticalAlignment(SwingConstants.CENTER);
        placeholderLabel.setBorder(BorderFactory.createEmptyBorder());
        placeholderLabel.setBackground(Color.LIGHT_GRAY);
        placeholderLabel.setOpaque(true);
        add(placeholderLabel, BorderLayout.CENTER);
    }

}
