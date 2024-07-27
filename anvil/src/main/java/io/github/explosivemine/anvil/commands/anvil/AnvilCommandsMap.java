package io.github.explosivemine.anvil.commands.anvil;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.commands.CommandsMap;
import io.github.explosivemine.anvil.commands.anvil.subcommands.Help;
import io.github.explosivemine.anvil.commands.anvil.subcommands.Open;
import io.github.explosivemine.anvil.commands.anvil.subcommands.Reload;

public class AnvilCommandsMap extends CommandsMap {
    public AnvilCommandsMap(AnvilPlugin plugin) {
        super(plugin);
    }

    @Override
    public void loadDefaultCommands() {
        registerCommand(new Help());
        registerCommand(new Reload());
        registerCommand(new Open());
    }

}