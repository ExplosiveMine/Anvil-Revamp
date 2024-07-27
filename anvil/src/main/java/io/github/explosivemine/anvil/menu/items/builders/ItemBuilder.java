package io.github.explosivemine.anvil.menu.items.builders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ItemBuilder extends BaseItemBuilder<ItemBuilder> {
    public ItemBuilder(@NotNull Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(@NotNull ItemStack item) {
        super(item);
    }

    @Override
    public @NotNull ItemBuilder getThis() {
        return this;
    }

}