package st.evening.mc.groovyworld.worldgen;

import net.minecraft.world.World;

import java.util.Random;

/**
 * A feature to be generated in a chunk.
 */
@FunctionalInterface
public interface GrWFeature {
    /**
     * Attempts to generate the feature in the given chunk.
     *
     * @param rng       The world generation RNG.
     * @param world     The world to generate in.
     * @param chunkMinX The minimum X-coordinate of the chunk.
     * @param chunkMinZ The minimum Z-coordinate of the chunk.
     */
    void generate(Random rng, World world, int chunkMinX, int chunkMinZ);
}
