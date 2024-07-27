package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.Permissions;
import io.github.explosivemine.anvil.config.parser.ConfigParser;
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
        ConfigParser configParser = getPlugin().getConfigSettings().getConfigParser();
        if (configParser.isVirtual()
                && action == Action.RIGHT_CLICK_AIR
                && (mat == Material.ANVIL || mat == Material.CHIPPED_ANVIL || mat == Material.DAMAGED_ANVIL)
                && Permissions.VIRTUAL.hasPermission(player)) {
            getPlugin().getMenuManager().open(MenuIdentifier.ANVIL, getPlugin().getSPlayerManager().get(player), player);
        }

        if (configParser.isUnbreakable()
                && action == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null
                && event.getClickedBlock().getType().toString().contains("ANVIL")
                && Permissions.UNBREAKABLE.hasPermission(player)
                && event.useInteractedBlock() != Event.Result.DENY) {
            event.setCancelled(true);
            getPlugin().getMenuManager().open(MenuIdentifier.ANVIL, getPlugin().getSPlayerManager().get(player), player);
        }
    }
}