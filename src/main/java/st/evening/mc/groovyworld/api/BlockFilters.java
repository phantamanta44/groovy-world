package st.evening.mc.groovyworld.api;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;
import st.evening.mc.groovyworld.util.collection.CollectionHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A library of block filters.
 */
public enum BlockFilters {
    ;

    /**
     * A block filter that rejects everything.
     */
    public static BlockFilter reject() {
        return (rng, world, pos) -> false;
    }

    /**
     * A block filter that accepts everything.
     */
    public static BlockFilter accept() {
        return (rng, world, pos) -> true;
    }

    /**
     * A block filter that randomly accepts or rejects blocks.
     *
     * @param probability The probability that a block is accepted.
     */
    public static BlockFilter random(final float probability) {
        return (rng, world, pos) -> RNGHelper.bernoulli(rng, probability);
    }

    /**
     * A block filter that randomly accepts or rejects blocks.
     *
     * @param n The odds (as in "one in n") that a block is accepted.
     */
    public static BlockFilter randomOneIn(final int n) {
        return (rng, world, pos) -> rng.nextInt(n) == 0;
    }

    /**
     * A block filter that randomly accepts or rejects blocks with odds worsening with distance from a reference point.
     * The probability decreases on a quadratic curve, unlike {@link #randomByDistance}, and is somewhat cheaper to
     * compute.
     *
     * @param x           The reference point's X-coordinate.
     * @param y           The reference point's Y-coordinate.
     * @param z           The reference point's Z-coordinate.
     * @param maxDist     The maximum distance from the reference point (i.e. the distance at which the acceptance
     *                    probability reaches zero).
     * @param probability The maximum acceptance probability (i.e. the acceptance probability at the reference point).
     */
    public static BlockFilter randomByDistanceSq(final double x, final double y, final double z, final double maxDist,
                                                 final float probability) {
        final double maxDistSq = maxDist * maxDist;
        final float probFrac = probability / (float) maxDistSq;
        return (rng, world, pos) ->
                RNGHelper.bernoulli(rng, probFrac * (float) (maxDistSq - pos.distanceSqToCenter(x, y, z)));
    }

    /**
     * A block filter that randomly accepts or rejects blocks with odds worsening with distance from a reference point.
     * The probability decreases on a quadratic curve, unlike {@link #randomByDistance}, and is somewhat cheaper to
     * compute.
     *
     * @param pos         The reference point.
     * @param maxDist     The maximum distance from the reference point (i.e. the distance at which the acceptance
     *                    probability reaches zero).
     * @param probability The maximum acceptance probability (i.e. the acceptance probability at the reference point).
     */
    public static BlockFilter randomByDistanceSq(final Vec3d pos, final double maxDist, final float probability) {
        return randomByDistanceSq(pos.x, pos.y, pos.z, maxDist, probability);
    }

