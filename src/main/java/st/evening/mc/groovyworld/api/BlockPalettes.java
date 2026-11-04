package st.evening.mc.groovyworld.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.List;

/**
 * A library of block palettes.
 */
public enum BlockPalettes {
    ;

    /**
     * Creates a builder for a custom block palette with weighted random sampling.
     *
     * @return A new block palette builder.
     */
    public static BlockPalette.Builder builder() {
        return new BlockPalette.Builder();
    }

    /**
     * A block palette that always outputs air.
     */
    public static BlockPalette air() {
        return (rng, world, pos) -> Blocks.AIR.getDefaultState();
    }

    /**
     * A block palette that always outputs a given block. The block's default state is used.
     *
     * @param block The block to output.
     */
    public static BlockPalette block(final Block block) {
        return (rng, world, pos) -> block.getDefaultState();
    }

    /**
     * A block palette that picks uniformly from a list of blocks. The blocks' default states are used.
     *
     * @param blocks The list of block outcomes.
     */
    public static BlockPalette blocks(final List<Block> blocks) {
        return (rng, world, pos) -> blocks.get(rng.nextInt(blocks.size())).getDefaultState();
    }

    /**
     * A block palette that picks uniformly from a list of blocks. The blocks' default states are used.
     *
     * @param blocks The list of block outcomes.
     */
    public static BlockPalette blocks(final Block... blocks) {
        return (rng, world, pos) -> blocks[rng.nextInt(blocks.length)].getDefaultState();
    }

    /**
     * A block palette that always outputs a given block state.
     *
     * @param state The block state to output.
     */
    public static BlockPalette state(final IBlockState state) {
        return (rng, world, pos) -> state;
    }

    /**
     * A block palette that picks uniformly from a list of block states.
     *
     * @param states The list of block state outcomes.
     */
    public static BlockPalette states(final List<IBlockState> states) {
        return (rng, world, pos) -> states.get(rng.nextInt(states.size()));
    }

    /**
     * A block palette that picks uniformly from a list of block states.
     *
     * @param states The list of block state outcomes.
     */
    public static BlockPalette states(final IBlockState... states) {
        return (rng, world, pos) -> states[rng.nextInt(states.length)];
    }
}
