package st.evening.mc.groovyworld.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A geometry which creates a brush at a given point in a world.
 */
@FunctionalInterface
public interface BrushGeometry {
    /**
     * Creates a brush at a position in a world. This operation should not modify the world!
     *
     * @param rng      The world generation RNG.
     * @param world    The world for which the brush is being created.
     * @param brushPos The position at which the brush should be created (the "base position").
     * @return The new brush.
     */
    Brush createBrush(final Random rng, final World world, final BlockPos brushPos);

    /**
     * Transforms this brush geometry by applying a map to each position in a created brush. If the map returns null at
     * a position, that position is dropped entirely.
     *
     * @param f The transformation map.
     * @return The transformed brush geometry.
     */
    default BrushGeometry transform(final Function<BlockPos, @Nullable BlockPos> f) {
        return (rng, world, brushPos) -> createBrush(rng, world, brushPos).transform(f);
    }

    /**
     * "Expands" this brush geometry by applying a sub-geometry to each position in a created brush, then combining all
     * the resulting sub-brushes into one big brush. Comparable to the {@code flatMap} operation on collections.
     *
     * @param subGeometry The sub-geometry.
     * @return The expanded brush geometry.
     */
    default BrushGeometry expand(final BrushGeometry subGeometry) {
        return (rng, world, brushPos) ->
                createBrush(rng, world, brushPos).expand(pos -> subGeometry.createBrush(rng, world, pos));
    }

    /**
     * Filters this brush geometry by filtering the positions in created brushes.
     *
     * @param f The filter.
     * @return The filtered brush geometry.
     */
    default BrushGeometry filter(final Predicate<? super BlockPos> f) {
        return (rng, world, brushPos) -> createBrush(rng, world, brushPos).filter(f);
    }

    /**
     * Filters this brush geometry by filtering the positions in created brushes with a block filter.
     *
     * @param filter The block filter.
     * @return The filtered brush geometry.
     */
    default BrushGeometry filterBlocks(final BlockFilter filter) {
        return (rng, world, brushPos) ->
                createBrush(rng, world, brushPos).filter(pos -> filter.matchPos(rng, world, pos));
    }

    /**
     * Filters this brush geometry by filtering each position in a created brush with a position-dependent block filter.
     *
     * @param f The position-dependent block filter provider.
     * @return The filtered brush geometry.
     */
    default BrushGeometry filterBlocks(final Function<BlockPos, BlockFilter> f) {
        return (rng, world, brushPos) -> {
            final BlockFilter filter = f.apply(brushPos);
            return createBrush(rng, world, brushPos).filter(pos -> filter.matchPos(rng, world, pos));
        };
    }

    /**
     * Translates this brush geometry by translation the positions in created brushes by a given offset vector.
     *
     * @param dx The offset on the X-axis.
     * @param dy The offset on the Y-axis.
     * @param dz The offset on the Z-axis.
     * @return The translated brush geometry.
     */
    default BrushGeometry offset(final int dx, final int dy, final int dz) {
        return transform(pos -> pos.add(dx, dy, dz));
    }

    /**
     * Translates this brush geometry by translation the positions in created brushes by a given offset vector.
     *
     * @param offset The offset vector.
     * @return The translated brush geometry.
     */
    default BrushGeometry offset(final Vec3i offset) {
        return transform(pos -> pos.add(offset));
    }

    /**
     * Combines this brush geometry with another by combining the created brushes.
     *
     * @param o The other brush geometry.
     * @return The combined brush geometry.
     */
    default BrushGeometry plus(final BrushGeometry o) {
        return (rng, world, brushPos) -> createBrush(rng, world, brushPos).plus(o.createBrush(rng, world, brushPos));
    }
}
