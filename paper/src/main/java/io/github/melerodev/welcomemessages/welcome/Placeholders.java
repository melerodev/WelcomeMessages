package io.github.melerodev.welcomemessages.welcome;

import io.github.melerodev.welcomemessages.hook.Hook;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Resolves the built-in placeholders, PlaceholderAPI and MiniPlaceholders for a player.
 */
public final class Placeholders {
    private Placeholders() {
    }

    /**
     * Parse a MiniMessage/legacy string into a component for the given player.
     *
     * @param message the raw message
     * @param player  the player the placeholders refer to
     * @param viewer  the player who will see the message, used for relational placeholders
     * @return the component
     */
    public static @NotNull Component component(@NotNull String message, @NotNull Player player, @NotNull Player viewer) {
        return ColorParser.of(message)
            .legacy()
            .with("player", player.getName())
            .with("displayname", player.displayName())
            .with("uuid", player.getUniqueId().toString())
            .with("world", player.getWorld().getName())
            .with("online", String.valueOf(Bukkit.getOnlinePlayers().size()))
            .with("max_players", String.valueOf(Bukkit.getMaxPlayers()))
            .with("unique_joins", String.valueOf(Bukkit.getOfflinePlayers().length))
            .papi(player)
            .mini(viewer)
            .build();
    }

    /**
     * Parse a message for the player itself.
     */
    public static @NotNull Component component(@NotNull String message, @NotNull Player player) {
        return component(message, player, player);
    }

    /**
     * Replace placeholders in a plain string, for use in commands.
     *
     * @param text   the raw text
     * @param player the player
     * @return the text with placeholders replaced
     */
    public static @NotNull String plain(@NotNull String text, @NotNull Player player) {
        String result = text
            .replace("<player>", player.getName())
            .replace("<displayname>", PlainTextComponentSerializer.plainText().serialize(player.displayName()))
            .replace("<uuid>", player.getUniqueId().toString())
            .replace("<world>", player.getWorld().getName())
            .replace("<online>", String.valueOf(Bukkit.getOnlinePlayers().size()))
            .replace("<max_players>", String.valueOf(Bukkit.getMaxPlayers()));

        if (Hook.PAPI.isLoaded())
            result = PlaceholderAPI.setPlaceholders(player, result);

        return result;
    }
}
