package io.github.melerodev.welcomemessages;

import io.github.melerodev.welcomemessages.api.WelcomeMessagesAPI;

class WelcomeMessagesAPIProvider extends WelcomeMessagesAPI implements Reloadable {
    private final WelcomeMessages plugin;

    WelcomeMessagesAPIProvider(WelcomeMessages plugin) {
        super();
        this.plugin = plugin;
        setInstance(this);
    }
}
