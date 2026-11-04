package st.evening.mc.groovyworld.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * A filter for matching positions and blocks in a world.
 */
@FunctionalInterface
public interface BlockFilter {
    /**
     * Checks for a match at a position in a world. This operation should not modify the world!
     *
     * @param rng   The world generation RNG.
     * @param world The world to examine.
     * @param pos   The position in the world to examine.
     * @return Whether a match was found or not.
     */
    boolean matchPos(final Random rng, World world, BlockPos pos);

    /**
     * Constructs a logically inverted filter.
     *
     * @return The inverted filter.
     */
    default BlockFilter not() {
        return (rng, world, pos) -> !matchPos(rng, world, pos);
    }

    /**
     * Constructs a logically inverted filter. Convenience operator overload for Groovy.
     *
     * @return The inverted filter.
     */
    default BlockFilter bitwiseNegate() {
        return not();
    }

    /**
     * Constructs a filter that must match blocks under both this filter and the other given filter.
     *
     * @param o The other filter.
     * @return The conjunction filter.
     */
    default BlockFilter and(final BlockFilter o) {
        return (rng, world, pos) -> matchPos(rng, world, pos) && o.matchPos(rng, world, pos);
    }

    /**
     * Constructs a filter that can match blocks under either this filter or the other given filter.
     *
     * @param o The other filter.
     * @return The disjunction filter.
     */
    default BlockFilter or(final BlockFilter o) {
        return (rng, world, pos) -> matchPos(rng, world, pos) || o.matchPos(rng, world, pos);
    }

    /**
     * Constructs a filter that matches at an offset from the given position.
     *
     * @param dx The offset on the X-axis.
     * @param dy The offset on the Y-axis.
     * @param dz The offset on the Z-axis.
     * @return The offset filter.
     */
    default BlockFilter atOffset(final int dx, final int dy, final int dz) {
        return (rng, world, pos) -> matchPos(rng, world, pos.add(dx, dy, dz));
    }

    /**
     * Constructs a filter that matches at the block above the given position.
     *
     * @return The offset filter.
     */
    default BlockFilter above() {
        return (rng, world, pos) -> matchPos(rng, world, pos.up());
    }

    /**
     * Constructs a filter that matches at the block below the given position.
     *
     * @return The offset filter.
     */
    default BlockFilter below() {
        return (rng, world, pos) -> matchPos(rng, world, pos.down());
    }

    /**
     * Constructs a filter that matches at the block to the north of the given position.
     *
     * @return The offset filter.
     */
    default BlockFilter north() {
        return (rng, world, pos) -> matchPos(rng, world, pos.north());
    }

    /**
     * Constructs a filter that matches at the block to the east of the given position.
     *
     * @return The offset filter.
     */
    default BlockFilter east() {
        return (rng, world, pos) -> matchPos(rng, world, pos.east());
    }

    /**
     * Constructs a filter that matches at the block to the south of the given position.
     *
     * @return The offset filter.
     */
    default BlockFilter south() {
        return (rng, world, pos) -> matchPos(rng, world, pos.south());
    }

    /**
     * Constructs a filter that matches at the block to the west of the given position.
     *
     * @return The offset filter.
     */
    default BlockFilter west() {
        return (rng, world, pos) -> matchPos(rng, world, pos.west());
    }
}
