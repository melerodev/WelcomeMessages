package io.github.melerodev.welcomemessages.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import io.github.melerodev.welcomemessages.AbstractWelcomeMessages;
import io.github.melerodev.welcomemessages.Reloadable;
import io.github.melerodev.welcomemessages.WelcomeMessages;

/**
 * A class to handle registration of commands.
 */
public class CommandHandler implements Reloadable {
    public static final String BASE_PERM = "welcomemessages.command";
    private final WelcomeMessages plugin;

    /**
     * Instantiates the Command handler.
     *
     * @param plugin the plugin
     */
    public CommandHandler(WelcomeMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad(AbstractWelcomeMessages plugin) {
        CommandAPI.onLoad(
            new CommandAPIPaperConfig(plugin)
                .silentLogs(true)
        );
    }

    @Override
    public void onEnable(AbstractWelcomeMessages plugin) {
        if (!CommandAPI.isLoaded())
            return;

        CommandAPI.onEnable();

        // Register commands here
        new WelcomeCommand(this.plugin)
            .command()
            .withAliases("wm")
            .register();
    }

    @Override
    public void onDisable(AbstractWelcomeMessages plugin) {
        if (!CommandAPI.isLoaded())
            return;

        CommandAPI.onDisable();
    }
}
