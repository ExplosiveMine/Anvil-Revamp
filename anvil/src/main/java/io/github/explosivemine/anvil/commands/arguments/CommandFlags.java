package io.github.explosivemine.anvil.commands.arguments;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public enum CommandFlags {
    PERMANENT("--permanent"),
    PLAYER_LOCATION("--here"),
    VECTOR("--vector"),
    ENTITY("--entity");

    @Getter private final String flag;

    CommandFlags(String flag) {
        this.flag = flag;
    }

    public boolean isFlag(@Nullable String argument) {
        return flag.equalsIgnoreCase(argument);
    }

}