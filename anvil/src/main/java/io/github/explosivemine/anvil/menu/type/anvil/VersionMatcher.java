package io.github.explosivemine.anvil.menu.type.anvil;

import io.github.explosivemine.anvil.version.VersionWrapper;
import org.bukkit.Bukkit;

public final class VersionMatcher {
    public VersionWrapper match() {
        final String serverVersion = Bukkit.getServer()
                .getClass()
                .getPackage()
                .getName()
                .split("\\.")[3]
                .substring(1);

        try {
            return (VersionWrapper) Class.forName("io.github.explosivemine.anvil.version.Wrapper" + serverVersion).newInstance();
        } catch (IllegalAccessException | InstantiationException exception) {
            throw new IllegalStateException("Failed to instantiate version wrapper for version " + serverVersion, exception);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Anvil does not support server version \"" + serverVersion + "\"", exception);
        }
    }

}