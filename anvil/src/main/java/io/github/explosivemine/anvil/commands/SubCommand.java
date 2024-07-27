package io.github.explosivemine.anvil.commands;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.Permissions;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public interface SubCommand {
    @NotNull CommandIdentifier getCommandIdentifier();
    @NotNull List<String> getLabels();

    @NotNull
    Permissions getPermission();

    @NotNull String getUsage();

    @NotNull String getDescription();

    int getMinArgs();

    int getMaxArgs();

    boolean canBeExecutedByConsole();

    default long getCooldown() {
        return 0L;
    }

    default boolean displayCommand() {
        return true;
    }

    void execute(AnvilPlugin plugin, CommandSender sender, String[] args);

    default @NotNull List<String> tabComplete(AnvilPlugin plugin, CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}