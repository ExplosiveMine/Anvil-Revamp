package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.utils.Logging;
import io.github.explosivemine.anvil.utils.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerLoadEvent;

public final class ServerEvents extends EventListener {

    public ServerEvents(AnvilPlugin plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onServerLoad(ServerLoadEvent event) {
        new UpdateChecker(plugin, 77142).checkVersion(s -> {
            Logging.info("&3&l[Anvil] &bWe have a new update: " + s + ", you are running " + plugin.getDescription().getVersion());
            Logging.info("&bDownload it here: " + plugin.getDescription().getWebsite());
        });
    }

}