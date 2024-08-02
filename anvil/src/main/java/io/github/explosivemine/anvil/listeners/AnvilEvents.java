package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.Permissions;
import io.github.explosivemine.anvil.config.parser.Lang;
import io.github.explosivemine.anvil.menu.items.builders.ItemBuilder;
import io.github.explosivemine.anvil.menu.type.Menu;
import io.github.explosivemine.anvil.menu.type.anvil.VersionMatcher;
import io.github.explosivemine.anvil.version.VersionWrapper;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public final class AnvilEvents extends EventListener {
    // PrepareAnvilEvent cooldown
    private final HashMap<UUID, Long> players = new HashMap<>();
    private final int maxCost;

    public AnvilEvents(AnvilPlugin plugin) {
        super(plugin);
        maxCost = plugin.getConfigParser().getCostLimit();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        Menu menu = getPlugin().getMenuManager().getMenu(event.getView().getPlayer());
        if (menu == null)
            return;

        Set<UUID> instaBuild = getPlugin().getMenuManager().getInstaBuild();

        Player player = (Player) event.getView().getPlayer();
        UUID uuid = player.getUniqueId();

        VersionWrapper versionWrapper = new VersionMatcher().match();
        if (instaBuild.contains(uuid) && !player.getGameMode().equals(GameMode.CREATIVE)) {
            versionWrapper.setInstaBuild(player, false);
        }

        AnvilInventory inv = event.getInventory();
        boolean slot1HasItem = isItemPresent(inv, 1);
        boolean onlyRename = isItemPresent(inv, 0) && !slot1HasItem
                && !inv.getItem(0).getItemMeta().getDisplayName().equals(inv.getRenameText());

        // handle renaming costs
        if (getPlugin().getConfigParser().isChangeRenameCost() && onlyRename) {
            inv.setRepairCost(getPlugin().getConfigParser().getRenameCost());
        } else if (!onlyRename) {
            // handle max repair costs
            int cost = versionWrapper.getRepairCost(event);
            if (cost < maxCost) {
                // leave it as too expensive and send the player a message about what the repair cost would have been
                if (cost > player.getLevel() && player.getGameMode() != GameMode.CREATIVE) {
                    if (slot1HasItem && shouldSendMessage(player.getUniqueId())) {
                        Lang.TOO_EXPENSIVE.send(player, cost);
                    }
                    return;
                }

                // the player has enough xp and the repair is below the limit, so we should
                // display the green text and set maximum cost
                if (cost > 39) {
                    inv.setMaximumRepairCost(maxCost);
                    inv.setRepairCost(cost);
                    instaBuild.add(uuid);
                    new VersionMatcher().match().setInstaBuild(player, true);
                }
            } else {
                inv.setMaximumRepairCost(maxCost);
            }
        }

        // handle colours
        ItemStack result = event.getResult();
        String text = inv.getRenameText();
        if (result == null || !result.hasItemMeta() || text == null)
            return;


        if (getPlugin().getConfigParser().isColours()
                && (Permissions.COLOUR.hasPermission(event.getView().getPlayer())
                || Permissions.COLOR.hasPermission(event.getView().getPlayer()))) {
            event.setResult(new ItemBuilder(result)
                    .setDisplayName(Lang.colour(text))
                    .getItem());
        }
    }

    // the AnvilPrepareEvent is called multiple times, so we have to limit the number of messages sent
    private boolean shouldSendMessage(UUID uuid) {
        if (System.currentTimeMillis() - players.getOrDefault(uuid, 0L) < 10)
            return false;

        players.put(uuid, System.currentTimeMillis());
        return true;
    }

    private boolean isItemPresent(AnvilInventory inv, int slot) {
        return (inv.getItem(slot) != null && !inv.getItem(slot).getType().isAir());
    }

}