// From: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates

package io.github.explosivemine.anvil.utils;

import io.github.explosivemine.anvil.AnvilPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public final class UpdateChecker {
    private final AnvilPlugin plugin;
    private final int resourceId;

    public UpdateChecker(AnvilPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    /**
     * @param action provides the latest plugin version. It is only run if the plugin is out of date.
     */
    public void checkVersion(Consumer<String> action) {
        Executor.async(plugin, runnable -> {
            try {
                InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream();
                Scanner scanner = new Scanner(inputStream);
                if (scanner.hasNext()) {
                    String s = scanner.next();
                    if (s.replace("v", "").compareTo(plugin.getDescription().getVersion()) > 0)
                        action.accept(s);
                } else {
                    plugin.getLogger().fine("Could not check plugin's version!");
                }
            } catch (IOException exception) {
                plugin.getLogger().warning("MalformedURLException by url provided in UpdateChecker. " +
                        "Please inform the developer.");
            }
        });
    }
}