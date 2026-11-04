package st.evening.mc.groovyworld.util.random;

import java.util.Random;

/**
 * A distribution describing an integer random variable.
 */
@FunctionalInterface
public interface IntDistribution {
    /**
     * Samples an integer from the distribution.
     *
     * @param rng The RNG.
     * @return The sampled value.
     */
    int sampleInt(final Random rng);
}
