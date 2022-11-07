package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.config.parser.Lang;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import io.github.explosivemine.anvil.utils.Logging;
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

        event.setCancelled(true);
        plugin.getMenuManager().open(MenuIdentifier.ANVIL, plugin.getSPlayerManager().get(player), player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            new UpdateChecker(plugin, 77142).getVersion(s -> {
                if (s.compareTo("v" + plugin.getDescription().getVersion()) > 0) {
                    Lang.CUSTOM.send(player, "&3&l[Anvil] &bWe have a new update: " + s + ", you are running v" + plugin.getDescription().getVersion());
                    Lang.CUSTOM.send(player, "&bDownload it here: " + plugin.getDescription().getWebsite());
                }
            });
        }
    }

}