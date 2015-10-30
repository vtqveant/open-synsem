package ru.eventflow.synsem.client.ui.event;

import ru.eventflow.synsem.client.eventbus.Event;

public class DialogEvent extends Event<DialogEventHandler> {

    public final static Type<DialogEventHandler> TYPE = new Type<DialogEventHandler>();

    public DialogEvent() {
    }

    @Override
    public Type<DialogEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DialogEventHandler handler) {
        handler.onEvent(this);
    }
}
