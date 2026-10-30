package io.github.melerodev.welcomemessages;

import io.github.melerodev.welcomemessages.api.WelcomeMessagesAPI;
import io.github.melerodev.welcomemessages.command.CommandHandler;
import io.github.melerodev.welcomemessages.config.ConfigHandler;
import io.github.melerodev.welcomemessages.hook.HookManager;
import io.github.melerodev.welcomemessages.listener.ListenerHandler;
import io.github.melerodev.welcomemessages.threadutil.SchedulerHandler;
import io.github.melerodev.welcomemessages.translation.TranslationHandler;
import io.github.melerodev.welcomemessages.updatechecker.UpdateHandler;
import io.github.melerodev.welcomemessages.utility.Logger;
import io.github.melerodev.welcomemessages.welcome.WelcomeService;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.milkdrinkers.wordweaver.Translation;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Main class.
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class WelcomeMessages extends AbstractWelcomeMessages {
    private static WelcomeMessages instance;

    // Handlers/Managers
    private ConfigHandler configHandler;
    private TranslationHandler translationHandler;
    private HookManager hookManager;
    private CommandHandler commandHandler;
    private ListenerHandler listenerHandler;
    private UpdateHandler updateHandler;
    private SchedulerHandler schedulerHandler;
    private WelcomeService welcomeService;
    private WelcomeMessagesAPIProvider apiHandler;

    // Handlers list (defines order of load/enable/disable)
    private List<? extends Reloadable> handlers;

    @Override
    public void onLoad() {
        instance = this;

        configHandler = new ConfigHandler(this);
        welcomeService = new WelcomeService(this);
        translationHandler = new TranslationHandler(configHandler);
        hookManager = new HookManager(this);
        commandHandler = new CommandHandler(this);
        listenerHandler = new ListenerHandler(this);
        updateHandler = new UpdateHandler(this);
        schedulerHandler = new SchedulerHandler();
        apiHandler = new WelcomeMessagesAPIProvider(this);

        handlers = List.of(
            configHandler,
            translationHandler,
            hookManager,
            commandHandler,
            listenerHandler,
            updateHandler,
            schedulerHandler,
            apiHandler
        );

        for (Reloadable handler : handlers)
            handler.onLoad(instance);
    }

    @Override
    public void onEnable() {
        if (configHandler.getConfig() == null) {
            Logger.get().error(ColorParser.of("<red>config.yml could not be loaded, check the console for errors. Disabling <plugin>.").with("plugin", getName()).build());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        for (Reloadable handler : handlers)
            handler.onEnable(instance);
    }

    @Override
    public void onDisable() {
        if (handlers == null)
            return;

        for (Reloadable handler : handlers.reversed()) // If reverse doesn't work implement a new List with your desired disable order
            handler.onDisable(instance);
    }

    /**
     * Reloads the configuration and translation files without restarting the plugin.
     *
     * @return whether the configuration was reloaded successfully
     */
    public boolean reloadConfiguration() {
        final boolean success = configHandler.reload();
        Translation.setLocale(configHandler.getConfig().language);
        Translation.reload();
        return success;
    }

    /**
     * Gets plugin instance.
     *
     * @return the plugin instance
     */
    public static WelcomeMessages getPlugin() {
        return instance;
    }

    @Override
    public @NotNull ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public @NotNull HookManager getHookManager() {
        return hookManager;
    }

    public @NotNull WelcomeService getWelcomeService() {
        return welcomeService;
    }

    public @NotNull UpdateHandler getUpdateHandler() {
        return updateHandler;
    }

    public @NotNull WelcomeMessagesAPI getApiHandler() {
        return apiHandler;
    }
}
