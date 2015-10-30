package ru.eventflow.synsem.client.ui.view;

import ru.eventflow.synsem.client.ui.Defaults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainView extends JPanel {

    public static final int DIVIDER_SIZE = 3;
    public static final double DIVIDER_LOCATION = 0.75d;

    private final JSplitPane mainSplitPane;

    private final JLabel statusLabel;

    private final NoneSelectedButtonGroup group = new NoneSelectedButtonGroup();
    private final JPanel horizontalButtonsPanel = new JPanel();
    private final JPanel statusBarPanel;

    public MainView() {
        setLayout(new BorderLayout());

        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, null, null);
        mainSplitPane.setDividerLocation(DIVIDER_LOCATION);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setDividerSize(0);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setBorder(BorderFactory.createEmptyBorder());
        add(mainSplitPane, BorderLayout.CENTER);

        // to contain toggle buttons and a status bar at the bottom
        final JPanel bottomPanel = new JPanel(new BorderLayout());

        horizontalButtonsPanel.setLayout(new BoxLayout(horizontalButtonsPanel, BoxLayout.LINE_AXIS));
        horizontalButtonsPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 1, 2));
        horizontalButtonsPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(horizontalButtonsPanel, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(Defaults.SMALL_FONT);

        statusBarPanel = new JPanel();
        statusBarPanel.setLayout(new BoxLayout(statusBarPanel, BoxLayout.LINE_AXIS));
        statusBarPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 2, 6));
        statusBarPanel.add(statusLabel);
        bottomPanel.add(statusBarPanel, BorderLayout.PAGE_END);

        add(bottomPanel, BorderLayout.PAGE_END);
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public void displayAboutDialog() {
        AboutDialog dialog = new AboutDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
    }

    public void setStatusBarVisible(boolean visible) {
        statusBarPanel.setVisible(visible);
    }

    public void setTopPanel(JPanel panel) {
        mainSplitPane.setTopComponent(panel);
    }

    public void addSliderPanel(SliderPanel panel) {
        JToggleButton button = new JToggleButton(panel.getTitle());
        button.setFocusable(false);
        button.setIcon(panel.getIcon());
        button.setMaximumSize(new Dimension(60, 22));
        button.setMinimumSize(new Dimension(60, 22));
        button.setFont(Defaults.SMALL_FONT);
        button.addItemListener(new ToggleItemListener(panel));
        group.add(button);

        // add button to the left
        horizontalButtonsPanel.add(Box.createRigidArea(new Dimension(2, 0)), 0);
        horizontalButtonsPanel.add(button, 0);

        if (mainSplitPane.getBottomComponent() == null) {
            mainSplitPane.setBottomComponent(panel);
        }
        panel.setVisible(false);
    }

    /**
     * holds the sliding panel visibility status
     */
    private class ToggleItemListener implements ItemListener {

        private int location = -1;
        private JPanel panel;

        public ToggleItemListener(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (panel != mainSplitPane.getBottomComponent()) {
                    mainSplitPane.setBottomComponent(panel);
                }
                panel.setVisible(true);
                if (location == -1) {
                    location = (int) ((double) (mainSplitPane.getHeight() -
                            mainSplitPane.getDividerSize()) * DIVIDER_LOCATION);
                }
                mainSplitPane.setDividerLocation(location);
                mainSplitPane.setDividerSize(DIVIDER_SIZE);
            }

            if (e.getStateChange() == ItemEvent.DESELECTED) {
                location = mainSplitPane.getDividerLocation();
                mainSplitPane.setDividerSize(0);
                panel.setVisible(false);
            }
        }
    }

    /**
     * to be able to deselect all buttons in the group
     */
    private class NoneSelectedButtonGroup extends ButtonGroup {
        @Override
        public void setSelected(ButtonModel model, boolean selected) {
            if (selected) {
                super.setSelected(model, true);
            } else {
                clearSelection();
            }
        }
    }
}
