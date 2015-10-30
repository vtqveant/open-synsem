package ru.eventflow.synsem.client.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.synsem.client.eventbus.EventBus;
import ru.eventflow.synsem.client.ui.event.StatusUpdateEvent;
import ru.eventflow.synsem.client.ui.event.StatusUpdateEventHandler;
import ru.eventflow.synsem.client.ui.view.MessagesSliderView;

public class MessagesSliderPresenter implements Presenter<MessagesSliderView>, StatusUpdateEventHandler {

    private MessagesSliderView view;

    @Inject
    public MessagesSliderPresenter(final EventBus eventBus) {
        this.view = new MessagesSliderView();
        eventBus.addHandler(StatusUpdateEvent.TYPE, this);
    }

    @Override
    public MessagesSliderView getView() {
        return view;
    }

    @Override
    public void onEvent(StatusUpdateEvent e) {
        view.addRecord(e.getMessage());
    }
}
