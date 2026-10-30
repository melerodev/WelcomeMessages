package io.github.melerodev.welcomemessages.translation;

import io.github.melerodev.welcomemessages.AbstractWelcomeMessages;
import io.github.melerodev.welcomemessages.Reloadable;
import io.github.melerodev.welcomemessages.config.ConfigHandler;

/**
 * A wrapper handler class for handling the {@link Translation} lifecycle.
 */
public class TranslationHandler implements Reloadable {
    private final ConfigHandler configHandler;

    public TranslationHandler(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    @Override
    public void onEnable(AbstractWelcomeMessages plugin) {
        Translation.initialize(plugin, configHandler.getConfig().language);
    }
}
