package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.config.parser.Lang;
import io.github.explosivemine.anvil.utils.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerLoadEvent;

public final class ServerEvents extends EventListener {

    public ServerEvents(AnvilPlugin plugin) {
        super(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onServerLoad(ServerLoadEvent event) {
        new UpdateChecker(getPlugin(), 77142).checkVersion(s -> {
            getPlugin().getLogger().info(Lang.CUSTOM.get("&3&l[Anvil] &bWe have a new update: {0}, you are running {1}", s, getPlugin().getDescription().getVersion()));
            String website = getPlugin().getDescription().getWebsite();
            if (website != null) {
                getPlugin().getLogger().info(Lang.CUSTOM.get("&bDownload it here: {0}", website));
            }
        });
    }

}