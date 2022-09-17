package io.github.explosivemine.anvil.version;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface VersionWrapper {
    Inventory openInventory(Player player);

    void setInstaBuild(Player player, boolean value);

    Object getServerPlayer(Player player);

}