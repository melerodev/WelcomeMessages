package io.github.melerodev.welcomemessages;

/**
 * Implemented in classes that should support being reloaded IE executing the methods during runtime after startup.
 */
public interface Reloadable {
    /**
     * On plugin load.
     */
    default void onLoad(AbstractWelcomeMessages plugin) {
    }

    /**
     * On plugin enable.
     */
    default void onEnable(AbstractWelcomeMessages plugin) {
    }

    /**
     * On plugin disable.
     */
    default void onDisable(AbstractWelcomeMessages plugin) {
    }

}
