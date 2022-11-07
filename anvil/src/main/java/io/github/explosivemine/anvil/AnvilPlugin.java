package io.github.explosivemine.anvil;

import io.github.explosivemine.anvil.config.parser.Lang;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import io.github.explosivemine.anvil.menu.MenuManager;
import io.github.explosivemine.anvil.player.SPlayerManager;
import io.github.explosivemine.anvil.config.ConfigSettings;
import io.github.explosivemine.anvil.listeners.AnvilEvents;
import io.github.explosivemine.anvil.listeners.PlayerEvents;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


public final class AnvilPlugin extends JavaPlugin implements CommandExecutor {
    @Getter private final ConfigSettings configSettings = new ConfigSettings(this);

    @Getter private final SPlayerManager sPlayerManager = new SPlayerManager(this);

    @Getter private final MenuManager menuManager = new MenuManager(this);

    @Override
    public void onEnable() {
        // Plugin startup logic
        configSettings.init();

        getCommand("anvil").setExecutor(this);

        new PlayerEvents(this);
        new AnvilEvents(this);

        new Metrics(this, 11598);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("anvil.admin")) {
                Lang.NO_PERMISSION.send(sender);
                return false;
            }

            Lang.reload(this);
            Lang.RELOAD_MESSAGES.send(sender);
            return false;
        }

        if (!(sender instanceof Player player)) {
            Lang.ON_CONSOLE_EXECUTE.send(sender);
            return false;
        }

        if (!player.hasPermission("anvil.use")) {
            Lang.NO_PERMISSION.send(player);
            return false;
        }

        menuManager.open(MenuIdentifier.ANVIL, sPlayerManager.get(player), player);
        return false;
    }

}