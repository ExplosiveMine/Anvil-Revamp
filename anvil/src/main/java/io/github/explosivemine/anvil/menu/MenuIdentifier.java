package io.github.explosivemine.anvil.menu;

import lombok.Getter;

@Getter
public enum MenuIdentifier {
    ANVIL();

    private final MenuIdentifier parentIdentifier;

    MenuIdentifier() {
        this(null);
    }

    MenuIdentifier(MenuIdentifier parentIdentifier) {
        this.parentIdentifier = parentIdentifier;
    }

}