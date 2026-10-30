package io.github.melerodev.welcomemessages.utility;

import io.github.melerodev.welcomemessages.AbstractWelcomeMessages;
import io.github.melerodev.welcomemessages.config.ConfigHandler;
import io.github.melerodev.welcomemessages.config.PluginConfig;
import org.jetbrains.annotations.NotNull;

/**
 * Convenience class for accessing {@link ConfigHandler#getConfig}
 */
public final class Cfg {
    /**
     * Convenience method for {@link ConfigHandler#getConfig} to getConnection {@link PluginConfig}
     *
     * @return the config
     */
    @NotNull
    public static PluginConfig get() {
        return AbstractWelcomeMessages.getInstance().getConfigHandler().getConfig();
    }
}
