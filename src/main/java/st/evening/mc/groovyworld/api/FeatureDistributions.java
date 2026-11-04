package st.evening.mc.groovyworld.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import st.evening.mc.groovyworld.util.random.IntDistribution;

/**
 * A library of feature distributions.
 */
public enum FeatureDistributions {
    ;

    /**
     * A feature distribution that distributes features uniformly in a fixed vertical range.
     *
     * @param minY The minimum Y-coordinate.
     * @param maxY The maximum Y-coordinate.
     */
    public static FeatureDistribution uniformY(final int minY, final int maxY) {
        return (rng, world, chunkMinX, chunkMinZ) ->
                RNGHelper.sampleChunkUniformXYZ(rng, chunkMinX, chunkMinZ, minY, maxY);
    }

    /**
     * A feature distribution with uniform horizontal distribution and a given Y-coordinate distribution.
     *
     * @param distributionY The distribution of the Y-coordinate.
     */
    public static FeatureDistribution randomY(final IntDistribution distributionY) {
        return (rng, world, chunkMinX, chunkMinZ) ->
                RNGHelper.sampleChunkUniformXZRandomY(rng, chunkMinX, chunkMinZ, distributionY);
    }

    /**
     * A feature distribution with uniform horizontal distribution that selects Y-coordinates above all non-air blocks.
     */
    public static FeatureDistribution upperBoundY() {
        return (rng, world, chunkMinX, chunkMinZ) -> new BlockPos(
                RNGHelper.sampleUniformChunkCoord(rng, chunkMinX),
                ScanHelper.findUpperBoundY(world, chunkMinX, chunkMinZ), // height map is per-chunk rather than per-pos
                RNGHelper.sampleUniformChunkCoord(rng, chunkMinZ));
    }

    /**
     * A feature distribution with uniform horizontal distribution that selects Y-coordinates at the surface of the
     * world as defined by a block filter.
     *
     * @param surfaceMatcher The block filter for the surface.
     */
    public static FeatureDistribution surface(final BlockFilter surfaceMatcher) {
        return (rng, world, chunkMinX, chunkMinZ) -> ScanHelper.findSurfacePos(rng, world,
                RNGHelper.sampleUniformChunkCoord(rng, chunkMinX), RNGHelper.sampleUniformChunkCoord(rng, chunkMinZ),
                surfaceMatcher);
    }

    /**
     * A feature distribution with uniform horizontal distribution that selects Y-coordinates at the surface of the
     * world by filtering for typical overworld decoration blocks.
     */
    public static FeatureDistribution surface() {
        return surface(BlockFilters.surfaceDecoration().not());
    }

    /**
     * Filters a feature distribution to positions with replaceable blocks.
     *
     * @param parent The parent distribution.
     * @return The filtered distribution.
     */
    public static FeatureDistribution replaceable(final FeatureDistribution parent) {
        return parent.filter(BlockFilters.replaceable());
    }

    /**
     * Transforms a distribution by performing a scan in a fixed direction from each sampled position.
     *
     * @param parent           The parent distribution.
     * @param dir              The direction to scan in.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The transformed distribution.
     */
    public static FeatureDistribution scan(final FeatureDistribution parent, final EnumFacing dir,
                                           final int maxDistExclusive, final BlockFilter matcher) {
        return (rng, world, chunkMinX, chunkMinZ) -> {
            final BlockPos pos = parent.sample(rng, world, chunkMinX, chunkMinZ);
            return pos != null ? ScanHelper.scan(rng, world, pos, dir, maxDistExclusive, matcher) : null;
        };
    }

    /**
     * Transforms a distribution by performing a scan in a random direction from each sampled position.
     *
     * @param parent           The parent distribution.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The transformed distribution.
     */
    public static FeatureDistribution scanRandomDir(final FeatureDistribution parent,
                                                    final int maxDistExclusive, final BlockFilter matcher) {
        return (rng, world, chunkMinX, chunkMinZ) -> {
            final BlockPos pos = parent.sample(rng, world, chunkMinX, chunkMinZ);
            return pos == null ? null
                    : ScanHelper.scan(rng, world, pos, RNGHelper.sampleUniformDir(rng), maxDistExclusive, matcher);
        };
    }

    /**
     * Transforms a distribution by scanning downwards for a solid floor from each sampled position.
     *
     * @param parent           The parent distribution.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @return The transformed distribution.
     */
    public static FeatureDistribution floor(final FeatureDistribution parent, final int maxDistExclusive) {
        return scan(replaceable(parent), EnumFacing.DOWN, maxDistExclusive, BlockFilters.replaceable().not());
    }

    /**
     * Transforms a distribution by scanning upwards for a solid floor from each sampled position.
     *
     * @param parent           The parent distribution.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @return The transformed distribution.
     */
    public static FeatureDistribution ceiling(final FeatureDistribution parent, final int maxDistExclusive) {
        return scan(replaceable(parent), EnumFacing.UP, maxDistExclusive, BlockFilters.replaceable().not());
    }

    /**
     * Transforms a distribution by scanning in a random direction for a solid surface from each sampled position.
     *
     * @param parent           The parent distribution.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @return The transformed distribution.
     */
    public static FeatureDistribution randomWall(final FeatureDistribution parent, final int maxDistExclusive) {
        return scanRandomDir(replaceable(parent), maxDistExclusive, BlockFilters.replaceable().not());
    }
}
