package io.github.explosivemine.anvil.listeners;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.config.parser.Lang;
import io.github.explosivemine.anvil.menu.items.builders.ItemBuilder;
import io.github.explosivemine.anvil.menu.type.Menu;
import io.github.explosivemine.anvil.menu.type.anvil.VersionMatcher;
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
    // prepare anvil event cooldown
    private final HashMap<UUID, Long> players = new HashMap<>();
    private final int maxCost;

    public AnvilEvents(AnvilPlugin plugin) {
        super(plugin);
        maxCost = plugin.getConfigSettings().getConfigParser().getCostLimit();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        Menu menu = plugin.getMenuManager().getMenu(event.getView().getPlayer());
        if (menu == null)
            return;

        Set<UUID> instaBuild = plugin.getMenuManager().getInstaBuild();

        Player player = (Player) event.getView().getPlayer();
        UUID uuid = player.getUniqueId();
        if (instaBuild.contains(uuid) && !player.getGameMode().equals(GameMode.CREATIVE))
            new VersionMatcher().match().setInstaBuild(player, false);

        AnvilInventory inv = event.getInventory();
        boolean slot1HasItem = isItemPresent(inv, 1);
        boolean onlyRename = isItemPresent(inv, 0) && !slot1HasItem
                && !inv.getItem(0).getItemMeta().getDisplayName().equals(inv.getRenameText());

        // handle renaming costs
        if (plugin.getConfigSettings().getConfigParser().isChangeRenameCost() && onlyRename) {
            inv.setRepairCost(plugin.getConfigSettings().getConfigParser().getRenameCost());
        } else if (!onlyRename) {
            // handle max repair costs
            int cost = inv.getRepairCost();
            if (cost < maxCost) {
                // leave it as too expensive and send the player a message about what the repair cost would have been
                if (cost > player.getLevel() && player.getGameMode() != GameMode.CREATIVE) {
                    if (slot1HasItem && shouldSendMessage(player.getUniqueId()))
                        Lang.TOO_EXPENSIVE.send(player, cost);
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

        if (plugin.getConfigSettings().getConfigParser().isColours() && event.getView().getPlayer().hasPermission("anvil.colour"))
            event.setResult(new ItemBuilder(result)
                .setDisplayName(text)
                .toItem());
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