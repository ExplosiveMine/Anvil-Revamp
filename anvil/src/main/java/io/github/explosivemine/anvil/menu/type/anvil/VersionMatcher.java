package io.github.explosivemine.anvil.menu.type.anvil;

import io.github.explosivemine.anvil.version.*;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public final class VersionMatcher {
    private final Map<String, String> map = new HashMap<>() {{
        put("1.17", "Wrapper1_17_R1");
        put("1.17.1", "Wrapper1_17_R1");
        put("1.18", "Wrapper1_18_R1");
        put("1.18.1", "Wrapper1_18_R1");
        put("1.18.2", "Wrapper1_18_R2");
        put("1.19", "Wrapper1_19_R1");
        put("1.19.1", "Wrapper1_19_1_R1");
        put("1.19.2", "Wrapper1_19_1_R1");
        put("1.19.3", "Wrapper1_19_R2");
        put("1.19.4", "Wrapper1_19_R3");
        put("1.20", "Wrapper1_20_R1");
        put("1.20.1", "Wrapper1_20_R1");
        put("1.20.2", "Wrapper1_20_R2");
        put("1.20.3", "Wrapper1_20_R3");
        put("1.20.4", "Wrapper1_20_R3");
        put("1.20.5", "Wrapper1_20_R4");
        put("1.20.6", "Wrapper1_20_R4");
        put("1.21", "Wrapper1_21_R1");
        put("1.21.1", DEFAULT_WRAPPER);
    }};

    // The default will be the latest version. This must be updated every time a new version is released
    private final static String DEFAULT_WRAPPER = "Wrapper1_21_R1";

    public VersionWrapper match() {
        String bukkitVersion = Bukkit.getBukkitVersion().split("-")[0];
        try {
            return (VersionWrapper) Class.forName("io.github.explosivemine.anvil.version." + map.getOrDefault(bukkitVersion, DEFAULT_WRAPPER))
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to instantiate version wrapper for version " + bukkitVersion, exception);
        }
    }

}