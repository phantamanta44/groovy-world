package st.evening.mc.groovyworld.api;

import net.minecraft.world.World;

import java.util.Random;

/**
 * A filter for matching chunks in a world.
 */
@FunctionalInterface
public interface ChunkFilter {
    /**
     * Checks for a match at the chunk containing a given position. This operation should not modify the world!
     * The chunk coordinates can be obtained by shifting {@code posX} and {@code posZ} right by four bits.
     *
     * @param rng   The world generation RNG.
     * @param world The world to examine.
     * @param posX  The X-coordinate of the position to examine.
     * @param posZ  The Z-coordinate of the position to examine.
     * @return Whether the chunk matches or not.
     */
    boolean matchChunk(Random rng, World world, int posX, int posZ);

    /**
     * Constructs a logically inverted filter.
     *
     * @return The inverted filter.
     */
    default ChunkFilter not() {
        return (rng, world, posX, posZ) -> !matchChunk(rng, world, posX, posZ);
    }

    /**
     * Constructs a logically inverted filter. Convenience operator overload for Groovy.
     *
     * @return The inverted filter.
     */
    default ChunkFilter bitwiseNegate() {
        return not();
    }

    /**
     * Constructs a filter that must match chunks under both this filter and the other given filter.
     *
     * @param o The other filter.
     * @return The conjunction filter.
     */
    default ChunkFilter and(final ChunkFilter o) {
        return (rng, world, posX, posZ) -> matchChunk(rng, world, posX, posZ) && o.matchChunk(rng, world, posX, posZ);
    }

    /**
     * Constructs a filter that can match chunks under either this filter or the other given filter.
     *
     * @param o The other filter.
     * @return The disjunction filter.
     */
    default ChunkFilter or(final ChunkFilter o) {
        return (rng, world, posX, posZ) -> matchChunk(rng, world, posX, posZ) || o.matchChunk(rng, world, posX, posZ);
    }
}
