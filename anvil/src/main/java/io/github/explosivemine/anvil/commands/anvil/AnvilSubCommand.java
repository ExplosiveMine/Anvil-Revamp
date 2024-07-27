package io.github.explosivemine.anvil.commands.anvil;

import io.github.explosivemine.anvil.commands.CommandIdentifier;
import io.github.explosivemine.anvil.commands.SubCommand;
import org.jetbrains.annotations.NotNull;

public interface AnvilSubCommand extends SubCommand {
    @Override
    default @NotNull CommandIdentifier getCommandIdentifier() {
        return CommandIdentifier.ANVIL;
    }

}