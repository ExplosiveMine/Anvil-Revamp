package io.github.explosivemine.anvil.commands.anvil.subcommands;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.Permissions;
import io.github.explosivemine.anvil.commands.CommandIdentifier;
import io.github.explosivemine.anvil.commands.SubCommand;
import io.github.explosivemine.anvil.commands.anvil.AnvilSubCommand;
import io.github.explosivemine.anvil.commands.arguments.CommandArguments;
import io.github.explosivemine.anvil.config.parser.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public final class Help implements AnvilSubCommand {
    private List<SubCommand> commands = new ArrayList<>();

    @Override
    public @NotNull List<String> getLabels() {
        return Collections.singletonList("help");
    }

    @Override
    public @NotNull String getUsage() {
        return "help [page]";
    }

    @Override
    public @NotNull String getDescription() {
        return "List of commands which the user can execute.";
    }

    @Override
    public int getMinArgs() {
        return 1;
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
        return Permissions.HELP;
    }

    @Override
    public void execute(AnvilPlugin plugin, CommandSender sender, String[] args) {
        List<SubCommand> subCommands = getCommands(plugin).stream()
                .filter(SubCommand::displayCommand)
                .filter(subCommand -> subCommand.getPermission().hasPermission(sender))
                .toList();

        int numPages = (int) Math.ceil(subCommands.size() / 7d);
        int page = 1;
        if (args.length == 2) {
            Optional<Integer> optionalPage = CommandArguments.parseInteger(sender, args[1], true);
            if (optionalPage.isEmpty())
                return;

            page = optionalPage.get();
        }

        if (page < 1 || page > numPages) {
            Lang.INVALID_NUMBER.send(sender, page);
            return;
        }

        if (subCommands.isEmpty()) {
            Lang.PLAYER_NO_PERM.send(sender);
            return;
        }

        Lang.HELP_HEADER.send(sender, page, numPages);

        if (Permissions.OPEN.hasPermission(sender)) {
            Lang.HELP_ANVIL_COMMAND_DESCRIPTION.send(sender);
        }

        for (int i = (page-1) * 7; i < page * 7 ; i++) {
            if (i == subCommands.size())
                break;

            SubCommand subCommand = subCommands.get(i);
            String description = subCommand.getDescription();
            Lang.HELP_LINE.send(sender,subCommand.getCommandIdentifier().getLabel() + " " + subCommand.getUsage(), description);
        }

        if (page != numPages)
            Lang.HELP_NEXT_PAGE.send(sender,page + 1);
        else
            Lang.HELP_FOOTER.send(sender);
    }

    @Override
    public @NotNull List<String> tabComplete(AnvilPlugin plugin, CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();

        if (args.length == 2) {
            List<SubCommand> subCommands = getCommands(plugin).stream()
                    .filter(subCommand -> subCommand.displayCommand() && subCommand.getPermission().hasPermission(sender))
                    .toList();

            int numPages = (int) Math.ceil(subCommands.size() / 7d);
            for (int i = 1; i <= numPages; i++)
                list.add(i + "");
        }

        return list;
    }

    public Collection<SubCommand> getCommands(AnvilPlugin plugin) {
        if (commands.isEmpty()) {
            commands.addAll(plugin.getAnvilCommand().getCommandsMap().getSubCommands());
            commands = commands.stream().sorted(Comparator.comparing(SubCommand::getCommandIdentifier,
                    Comparator.comparing(CommandIdentifier::getLabel)).thenComparing(o -> o.getLabels().get(0))).collect(Collectors.toList());
        }

        return commands;
    }

}