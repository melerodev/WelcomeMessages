package io.github.melerodev.welcomemessages.welcome;

import io.github.melerodev.welcomemessages.WelcomeMessages;
import io.github.melerodev.welcomemessages.utility.Logger;
import net.kyori.adventure.key.InvalidKeyException;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

/**
 * Runs configured actions such as {@code [console] give <player> bread}.
 */
public final class ActionRunner {
    private final WelcomeMessages plugin;

    public ActionRunner(WelcomeMessages plugin) {
        this.plugin = plugin;
    }

    /**
     * Run a list of actions for a player. Must be called from the player's thread.
     *
     * @param player  the player
     * @param actions the actions
     */
    public void run(@NotNull Player player, @NotNull List<String> actions) {
        for (String action : actions)
            run(player, action);
    }

    private void run(@NotNull Player player, @NotNull String action) {
        final String trimmed = action.trim();
        if (trimmed.isEmpty())
            return;

        final int end = trimmed.indexOf(']');
        if (!trimmed.startsWith("[") || end < 0) {
            Logger.get().warn("Invalid action (missing [type]): {}", action);
            return;
        }

        final String type = trimmed.substring(1, end).toLowerCase(Locale.ROOT);
        final String value = trimmed.substring(end + 1).trim();

        switch (type) {
            case "console" -> {
                final String command = stripSlash(Placeholders.plain(value, player));
                Bukkit.getGlobalRegionScheduler().execute(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
            }
            case "player" -> player.performCommand(stripSlash(Placeholders.plain(value, player)));
            case "message" -> player.sendMessage(Placeholders.component(value, player));
            case "broadcast" -> {
                for (Player online : Bukkit.getOnlinePlayers())
                    online.sendMessage(Placeholders.component(value, player, online));
                Bukkit.getConsoleSender().sendMessage(Placeholders.component(value, player));
            }
            case "actionbar" -> player.sendActionBar(Placeholders.component(value, player));
            case "title" -> {
                final String[] parts = value.split(";", 2);
                final Component title = Placeholders.component(parts[0], player);
                final Component subtitle = parts.length > 1 ? Placeholders.component(parts[1], player) : Component.empty();
                player.showTitle(Title.title(title, subtitle));
            }
            case "sound" -> {
                final String[] parts = value.split(";");
                final Sound sound = sound(parts[0], parts.length > 1 ? parseFloat(parts[1], 1f) : 1f, parts.length > 2 ? parseFloat(parts[2], 1f) : 1f);
                if (sound != null)
                    player.playSound(sound, Sound.Emitter.self());
            }
            default -> Logger.get().warn("Unknown action type [{}] in: {}", type, action);
        }
    }

    /**
     * Build an adventure sound, or null if the key is invalid.
     *
     * @param key    the sound key
     * @param volume the volume
     * @param pitch  the pitch
     * @return the sound or null
     */
    public static Sound sound(@NotNull String key, float volume, float pitch) {
        try {
            return Sound.sound(Key.key(key.trim().toLowerCase(Locale.ROOT)), Sound.Source.MASTER, volume, pitch);
        } catch (InvalidKeyException e) {
            Logger.get().warn("Invalid sound key: {}", key);
            return null;
        }
    }

    private static float parseFloat(String value, float fallback) {
        try {
            return Float.parseFloat(value.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static String stripSlash(String command) {
        return command.startsWith("/") ? command.substring(1) : command;
    }
}
