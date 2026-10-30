package io.github.melerodev.welcomemessages.hook.placeholderapi;

import io.github.melerodev.welcomemessages.WelcomeMessages;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * PlaceholderAPI expansion: {@code %welcomemessages_unique_joins%} and {@code %welcomemessages_first_join%}.
 */
public class PAPIExpansion extends PlaceholderExpansion {
    private final WelcomeMessages plugin;

    public PAPIExpansion(WelcomeMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull String getIdentifier() {
        return plugin.getPluginMeta().getName().replace(' ', '_').toLowerCase();
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getPluginMeta().getAuthors());
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This needs to be true, or PlaceholderAPI will unregister the expansion during a plugin reload.
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer p, @NotNull String params) {
        return switch (params) {
            case "unique_joins" -> String.valueOf(Bukkit.getOfflinePlayers().length);
            case "first_join" -> p == null ? null : String.valueOf(p.getFirstPlayed());
            default -> null;
        };
    }
}
