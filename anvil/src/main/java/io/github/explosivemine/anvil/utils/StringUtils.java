package io.github.explosivemine.anvil.utils;

import org.bukkit.ChatColor;

public final class StringUtils {
    private StringUtils() { }

    public static String colour(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String replaceArgs(String msg, Object... objects) {
        if (msg == null) return null;

        for (int i = 0; i < objects.length; i++) {
            String objectString = objects[i].toString();
            msg = msg.replace("{" + i + "}", objectString);
        }

        return msg;
    }

}