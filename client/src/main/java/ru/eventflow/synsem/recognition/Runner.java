package ru.eventflow.synsem.recognition;

import javax.swing.*;
import java.awt.*;

public class Runner {

    public static void main(String[] args) {
        MainClientForm mainClientForm = new MainClientForm();
        mainClientForm.setBackendProfile(BackendProfile.Vendor.DUMMY);

        JFrame frame = new JFrame("SynSem Client");
        frame.setPreferredSize(new Dimension(1024, 680));
        frame.setContentPane(mainClientForm.getRootPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