    /**
     * A block filter that randomly accepts or rejects blocks with odds worsening with distance from a reference point.
     * The probability decreases on a quadratic curve, unlike {@link #randomByDistance}, and is somewhat cheaper to
     * compute.
     *
     * @param pos         The reference point.
     * @param maxDist     The maximum distance from the reference point (i.e. the distance at which the acceptance
     *                    probability reaches zero).
     * @param probability The maximum acceptance probability (i.e. the acceptance probability at the reference point).
     */
    public static BlockFilter randomByDistanceSq(final BlockPos pos, final double maxDist, final float probability) {
        return randomByDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, maxDist, probability);
    }

    /**
     * A block filter that randomly accepts or rejects blocks with odds worsening with distance from a reference point.
     * The probability decreases linearly with distance, unlike {@link #randomByDistanceSq}, and is somewhat more
     * expensive to compute.
     *
     * @param x           The reference point's X-coordinate.
     * @param y           The reference point's Y-coordinate.
     * @param z           The reference point's Z-coordinate.
     * @param maxDist     The maximum distance from the reference point (i.e. the distance at which the acceptance
     *                    probability reaches zero).
     * @param probability The maximum acceptance probability (i.e. the acceptance probability at the reference point).
     */
    public static BlockFilter randomByDistance(final double x, final double y, final double z, final double maxDist,
                                               final float probability) {
        final float probFrac = probability / (float) maxDist;
        return (rng, world, pos) ->
                RNGHelper.bernoulli(rng, probFrac * (float) (maxDist - Math.sqrt(pos.distanceSqToCenter(x, y, z))));
    }

    /**
     * A block filter that randomly accepts or rejects blocks with odds worsening with distance from a reference point.
     * The probability decreases linearly with distance, unlike {@link #randomByDistanceSq}, and is somewhat more
     * expensive to compute.
     *
     * @param pos         The reference point.
     * @param maxDist     The maximum distance from the reference point (i.e. the distance at which the acceptance
     *                    probability reaches zero).
     * @param probability The maximum acceptance probability (i.e. the acceptance probability at the reference point).
     */
    public static BlockFilter randomByDistance(final Vec3d pos, final double maxDist, final float probability) {
        return randomByDistance(pos.x, pos.y, pos.z, maxDist, probability);
    }

    /**
     * A block filter that randomly accepts or rejects blocks with odds worsening with distance from a reference point.
     * The probability decreases linearly with distance, unlike {@link #randomByDistanceSq}, and is somewhat more
     * expensive to compute.
     *
     * @param pos         The reference point.
     * @param maxDist     The maximum distance from the reference point (i.e. the distance at which the acceptance
     *                    probability reaches zero).
     * @param probability The maximum acceptance probability (i.e. the acceptance probability at the reference point).
     */
    public static BlockFilter randomByDistance(final BlockPos pos, final double maxDist, final float probability) {
        return randomByDistance(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, maxDist, probability);
    }

    /**
     * A block filter that checks that the target position is sufficiently close to a reference point.
     *
     * @param x       The reference point's X-coordinate.
     * @param y       The reference point's X-coordinate.
     * @param z       The reference point's Z-coordinate.
     * @param maxDist The maximum permissible distance from the reference point.
     */
    public static BlockFilter distance(final double x, final double y, final double z, final double maxDist) {
        final double maxDistSq = maxDist * maxDist;
        return (rng, world, pos) -> pos.distanceSqToCenter(x, y, z) <= maxDistSq;
    }

    /**
     * A block filter that checks that the target position is sufficiently close to a reference point.
     *
     * @param pos     The reference point.
     * @param maxDist The maximum permissible distance from the reference point.
     */
    public static BlockFilter distance(final Vec3d pos, final double maxDist) {
        return distance(pos.x, pos.y, pos.z, maxDist);
    }

    /**
     * A block filter that checks that the target position is sufficiently close to a reference point.
     *
     * @param pos     The reference point.
     * @param maxDist The maximum permissible distance from the reference point.
     */
    public static BlockFilter distance(final BlockPos pos, final double maxDist) {
        return distance(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, maxDist);
    }

    /**
     * A block filter that matches on the biome at the target position.
     *
     * @param biome The required biome.
     */
    public static BlockFilter biome(final Biome biome) {
        return (rng, world, pos) -> world.getBiome(pos) == biome;
    }


    /**
     * A block filter that matches on the biome at the target position.
     *
     * @param biomes A collection of permissible biomes.
     */
    public static BlockFilter biomes(final Collection<Biome> biomes) {
        return (rng, world, pos) -> biomes.contains(world.getBiome(pos));
    }

    /**
     * A block filter that matches on the biome at the target position.
     *
     * @param biomes A list of permissible biomes.
     */
    public static BlockFilter biomes(final Biome... biomes) {
        return biomes(Sets.newHashSet(biomes));
    }

    /**
     * A block filter that matches on the biome dictionary tags of the biome at the target position.
     *
     * @param type The required biome dictionary tag.
     */
    public static BlockFilter biomeDict(final BiomeDictionary.Type type) {
        return (rng, world, pos) -> BiomeDictionary.hasType(world.getBiome(pos), type);
    }

    /**
     * A block filter that matches on the biome dictionary tags of the biome at the target position.
     *
     * @param types A collection of permissible biome dictionary tags.
     */
    public static BlockFilter biomeDicts(final Collection<BiomeDictionary.Type> types) {
        return (rng, world, pos) -> CollectionHelper.containsAny(BiomeDictionary.getTypes(world.getBiome(pos)), types);
    }

    /**
     * A block filter that matches on the biome dictionary tags of the biome at the target position.
     *
     * @param types A list of permissible biome dictionary tags.
     */
    public static BlockFilter biomeDicts(final BiomeDictionary.Type... types) {
        return (rng, world, pos) -> CollectionHelper.containsAny(BiomeDictionary.getTypes(world.getBiome(pos)), types);
    }

    /**
     * A block filter that matches on the biome temperature categories of the biome at the target position.
     *
     * @param category The required biome temperature category.
     */
    public static BlockFilter biomeTemperatureCategory(final Biome.TempCategory category) {
        return (rng, world, pos) -> world.getBiome(pos).getTempCategory() == category;
    }

    /**
     * A block filter that matches on the biome temperature categories of the biome at the target position.
     *
     * @param categories A collection of permissible biome temperature categories.
     */
    public static BlockFilter biomeTemperatureCategories(final Collection<Biome.TempCategory> categories) {
        return (rng, world, pos) -> categories.contains(world.getBiome(pos).getTempCategory());
    }

    /**
     * A block filter that matches on the biome temperature categories of the biome at the target position.
     *
     * @param categories A list of permissible biome temperature categories.
     */
    public static BlockFilter biomeTemperatureCategories(final Biome.TempCategory... categories) {
        return (rng, world, pos) -> ArrayUtils.contains(categories, world.getBiome(pos).getTempCategory());
    }

    /**
     * A block filter that matches on the biome temperature of the biome at the target position.
     *
     * @param minTemp The minimum permissible biome temperature.
     * @param maxTemp The maximum permissible biome temperature.
     */
    public static BlockFilter biomeTemperature(final float minTemp, final float maxTemp) {
        return (rng, world, pos) -> BlockFilterHelper.isBiomeTemperatureInRange(world, pos, minTemp, maxTemp);
    }

    /**
     * A block filter that matches on the biome humidity (rainfall strength) of the biome at the target position.
     *
     * @param minHumidity The minimum permissible biome humidity.
     * @param maxHumidity The maximum permissible biome humidity.
     */
    public static BlockFilter biomeHumidity(final float minHumidity, final float maxHumidity) {
        return (rng, world, pos) -> BlockFilterHelper.isBiomeHumidityInRange(world, pos, minHumidity, maxHumidity);
    }

    /**
     * A block filter that checks that there's a clear line of sight to the sky from the target position.
     */
    public static BlockFilter skyVisible() {
        return (rng, world, pos) -> world.canSeeSky(pos);
    }

    /**
     * A block filter that matches air blocks.
     */
    public static BlockFilter air() {
        return (rng, world, pos) -> world.isAirBlock(pos);
    }

    /**
     * A block filter that matches on the block at the target position.
     *
     * @param block The required block.
     */
    public static BlockFilter block(final Block block) {
        return (rng, world, pos) -> world.getBlockState(pos).getBlock() == block;
    }

    /**
     * A block filter that matches on the block at the target position.
     *
     * @param blocks A collection of permissible blocks.
     */
    public static BlockFilter blocks(final Collection<Block> blocks) {
        return (rng, world, pos) -> blocks.contains(world.getBlockState(pos).getBlock());
    }

    /**
     * A block filter that matches on the block at the target position.
     *
     * @param blocks A list of permissible blocks.
     */
    public static BlockFilter blocks(final Block... blocks) {
        return blocks(Sets.newHashSet(blocks));
    }

    /**
     * A block filter that matches on the block state at the target position.
     *
     * @param state The required block state.
     */
    public static BlockFilter blockState(final IBlockState state) {
        return (rng, world, pos) -> world.getBlockState(pos) == state;
    }

    /**
     * A block filter that matches on the block state at the target position.
     *
     * @param states A collection of permissible block states.
     */
    public static BlockFilter blockStates(final Collection<IBlockState> states) {
        return (rng, world, pos) -> states.contains(world.getBlockState(pos));
    }

    /**
     * A block filter that matches on the block state at the target position.
     *
     * @param states A list of permissible block states.
     */
    public static BlockFilter blockStates(final IBlockState... states) {
        return blockStates(Sets.newHashSet(states));
    }

    /**
     * A block filter that matches on the block material at the target position.
     *
     * @param material The required block material.
     */
    public static BlockFilter material(final Material material) {
        return (rng, world, pos) -> world.getBlockState(pos).getMaterial() == material;
    }

    /**
     * A block filter that matches on the block material at the target position.
     *
     * @param materials A collection of permissible block materials.
     */
    public static BlockFilter materials(final Collection<Material> materials) {
        return (rng, world, pos) -> materials.contains(world.getBlockState(pos).getMaterial());
    }

    /**
     * A block filter that matches on the block material at the target position.
     *
     * @param materials A list of permissible block materials.
     */
    public static BlockFilter materials(final Material... materials) {
        return materials(Sets.newHashSet(materials));
    }

    /**
     * A block filter that matches fluid blocks (i.e. water, lava, and modded fluids).
     */
    public static BlockFilter fluid() {
        return (rng, world, pos) -> BlockFilterHelper.isFluid(world.getBlockState(pos));
    }

    /**
     * A block filter that matches on the fluid represented by a fluid block at the target position.
     *
     * @param fluid The required fluid.
     */
    public static BlockFilter fluid(final Fluid fluid) {
        return (rng, world, pos) -> BlockFilterHelper.getFluidFromBlock(world.getBlockState(pos)) == fluid;
    }

    /**
     * A block filter that matches on the fluid represented by a fluid block at the target position.
     *
     * @param fluidStack The required fluid, represented as a fluid stack. The stack's NBT tag will not be checked!
     */
    public static BlockFilter fluid(final FluidStack fluidStack) {
        return fluid(fluidStack.getFluid());
    }

    /**
     * A block filter that matches water blocks.
     */
    public static BlockFilter water() {
        return fluid(FluidRegistry.WATER);
    }

    /**
     * A block filter that matches lava blocks.
     */
    public static BlockFilter lava() {
        return fluid(FluidRegistry.LAVA);
    }

    /**
     * A block filter that matches on the fluid represented by a fluid block at the target position.
     *
     * @param fluids A collection of permissible fluids.
     */
    public static BlockFilter fluids(final Collection<Fluid> fluids) {
        return (rng, world, pos) -> fluids.contains(BlockFilterHelper.getFluidFromBlock(world.getBlockState(pos)));
    }

    /**
     * A block filter that matches on the fluid represented by a fluid block at the target position.
     *
     * @param fluids A list of permissible fluids.
     */
    public static BlockFilter fluids(final Fluid... fluids) {
        return fluids(Sets.newHashSet(fluids));
    }

    /**
     * A block filter that matches on the fluid represented by a fluid block at the target position.
     *
     * @param fluids A collection of permissible fluids, represented as fluid stacks. The NBT tags will not be checked!
     */
    public static BlockFilter fluidsFromStacks(final Collection<FluidStack> fluids) {
        final Set<Fluid> allowed = new HashSet<>();
        for (final FluidStack fluid : fluids) {
            allowed.add(fluid.getFluid());
        }
        return fluids(allowed);
    }

    /**
     * A block filter that matches on the fluid represented by a fluid block at the target position.
     *
     * @param fluids A list of permissible fluids, represented as fluid stacks. The NBT tags will not be checked!
     */
    public static BlockFilter fluids(final FluidStack... fluids) {
        final Set<Fluid> allowed = new HashSet<>();
        for (final FluidStack fluid : fluids) {
            allowed.add(fluid.getFluid());
        }
        return fluids(allowed);
    }

    /**
     * A block filter that matches replaceable blocks. These are blocks that can be directly overwritten by another
     * block, which includes tall grass and liquid blocks.
     */
    public static BlockFilter replaceable() {
        return (rng, world, pos) -> BlockFilterHelper.isReplaceable(world, pos);
    }

    /**
     * A block filter that matches typical overworld surface decoration. This broadly includes trees, plants, fluids,
     * and air.
     */
    public static BlockFilter surfaceDecoration() {
        return (rng, world, pos) -> BlockFilterHelper.isSurfaceDecoration(world, pos);
    }

    /**
     * A block filter that matches "natural" stone blocks (i.e. not one of the crafted smooth variants).
     */
    public static BlockFilter naturalStone() {
        return (rng, world, pos) -> BlockFilterHelper.isNaturalStone(world.getBlockState(pos));
    }
}
