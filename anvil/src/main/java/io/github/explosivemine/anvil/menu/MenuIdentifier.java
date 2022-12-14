package io.github.explosivemine.anvil.menu;

import lombok.Getter;

public enum MenuIdentifier {
    ANVIL();

    @Getter private final MenuIdentifier parentIdentifier;

    MenuIdentifier() {
        this(null);
    }

    MenuIdentifier(MenuIdentifier parentIdentifier) {
        this.parentIdentifier = parentIdentifier;
    }

}