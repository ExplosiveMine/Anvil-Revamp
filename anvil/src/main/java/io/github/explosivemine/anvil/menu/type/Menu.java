package io.github.explosivemine.anvil.menu.type;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.menu.MenuAction;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import io.github.explosivemine.anvil.menu.items.builders.BaseItemBuilder;
import io.github.explosivemine.anvil.player.SPlayer;
import io.github.explosivemine.anvil.utils.Executor;
import io.github.explosivemine.anvil.utils.StringUtils;
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class Menu implements InventoryHolder {
    protected final AnvilPlugin plugin;

    @Getter protected final MenuIdentifier identifier;

    protected final String title;
    protected Function<SPlayer, String> titleProvider;

    @Getter protected final InventoryType type;

    @Getter protected final int size;

    @Getter @Setter protected MenuAction<InventoryCloseEvent> closeAction;

    @Getter @Setter protected BaseItemBuilder<?> filler;

    protected final Map<Integer, MenuItem> defaultItems = new HashMap<>();
    protected boolean setup = false;

    public Menu(AnvilPlugin plugin, MenuIdentifier identifier, String title, InventoryType type) {
        this(plugin, identifier, title, type, type.getDefaultSize());
    }

    public Menu(AnvilPlugin plugin, MenuIdentifier identifier, String title, int size) {
        this(plugin, identifier, title, InventoryType.CHEST, size);
    }

    private Menu(AnvilPlugin plugin, MenuIdentifier identifier, String title, InventoryType type, int size) {
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
                inventory.setItem(i, getFiller().toItem());
        }

        defaultItems.forEach((key, value) -> inventory.setItem(key, value.getItem(sPlayer)));
    }

    protected Inventory createInv(SPlayer sPlayer, InventoryHolder inventoryHolder) {
        String title = sPlayer == null || titleProvider == null ? getTitle() : titleProvider.apply(sPlayer);
        return InventoryType.CHEST.equals(type) ? Bukkit.createInventory(inventoryHolder, size, title) : Bukkit.createInventory(inventoryHolder, type, title);
    }

    protected String getTitle() {
        return StringUtils.colour(title);
    }

    public void close(SPlayer sPlayer, boolean openParentMenu) {
        //todo
        // impl inventoryHolder
//        sPlayer.closeInventory();
//
//        if (openParentMenu)
//            plugin.getMenuManager().openParentMenu(sPlayer, identifier);
    }

    public void onClose(InventoryCloseEvent event, SPlayer sPlayer) {
        boolean reopenInventory = false;

        if (closeAction != null)
            reopenInventory = closeAction.apply(event);

        if (reopenInventory)
            Executor.sync(plugin, runnable -> sPlayer.openInventory(event.getInventory()), 1L);
        else
            plugin.getMenuManager().close(getHolder(sPlayer));
    }

    public InventoryHolder getHolder(SPlayer sPlayer) {
        return this;
    }

    public void setItem(int slot, BaseItemBuilder<?> builder) {
        defaultItems.put(slot, builder.toMenuItem());
    }

    public void clickItem(SPlayer sPlayer, InventoryClickEvent event) {
        MenuItem item = defaultItems.get(event.getSlot());
        if (item != null && item.getAction() != null)
            item.getAction().accept(event, sPlayer);
    }

    // Gets the player's open inventory, and rebuilds it.
    public void update(SPlayer sPlayer) {
        sPlayer.runIfOnline(player -> populate(player.getOpenInventory().getTopInventory(), sPlayer));
    }

}