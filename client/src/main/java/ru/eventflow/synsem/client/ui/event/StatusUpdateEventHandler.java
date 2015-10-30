package ru.eventflow.synsem.client.ui.event;

import ru.eventflow.synsem.client.eventbus.EventHandler;

public interface StatusUpdateEventHandler extends EventHandler {
    void onEvent(StatusUpdateEvent e);
}
