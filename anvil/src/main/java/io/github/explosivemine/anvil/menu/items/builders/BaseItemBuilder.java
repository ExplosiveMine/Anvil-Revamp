package io.github.explosivemine.anvil.menu.items.builders;

import io.github.explosivemine.anvil.menu.items.MenuItem;
import io.github.explosivemine.anvil.player.SPlayer;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseItemBuilder<B> {
    @Getter
    private final ItemStack item;

    private BiConsumer<InventoryClickEvent, SPlayer> action;

    private BiFunction<ItemStack, SPlayer, ItemStack> function;

    protected BaseItemBuilder(@NotNull ItemStack item) {
        this.item = item;
    }

    public @NotNull B setAmount(int amount) {
        item.setAmount(amount);
        return getThis();
    }

    public @NotNull B setDisplayName(@NotNull String displayName) {
        modifyItemMeta(meta -> meta.setDisplayName(displayName));
        return getThis();
    }


    public @NotNull B setLore(String... lore) {
        // Stream#toList() returns an immutable list. Since I am unsure how it will be handled, I'm using the method
        // that returns a mutable list.
        modifyItemMeta(meta -> meta.setLore(Arrays.stream(lore).collect(Collectors.toList())));
        return getThis();
    }

    public @NotNull B setGlowing(boolean glowing) {
        modifyItemMeta(meta -> {
            if (glowing) {
                meta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
        });

        return getThis();
    }

    public @NotNull B setAction(@NotNull BiConsumer<InventoryClickEvent, SPlayer> action) {
        this.action = action;
        return getThis();
    }

    public @NotNull B setFunction(@NotNull BiFunction<ItemStack, SPlayer, ? extends BaseItemBuilder<?>> function) {
        this.function = (itemStack, bPlayer) -> function.apply(itemStack, bPlayer).getItem();
        return getThis();
    }

    public @NotNull MenuItem toMenuItem() {
        return new MenuItem(getItem(), action, function);
    }

    public abstract @NotNull B getThis();

    private void modifyItemMeta(@NotNull Consumer<ItemMeta> consumer) {
        if (!item.hasItemMeta())
            return;

        ItemMeta itemMeta = item.getItemMeta();
        consumer.accept(itemMeta);
        item.setItemMeta(itemMeta);
    }

    private <T> @Nullable T getFromItemMeta(@NotNull Function<ItemMeta, T> function) {
        if (!item.hasItemMeta())
            return null;

        ItemMeta itemMeta = item.getItemMeta();
        T returnValue = function.apply(itemMeta);
        item.setItemMeta(itemMeta);
        return returnValue;
    }

}