package io.github.explosivemine.anvil.commands.anvil;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.commands.CommandIdentifier;
import io.github.explosivemine.anvil.Permissions;
import io.github.explosivemine.anvil.commands.CommandsMap;
import io.github.explosivemine.anvil.commands.SubCommand;
import io.github.explosivemine.anvil.config.parser.Lang;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import io.github.explosivemine.anvil.utils.StringUtils;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.*;

public class AnvilPluginCommand implements CommandExecutor, TabCompleter {
    @Getter private final AnvilPlugin plugin;

    @Getter private final CommandIdentifier identifier = CommandIdentifier.ANVIL;

    @Getter private final CommandsMap commandsMap;

    private final Map<UUID, Map<String, Long>> commandsCooldown = new HashMap<>();

    public AnvilPluginCommand(AnvilPlugin plugin, CommandsMap commandsMap) {
        this.plugin = plugin;
        this.commandsMap = commandsMap;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command bukkitCmc, @NotNull String label,
                             @NotNull String[] args) {
        if (args.length == 0) {
            if (!Permissions.OPEN.hasPermission(sender)) {
                Lang.PLAYER_NO_PERM.send(sender);
                return false;
            }

            if (sender instanceof Player player) {
                plugin.getMenuManager().open(MenuIdentifier.ANVIL, plugin.getSPlayerManager().get(player), player);
                return true;
            } else {
                Lang.CONSOLE_NO_PERM.send(sender);
                return false;
            }
        }

        SubCommand command = commandsMap.getSubCommand(args[0]);
        if (command == null) {
            plugin.getServer().dispatchCommand(sender, label + " help");
            return true;
        }

        if (!(sender instanceof Player) && !command.canBeExecutedByConsole()) {
            Lang.CONSOLE_NO_PERM.send(sender);
            return false;
        }

        if (!command.getPermission().hasPermission(sender)) {
            Lang.PLAYER_NO_PERM.send(sender);
            return false;
        }

        if (args.length < command.getMinArgs() || args.length > command.getMaxArgs()) {
            Lang.COMMAND_USAGE.send(sender, label + " " + command.getUsage());
            return false;
        }

        if (sender instanceof Player player && !Permissions.BYPASS_COOLDOWN.hasPermission(sender)) {
            UUID uuid = player.getUniqueId();
            String commandLabel = command.getLabels().get(0);

            long timeNow = System.currentTimeMillis();
            long timeToExecute = commandsCooldown.containsKey(uuid) ?
                    commandsCooldown.get(uuid).getOrDefault(commandLabel, -1L) : -1L;

            if (timeNow < timeToExecute) {
                Lang.COMMAND_COOLDOWN.send(sender, StringUtils.formatTime(Duration.ofMillis(timeToExecute - timeNow)));
                return false;
            }

            commandsCooldown.computeIfAbsent(uuid, k -> new HashMap<>()).put(commandLabel,
                    timeNow + command.getCooldown());
        }

        if (canSenderExecuteCommand(command, sender)) {
            command.execute(plugin, sender, args);
            return true;
        }

        return false;
    }

    /**
     * This is intended to be used like a {@link java.util.function.BiPredicate}. It is called
     * before the command is executed. If this method returns true, the command will be executed.
     * If it returns false, the command will not be executed.
     * <p>
     * Subclasses override this method. This may be to avoid repeating the same checks in a group of commands.
     * @param command the subcommand that will be executed
     * @param sender who executed the command
     * @return the command will be executed if this method returns true. If it returns false,
     * the command will not be executed.
     */
    public boolean canSenderExecuteCommand(@NotNull SubCommand command, @NotNull CommandSender sender) {
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command abcCommand,
                                                @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            SubCommand command = commandsMap.getSubCommand(args[0]);
            if (command != null) {
                if (!command.getPermission().hasPermission(sender)) {
                    return new ArrayList<>();
                } else {
                    return command.tabComplete(plugin, sender, args);
                }
            }
        }

        return commandsMap.getSubCommands().stream()
                .filter(subCommand -> subCommand.getPermission().hasPermission(sender))
                .filter(subCommand -> !subCommand.getLabels().isEmpty())
                .map(subCommand -> subCommand.getLabels().get(0))
                .filter(s -> s.contains(args[0].toLowerCase()))
                .toList();
    }

}