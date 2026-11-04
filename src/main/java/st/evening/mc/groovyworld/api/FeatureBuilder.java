package st.evening.mc.groovyworld.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import st.evening.mc.groovyworld.worldgen.GrWFeature;

import java.util.Random;

/**
 * A builder for a simple feature generator.
 * <p>
 * The generation process for each chunk is roughly as follows:
 * <ol>
 *     <li>Test the chunk against the chunk filter. If it's rejected, abort the generation attempt.</li>
 *     <li>Sample a candidate position from the block distribution.</li>
 *     <li>Test the position against the block filter. If it's rejected, retry from (2).</li>
 *     <li>Try to paint at the position with the painter. If it fails, retry from (2).</li>
 *     <li>Check if we've reached the maximum success count; if not, repeat from (2).</li>
 * </ol>
 */
public class FeatureBuilder {
    private ChunkFilter chunkFilter = ChunkFilters.accept();
    private int triesPerChunk = 1;
    private int maxSuccessesPerChunk = Integer.MAX_VALUE;
    private @Nullable FeatureDistribution distribution = null;
    private BlockFilter blockFilter = BlockFilters.accept();
    private @Nullable Painter painter = null;

    /**
     * Set the chunk filter.
     *
     * @param chunkFilter The new chunk builder.
     */
    public FeatureBuilder setChunkFilter(final ChunkFilter chunkFilter) {
        this.chunkFilter = chunkFilter;
        return this;
    }

    /**
     * Set the maximum number of generation attempts per chunk.
     *
     * @param triesPerChunk        The maximum number of generation attempts per chunk.
     * @param maxSuccessesPerChunk The maximum number of successes allowed per chunk.
     */
    public FeatureBuilder setTriesPerChunk(final int triesPerChunk, final int maxSuccessesPerChunk) {
        this.triesPerChunk = triesPerChunk;
        this.maxSuccessesPerChunk = maxSuccessesPerChunk;
        return this;
    }

    /**
     * Set the maximum number of generation attempts per chunk.
     *
     * @param triesPerChunk The maximum number of generation attempts per chunk.
     */
    public FeatureBuilder setTriesPerChunk(final int triesPerChunk) {
        return setTriesPerChunk(triesPerChunk, Integer.MAX_VALUE);
    }

    /**
     * Sets the feature distribution.
     *
     * @param distribution The new feature distribution.
     */
    public FeatureBuilder setDistribution(final FeatureDistribution distribution) {
        this.distribution = distribution;
        return this;
    }

    /**
     * Sets the block filter.
     *
     * @param blockFilter The new block filter.
     */
    public FeatureBuilder setBlockFilter(final BlockFilter blockFilter) {
        this.blockFilter = blockFilter;
        return this;
    }

    /**
     * Sets the painter.
     *
     * @param painter The new painter.
     */
    public FeatureBuilder setPainter(final Painter painter) {
        this.painter = painter;
        return this;
    }

    /**
     * Builds the generation feature from the inputs to this builder.
     *
     * @return The newly-constructed feature.
     */
    public GrWFeature build() {
        if (distribution == null) throw new IllegalStateException("Cannot build feature without a distribution!");
        if (painter == null) throw new IllegalStateException("Cannot build feature without a painter!");
        return new BuiltFeature(chunkFilter, triesPerChunk, maxSuccessesPerChunk, distribution, blockFilter, painter);
    }

    private static class BuiltFeature implements GrWFeature {
        private final ChunkFilter chunkFilter;
        private final int triesPerChunk;
        private final int maxSuccessesPerChunk;
        private final FeatureDistribution distribution;
        private final BlockFilter blockFilter;
        private final Painter painter;

        private BuiltFeature(final ChunkFilter chunkFilter, final int triesPerChunk, final int maxSuccessesPerChunk,
                             final FeatureDistribution distribution, final BlockFilter blockFilter,
                             final Painter painter) {
            this.chunkFilter = chunkFilter;
            this.triesPerChunk = triesPerChunk;
            this.maxSuccessesPerChunk = maxSuccessesPerChunk;
            this.distribution = distribution;
            this.blockFilter = blockFilter;
            this.painter = painter;
        }

        @Override
        public void generate(final Random rng, final World world, final int chunkMinX, final int chunkMinZ) {
            if (!chunkFilter.matchChunk(rng, world, chunkMinX, chunkMinZ)) return;
            int successes = 0;
            for (int tryNum = 0; tryNum < triesPerChunk; tryNum++) {
                final BlockPos pos = distribution.sample(rng, world, chunkMinX, chunkMinZ);
                if (pos != null && blockFilter.matchPos(rng, world, pos) && painter.paint(rng, world, pos)) {
                    successes++;
                    if (successes >= maxSuccessesPerChunk) break;
                }
            }
        }
    }
}
