package ru.eventflow.synsem.client;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import ru.eventflow.synsem.client.eventbus.EventBus;
import ru.eventflow.synsem.client.ui.presenter.ContainerPresenter;
import ru.eventflow.synsem.client.ui.presenter.MainPresenter;
import ru.eventflow.synsem.client.ui.presenter.MenuPresenter;
import ru.eventflow.synsem.client.ui.presenter.MessagesSliderPresenter;

public class AnnotationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBus.class).in(Singleton.class);

        bind(MainPresenter.class).in(Singleton.class);
        bind(MenuPresenter.class).in(Singleton.class);
        bind(ContainerPresenter.class).in(Singleton.class);
        bind(MessagesSliderPresenter.class).in(Singleton.class);
    }
}
