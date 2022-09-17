package io.github.explosivemine.anvil.menu.impl;

import io.github.explosivemine.anvil.AnvilPlugin;
import io.github.explosivemine.anvil.config.parser.Lang;
import io.github.explosivemine.anvil.menu.MenuIdentifier;
import io.github.explosivemine.anvil.menu.type.anvil.AnvilMenu;

public final class Anvil extends AnvilMenu {
    public Anvil(AnvilPlugin plugin) {
        super(plugin, MenuIdentifier.ANVIL, Lang.TITLE.get());
    }

}