package io.github.explosivemine.anvil.menu.type.anvil;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import io.github.explosivemine.anvil.menu.items.builders.ItemBuilder;
import io.github.explosivemine.anvil.menu.type.Menu;
import io.github.explosivemine.anvil.player.SPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class AnvilMenu extends Menu {
    public AnvilMenu(AnvilPlugin plugin, MenuIdentifier identifier, String title) {
        super(plugin, identifier, title, InventoryType.ANVIL);
    }

    @Override
    protected Inventory createInv(SPlayer sPlayer, InventoryHolder inventoryHolder) {
        return new VersionMatcher().match().openInventory(sPlayer.toOfflinePlayer().getPlayer());
    }

    @Override
    public void create() {
        setItem(2, new ItemBuilder(Material.AIR)
                .setAction((event, sPlayer) -> {
                    if (!(event.getClickedInventory() instanceof AnvilInventory inv))
                        return;

                    if (plugin.getMenuManager().getInstaBuild().contains(sPlayer.getUuid()))
                        sPlayer.runIfOnline(player -> player.setLevel(player.getLevel() - inv.getRepairCost()));
                }));
    }

    @Override
    public void open(SPlayer sPlayer, InventoryHolder inventoryHolder) {
        if (sPlayer.isSleeping())
            return;

        build(sPlayer, inventoryHolder);
    }

}