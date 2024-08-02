package io.github.explosivemine.anvil.player;

import io.github.explosivemine.anvil.AnvilPlugin;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;
import java.util.function.Consumer;

public final class SPlayer {
    private final AnvilPlugin plugin;

    @Getter private final UUID uuid;

    public SPlayer(AnvilPlugin plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public OfflinePlayer toOfflinePlayer() {
        return plugin.getServer().getOfflinePlayer(uuid);
    }

    private Player toPlayer() {
        return toOfflinePlayer().getPlayer();
    }

    public boolean isOffline() {
        return !toOfflinePlayer().isOnline();
    }

    public String getName() {
        return toOfflinePlayer().getName();
    }

    public void runIfOnline(Consumer<Player> consumer) {
        if (isOffline())
            return;

        consumer.accept(toPlayer());
    }

    public boolean isSleeping() {
        if (isOffline())
            return false;

        return toPlayer().isSleeping();
    }
    public void openInventory(Inventory inventory) {
        runIfOnline(player -> player.openInventory(inventory));
    }

}