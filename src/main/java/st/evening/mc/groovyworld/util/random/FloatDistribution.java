package st.evening.mc.groovyworld.util.random;

import java.util.Random;

/**
 * A distribution describing a float random variable.
 */
@FunctionalInterface
public interface FloatDistribution {
    /**
     * Samples a float from the distribution.
     *
     * @param rng The RNG.
     * @return The sampled value.
     */
    float sampleFloat(final Random rng);
}
