package io.github.explosivemine.anvil.config.parser;

import io.github.explosivemine.anvil.AnvilPlugin;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

public final class ConfigParser extends SectionParser {
    @Getter private int costLimit;
    @Getter private int renameCost;

    @Getter private boolean changeRenameCost = false;
    @Getter private boolean unbreakable;
    @Getter private boolean colours;
    @Getter private boolean debug;

    public ConfigParser(AnvilPlugin plugin) {
        super(plugin);
    }

    @Override
    public void parse() {
        ConfigurationSection config = getConfig();

        unbreakable = config.getBoolean("unbreakable anvils", false);
        colours = config.getBoolean("anvil colours", false);

        costLimit = config.getInt("maximum repair cost", 40);
        if (costLimit == -1)
            costLimit = Integer.MAX_VALUE;

        renameCost = config.getInt("renaming cost", -1);
        if (renameCost != -1)
            changeRenameCost = true;

        debug = config.getBoolean("debug", false);
    }
}