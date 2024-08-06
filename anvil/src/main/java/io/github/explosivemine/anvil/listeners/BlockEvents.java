package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockEvents extends EventListener {
    public BlockEvents(AnvilPlugin plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType().toString().contains("ANVIL")) {
            getPlugin().getAnvilManager().onAnvilBreak(event.getBlock().getLocation());
        }
    }

}