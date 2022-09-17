package io.github.explosivemine.anvil.config.parser;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.utils.FileUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class SectionParser {
    protected final AnvilPlugin plugin;

    protected final String filePath;
    protected final String sectionPath;

    private ConfigurationSection section;

    public SectionParser(AnvilPlugin plugin, @NotNull String filePath, @NotNull String sectionPath) {
        this.plugin = plugin;
        this.filePath = filePath;
        this.sectionPath = sectionPath;
    }

    public SectionParser(AnvilPlugin plugin, @NotNull String sectionPath) {
        this(plugin, "config.yml", sectionPath);
    }

    public SectionParser(AnvilPlugin plugin) {
        this(plugin, "config.yml", "");
    }

    public abstract void parse();

    public ConfigurationSection getSection() {
        if (section == null) {
            this.section = getConfig();

            if (!sectionPath.isEmpty())
                this.section = section.getConfigurationSection(sectionPath);
        }

        return section;
    }

    public ConfigurationSection getConfig() {
        return "config.yml".equals(filePath) ? plugin.getConfig() : YamlConfiguration.loadConfiguration(FileUtils.loadFile(plugin, filePath));
    }

}