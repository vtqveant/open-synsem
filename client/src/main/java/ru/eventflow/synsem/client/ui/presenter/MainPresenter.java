package ru.eventflow.synsem.client.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.synsem.client.eventbus.EventBus;
import ru.eventflow.synsem.client.ui.event.DialogEvent;
import ru.eventflow.synsem.client.ui.event.DialogEventHandler;
import ru.eventflow.synsem.client.ui.event.StatusUpdateEvent;
import ru.eventflow.synsem.client.ui.event.StatusUpdateEventHandler;
import ru.eventflow.synsem.client.ui.view.MainView;

public class MainPresenter implements Presenter<MainView>, StatusUpdateEventHandler, DialogEventHandler {

    private final MainView view;
    private final EventBus eventBus;

    @Inject
    public MainPresenter(final EventBus eventBus,
                         final ContainerPresenter containerPresenter,
                         final MessagesSliderPresenter messagesPresenter) {
        this.eventBus = eventBus;
        this.view = new MainView();
        this.view.setTopPanel(containerPresenter.getView());
        this.view.addSliderPanel(messagesPresenter.getView());
        init();
    }

    private void init() {
        this.eventBus.addHandler(StatusUpdateEvent.TYPE, this);
        this.eventBus.addHandler(DialogEvent.TYPE, this);
    }

    @Override
    public MainView getView() {
        return view;
    }

    @Override
    public void onEvent(StatusUpdateEvent e) {
        view.getStatusLabel().setText(e.getMessage());
    }

    @Override
    public void onEvent(DialogEvent e) {
        view.displayAboutDialog();
    }
}
