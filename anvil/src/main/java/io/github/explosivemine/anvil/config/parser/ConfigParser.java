package io.github.explosivemine.anvil.config.parser;

import io.github.explosivemine.anvil.AnvilPlugin;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

@Getter
public final class ConfigParser extends SectionParser<Void> {
    private int costLimit;
    private int renameCost;

    private boolean changeRenameCost = false;
    private boolean unbreakable;
    private boolean colours;
    private boolean virtual;
    private boolean debug;

    public ConfigParser(AnvilPlugin plugin) {
        super(plugin);
    }

    /**
     *
     * @return null
     */
    @Override
    public @Nullable Void parse() {
        ConfigurationSection config = getSection();
        if (config == null) {
            return null;
        }

        unbreakable = config.getBoolean("unbreakable anvils", false);
        colours = config.getBoolean("anvil colours", false);
        virtual = config.getBoolean("virtual anvils", true);

        costLimit = config.getInt("maximum repair cost", 40);
        if (costLimit == -1)
            costLimit = Integer.MAX_VALUE;

        renameCost = config.getInt("renaming cost", -1);
        changeRenameCost = renameCost != -1;

        debug = config.getBoolean("debug", false);
        return null;
    }

}