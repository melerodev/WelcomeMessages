package io.github.melerodev.welcomemessages.hook.placeholderapi;

import io.github.melerodev.welcomemessages.AbstractWelcomeMessages;
import io.github.melerodev.welcomemessages.WelcomeMessages;
import io.github.melerodev.welcomemessages.hook.AbstractHook;
import io.github.melerodev.welcomemessages.hook.Hook;

/**
 * A hook to interface with <a href="https://wiki.placeholderapi.com/">PlaceholderAPI</a>.
 */
public class PAPIHook extends AbstractHook {
    private PAPIExpansion PAPIExpansion;

    /**
     * Instantiates a new PlaceholderAPI hook.
     *
     * @param plugin the plugin instance
     */
    public PAPIHook(WelcomeMessages plugin) {
        super(plugin);
    }

    @Override
    public void onEnable(AbstractWelcomeMessages plugin) {
        if (!isHookLoaded())
            return;

        PAPIExpansion = new PAPIExpansion(super.getPlugin());
        PAPIExpansion.register();
    }

    @Override
    public void onDisable(AbstractWelcomeMessages plugin) {
        if (PAPIExpansion == null)
            return;

        PAPIExpansion.unregister();
        PAPIExpansion = null;
    }

    @Override
    public boolean isHookLoaded() {
        return isPluginPresent(Hook.PAPI.getPluginName()) && isPluginEnabled(Hook.PAPI.getPluginName());
    }
}
