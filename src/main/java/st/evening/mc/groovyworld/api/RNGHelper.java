package st.evening.mc.groovyworld.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import st.evening.mc.groovyworld.util.HBlockPos;
import st.evening.mc.groovyworld.util.random.IntDistribution;

import java.util.Random;

/**
 * Utilities for sampling from random number generators.
 */
public enum RNGHelper {
    ;

    /**
     * Simulates a Bernoulli trial.
     *
     * @param rng         The RNG.
     * @param probability The success probability, expressed as a float between 0 and 1.
     * @return Whether the trial succeeded or not.
     */
    public static boolean bernoulli(final Random rng, final float probability) {
        return probability > 0F && (probability >= 1F || rng.nextFloat() < probability);
    }

    /**
     * Samples a uniformly-distributed double.
     *
     * @param rng      The RNG.
     * @param minValue The minimum outcome, inclusive.
     * @param maxValue The maximum outcome, exclusive.
     * @return The sampled value.
     */
    public static double sampleUniform(final Random rng, final double minValue, final double maxValue) {
        return minValue + rng.nextDouble() * (maxValue - minValue);
    }

    /**
     * Samples a uniformly-distributed float.
     *
     * @param rng      The RNG.
     * @param minValue The minimum outcome, inclusive.
     * @param maxValue The maximum outcome, exclusive.
     * @return The sampled value.
     */
    public static float sampleUniformFloat(final Random rng, final float minValue, final float maxValue) {
        return minValue + rng.nextFloat() * (maxValue - minValue);
    }

    /**
     * Samples a uniformly-distributed integer.
     *
     * @param rng               The RNG.
     * @param minValue          The minimum outcome, inclusive.
     * @param maxValueExclusive The maximum outcome, exclusive.
     * @return The sampled value.
     */
    public static int sampleUniformIntExclusive(final Random rng, final int minValue, final int maxValueExclusive) {
        return minValue + rng.nextInt(maxValueExclusive - minValue);
    }

    /**
     * Samples a uniformly-distributed integer.
     *
     * @param rng               The RNG.
     * @param minValue          The minimum outcome, inclusive.
     * @param maxValueInclusive The maximum outcome, inclusive.
     * @return The sampled value.
     */
    public static int sampleUniformInt(final Random rng, final int minValue, final int maxValueInclusive) {
        return sampleUniformIntExclusive(rng, minValue, maxValueInclusive + 1);
    }

    /**
     * Samples a uniformly-distributed coordinate in a chunk.
     *
     * @param rng           The RNG.
     * @param chunkMinCoord The minimum coordinate within the chunk of interest.
     * @return The sampled value.
     */
    public static int sampleUniformChunkCoord(final Random rng, final int chunkMinCoord) {
        return sampleUniformIntExclusive(rng, chunkMinCoord, chunkMinCoord + 16);
    }

    /**
     * Samples a triangularly-distributed double. The mean is the most likely outcome, with the probability decreasing
     * linearly to zero towards {@code (mean + extent)} and {@code (mean - extent)}.
     *
     * @param rng    The RNG.
     * @param mean   The mean of the distribution.
     * @param extent The extent of the distribution.
     * @return The sampled value.
     */
    public static double sampleTriangular(final Random rng, final double mean, final double extent) {
        return mean + extent * (rng.nextDouble() - rng.nextDouble());
    }

    /**
     * Samples a triangularly-distributed float. The mean is the most likely outcome, with the probability decreasing
     * linearly to zero towards {@code (mean + extent)} and {@code (mean - extent)}.
     *
     * @param rng    The RNG.
     * @param mean   The mean of the distribution.
     * @param extent The extent of the distribution.
     * @return The sampled value.
     */
    public static float sampleTriangularFloat(final Random rng, final float mean, final float extent) {
        return mean + extent * (rng.nextFloat() - rng.nextFloat());
    }

    /**
     * Samples a triangularly-distributed integer. The mean is the most likely outcome, with the probability decreasing
     * linearly to zero towards {@code (mean + extent)} and {@code (mean - extent)}.
     *
     * @param rng    The RNG.
     * @param mean   The mean of the distribution.
     * @param extent The extent of the distribution.
     * @return The sampled value.
     */
    public static int sampleTriangularInt(final Random rng, final int mean, final int extent) {
        return mean + rng.nextInt(extent + 1) - rng.nextInt(extent + 1);
    }

    /**
     * Samples a normally-distributed double.
     *
     * @param rng      The RNG.
     * @param mean     The mean of the distribution.
     * @param variance The variance of the distribution.
     * @return The sampled value.
     */
    public static double sampleGaussian(final Random rng, final double mean, final double variance) {
        return mean + rng.nextGaussian() * variance;
    }

    /**
     * Samples a normally-distributed float.
     *
     * @param rng      The RNG.
     * @param mean     The mean of the distribution.
     * @param variance The variance of the distribution.
     * @return The sampled value.
     */
    public static float sampleGaussianFloat(final Random rng, final double mean, final double variance) {
        return (float) sampleGaussian(rng, mean, variance);
    }

    /**
     * Samples a normally-distributed integer.
     *
     * @param rng      The RNG.
     * @param mean     The mean of the distribution.
     * @param variance The variance of the distribution.
     * @return The sampled value.
     */
    public static int sampleGaussianInt(final Random rng, final double mean, final double variance) {
        return Math.round(sampleGaussianFloat(rng, mean, variance));
    }

