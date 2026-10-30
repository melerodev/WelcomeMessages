package io.github.melerodev.welcomemessages.listener;

import io.github.melerodev.welcomemessages.config.PluginConfig;
import io.github.melerodev.welcomemessages.welcome.Placeholders;
import io.github.melerodev.welcomemessages.welcome.WelcomeService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handles join and quit messages.
 */
public final class WelcomeListener implements Listener {
    public static final String SILENT_PERM = "welcomemessages.silent";

    private final WelcomeService welcomeService;

    public WelcomeListener(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        final PluginConfig.Profile profile = WelcomeService.profile(player.getWorld());
        if (profile == null)
            return;

        final boolean firstJoin = !player.hasPlayedBefore();

        if (player.hasPermission(SILENT_PERM)) {
            e.joinMessage(null);
        } else if (firstJoin && !profile.firstJoinMessage.isBlank()) {
            broadcast(profile.firstJoinMessage, player);
            e.joinMessage(null);
        } else {
            switch (profile.joinMessage.mode) {
                case CUSTOM -> {
                    broadcast(profile.joinMessage.message, player);
                    e.joinMessage(null);
                }
                case HIDDEN -> e.joinMessage(null);
                case VANILLA -> {
                }
            }
        }

        welcomeService.schedule(player, profile, firstJoin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        final PluginConfig.Profile profile = WelcomeService.profile(player.getWorld());
        if (profile == null)
            return;

        if (player.hasPermission(SILENT_PERM)) {
            e.quitMessage(null);
            return;
        }

        switch (profile.quitMessage.mode) {
            case CUSTOM -> {
                broadcast(profile.quitMessage.message, player);
                e.quitMessage(null);
            }
            case HIDDEN -> e.quitMessage(null);
            case VANILLA -> {
            }
        }
    }

    /**
     * Broadcast a message parsed for each viewer, so MiniPlaceholders relational placeholders work.
     */
    private static void broadcast(String message, Player player) {
        if (message.isBlank())
            return;

        for (Player online : Bukkit.getOnlinePlayers())
            online.sendMessage(Placeholders.component(message, player, online));

        final Component console = Placeholders.component(message, player);
        Bukkit.getConsoleSender().sendMessage(console);
    }
}
