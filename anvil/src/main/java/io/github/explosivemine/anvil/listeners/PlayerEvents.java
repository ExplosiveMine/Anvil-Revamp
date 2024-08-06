package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.Permissions;
import io.github.explosivemine.anvil.config.parser.ConfigParser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
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
        ConfigParser configParser = getPlugin().getConfigParser();
        if (configParser.isVirtual()
                && action == Action.RIGHT_CLICK_AIR
                && (mat == Material.ANVIL || mat == Material.CHIPPED_ANVIL || mat == Material.DAMAGED_ANVIL)
                && Permissions.VIRTUAL.hasPermission(player)) {
            getPlugin().getAnvilManager().openAnvil(player);
            event.setCancelled(true);
            return;
        }

        if (action == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null
                && Permissions.OPEN.hasPermission(event.getPlayer())
                && !event.getPlayer().isSneaking()
                && event.getClickedBlock().getType().toString().contains("ANVIL")
                && event.useInteractedBlock() != Event.Result.DENY) {
            event.setCancelled(true);
            getPlugin().getAnvilManager().openAnvil(player, event.getClickedBlock().getLocation());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getBlock().toString().contains("ANVIL") && event.getTo().isAir()) {
            getPlugin().getAnvilManager().onAnvilFall(event.getBlock().getLocation());
        }
    }
}