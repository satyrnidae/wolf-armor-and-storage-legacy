package dev.satyrn.wolfarmor.api.compatibility;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Provider {
    String value();

    short priority() default Priority.LOWEST;

    abstract class ProviderType {
        /**
         * The mod ID which is handled by this provider
         * @return The mod ID which is provided
         * @since 4.5.0-alpha
         */
        @Nonnull
        public final String getModId() {
            return this.getClass().isAnnotationPresent(Provider.class)
                    ? this.getClass().getAnnotation(Provider.class).value()
                    : "";
        }

        public final short getPriority() {
            return this.getClass().isAnnotationPresent(Provider.class)
                    ? this.getClass().getAnnotation(Provider.class).priority()
                    : Priority.LOWEST;
        }
    }

    class Priority {
        private Priority() {}
        public static final short HIGHEST = Short.MAX_VALUE;
        public static final short LOWEST = Short.MIN_VALUE;
    }
}
