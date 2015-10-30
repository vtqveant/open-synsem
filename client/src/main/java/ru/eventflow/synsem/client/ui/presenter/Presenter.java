package ru.eventflow.synsem.client.ui.presenter;

import java.awt.*;

public interface Presenter<T extends Container> {
    T getView();
}
