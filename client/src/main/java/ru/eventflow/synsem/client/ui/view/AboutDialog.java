package ru.eventflow.synsem.client.ui.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class AboutDialog extends JDialog implements WindowFocusListener {

    private static final int WIDTH = 360;
    private static final int HEIGHT = 200;

    private static final String HTML = "<html>" +
            "<div height=\"110\">" +
            "Grounded Semantic Interpretation<br/>" +
            "</div>" +
            "<div align=\"right\"><font size=\"-2\">by Kosta Sokolov<br/>vtqveant@gmail.com</font></div></html>";

    public AboutDialog(JFrame owner) {
        super(owner);

        setSize(WIDTH, HEIGHT);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (WIDTH / 2);
        int y = (screenSize.height / 2) - (HEIGHT / 2);
        setLocation(x, y);

        setUndecorated(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowFocusListener(this);

        JLabel label = new JLabel();
        label.setBackground(Color.LIGHT_GRAY);
        label.setText(HTML);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(label);
        setContentPane(panel);
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        // nothing
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        setVisible(false);
    }
}
