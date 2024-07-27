package io.github.explosivemine.anvil.config;

import io.github.explosivemine.anvil.AnvilPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;

public final class PluginResourceLoader {
    public @NotNull File loadFile(@NotNull AnvilPlugin plugin, @NotNull File file) {
        if (!file.exists()) {
            String path = file.getPath().split(plugin.getName() + File.separator)[1];
            if (plugin.getResource(path) == null) {
                plugin.getLogger().log(Level.SEVERE, "Default plugin resource could not be found at: {0}", path);
            } else {
                plugin.saveResource(path, false);
            }
        }

        return file;
    }

}