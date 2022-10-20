package io.github.explosivemine.anvil.menu.type.anvil;

import io.github.explosivemine.anvil.version.*;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public final class VersionMatcher {
    private final static Map<String, Class<? extends VersionWrapper>> map = new HashMap<>() {{
        put("1.17-R0.1-SNAPSHOT", Wrapper1_17_R1.class);
        put("1.17.1-R0.1-SNAPSHOT", Wrapper1_17_R1.class);
        put("1.18-R0.1-SNAPSHOT", Wrapper1_18_R1.class);
        put("1.18.1-R0.1-SNAPSHOT", Wrapper1_18_R1.class);
        put("1.18.2-R0.1-SNAPSHOT", Wrapper1_18_R2.class);
        put("1.19-R0.1-SNAPSHOT", Wrapper1_19_R1.class);
        put("1.19.1-R0.1-SNAPSHOT", Wrapper1_19_1_R1.class);
        put("1.19.2-R0.1-SNAPSHOT", Wrapper1_19_1_R1.class);
    }};

    public VersionWrapper match() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        try {
            return map.get(bukkitVersion).newInstance();
        } catch (IllegalAccessException | InstantiationException exception) {
            throw new IllegalStateException("Failed to instantiate version wrapper for version " + bukkitVersion, exception);
        }
    }

}