package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import lombok.Getter;
import org.bukkit.event.Listener;

@Getter
public abstract class EventListener implements Listener {
    private final AnvilPlugin plugin;

    protected EventListener(AnvilPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}