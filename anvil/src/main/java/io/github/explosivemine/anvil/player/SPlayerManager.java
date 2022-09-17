package io.github.explosivemine.anvil.player;

import io.github.explosivemine.anvil.AnvilPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SPlayerManager {
    private final AnvilPlugin plugin;

    private final Map<UUID, SPlayer> players = new HashMap<>();

    public SPlayerManager(AnvilPlugin plugin) {
        this.plugin = plugin;
    }

    public SPlayer create(UUID uuid) {
        return new SPlayer(plugin, uuid);
    }

    public SPlayer get(UUID uuid) {
        return players.computeIfAbsent(uuid, this::create);
    }

    public SPlayer get(Player player) {
        return get(player.getUniqueId());
    }

}