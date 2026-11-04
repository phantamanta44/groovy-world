package st.evening.mc.groovyworld.api;

import com.google.common.primitives.Ints;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.Collection;

/**
 * A library of chunk filters.
 */
public enum ChunkFilters {
    ;

    /**
     * A chunk filter that rejects everything.
     */
    public static ChunkFilter reject() {
        return (rng, world, posX, posZ) -> false;
    }

    /**
     * A chunk filter that accepts everything.
     */
    public static ChunkFilter accept() {
        return (rng, world, posX, posZ) -> true;
    }

    /**
     * A chunk filter that randomly accepts or rejects chunks.
     *
     * @param probability The probability that a chunk is accepted.
     */
    public static ChunkFilter random(final float probability) {
        return (rng, world, posX, posZ) -> RNGHelper.bernoulli(rng, probability);
    }

    /**
     * A chunk filter that randomly accepts or rejects chunks.
     *
     * @param n The odds (as in "one in n") that a chunk is accepted.
     */
    public static ChunkFilter randomOneIn(final int n) {
        return (rng, world, posX, posZ) -> rng.nextInt(n) == 0;
    }

    /**
     * A chunk filter that matches on the current dimension. Vanilla assigns the IDs 0 for the overworld, -1 for the
     * nether, and 1 for the end.
     *
     * @param dimension The required dimension ID.
     */
    public static ChunkFilter dimension(final int dimension) {
        return (rng, world, posX, posZ) -> world.provider.getDimension() == dimension;
    }

    /**
     * A chunk filter that matches on the current dimension. Vanilla assigns the IDs 0 for the overworld, -1 for the
     * nether, and 1 for the end.
     *
     * @param dimensions A collection of permissible dimension IDs.
     */
    public static ChunkFilter dimensions(final Collection<Integer> dimensions) {
        return (rng, world, posX, posZ) -> dimensions.contains(world.provider.getDimension());
    }

    /**
     * A chunk filter that matches on the current dimension. Vanilla assigns the IDs 0 for the overworld, -1 for the
     * nether, and 1 for the end.
     *
     * @param dimensions A set of permissible dimension IDs.
     */
    public static ChunkFilter dimensions(final IntSet dimensions) {
        return (rng, world, posX, posZ) -> dimensions.contains(world.provider.getDimension());
    }

    /**
     * A chunk filter that matches on the current dimension. Vanilla assigns the IDs 0 for the overworld, -1 for the
     * nether, and 1 for the end.
     *
     * @param dimensions A list of permissible dimension IDs.
     */
    public static ChunkFilter dimensions(final int... dimensions) {
        return (rng, world, posX, posZ) -> Ints.contains(dimensions, world.provider.getDimension());
    }
}
