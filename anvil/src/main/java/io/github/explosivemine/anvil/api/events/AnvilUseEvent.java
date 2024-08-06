package io.github.explosivemine.anvil.api.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

@Getter
public class AnvilUseEvent extends InventoryEvent implements Cancellable {
    @Setter
    private Result result = Result.DEFAULT;

    private final int clickedSlot;

    public AnvilUseEvent(@NotNull InventoryView view, int clickedSlot) {
        super(view);
        this.clickedSlot = clickedSlot;
    }

    public AnvilInventory getAnvilInventory() {
        return (AnvilInventory) getView().getInventory(clickedSlot);
    }

    public HumanEntity getWhoClicked() {
        return getView().getPlayer();
    }

    @Override
    public boolean isCancelled() {
        return result == Result.DENY;
    }

    @Override
    public void setCancelled(boolean toCancel) {
        this.result = toCancel ? Result.DENY : Result.ALLOW;
    }
}
