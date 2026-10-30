package io.github.melerodev.welcomemessages.welcome;

import io.github.melerodev.welcomemessages.WelcomeMessages;
import io.github.melerodev.welcomemessages.config.PluginConfig;
import io.github.melerodev.welcomemessages.utility.Cfg;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;

/**
 * Sends the private welcome (chat, title, action bar, sounds and actions) to a player.
 */
public final class WelcomeService {
    private final WelcomeMessages plugin;
    private final ActionRunner actionRunner;

    public WelcomeService(WelcomeMessages plugin) {
        this.plugin = plugin;
        this.actionRunner = new ActionRunner(plugin);
    }

    /**
     * Resolve the profile for a world, or null if the world is disabled.
     *
     * @param world the world
     * @return the profile or null
     */
    public static @Nullable PluginConfig.Profile profile(@NotNull World world) {
        final PluginConfig cfg = Cfg.get();
        if (cfg.disabledWorlds.contains(world.getName()))
            return null;

        final PluginConfig.Profile profile = cfg.worlds.getOrDefault(world.getName(), cfg.defaults);
        return profile.enabled ? profile : null;
    }

    /**
     * Schedule the private welcome for a player using the profile's delay.
     *
     * @param player    the player
     * @param profile   the profile
     * @param firstJoin whether this is the player's first join
     */
    public void schedule(@NotNull Player player, @NotNull PluginConfig.Profile profile, boolean firstJoin) {
        final long delay = Math.max(1, profile.delayTicks);
        player.getScheduler().runDelayed(plugin, task -> welcome(player, profile, firstJoin), null, delay);

        if (profile.broadcastSound.enabled) {
            final Sound sound = ActionRunner.sound(profile.broadcastSound.sound, (float) profile.broadcastSound.volume, (float) profile.broadcastSound.pitch);
            if (sound != null) {
                for (Player other : Bukkit.getOnlinePlayers()) {
                    if (!other.equals(player))
                        other.getScheduler().run(plugin, task -> other.playSound(sound, Sound.Emitter.self()), null);
                }
            }
        }
    }

    /**
     * Send the private welcome immediately. Must be called on the player's thread.
     *
     * @param player    the player
     * @param profile   the profile
     * @param firstJoin whether this is the player's first join
     */
    public void welcome(@NotNull Player player, @NotNull PluginConfig.Profile profile, boolean firstJoin) {
        if (!player.isOnline())
            return;

        final List<String> lines = firstJoin && !profile.firstJoinWelcome.isEmpty() ? profile.firstJoinWelcome : profile.welcome;
        for (String line : lines)
            player.sendMessage(Placeholders.component(line, player));

        final PluginConfig.TitleSettings title = firstJoin && profile.firstJoinTitle.enabled ? profile.firstJoinTitle : profile.title;
        if (title.enabled) {
            player.showTitle(Title.title(
                Placeholders.component(title.title, player),
                Placeholders.component(title.subtitle, player),
                Title.Times.times(ticks(title.fadeIn), ticks(title.stay), ticks(title.fadeOut))
            ));
        }

        if (!profile.actionBar.isBlank())
            player.sendActionBar(Placeholders.component(profile.actionBar, player));

        if (profile.sound.enabled) {
            final Sound sound = ActionRunner.sound(profile.sound.sound, (float) profile.sound.volume, (float) profile.sound.pitch);
            if (sound != null)
                player.playSound(sound, Sound.Emitter.self());
        }

        actionRunner.run(player, profile.actions);
        if (firstJoin)
            actionRunner.run(player, profile.firstJoinActions);
    }

    private static Duration ticks(int ticks) {
        return Duration.ofMillis(Math.max(0, ticks) * 50L);
    }
}
