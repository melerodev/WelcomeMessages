package io.github.melerodev.welcomemessages.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.melerodev.welcomemessages.WelcomeMessages;
import io.github.melerodev.welcomemessages.config.PluginConfig;
import io.github.melerodev.welcomemessages.welcome.WelcomeService;
import io.github.melerodev.welcomemessages.translation.Translation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static io.github.melerodev.welcomemessages.command.CommandHandler.BASE_PERM;

/**
 * The /welcomemessages command.
 */
final class WelcomeCommand extends Command {
    private final WelcomeMessages plugin;

    WelcomeCommand(WelcomeMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandAPICommand command() {
        return new CommandAPICommand("welcomemessages")
            .withHelp("WelcomeMessages commands.", "WelcomeMessages commands.")
            .withPermission(BASE_PERM)
            .withSubcommands(
                new CommandAPICommand("reload")
                    .withPermission(BASE_PERM + ".reload")
                    .executes(this::executorReload),
                new CommandAPICommand("test")
                    .withPermission(BASE_PERM + ".test")
                    .withOptionalArguments(new BooleanArgument("first-join"))
                    .executesPlayer(this::executorTest)
            )
            .executes(this::executorHelp);
    }

    private void executorHelp(CommandSender sender, CommandArguments args) {
        sender.sendMessage(Translation.as("commands.help"));
    }

    private void executorReload(CommandSender sender, CommandArguments args) {
        sender.sendMessage(Translation.as(plugin.reloadConfiguration() ? "commands.reload.success" : "commands.reload.failure"));
    }

    private void executorTest(Player player, CommandArguments args) {
        final boolean firstJoin = (boolean) args.getOrDefault("first-join", false);
        final PluginConfig.Profile profile = WelcomeService.profile(player.getWorld());
        if (profile == null) {
            player.sendMessage(Translation.as("commands.test.disabled"));
            return;
        }

        plugin.getWelcomeService().welcome(player, profile, firstJoin);
    }
}
