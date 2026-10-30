package io.github.melerodev.welcomemessages.utility;


import io.github.melerodev.welcomemessages.AbstractWelcomeMessages;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

/**
 * A class that provides shorthand access to {@link AbstractWelcomeMessages#getComponentLogger}.
 */
public class Logger {
    /**
     * Get component logger. Shorthand for:
     *
     * @return the component logger {@link AbstractWelcomeMessages#getComponentLogger}.
     */
    @NotNull
    public static ComponentLogger get() {
        return AbstractWelcomeMessages.getInstance().getComponentLogger();
    }
}
