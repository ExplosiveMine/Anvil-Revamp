package io.github.explosivemine.anvil.menu;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.menu.impl.Anvil;
import io.github.explosivemine.anvil.menu.type.Menu;
import io.github.explosivemine.anvil.player.SPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class MenuManager {
    private final AnvilPlugin plugin;

    private final Map<MenuIdentifier, Menu> menus = new HashMap<>();

    private final Map<InventoryHolder, MenuIdentifier> entityMenus = new HashMap<>();

    //custom menu stuff

    // players which have instaBuild enabled
    @Getter private final Set<UUID> instaBuild = new HashSet<>();

    public MenuManager(AnvilPlugin plugin) {
        this.plugin = plugin;
    }

    private void loadMenus() {
        plugin.getServer().getPluginManager().registerEvents(new MenuListener(plugin), plugin);

        registerMenu(new Anvil(plugin));
    }

    private void registerMenu(Menu menu) {
        menus.put(menu.getIdentifier(), menu);
    }

    public void openParentMenu(SPlayer sPlayer, MenuIdentifier identifier) {
        MenuIdentifier parentIdentifier = identifier.getParentIdentifier();
        if (parentIdentifier != null)
            open(parentIdentifier, sPlayer);
    }

    public void open(MenuIdentifier menuIdentifier, SPlayer sPlayer) {
        getMenu(menuIdentifier).open(sPlayer);
    }

    public <T extends InventoryHolder> void open(MenuIdentifier menuIdentifier, SPlayer sPlayer, T holder) {
        entityMenus.put(holder, menuIdentifier);
        getMenu(menuIdentifier).open(sPlayer, holder);
    }

    public <T extends InventoryHolder> void open(MenuIdentifier menuIdentifier, Player player) {
        open(menuIdentifier, plugin.getSPlayerManager().get(player));
    }

    public @Nullable <T extends InventoryHolder> Menu getMenu(T holder) {
        return menus.get(entityMenus.get(holder));
    }

    public @NotNull Menu getMenu(MenuIdentifier identifier) {
        if (menus.isEmpty())
            loadMenus();

        return menus.get(identifier);
    }

    public void close(InventoryHolder inventoryHolder) {
        entityMenus.remove(inventoryHolder);
    }

}