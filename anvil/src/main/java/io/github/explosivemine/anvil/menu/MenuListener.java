package io.github.explosivemine.anvil.menu;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.menu.type.Menu;
import io.github.explosivemine.anvil.menu.type.anvil.VersionMatcher;
import io.github.explosivemine.anvil.player.SPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryHolder;

public final class MenuListener implements Listener {
    private final AnvilPlugin plugin;

    public MenuListener(AnvilPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        SPlayer sPlayer = plugin.getSPlayerManager().get(event.getWhoClicked().getUniqueId());

        if (!(holder instanceof Menu) && plugin.getMenuManager().getMenu(event.getInventory().getHolder()) == null) {
            if (event.getInventory() instanceof AnvilInventory) {
                plugin.getMenuManager().getMenu(MenuIdentifier.ANVIL).clickItem(sPlayer, event);
            } else return;
        }

        Menu menu = holder instanceof Menu ? (Menu) holder : plugin.getMenuManager().getMenu(event.getInventory().getHolder());
        if (menu == null)
            return;

        menu.clickItem(sPlayer, event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && event.getInventory().getType().equals(InventoryType.ANVIL)) {
            Player player = (Player) event.getPlayer();
            new VersionMatcher().match().setInstaBuild(player, false);
            plugin.getMenuManager().getInstaBuild().remove(player.getUniqueId());
        }

        if (!(event.getInventory().getHolder() instanceof Menu menu))
            return;

        SPlayer sPlayer = plugin.getSPlayerManager().get(event.getPlayer().getUniqueId());
        menu.onClose(event, sPlayer);
    }

}