package io.github.melerodev.welcomemessages.translation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.melerodev.welcomemessages.utility.Logger;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Loads the plugin's own messages from {@code lang/<locale>.json}.
 * <p>
 * Bundled files are extracted to {@code plugins/<plugin>/lang/} so they can be edited. Missing keys fall back to the
 * bundled file of the selected locale and then to {@link #DEFAULT_LOCALE}. Works on every supported Paper version
 * (it only needs Gson, which Paper provides).
 */
public final class Translation {
    public static final String DEFAULT_LOCALE = "en_US";
    private static final List<String> BUNDLED_LOCALES = List.of("en_US", "es_ES");

    private static volatile Map<String, String> entries = Map.of();
    private static JavaPlugin plugin;
    private static String locale = DEFAULT_LOCALE;

    private Translation() {
    }

    /**
     * Extract the bundled language files and load the given locale.
     *
     * @param owner     the plugin
     * @param newLocale the locale, e.g. {@code es_ES}
     */
    public static void initialize(@NotNull JavaPlugin owner, @NotNull String newLocale) {
        plugin = owner;
        locale = newLocale;

        for (String bundled : BUNDLED_LOCALES) {
            if (!Files.exists(directory().resolve(bundled + ".json")))
                owner.saveResource("lang/" + bundled + ".json", false);
        }

        reload();
    }

    /**
     * Set the locale. Call {@link #reload()} afterwards.
     *
     * @param newLocale the locale, e.g. {@code es_ES}
     */
    public static void setLocale(@NotNull String newLocale) {
        locale = newLocale;
    }

    /**
     * Reload the language files from disk.
     */
    public static void reload() {
        if (plugin == null)
            return;

        final Map<String, String> loaded = new HashMap<>();
        readBundled(DEFAULT_LOCALE, loaded);
        readBundled(locale, loaded);

        final Path file = directory().resolve(locale + ".json");
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                flatten("", JsonParser.parseReader(reader), loaded);
            } catch (IOException | RuntimeException e) {
                Logger.get().warn("Could not read {}: {}", file, e.getMessage());
            }
        } else if (!Set.copyOf(BUNDLED_LOCALES).contains(locale)) {
            Logger.get().warn("Language file lang/{}.json not found, using {}", locale, DEFAULT_LOCALE);
        }

        entries = Map.copyOf(loaded);
    }

    /**
     * Get a raw translation.
     *
     * @param key the key, e.g. {@code commands.reload.success}
     * @return the translation, or the key itself if it does not exist
     */
    public static @NotNull String of(@NotNull String key) {
        return entries.getOrDefault(key, key);
    }

    /**
     * Get a translation parsed as a component.
     *
     * @param key the key
     * @return the parsed translation
     */
    public static @NotNull Component as(@NotNull String key) {
        return ColorParser.of(of(key)).build();
    }

    private static Path directory() {
        return plugin.getDataFolder().toPath().resolve("lang");
    }

    private static void readBundled(String name, Map<String, String> into) {
        try (InputStream in = plugin.getResource("lang/" + name + ".json")) {
            if (in == null)
                return;

            flatten("", JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)), into);
        } catch (IOException | RuntimeException e) {
            Logger.get().warn("Could not read bundled lang/{}.json: {}", name, e.getMessage());
        }
    }

    private static void flatten(String prefix, JsonElement element, Map<String, String> into) {
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : object.entrySet())
                flatten(prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey(), entry.getValue(), into);
        } else if (element.isJsonPrimitive()) {
            into.put(prefix, element.getAsString());
        }
    }
}
