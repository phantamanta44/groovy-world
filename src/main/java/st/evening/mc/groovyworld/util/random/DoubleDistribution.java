package st.evening.mc.groovyworld.util.random;

import java.util.Random;

/**
 * A distribution describing a double random variable.
 */
@FunctionalInterface
public interface DoubleDistribution {
    /**
     * Samples a double from the distribution.
     *
     * @param rng The RNG.
     * @return The sampled value.
     */
    double sampleDouble(final Random rng);
}
