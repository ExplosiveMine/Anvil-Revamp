package io.github.explosivemine.anvil.menu.type.anvil;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.api.events.AnvilUseEvent;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import io.github.explosivemine.anvil.menu.items.builders.ItemBuilder;
import io.github.explosivemine.anvil.menu.type.Menu;
import io.github.explosivemine.anvil.player.SPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class AnvilMenu extends Menu {
    protected AnvilMenu(AnvilPlugin plugin, MenuIdentifier identifier, String title) {
        super(plugin, identifier, title, InventoryType.ANVIL);
        setCloseAction(inventoryCloseEvent -> {
            plugin.getAnvilManager().onAnvilClose(((Player) inventoryCloseEvent.getPlayer()));
            return false;
        });
    }

    @Override
    protected Inventory createInv(SPlayer sPlayer, InventoryHolder inventoryHolder) {
        return new VersionMatcher().match().openInventory(sPlayer.toOfflinePlayer().getPlayer(), getTitle(sPlayer));
    }

    @Override
    public void create() {
        setItem(2, new ItemBuilder(Material.AIR).setAction((event, sPlayer) -> {
            if (!(event.getClickedInventory() instanceof AnvilInventory)) {
                return;
            }

            AnvilUseEvent anvilUseEvent = new AnvilUseEvent(event.getView(), 2);
            getPlugin().getServer().getPluginManager().callEvent(anvilUseEvent);
            event.setCancelled(anvilUseEvent.isCancelled());
        }));
    }

    @Override
    public void open(SPlayer sPlayer, InventoryHolder inventoryHolder) {
        if (sPlayer.isSleeping())
            return;

        build(sPlayer, inventoryHolder);
    }

    @Override
    public InventoryHolder getHolder(SPlayer sPlayer) {
        return sPlayer.isOffline() ? this : sPlayer.toOfflinePlayer().getPlayer();
    }

}