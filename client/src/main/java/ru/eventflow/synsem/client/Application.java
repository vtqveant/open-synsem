package ru.eventflow.synsem.client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jtattoo.plaf.fast.FastLookAndFeel;
import ru.eventflow.synsem.client.ui.presenter.MainPresenter;
import ru.eventflow.synsem.client.ui.presenter.MenuPresenter;
import ru.eventflow.synsem.client.ui.presenter.Presenter;
import ru.eventflow.synsem.client.ui.view.MainView;
import ru.eventflow.synsem.client.ui.view.MenuView;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class Application {

    private static final Injector injector = Guice.createInjector(new AnnotationModule());
    private static final ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("images/corpus.png"));

    public static void main(String[] args) {
        try {
            // LaF properties
            Properties props = new Properties();
            props.put("logoString", "");
            props.put("windowDecoration", "off");
            props.put("linuxStyleScrollBar", "off");
            props.put("tooltipBorderSize", "1");
            props.put("tooltipBackgroundColor", "255 255 204"); // light yellow
            props.put("tooltipCastShadow", "on");
            props.put("tooltipShadowSize", "1");

            FastLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");

            final Presenter<MainView> mainPresenter = injector.getInstance(MainPresenter.class);
            final Presenter<MenuView> menuPresenter = injector.getInstance(MenuPresenter.class);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    final JFrame frame = new JFrame("CCG Corpus Annotation Tool");
                    frame.setJMenuBar(menuPresenter.getView());
                    frame.setContentPane(mainPresenter.getView());
                    frame.setLocationByPlatform(true);
                    frame.setIconImage(icon.getImage());
                    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setSize(new Dimension(640, 480));
                    frame.setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

