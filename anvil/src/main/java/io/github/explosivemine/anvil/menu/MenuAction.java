package io.github.explosivemine.anvil.menu;

import io.github.explosivemine.anvil.utils.Action;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryEvent;

import java.util.function.Function;

public final class MenuAction<T extends InventoryEvent> extends Action<T, Boolean> {
    /**
     *
     * @param function action accepts an InventoryEvent and the return value is whether the event is cancelled or not if possible
     */
    public MenuAction(Function<T, Boolean> function) {
        super(function);
    }

    @Override
    public Boolean apply(T event) {
        boolean cancel = super.apply(event);
        if (cancel && event instanceof Cancellable cancellable)
            cancellable.setCancelled(true);

        return cancel;
    }
}