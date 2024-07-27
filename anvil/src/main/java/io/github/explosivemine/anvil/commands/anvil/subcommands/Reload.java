package io.github.explosivemine.anvil.commands.anvil.subcommands;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.Permissions;
import io.github.explosivemine.anvil.commands.anvil.AnvilSubCommand;
import io.github.explosivemine.anvil.config.parser.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class Reload implements AnvilSubCommand {
    @Override
    public @NotNull List<String> getLabels() {
        return Collections.singletonList("reload");
    }

    @Override
    public @NotNull String getUsage() {
        return "reload";
    }

    @Override
    public @NotNull String getDescription() {
        return "Loads the plugin's config files.";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return true;
    }

    @Override
    public @NotNull Permissions getPermission() {
        return Permissions.RELOAD;
    }

    @Override
    public void execute(AnvilPlugin plugin, CommandSender sender, String[] args) {
        Lang.reload(plugin);
        Lang.RELOAD_MESSAGES.send(sender);
    }

}