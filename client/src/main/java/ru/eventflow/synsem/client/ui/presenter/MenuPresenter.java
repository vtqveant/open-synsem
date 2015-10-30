package ru.eventflow.synsem.client.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.synsem.client.eventbus.EventBus;
import ru.eventflow.synsem.client.ui.event.DialogEvent;
import ru.eventflow.synsem.client.ui.view.MenuView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class MenuPresenter implements Presenter<MenuView> {

    private MenuView view;
    private EventBus eventBus;

    @Inject
    public MenuPresenter(final EventBus eventBus) {
        this.view = new MenuView();
        this.eventBus = eventBus;
        init();
    }

    private void init() {
        this.view.getExitMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO maybe we need to do some work before closing (save, free resources, etc.)
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        this.view.getAboutMenuItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventBus.fireEvent(new DialogEvent());
            }
        });
    }

    @Override
    public MenuView getView() {
        return view;
    }
}
