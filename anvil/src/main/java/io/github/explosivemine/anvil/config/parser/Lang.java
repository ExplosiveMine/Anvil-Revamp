package io.github.explosivemine.anvil.config.parser;

import io.github.explosivemine.anvil.AnvilPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum Lang {
    COMMAND_COOLDOWN,
    COMMAND_USAGE,
    CONSOLE_NO_PERM,
    HELP_ANVIL_COMMAND_DESCRIPTION,
    HELP_FOOTER,
    HELP_HEADER,
    HELP_LINE,
    HELP_NEXT_PAGE,
    INVALID_NUMBER,
    INVALID_PLAYER,
    PLAYER_NO_PERM,
    RELOAD_MESSAGES,
    TITLE,
    TOO_EXPENSIVE,
    CUSTOM {
        @Override
        public void send(@NotNull CommandSender sender, @NotNull Object... arguments) {
            sender.sendMessage(get(arguments));
        }

        @Override
        public @NotNull String get(@NotNull Object... arguments) {
            if (arguments.length == 1) {
                return new Message(String.valueOf(arguments[0])).getMessage();
            } else if (arguments.length > 1) {
                return new Message(String.valueOf(arguments[0])).getMessage(Arrays.copyOfRange(arguments, 1, arguments.length));
            }
            return "";
        }

    };

    private static final Map<Lang, Message> messages = new EnumMap<>(Lang.class);

    public static void reload(@NotNull AnvilPlugin plugin) {
        Logger logger = plugin.getLogger();
        logger.info("Reloading messages...");
        long startTime = System.currentTimeMillis();

        File langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists())
            plugin.saveResource("lang.yml", false);

        YamlConfiguration langCfg = YamlConfiguration.loadConfiguration(langFile);

        Arrays.stream(values()).forEach(lang -> messages.put(lang, new Message(langCfg.getString(lang.name(), "{0}"))));

        logger.log(Level.INFO, "Messages have been reloaded. This took {0} ms.",
                (System.currentTimeMillis() - startTime));
    }

    public void send(@NotNull CommandSender sender, @NotNull Object... arguments) {
        sender.sendMessage(messages.get(this).getMessage(arguments));
    }

    public String get(@NotNull Object... arguments) {
        return messages.get(this).getMessage(arguments);
    }

    public static @NotNull String colour(@NotNull String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static @NotNull String replaceArgs(@NotNull String msg, @NotNull Object... arguments) {
        for (int i = 0; i < arguments.length; i++) {
            String objectString = arguments[i].toString();
            msg = msg.replace("{" + i + "}", objectString);
        }

        return msg;
    }

    private static final class Message {
        private final String string;

        Message(@NotNull String message) {
            this.string = message;
        }

        @NotNull String getMessage(@NotNull Object... arguments) {
            return colour(replaceArgs(string, arguments));
        }
    }

}