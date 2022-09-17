package io.github.explosivemine.anvil.utils;

import io.github.explosivemine.anvil.AnvilPlugin;

import java.io.File;

public final class FileUtils {
    public static File loadFile(AnvilPlugin plugin, String path) {
        File file = new File(plugin.getDataFolder(), path);
        if (!file.exists()) {
            if (plugin.getResource(path) == null)
                Logging.severe("Could not resolve path:" + path);
            else
                plugin.saveResource(path, false);
        }

        return file;
    }
}
