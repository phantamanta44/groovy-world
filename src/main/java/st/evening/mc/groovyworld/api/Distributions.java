package st.evening.mc.groovyworld.api;

import st.evening.mc.groovyworld.util.random.DoubleDistribution;
import st.evening.mc.groovyworld.util.random.FloatDistribution;
import st.evening.mc.groovyworld.util.random.IntDistribution;

/**
 * A library of numerical distributions.
 */
public enum Distributions {
    ;

    /**
     * A distribution with only a single outcome.
     *
     * @param value The constant outcome.
     */
    public static DoubleDistribution constant(final double value) {
        return rng -> value;
    }

    /**
     * A distribution with only a single outcome.
     *
     * @param value The constant outcome.
     */
    public static FloatDistribution constantFloat(final float value) {
        return rng -> value;
    }

    /**
     * A distribution with only a single outcome.
     *
     * @param value The constant outcome.
     */
    public static IntDistribution constantInt(final int value) {
        return rng -> value;
    }

    /**
     * A uniform distribution of doubles on a half-open interval.
     *
     * @param minValue The minimum outcome, inclusive.
     * @param maxValue The maximum outcome, exclusive.
     */
    public static DoubleDistribution uniform(final double minValue, final double maxValue) {
        return rng -> RNGHelper.sampleUniform(rng, minValue, maxValue);
    }

    /**
     * A uniform distribution of floats on a half-open interval.
     *
     * @param minValue The minimum outcome, inclusive.
     * @param maxValue The maximum outcome, exclusive.
     */
    public static FloatDistribution uniformFloat(final float minValue, final float maxValue) {
        return rng -> RNGHelper.sampleUniformFloat(rng, minValue, maxValue);
    }

    /**
     * A uniform distribution of integers.
     *
     * @param minValue The minimum outcome, inclusive.
     * @param maxValue The maximum outcome, inclusive.
     */
    public static IntDistribution uniformInt(final int minValue, final int maxValue) {
        return rng -> RNGHelper.sampleUniformInt(rng, minValue, maxValue);
    }

    /**
     * A triangular distribution of doubles. The mean is the most likely outcome, with probability decreasing linearly
     * to zero towards {@code (mean + extent)} and {@code (mean - extent)}.
     *
     * @param mean   The mean outcome.
     * @param extent The extent of the distribution.
     */
    public static DoubleDistribution triangular(final double mean, final double extent) {
        return rng -> RNGHelper.sampleTriangular(rng, mean, extent);
    }

    /**
     * A triangular distribution of floats. The mean is the most likely outcome, with probability decreasing linearly to
     * zero towards {@code (mean + extent)} and {@code (mean - extent)}.
     *
     * @param mean   The mean outcome.
     * @param extent The extent of the distribution.
     */
    public static FloatDistribution triangularFloat(final float mean, final float extent) {
        return rng -> RNGHelper.sampleTriangularFloat(rng, mean, extent);
    }

    /**
     * A triangular distribution of integers. The mean is the most likely outcome, with probability decreasing linearly
     * to zero towards {@code (mean + extent)} and {@code (mean - extent)}.
     *
     * @param mean   The mean outcome.
     * @param extent The extent of the distribution.
     */
    public static IntDistribution triangularInt(final int mean, final int extent) {
        return rng -> RNGHelper.sampleTriangularInt(rng, mean, extent);
    }

    /**
     * A Gaussian (normal) distribution of doubles.
     *
     * @param mean     The mean outcome.
     * @param variance The variance of the distribution.
     */
    public static DoubleDistribution gaussian(final double mean, final double variance) {
        return rng -> RNGHelper.sampleGaussian(rng, mean, variance);
    }

    /**
     * A Gaussian (normal) distribution of floats.
     *
     * @param mean     The mean outcome.
     * @param variance The variance of the distribution.
     */
    public static FloatDistribution gaussianFloat(final double mean, final double variance) {
        return rng -> RNGHelper.sampleGaussianFloat(rng, mean, variance);
    }

    /**
     * A Gaussian (normal) distribution of integers.
     *
     * @param mean     The mean outcome.
     * @param variance The variance of the distribution.
     */
    public static IntDistribution gaussianInt(final double mean, final double variance) {
        return rng -> RNGHelper.sampleGaussianInt(rng, mean, variance);
    }
}
