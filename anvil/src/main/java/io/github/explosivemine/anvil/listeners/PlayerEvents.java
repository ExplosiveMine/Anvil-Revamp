package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class PlayerEvents extends EventListener {
    public PlayerEvents(AnvilPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();

        Material mat = player.getInventory().getItemInMainHand().getType();
        if (plugin.getConfigSettings().getConfigParser().isVirtual()
                && action == Action.RIGHT_CLICK_AIR
                && (mat == Material.ANVIL || mat == Material.CHIPPED_ANVIL || mat == Material.DAMAGED_ANVIL)
                && player.hasPermission("anvil.virtual")) {
            plugin.getMenuManager().open(MenuIdentifier.ANVIL, plugin.getSPlayerManager().get(player), player);
        }

        if (plugin.getConfigSettings().getConfigParser().isUnbreakable()
                && action == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null
                && event.getClickedBlock().getType().toString().contains("ANVIL")
                && player.hasPermission("anvil.unbreakable")
                && event.useInteractedBlock() != Event.Result.DENY) {
            event.setCancelled(true);
            plugin.getMenuManager().open(MenuIdentifier.ANVIL, plugin.getSPlayerManager().get(player), player);
        }
    }
}