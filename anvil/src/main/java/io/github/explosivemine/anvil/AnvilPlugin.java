package io.github.explosivemine.anvil;

import io.github.explosivemine.anvil.commands.anvil.AnvilPluginCommand;
import io.github.explosivemine.anvil.commands.anvil.AnvilCommandsMap;
import io.github.explosivemine.anvil.listeners.ServerEvents;
import io.github.explosivemine.anvil.menu.MenuManager;
import io.github.explosivemine.anvil.player.SPlayerManager;
import io.github.explosivemine.anvil.config.ConfigSettings;
import io.github.explosivemine.anvil.listeners.AnvilEvents;
import io.github.explosivemine.anvil.listeners.PlayerEvents;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public final class AnvilPlugin extends JavaPlugin {
    private final ConfigSettings configSettings = new ConfigSettings(this);

    private final SPlayerManager sPlayerManager = new SPlayerManager(this);

    private final MenuManager menuManager = new MenuManager(this);

    private final AnvilPluginCommand anvilCommand = new AnvilPluginCommand(this, new AnvilCommandsMap(this));

    @Override
    public void onEnable() {
        configSettings.init();

        org.bukkit.command.PluginCommand cmd = getServer().getPluginCommand(anvilCommand.getIdentifier().getLabel());
        if (cmd == null) {
            getLogger().log(Level.WARNING, "Could not register command \"{0}\": command is null",
                    anvilCommand.getIdentifier().getLabel());
        } else {
            cmd.setExecutor(anvilCommand);
            cmd.setTabCompleter(anvilCommand);
        }
        anvilCommand.getCommandsMap().loadDefaultCommands();

        new PlayerEvents(this);
        new AnvilEvents(this);
        new ServerEvents(this);

        Metrics metrics = new Metrics(this, 11598);
        metrics.addCustomChart(new SimplePie("unbreakable_anvils", () -> {
            return String.valueOf(configSettings.getConfigParser().isUnbreakable());
        }));

        metrics.addCustomChart(new SimplePie("anvil_colours", () -> {
            return String.valueOf(configSettings.getConfigParser().isUnbreakable());
        }));

        metrics.addCustomChart(new SimplePie("maximum_repair_cost", () -> {
            int costLimit = configSettings.getConfigParser().getCostLimit();
            return costLimit == Integer.MAX_VALUE ? "Disabled" : String.valueOf(costLimit);
        }));

        metrics.addCustomChart(new SimplePie("renaming_cost", () -> {
            int renameCost = configSettings.getConfigParser().getRenameCost();
            return renameCost == -1 ? "Disabled" : String.valueOf(renameCost);
        }));

        metrics.addCustomChart(new SimplePie("virtual_anvils", () -> {
            return String.valueOf(configSettings.getConfigParser().isVirtual());
        }));
    }

}