package io.github.explosivemine.anvil.commands.arguments;

import io.github.explosivemine.anvil.config.parser.Lang;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class CommandArguments {
    private CommandArguments() {
    }

    public static Optional<Integer> parseInteger(CommandSender sender, @NotNull String argument, boolean errorMessage) {
        try {
            return Optional.of(Integer.parseInt(argument));
        } catch (NumberFormatException ignored) {
            if (errorMessage)
                Lang.INVALID_NUMBER.send(sender, argument);
            return Optional.empty();
        }
    }

}