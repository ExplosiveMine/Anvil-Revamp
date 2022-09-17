package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.config.parser.Lang;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import io.github.explosivemine.anvil.utils.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerEvents extends EventListener {
    public PlayerEvents(AnvilPlugin plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.getConfigSettings().getConfigParser().isUnbreakable())
            return;

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (!event.getClickedBlock().getType().toString().contains("ANVIL"))
            return;

        Player player = event.getPlayer();
        if (!event.getPlayer().hasPermission("anvil.unbreakable"))
            return;

        plugin.getMenuManager().open(MenuIdentifier.ANVIL, player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp()) {
            new UpdateChecker(plugin, 77142).getVersion(s -> {
                if (s.compareTo(plugin.getDescription().getVersion()) > 0) {
                    Lang.CUSTOM.send(event.getPlayer(), "&c&l[Anvil] &cWe have a new update: " + s);
                    Lang.CUSTOM.send(event.getPlayer(), "&cPlease download it here:" + plugin.getDescription().getWebsite());
                }
            });
        }
    }

}