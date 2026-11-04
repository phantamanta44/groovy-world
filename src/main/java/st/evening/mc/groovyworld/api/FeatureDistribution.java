package st.evening.mc.groovyworld.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A distribution of positions in a chunk.
 */
@FunctionalInterface
public interface FeatureDistribution {
    /**
     * Samples a position from this distribution. This operation should not modify the world!
     *
     * @param rng       The world generation RNG.
     * @param world     The world in which features are being distributed.
     * @param chunkMinX The minimum X-coordinate of the chunk of interest.
     * @param chunkMinZ The minimum Z-coordinate of the chunk of interest.
     * @return The sampled position, or {@code null} if a position could not be sampled for some reason.
     */
    @Nullable BlockPos sample(Random rng, final World world, int chunkMinX, int chunkMinZ);

    /**
     * Transforms this distribution by applying a map to each sampled position.
     *
     * @param f The transformation map.
     * @return The transformed distribution.
     */
    default FeatureDistribution transform(final Function<BlockPos, @Nullable BlockPos> f) {
        return (rng, world, chunkMinX, chunkMinZ) -> {
            final BlockPos pos = sample(rng, world, chunkMinX, chunkMinZ);
            return pos != null ? f.apply(pos) : null;
        };
    }

    /**
     * Filters the positions in this distribution.
     *
     * @param f The filter.
     * @return The filtered distribution.
     */
    default FeatureDistribution filter(final Predicate<BlockPos> f) {
        return (rng, world, chunkMinX, chunkMinZ) -> {
            final BlockPos pos = sample(rng, world, chunkMinX, chunkMinZ);
            return pos != null && f.test(pos) ? pos : null;
        };
    }

    /**
     * Filters the positions in this distribution with a block filter.
     *
     * @param filter The filter.
     * @return The filtered distribution.
     */
    default FeatureDistribution filter(final BlockFilter filter) {
        return (rng, world, chunkMinX, chunkMinZ) -> {
            final BlockPos pos = sample(rng, world, chunkMinX, chunkMinZ);
            return pos != null && filter.matchPos(rng, world, pos) ? pos : null;
        };
    }

    /**
     * Translates this distribution by adding a fixed offset to each sampled position.
     *
     * @param dx The offset on the X-axis.
     * @param dy The offset on the Y-axis.
     * @param dz The offset on the Z-axis.
     * @return The translated distribution.
     */
    default FeatureDistribution offset(final int dx, final int dy, final int dz) {
        return (rng, world, chunkMinX, chunkMinZ) -> {
            final BlockPos pos = sample(rng, world, chunkMinX, chunkMinZ);
            return pos != null ? pos.add(dx, dy, dz) : null;
        };
    }

    /**
     * Translates this distribution by adding a fixed offset to each sampled position.
     *
     * @param offset The offset vector.
     * @return The translated distribution.
     */
    default FeatureDistribution offset(final Vec3i offset) {
        return (rng, world, chunkMinX, chunkMinZ) -> {
            final BlockPos pos = sample(rng, world, chunkMinX, chunkMinZ);
            return pos != null ? pos.add(offset) : null;
        };
    }
}
