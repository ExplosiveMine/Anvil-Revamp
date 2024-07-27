package io.github.explosivemine.anvil.utils;

import org.bukkit.event.Event;

import java.util.function.Function;

public abstract class Action<T extends Event, R> {
    private final Function<T, R> function;

    protected Action(Function<T, R> function) {
        this.function = function;
    }

    public R apply(T event) {
        return function.apply(event);
    }

}
