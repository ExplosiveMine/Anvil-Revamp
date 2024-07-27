package io.github.explosivemine.anvil.config.parser;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.config.PluginResourceLoader;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public abstract class SectionParser<T> {
    @Getter private final AnvilPlugin plugin;

    @Getter private final File file;

    @Getter private final String sectionPath;

    private ConfigurationSection section;

    protected SectionParser(@NotNull AnvilPlugin plugin, @NotNull File file, @NotNull String sectionPath) {
        this.plugin = plugin;
        this.file = file;
        this.sectionPath = sectionPath;
    }

    protected SectionParser(@NotNull AnvilPlugin plugin) {
        this(plugin, new File(plugin.getDataFolder(), "config.yml"), "");
    }

    public abstract T parse();

    public @Nullable ConfigurationSection getSection() {
        if (section == null) {
            if (file.getName().equals("config.yml")) {
                section = plugin.getConfig();
            } else {
                section = YamlConfiguration.loadConfiguration(new PluginResourceLoader().loadFile(plugin, file));
            }

            if (!sectionPath.isEmpty()) {
                ConfigurationSection sectionPathConfig = section.getConfigurationSection(sectionPath);
                if (sectionPathConfig == null) {
                    getPlugin().getLogger().warning("Could not find section " + getSectionPath() + " in " + getFile());
                    return null;
                }
                section = sectionPathConfig;
            }
        }

        return section;
    }

}