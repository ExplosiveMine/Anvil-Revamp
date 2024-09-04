package io.github.explosivemine.anvil.menu.type;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.menu.MenuAction;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import io.github.explosivemine.anvil.menu.items.builders.BaseItemBuilder;
import io.github.explosivemine.anvil.player.SPlayer;
import io.github.explosivemine.anvil.utils.Executor;
import io.github.explosivemine.anvil.menu.items.MenuItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class Menu implements InventoryHolder {
    @Getter
    private final AnvilPlugin plugin;

    @Getter
    private final MenuIdentifier identifier;

    private final String title;
    @Getter @Setter
    private Function<SPlayer, String> titleProvider;

    @Getter
    private final InventoryType type;

    @Getter
    private final int size;

    @Getter
    private MenuAction<InventoryCloseEvent> closeAction;

    @Getter @Setter
    private BaseItemBuilder<?> filler;

    private final Map<Integer, MenuItem> defaultItems = new HashMap<>();
    private boolean setup = false;

    protected Menu(@NotNull AnvilPlugin plugin, @NotNull MenuIdentifier identifier, @NotNull String title,
                   @NotNull InventoryType type) {
        this(plugin, identifier, title, type, type.getDefaultSize());
    }

    protected Menu(@NotNull AnvilPlugin plugin, @NotNull MenuIdentifier identifier, @NotNull String title, int size) {
        this(plugin, identifier, title, InventoryType.CHEST, size);
    }

    private Menu(@NotNull AnvilPlugin plugin, @NotNull MenuIdentifier identifier, @NotNull String title,
                 @NotNull InventoryType type, int size) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.type = type;
        this.size = size;
        this.title = title;
    }

    /**
     * @return An inventory with default items and default title.
     */
    @Override
    public @NotNull Inventory getInventory() {
        return build(null, this);
    }

    public abstract void create();

    public void open(SPlayer sPlayer) {
        open(sPlayer, this);
    }

    public void open(SPlayer sPlayer, InventoryHolder inventoryHolder) {
        if (sPlayer.isSleeping())
            return;

        sPlayer.openInventory(build(sPlayer, inventoryHolder));
    }

    protected Inventory build(SPlayer sPlayer, InventoryHolder inventoryHolder) {
        if (!setup) {
            create();
            setup = true;
        }

        Inventory inventory = createInv(sPlayer, inventoryHolder);
        populate(inventory, sPlayer);
        return inventory;
    }

    public void populate(Inventory inventory, SPlayer sPlayer) {
        if (getFiller() != null) {
            for (int i = 0; i < size; i++)
                inventory.setItem(i, getFiller().getItem());
        }

        defaultItems.forEach((key, value) -> inventory.setItem(key, value.getItem(sPlayer)));
    }

    protected Inventory createInv(SPlayer sPlayer, InventoryHolder inventoryHolder) {
        String menuTitle = getTitle(sPlayer);
        return InventoryType.CHEST.equals(type) ? Bukkit.createInventory(inventoryHolder, size, menuTitle) :
                Bukkit.createInventory(inventoryHolder, type, menuTitle);
    }

    /**
     *
     * @param sPlayer the player to be passed to the titleProvider. If set to null, it just returns the default
     *                title
     * @return the title of the menu
     */
    public String getTitle(@Nullable SPlayer sPlayer) {
        return sPlayer == null || titleProvider == null ? title : titleProvider.apply(sPlayer);
    }

    public void onClose(InventoryCloseEvent event, SPlayer sPlayer) {
        boolean reopenInventory = false;

        if (closeAction != null) {
            reopenInventory = closeAction.apply(event);
        }

        if (reopenInventory) {
            Executor.sync(plugin, runnable -> sPlayer.openInventory(event.getInventory()), 1L);
        } else
            plugin.getMenuManager().onClose(getHolder(sPlayer));
    }

    public abstract InventoryHolder getHolder(SPlayer sPlayer);

    public void setItem(int slot, BaseItemBuilder<?> builder) {
        defaultItems.put(slot, builder.toMenuItem());
    }

    public void clickItem(SPlayer sPlayer, InventoryClickEvent event) {
        MenuItem item = defaultItems.get(event.getSlot());
        if (item != null && item.getAction() != null) {
            item.getAction().accept(event, sPlayer);
        }
    }

    // Gets the player's open inventory, and rebuilds it.
    public void update(SPlayer sPlayer) {
        sPlayer.runIfOnline(player -> populate(player.getOpenInventory().getTopInventory(), sPlayer));
    }

    public void setCloseAction(Function<InventoryCloseEvent, Boolean> closeAction) {
        this.closeAction = new MenuAction<>(closeAction);
    }
}