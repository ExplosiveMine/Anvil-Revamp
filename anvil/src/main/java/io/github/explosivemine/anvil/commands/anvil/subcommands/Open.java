package io.github.explosivemine.anvil.commands.anvil.subcommands;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.Permissions;
import io.github.explosivemine.anvil.commands.anvil.AnvilSubCommand;
import io.github.explosivemine.anvil.config.parser.Lang;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Open implements AnvilSubCommand {
    @Override
    public @NotNull List<String> getLabels() {
        return Collections.singletonList("open");
    }

    @Override
    public @NotNull String getUsage() {
        return "open <player-name>";
    }

    @Override
    public @NotNull String getDescription() {
        return "Opens the virtual anvil for yourself or the specified player.";
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return true;
    }

    @Override
    public @NotNull Permissions getPermission() {
        return Permissions.OPEN_OTHERS;
    }

    @Override
    public void execute(AnvilPlugin plugin, CommandSender sender, String[] args) {
        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            Lang.INVALID_PLAYER.send(sender);
            return;
        }

        plugin.getMenuManager().open(MenuIdentifier.ANVIL, plugin.getSPlayerManager().get(target), target);
    }

    @Override
    public @NotNull List<String> tabComplete(AnvilPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 2) {
            return plugin.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(s -> s.contains(args[1].toLowerCase()))
                    .toList();
        }

        return new ArrayList<>();
    }

}