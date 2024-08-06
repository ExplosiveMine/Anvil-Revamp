package io.github.explosivemine.anvil;

import io.github.explosivemine.anvil.api.events.AnvilDegradeEvent;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import io.github.explosivemine.anvil.player.SPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AnvilManager {
    private final AnvilPlugin plugin;

    private final Map<Location, Set<SPlayer>> openedAnvilLocations = new HashMap<>();

    public AnvilManager(AnvilPlugin plugin) {
        this.plugin = plugin;
    }

    public void openAnvil(Player player, Location anvilLocation) {
        openAnvil(player);
        SPlayer sPlayer = getSPlayer(player);
        openedAnvilLocations.computeIfAbsent(anvilLocation, loc -> new HashSet<>()).add(sPlayer);
        sPlayer.setOpenedAnvilLocation(anvilLocation);
    }

    public void openAnvil(Player player) {
        plugin.getMenuManager().open(MenuIdentifier.ANVIL, getSPlayer(player), player);
    }

    public void closeAnvil(Player player) {
        plugin.getMenuManager().close(player);
    }

    public void onAnvilClose(Player player) {
        SPlayer sPlayer = getSPlayer(player);
        Location location = sPlayer.getOpenedAnvilLocation();
        if (location == null) {
            return;
        }

        Set<SPlayer> sPlayers = getPlayersAtAnvil(location);
        sPlayers.remove(sPlayer);
        if (sPlayers.isEmpty()) {
            openedAnvilLocations.remove(location);
        }

        sPlayer.setOpenedAnvilLocation(null);
    }

    public void onAnvilUse(Player player) {
        Location openedAnvilLocation = getOpenedAnvilLocation(player);
        if (openedAnvilLocation == null) {
            return;
        }

        if (plugin.getConfigParser().isUnbreakable() && Permissions.UNBREAKABLE.hasPermission(player)) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        Set<SPlayer> playersAtAnvil = getPlayersAtAnvil(openedAnvilLocation);
        if (!playersAtAnvil.contains(getSPlayer(player))) {
            return;
        }

        //88% chance for anvil to NOT deteriorate
        if (ThreadLocalRandom.current().nextInt(0, 100) >= 12) {
            return;
        }

        Block anvilBlock = openedAnvilLocation.getBlock();
        Material nextState;
        if (anvilBlock.getType() == Material.ANVIL) {
            nextState = Material.CHIPPED_ANVIL;
        } else if (anvilBlock.getType() == Material.CHIPPED_ANVIL) {
            nextState = Material.DAMAGED_ANVIL;
        } else if (anvilBlock.getType() == Material.DAMAGED_ANVIL) {
            nextState = Material.AIR;
        } else {
            return;
        }

        AnvilDegradeEvent anvilDegradeEvent = new AnvilDegradeEvent(anvilBlock, nextState);
        plugin.getServer().getPluginManager().callEvent(anvilDegradeEvent);
        if (anvilDegradeEvent.isCancelled()) {
            return;
        }

        anvilBlock.setType(anvilDegradeEvent.getNextState());
        if (anvilDegradeEvent.isAnvilDestroyed()) {
            player.playSound(anvilBlock.getLocation(), Sound.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS, 1, 1);
            playersAtAnvil.forEach(sPlayer -> sPlayer.runIfOnline(this::closeAnvil));
        }
    }

    private @Nullable Location getOpenedAnvilLocation(Player player) {
        return getSPlayer(player).getOpenedAnvilLocation();
    }

    private Set<SPlayer> getPlayersAtAnvil(Location location) {
        return openedAnvilLocations.getOrDefault(location, new HashSet<>());
    }

    public void onAnvilBreak(Location location) {
        getPlayersAtAnvil(location).forEach(sPlayer -> sPlayer.runIfOnline(this::closeAnvil));
    }

    public void onAnvilFall(Location location) {
        getPlayersAtAnvil(location).forEach(sPlayer -> sPlayer.runIfOnline(this::closeAnvil));
    }

    private SPlayer getSPlayer(Player player) {
        return plugin.getSPlayerManager().get(player);
    }
}