package io.github.explosivemine.anvil.utils;

import org.bukkit.event.Event;

import java.util.function.Function;

public abstract class Action<T extends Event, R> {
    protected final Function<T, R> action;

    public Action(Function<T, R> action) {
        this.action = action;
    }

    public R apply(T event) {
        return action.apply(event);
    }

}
