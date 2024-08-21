package io.github.explosivemine.anvil.menu;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.listeners.EventListener;
import io.github.explosivemine.anvil.menu.type.Menu;
import io.github.explosivemine.anvil.menu.type.anvil.VersionMatcher;
import io.github.explosivemine.anvil.player.SPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.*;

public final class MenuListener extends EventListener {

    public MenuListener(AnvilPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        SPlayer sPlayer = getPlugin().getSPlayerManager().get(event.getWhoClicked().getUniqueId());
        Menu menu = getPlugin().getMenuManager().getMenu(event.getWhoClicked());
        if (menu == null) {
            return;
        }

        menu.clickItem(sPlayer, event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && event.getInventory().getType().equals(InventoryType.ANVIL)) {
            Player player = (Player) event.getPlayer();
            new VersionMatcher().match().setInstaBuild(player, false);
            getPlugin().getMenuManager().getInstaBuild().remove(player.getUniqueId());
        }

        Menu menu = getPlugin().getMenuManager().getMenu(event.getPlayer());
        if (menu == null) {
            return;
        }

        SPlayer sPlayer = getPlugin().getSPlayerManager().get(event.getPlayer().getUniqueId());
        menu.onClose(event, sPlayer);
    }

}