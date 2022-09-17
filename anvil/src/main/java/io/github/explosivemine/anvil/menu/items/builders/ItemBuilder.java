package io.github.explosivemine.anvil.menu.items.builders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class ItemBuilder extends BaseItemBuilder<ItemBuilder> {
    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(ItemStack item) {
        super(item);
    }

}