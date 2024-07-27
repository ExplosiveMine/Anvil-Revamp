package io.github.explosivemine.anvil.utils;

import io.github.explosivemine.anvil.AnvilPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Executor {
    private Executor () {}
    public static void async(AnvilPlugin plugin, Consumer<BukkitRunnable> consumer, long...args) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                consumer.accept(this);
            }
        };

        if (args.length == 0)
            runnable.runTaskAsynchronously(plugin);
        else if (args.length == 1)
            runnable.runTaskLaterAsynchronously(plugin, args[0]);
        else if (args.length == 2)
            runnable.runTaskTimerAsynchronously(plugin, args[0], args[1]);
    }

    public static void sync(AnvilPlugin plugin, Consumer<BukkitRunnable> consumer, long...args) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                consumer.accept(this);
            }
        };

        if (args.length == 0)
            runnable.runTaskLater(plugin, 0L);
        else if (args.length == 1)
            runnable.runTaskLater(plugin, args[0]);
        else if (args.length == 2)
            runnable.runTaskTimer(plugin, args[0], args[1]);
    }

    public static ComplexTask<Void> create() {
        return new ComplexTask<>();
    }

    public static final class ComplexTask<T> {
        private final CompletableFuture<T> completableFuture = new CompletableFuture<>();

        private ComplexTask() { }

        public void sync(AnvilPlugin plugin, Consumer<T> consumer, long...args) {
            Executor.async(plugin, unused -> completableFuture.whenComplete((t, throwable) -> Executor.sync(plugin, unused1 -> consumer.accept(t), args)));
        }

        public <R> ComplexTask<R> async(AnvilPlugin plugin, Supplier<R> supplier, long...args) {
            ComplexTask<R> task = new ComplexTask<>();
            Executor.async(plugin, unused -> task.completableFuture.complete(supplier.get()), args);
            return task;
        }
    }

}