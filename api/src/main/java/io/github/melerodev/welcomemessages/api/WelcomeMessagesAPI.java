package io.github.melerodev.welcomemessages.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * The WelcomeMessagesAPI class is the main entry point for accessing the WelcomeMessages API.
 */
public abstract class WelcomeMessagesAPI {
    private static WelcomeMessagesAPI INSTANCE;

    /**
     * Gets the instance of the WelcomeMessagesAPI.
     *
     * @return the instance of WelcomeMessagesAPI
     * @since 1.0.0
     */
    public static WelcomeMessagesAPI getInstance() {
        if (INSTANCE == null)
            throw new RuntimeException("API was accessed before being initialized!");
        return INSTANCE;
    }

    /**
     * Sets the instance of the WelcomeMessagesAPI.
     * This method is intended for internal use by the api provider only.
     *
     * @param api the instance of WelcomeMessagesAPI to set
     * @since 1.0.0
     */
    @ApiStatus.Internal
    protected static void setInstance(WelcomeMessagesAPI api) {
        INSTANCE = api;
    }
}
