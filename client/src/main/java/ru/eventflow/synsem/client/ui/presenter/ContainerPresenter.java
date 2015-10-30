package ru.eventflow.synsem.client.ui.presenter;

import com.google.inject.Inject;
import ru.eventflow.synsem.client.eventbus.EventBus;
import ru.eventflow.synsem.client.ui.view.ContainerView;

public class ContainerPresenter implements Presenter<ContainerView> {

    private final ContainerView view;
    private final EventBus eventBus;

    @Inject
    public ContainerPresenter(final EventBus eventBus) {
        this.view = new ContainerView();
        this.eventBus = eventBus;
    }

    @Override
    public ContainerView getView() {
        return view;
    }

}
