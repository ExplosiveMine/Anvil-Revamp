package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import org.bukkit.event.Listener;

public abstract class EventListener implements Listener {
    protected final AnvilPlugin plugin;

    public EventListener(AnvilPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}