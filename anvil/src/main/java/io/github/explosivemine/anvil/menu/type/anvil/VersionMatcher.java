package io.github.explosivemine.anvil.menu.type.anvil;

import io.github.explosivemine.anvil.version.*;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public final class VersionMatcher {
    private final static Map<String, String> map = new HashMap<>() {{
        put("1.17-R0.1-SNAPSHOT", "Wrapper1_17_R1");
        put("1.17.1-R0.1-SNAPSHOT", "Wrapper1_17_R1");
        put("1.18-R0.1-SNAPSHOT", "Wrapper1_18_R1");
        put("1.18.1-R0.1-SNAPSHOT", "Wrapper1_18_R1");
        put("1.18.2-R0.1-SNAPSHOT", "Wrapper1_18_R2");
        put("1.19-R0.1-SNAPSHOT", "Wrapper1_19_R1");
        put("1.19.1-R0.1-SNAPSHOT", "Wrapper1_19_1_R1");
        put("1.19.2-R0.1-SNAPSHOT", "Wrapper1_19_1_R1");
        put("1.19.3-R0.1-SNAPSHOT", "Wrapper1_19_R2");
        put("1.19.4-R0.1-SNAPSHOT", "Wrapper1_19_R3");
        put("1.20-R0.1-SNAPSHOT", "Wrapper1_20_R1");
        put("1.20.1-R0.1-SNAPSHOT", "Wrapper1_20_R1");
    }};

    public VersionWrapper match() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        try {
            return (VersionWrapper) Class.forName("io.github.explosivemine.anvil.version." + map.get(bukkitVersion))
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to instantiate version wrapper for version " + bukkitVersion, exception);
        }
    }

}