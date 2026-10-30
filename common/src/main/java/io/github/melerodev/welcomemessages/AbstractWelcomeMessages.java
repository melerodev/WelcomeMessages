package io.github.melerodev.welcomemessages;

import io.github.melerodev.welcomemessages.config.ConfigHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractWelcomeMessages extends JavaPlugin {
    private static AbstractWelcomeMessages instance;

    /**
     * Gets plugin instance.
     *
     * @return the plugin instance
     */
    public static AbstractWelcomeMessages getInstance() {
        return AbstractWelcomeMessages.instance;
    }

    AbstractWelcomeMessages() {
        AbstractWelcomeMessages.instance = this;
    }

    /**
     * Gets config handler.
     *
     * @return the config handler
     */
    public abstract @NotNull ConfigHandler getConfigHandler();
}
