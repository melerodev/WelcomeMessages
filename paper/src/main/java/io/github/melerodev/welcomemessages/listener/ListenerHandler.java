package io.github.melerodev.welcomemessages.listener;

import io.github.melerodev.welcomemessages.AbstractWelcomeMessages;
import io.github.melerodev.welcomemessages.Reloadable;
import io.github.melerodev.welcomemessages.WelcomeMessages;
import io.github.melerodev.welcomemessages.welcome.WelcomeService;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to handle registration of event listeners.
 */
public class ListenerHandler implements Reloadable {
    private final WelcomeMessages plugin;
    private final List<Listener> listeners = new ArrayList<>();

    /**
     * Instantiates a the Listener handler.
     *
     * @param plugin the plugin instance
     */
    public ListenerHandler(WelcomeMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable(AbstractWelcomeMessages plugin) {
        // Registered from code so the default is false (even for operators)
        if (plugin.getServer().getPluginManager().getPermission(WelcomeListener.SILENT_PERM) == null)
            plugin.getServer().getPluginManager().addPermission(new Permission(WelcomeListener.SILENT_PERM, "Join and leave without a broadcast message", PermissionDefault.FALSE));

        listeners.clear(); // Clear the list to avoid duplicate listeners when reloading the plugin
        listeners.add(new WelcomeListener(this.plugin.getWelcomeService()));

        // Register listeners here
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    @Override
    public void onDisable(AbstractWelcomeMessages plugin) {
        listeners.forEach(HandlerList::unregisterAll);
        listeners.clear();
        plugin.getServer().getPluginManager().removePermission(WelcomeListener.SILENT_PERM);
    }
}
