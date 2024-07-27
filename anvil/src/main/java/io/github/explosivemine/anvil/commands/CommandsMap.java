package io.github.explosivemine.anvil.commands;

import io.github.explosivemine.anvil.AnvilPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public abstract class CommandsMap {
    private final AnvilPlugin plugin;

    private final Map<String, SubCommand> subCommands = new HashMap<>();
    private final Map<String, SubCommand> aliasesToCommand = new HashMap<>();

    protected CommandsMap(AnvilPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void loadDefaultCommands();

    public void registerCommand(SubCommand subCommand) {
        String label = subCommand.getLabels().get(0).toLowerCase();

        if (subCommands.containsKey(label)) {
            plugin.getLogger().log(Level.WARNING, "Please notify the developer about the conflicting command: {0}",
                    label);
            return;
        }

        // remove aliases which conflict with the new command label
        aliasesToCommand.values().removeIf(cmd -> cmd.getLabels().get(0).equals(label));

        subCommands.put(label, subCommand);
        for (int i = 1; i < subCommand.getLabels().size(); i++)
            aliasesToCommand.put(subCommand.getLabels().get(i).toLowerCase(), subCommand);
    }

    public @Nullable SubCommand getSubCommand(String label) {
        label = label.toLowerCase();
        return subCommands.getOrDefault(label, aliasesToCommand.get(label));
    }

    public List<SubCommand> getSubCommands() {
        return List.copyOf(subCommands.values());
    }

}