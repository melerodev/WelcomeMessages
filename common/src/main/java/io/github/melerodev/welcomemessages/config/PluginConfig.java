package io.github.melerodev.welcomemessages.config;

import io.github.melerodev.welcomemessages.config.exception.ConfigValidationException;
import io.github.melerodev.welcomemessages.config.migration.Migration;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.interfaces.meta.Exclude;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class PluginConfig implements VersionedConfig {
    public static final String HEADER = """
        WelcomeMessages configuration
        
        All messages support MiniMessage (https://docs.advntr.dev/minimessage/format),
        legacy color codes (&a, &l...), PlaceholderAPI (%placeholder%) and MiniPlaceholders.
        
        Built-in placeholders: <player>, <displayname>, <uuid>, <world>, <online>, <max_players>, <unique_joins>
        
        Actions (one per line):
          [console] <command>        Run a command from the console
          [player] <command>         Run a command as the player
          [message] <text>           Send a chat message to the player
          [broadcast] <text>         Send a chat message to everyone
          [actionbar] <text>         Show an action bar message to the player
          [title] <title>;<subtitle> Show a title to the player
          [sound] <sound>;<volume>;<pitch>  Play a sound to the player""";

    @Comment("Do not change this value!")
    public int configVersion = 1;

    @Override
    @Exclude
    public int configVersion() {
        return configVersion;
    }

    @Override
    @Exclude
    public @NotNull Map<Integer, Migration> migrations() {
        return Map.of();
    }

    @Override
    @Exclude
    public void validate() throws ConfigValidationException {
        validateProfile("default", defaults);
        for (Map.Entry<String, Profile> entry : worlds.entrySet())
            validateProfile("worlds." + entry.getKey(), entry.getValue());
    }

    private static void validateProfile(String path, Profile profile) throws ConfigValidationException {
        if (profile == null)
            throw new ConfigValidationException("`" + path + "` cannot be empty");

        if (profile.delayTicks < 0)
            throw new ConfigValidationException("`" + path + ".delay-ticks` cannot be negative");
    }

    @Comment("Settings used in every world, unless the world has its own profile in `worlds`.")
    public Profile defaults = new Profile();

    @Comment("""
        Per-world profiles. The key is the world name; a world profile completely replaces `defaults`
        for players that join in that world. Copy any option from `defaults` here.""")
    public Map<String, Profile> worlds = new LinkedHashMap<>(Map.of("lobby", Profile.lobbyExample()));

    @Comment("Worlds where WelcomeMessages does nothing (vanilla join/quit messages are kept).")
    public List<String> disabledWorlds = new ArrayList<>();

    @ConfigSerializable
    public static class Profile {
        @Comment("Enable welcome features for this profile.")
        public boolean enabled = true;

        @Comment("Message broadcast to everyone when a player joins.")
        public ServerMessage joinMessage = new ServerMessage(MessageMode.CUSTOM, "<dark_gray>[<green>+</green>]</dark_gray> <gray><player>");

        @Comment("Message broadcast to everyone the first time a player joins. Leave empty to use `join-message`.")
        public String firstJoinMessage = "<gold>✦ <yellow>Welcome <white><player></white> to the server for the first time! <gray>(#<unique_joins>)";

        @Comment("Message broadcast to everyone when a player leaves.")
        public ServerMessage quitMessage = new ServerMessage(MessageMode.CUSTOM, "<dark_gray>[<red>-</red>]</dark_gray> <gray><player>");

        @Comment("Delay in ticks (20 ticks = 1 second) before sending the private welcome, title, sound and actions.")
        public long delayTicks = 10;

        @Comment("Private chat lines sent to the player that joined.")
        public List<String> welcome = List.of(
            "",
            "<gradient:#00C9FF:#92FE9D><bold>Welcome back, <player>!</bold></gradient>",
            "<gray>There are <white><online></white> players online.",
            ""
        );

        @Comment("Private chat lines sent on the first join. Leave empty to use `welcome`.")
        public List<String> firstJoinWelcome = List.of(
            "",
            "<gradient:#FFD700:#FF8C00><bold>Welcome to the server, <player>!</bold></gradient>",
            "<gray>Type <yellow>/help</yellow> to get started.",
            ""
        );

        @Comment("Action bar shown to the player. Leave empty to disable.")
        public String actionBar = "<green>Enjoy your stay, <player>!";

        @Comment("Title shown to the player.")
        public TitleSettings title = new TitleSettings(true, "<gradient:#00C9FF:#92FE9D>Welcome back</gradient>", "<gray><player>");

        @Comment("Title shown on the first join.")
        public TitleSettings firstJoinTitle = new TitleSettings(true, "<gold>Welcome!", "<yellow>Have fun, <player>");

        @Comment("Sound played to the player that joined. Use sound keys like `minecraft:entity.player.levelup`.")
        public SoundSettings sound = new SoundSettings(true, "minecraft:entity.player.levelup", 1.0, 1.0);

        @Comment("Sound played to everyone else when somebody joins.")
        public SoundSettings broadcastSound = new SoundSettings(false, "minecraft:block.note_block.pling", 0.6, 1.6);

        @Comment("Actions run every time the player joins.")
        public List<String> actions = new ArrayList<>();

        @Comment("Actions run only on the first join (in addition to `actions`).")
        public List<String> firstJoinActions = List.of(
            "[console] give <player> minecraft:bread 16"
        );

        static Profile lobbyExample() {
            final Profile profile = new Profile();
            profile.welcome = List.of("<aqua>You are in the lobby. <gray>Use the compass to pick a game!");
            profile.firstJoinWelcome = List.of();
            profile.firstJoinActions = List.of();
            profile.actions = List.of("[player] spawn");
            profile.title = new TitleSettings(true, "<aqua>Lobby", "<gray>Welcome, <player>");
            return profile;
        }
    }

    public enum MessageMode {
        /**
         * Use the configured message.
         */
        CUSTOM,
        /**
         * Keep the vanilla message.
         */
        VANILLA,
        /**
         * Hide the message.
         */
        HIDDEN
    }

    @ConfigSerializable
    public static class ServerMessage {
        @Comment("custom = use `message`, vanilla = keep the default Minecraft message, hidden = no message")
        public MessageMode mode = MessageMode.CUSTOM;

        public String message = "";

        public ServerMessage() {
        }

        ServerMessage(MessageMode mode, String message) {
            this.mode = mode;
            this.message = message;
        }
    }

    @ConfigSerializable
    public static class TitleSettings {
        public boolean enabled = true;
        public String title = "";
        public String subtitle = "";

        @Comment("Durations in ticks.")
        public int fadeIn = 10;
        public int stay = 60;
        public int fadeOut = 20;

        public TitleSettings() {
        }

        TitleSettings(boolean enabled, String title, String subtitle) {
            this.enabled = enabled;
            this.title = title;
            this.subtitle = subtitle;
        }
    }

    @ConfigSerializable
    public static class SoundSettings {
        public boolean enabled = true;
        public String sound = "minecraft:entity.player.levelup";
        public double volume = 1.0;
        public double pitch = 1.0;

        public SoundSettings() {
        }

        SoundSettings(boolean enabled, String sound, double volume, double pitch) {
            this.enabled = enabled;
            this.sound = sound;
            this.volume = volume;
            this.pitch = pitch;
        }
    }

    @Comment("Update Checker Settings")
    public UpdateChecker updateChecker = new UpdateChecker();

    @ConfigSerializable
    public static class UpdateChecker {
        @Comment("Should the plugin check for plugin updates on startup?")
        public boolean enabled = true;

        @Comment("Send update notifications to the console?")
        public boolean console = true;

        @Comment("Send update notifications to opped players on join?")
        public boolean op = true;
    }

    @Comment("Language, specify the language file to use, for example `en_US` or `es_ES` which will load `/lang/<language>.json`")
    public String language = "en_US";
}
