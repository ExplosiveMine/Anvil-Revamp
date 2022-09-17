package io.github.explosivemine.anvil.menu.items.builders;

import io.github.explosivemine.anvil.utils.StringUtils;
import io.github.explosivemine.anvil.menu.items.MenuItem;
import io.github.explosivemine.anvil.player.SPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked cast")
public abstract class BaseItemBuilder<Builder extends BaseItemBuilder<Builder>> {
    protected ItemStack item;

    protected ItemMeta meta;

    protected BiConsumer<InventoryClickEvent, SPlayer> action;

    protected BiFunction<ItemStack, SPlayer, ItemStack> function;

    public BaseItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public Builder setAmount(int amount) {
        item.setAmount(amount);
        return (Builder) this;
    }

    public Builder setDisplayName(String displayName) {
        meta.setDisplayName(StringUtils.colour(displayName));
        return (Builder) this;
    }


    public Builder setLore(String...lore) {
        meta.setLore(Arrays.stream(lore).map(s -> StringUtils.colour(s)).collect(Collectors.toList()));
        return (Builder) this;
    }

    public Builder setGlowing(boolean glowing) {
        if (glowing) {
            meta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);

        return (Builder) this;
    }

    public Builder setAction(BiConsumer<InventoryClickEvent, SPlayer> action) {
        this.action = action;
        return (Builder) this;
    }

    public Builder setFunction(BiFunction<ItemStack, SPlayer, ? extends BaseItemBuilder<?>> function) {
        this.function = (item, bPlayer) -> function.apply(item, bPlayer).toItem();
        return (Builder) this;
    }

    public ItemStack toItem() {
        item.setItemMeta(meta);
        return item;
    }

    private void setItem(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public MenuItem toMenuItem() {
        return new MenuItem(toItem(), action, function);
    }

}