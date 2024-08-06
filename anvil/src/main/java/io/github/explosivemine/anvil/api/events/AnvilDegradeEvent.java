package io.github.explosivemine.anvil.api.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class AnvilDegradeEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private Result result = Result.DEFAULT;
    private final Material nextState;

    public AnvilDegradeEvent(@NotNull Block anvilBlock, Material nextState) {
        super(anvilBlock);
        this.nextState = nextState;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isAnvilDestroyed() {
        return nextState.isAir();
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
