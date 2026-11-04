package st.evening.mc.groovyworld.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import org.jspecify.annotations.Nullable;

/**
 * Utilities for constructing block filters.
 */
public enum BlockFilterHelper {
    ;

    /**
     * Checks that the biome temperature at the given block position lies within the given range.
     *
     * @param world   The world to examine.
     * @param pos     The position in the world to examine.
     * @param minTemp The minimum permissible biome temperature.
     * @param maxTemp The maximum permissible biome temperature.
     * @return Whether the biome temperature is in the given range.
     */
    public static boolean isBiomeTemperatureInRange(final World world, final BlockPos pos,
                                                    final float minTemp, final float maxTemp) {
        final float temp = world.getBiome(pos).getTemperature(pos);
        return temp >= minTemp && temp <= maxTemp;
    }

    /**
     * Checks that the biome humidity (rainfall strength) at the given block position lies within the given range.
     *
     * @param world       The world to examine.
     * @param pos         The position in the world to examine.
     * @param minHumidity The minimum permissible biome humidity.
     * @param maxHumidity The maximum permissible biome humidity.
     * @return Whether the biome humidity is in the given range.
     */
    public static boolean isBiomeHumidityInRange(final World world, final BlockPos pos,
                                                 final float minHumidity, final float maxHumidity) {
        final float humidity = world.getBiome(pos).getRainfall();
        return humidity >= minHumidity && humidity <= maxHumidity;
    }

    /**
     * Checks whether a given block is a fluid block.
     *
     * @param block The block to examine.
     * @return Whether the block is a fluid block.
     */
    public static boolean isFluid(final Block block) {
        return block instanceof BlockLiquid || block instanceof IFluidBlock;
    }

    /**
     * Checks whether the block for a given block state is a fluid block.
     *
     * @param state The block state to examine.
     * @return Whether the block for the state is a fluid block.
     */
    public static boolean isFluid(final IBlockState state) {
        return isFluid(state.getBlock());
    }

    /**
     * Gets the fluid represented by a block, if any.
     *
     * @param block The block to examine.
     * @return The fluid represented by the block, or {@code null} if it's not a fluid block.
     */
    public static @Nullable Fluid getFluidFromBlock(final Block block) {
        if (block instanceof IFluidBlock) {
            return ((IFluidBlock) block).getFluid();
        }
        return FluidRegistry.lookupFluidForBlock(block);
    }

    /**
     * Gets the fluid represented by a block state, if any.
     *
     * @param state The block state to examine.
     * @return The fluid represented by the block state, or {@code null} if it's not a fluid block.
     */
    public static @Nullable Fluid getFluidFromBlock(final IBlockState state) {
        return getFluidFromBlock(state.getBlock());
    }

    /**
     * Checks whether the block at a position is replaceable. These are blocks that can be directly overwritten by
     * another block, which includes tall grass and liquid blocks.
     *
     * @param world The world to examine.
     * @param pos   The position in the world to examine.
     * @return Whether the block is replaceable or not.
     */
    public static boolean isReplaceable(final World world, final BlockPos pos) {
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
    }

    /**
     * Checks whether the block at a position is typical overworld surface decoration. This broadly includes trees,
     * plants, fluids, and air.
     *
     * @param world The world to examine.
     * @param pos   The position in the world to examine.
     * @return Whether the block is surface decoration or not.
     */
    public static boolean isSurfaceDecoration(final World world, final BlockPos pos) {
        final IBlockState state = world.getBlockState(pos);
        if (!state.getMaterial().blocksMovement()) return true;
        final Block block = state.getBlock();
        return block.isReplaceable(world, pos)
                || block.isWood(world, pos)
                || block.isFoliage(world, pos)
                || block.isLeaves(state, world, pos)
                || block.canBeReplacedByLeaves(state, world, pos);
    }

    /**
     * Checks whether the given block state is a "natural" stone block (i.e. not one of the crafted smooth variants).
     * Useful as a block filter for overworld ore generation.
     *
     * @param state The block state to examine.
     * @return Whether the given block state is a natural stone block.
     */
    public static boolean isNaturalStone(final IBlockState state) {
        return state.getBlock() == Blocks.STONE && state.getValue(BlockStone.VARIANT).isNatural();
    }
}
