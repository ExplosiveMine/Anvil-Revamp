package io.github.explosivemine.anvil;

import lombok.Getter;
import org.bukkit.permissions.Permissible;
import org.jetbrains.annotations.NotNull;

@Getter
public enum Permissions {
    OPEN("anvil.open"),
    OPEN_OTHERS("anvil.open.others"),
    HELP("anvil.help"),
    RELOAD("anvil.reload"),
    ADMIN("anvil.admin"),
    VIRTUAL("anvil.virtual"),
    UNBREAKABLE("anvil.unbreakable"),
    BYPASS_COOLDOWN("anvil.bypass.cooldown");

    private final @NotNull String node;

    Permissions(@NotNull String node) {
        this.node = node;
    }

    public boolean hasPermission(@NotNull Permissible permissible) {
        return getNode().isEmpty() || permissible.hasPermission(node) || permissible.hasPermission(ADMIN.getNode());
    }

}