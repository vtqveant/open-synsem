package ru.eventflow.synsem.client.ui.event;

import ru.eventflow.synsem.client.eventbus.EventHandler;

public interface DialogEventHandler extends EventHandler {
    void onEvent(DialogEvent e);
}