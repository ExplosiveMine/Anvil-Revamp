package io.github.explosivemine.anvil.version;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.Inventory;

public interface VersionWrapper {
    Inventory openInventory(Player player, String title);

    void setInstaBuild(Player player, boolean value);

    Object getServerPlayer(Player player);

    int getRepairCost(PrepareAnvilEvent prepareAnvilEvent);
}