    /**
     * Samples an index into a collection, excluding one index.
     *
     * @param rng           The RNG.
     * @param size          The size of the collection.
     * @param excludedIndex The index to exclude.
     * @return The sampled index.
     */
    public static int sampleIndexExcluding(final Random rng, final int size, final int excludedIndex) {
        final int index = rng.nextInt(size - 1);
        return index != excludedIndex ? index : (size - 1);
    }

    /**
     * Samples a random direction.
     *
     * @param rng The RNG.
     * @return The sampled direction.
     */
    public static EnumFacing sampleUniformDir(final Random rng) {
        return EnumFacing.VALUES[rng.nextInt(EnumFacing.VALUES.length)];
    }

    /**
     * Samples a random direction, excluding one. Useful for preventing backtracking in random walks.
     *
     * @param rng      The RNG.
     * @param excluded The direction to exclude.
     * @return The sampled index.
     */
    public static EnumFacing sampleUniformDirExcluding(final Random rng, final EnumFacing excluded) {
        return EnumFacing.VALUES[sampleIndexExcluding(rng, EnumFacing.VALUES.length, excluded.getIndex())];
    }

    /**
     * Samples a uniformly-distributed horizontal position.
     *
     * @param rng  The RNG.
     * @param minX The minimum X-coordinate.
     * @param maxX The maximum X-coordinate.
     * @param minZ The minimum Z-coordinate.
     * @param maxZ The maximum Z-coordinate.
     * @return The sampled position.
     */
    public static HBlockPos sampleUniformXZ(final Random rng, final int minX, final int maxX,
                                            final int minZ, final int maxZ) {
        return new HBlockPos(sampleUniformInt(rng, minX, maxX), sampleUniformInt(rng, minZ, maxZ));
    }

    /**
     * Samples a uniformly-distributed horizontal position in a chunk.
     *
     * @param rng  The RNG.
     * @param minX The minimum X-coordinate of the chunk.
     * @param minZ The minimum Z-coordinate of the chunk.
     * @return The sampled position.
     */
    public static HBlockPos sampleChunkUniformXZ(final Random rng, final int minX, final int minZ) {
        return sampleUniformXZ(rng, minX, minX + 15, minZ, minZ + 15);
    }

    /**
     * Samples a uniformly-distributed 3D position.
     *
     * @param rng  The RNG.
     * @param minX The minimum X-coordinate.
     * @param minY The minimum Y-coordinate.
     * @param minZ The minimum Z-coordinate.
     * @param maxX The maximum X-coordinate.
     * @param maxY The maximum Y-coordinate.
     * @param maxZ The maximum Z-coordinate.
     * @return The sampled position.
     */
    public static BlockPos sampleUniformXYZ(final Random rng, final int minX, final int minY, final int minZ,
                                            final int maxX, final int maxY, final int maxZ) {
        return new BlockPos(sampleUniformInt(rng, minX, maxX),
                sampleUniformInt(rng, minY, maxY),
                sampleUniformInt(rng, minZ, maxZ));
    }

    /**
     * Samples a uniformly-distributed 3D position in a chunk.
     *
     * @param rng       The RNG.
     * @param chunkMinX The minimum X-coordinate of the chunk.
     * @param chunkMinZ The minimum Z-coordinate of the chunk.
     * @param minY      The minimum Y-coordinate.
     * @param maxY      The maximum Y-coordinate.
     * @return The sampled position.
     */
    public static BlockPos sampleChunkUniformXYZ(final Random rng, final int chunkMinX, final int chunkMinZ,
                                                 final int minY, final int maxY) {
        return sampleUniformXYZ(rng, chunkMinX, minY, chunkMinZ, chunkMinX + 15, maxY, chunkMinZ + 15);
    }

    /**
     * Samples a 3D position in a chunk with a uniform horizontal distribution and a given Y-coordinate distribution.
     *
     * @param rng           The RNG.
     * @param minX          The minimum X-coordinate.
     * @param minZ          The minimum Z-coordinate.
     * @param maxX          The maximum X-coordinate.
     * @param maxZ          The maximum Z-coordinate.
     * @param distributionY The distribution of the Y-coordinate.
     * @return The sampled position.
     */
    public static BlockPos sampleUniformXZRandomY(final Random rng,
                                                  final int minX, final int minZ, final int maxX, final int maxZ,
                                                  final IntDistribution distributionY) {
        return new BlockPos(
                sampleUniformInt(rng, minX, maxX), distributionY.sampleInt(rng), sampleUniformInt(rng, minZ, maxZ));
    }

    /**
     * Samples a 3D position in a chunk with a uniform horizontal distribution and a given Y-coordinate distribution.
     *
     * @param rng           The RNG.
     * @param chunkMinX     The minimum X-coordinate of the chunk.
     * @param chunkMinZ     The minimum Z-coordinate of the chunk.
     * @param distributionY The distribution of the Y-coordinate.
     * @return The sampled position.
     */
    public static BlockPos sampleChunkUniformXZRandomY(final Random rng, final int chunkMinX, final int chunkMinZ,
                                                       final IntDistribution distributionY) {
        return sampleUniformXZRandomY(rng, chunkMinX, chunkMinZ, chunkMinX + 15, chunkMinZ + 15, distributionY);
    }
}
