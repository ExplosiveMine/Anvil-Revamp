package io.github.explosivemine.anvil.menu.items;

import io.github.explosivemine.anvil.player.SPlayer;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public final class MenuItem {
    private final ItemStack item;

    // action to do on inventory click
    @Getter private final BiConsumer<InventoryClickEvent, SPlayer> action;

    // modify the item based on the player
    @Getter private final BiFunction<ItemStack, SPlayer, ItemStack> function;

    public MenuItem(ItemStack item, BiConsumer<InventoryClickEvent, SPlayer> action) {
        this(item, action, null);
    }

    public MenuItem(ItemStack item, BiConsumer<InventoryClickEvent, SPlayer> action, BiFunction<ItemStack, SPlayer, ItemStack> function) {
        this.item = item;
        this.action = action;
        this.function = function;
    }

    public ItemStack getItem(SPlayer sPlayer) {
        return (function == null || sPlayer == null) ? item : function.apply(item.clone(), sPlayer);
    }

}