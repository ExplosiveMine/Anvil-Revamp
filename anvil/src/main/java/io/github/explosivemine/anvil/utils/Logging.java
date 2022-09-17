package io.github.explosivemine.anvil.utils;

import io.github.explosivemine.anvil.AnvilPlugin;
import org.bukkit.Bukkit;

public final class Logging {

    public static void info(String s) {
        Bukkit.getLogger().info(s);
    }

    public static void warning(String s) {
        Bukkit.getLogger().warning(s);
    }

    public static void severe(String s) {
        Bukkit.getLogger().severe(s);
    }

    public static void debug(AnvilPlugin plugin, String... args) {
        if (!plugin.getConfigSettings().getConfigParser().isDebug())
            return;

        for (String s : args) {
            info("[" + plugin.getName() + "] " + s);
        }
    }

}