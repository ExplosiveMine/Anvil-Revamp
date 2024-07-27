package io.github.explosivemine.anvil.config;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.config.parser.Lang;
import io.github.explosivemine.anvil.config.parser.ConfigParser;
import io.github.explosivemine.anvil.config.parser.SectionParser;
import lombok.Getter;

public final class ConfigSettings {
    private final AnvilPlugin plugin;

    @Getter private ConfigParser configParser;

    public ConfigSettings(AnvilPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        plugin.saveDefaultConfig();
        Lang.reload(plugin);

        configParser = new ConfigParser(plugin);

        SectionParser<?>[] parsers = new SectionParser[] {
                configParser
        };

        for (SectionParser<?> parser : parsers) {
            parser.parse();
        }
    }

}