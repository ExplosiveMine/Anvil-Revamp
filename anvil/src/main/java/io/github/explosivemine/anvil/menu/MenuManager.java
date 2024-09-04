package io.github.explosivemine.anvil.menu;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.menu.impl.Anvil;
import io.github.explosivemine.anvil.menu.type.Menu;
import io.github.explosivemine.anvil.player.SPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class MenuManager {
    private final AnvilPlugin plugin;

    private final Map<MenuIdentifier, Menu> menus = new EnumMap<>(MenuIdentifier.class);

    private final Map<InventoryHolder, MenuIdentifier> entityMenus = new HashMap<>();

    // players which have instaBuild enabled
    @Getter
    private final Set<UUID> instaBuild = new HashSet<>();

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

    public void open(MenuIdentifier menuIdentifier, SPlayer sPlayer, InventoryHolder holder) {
        entityMenus.put(holder, menuIdentifier);
        getMenu(menuIdentifier).open(sPlayer, holder);
    }

    public @Nullable Menu getMenu(InventoryHolder holder) {
        return getMenu(entityMenus.get(holder));
    }

    public Menu getMenu(MenuIdentifier identifier) {
        if (menus.isEmpty())
            loadMenus();

        return menus.get(identifier);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    public void onClose(InventoryHolder inventoryHolder) {
        entityMenus.remove(inventoryHolder);
    }

}