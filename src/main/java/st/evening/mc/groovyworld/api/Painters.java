package st.evening.mc.groovyworld.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DungeonHooks;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;

/**
 * A library of painters.
 */
public enum Painters {
    ;

    /**
     * A painter that paints nothing.
     */
    public static Painter nothing() {
        return (rng, world, paintPos) -> true;
    }

    /**
     * A painter that always fails.
     */
    public static Painter fail() {
        return (rng, world, paintPos) -> false;
    }

    /**
     * A painter that places a block's default state at the target position.
     *
     * @param block The block to place.
     */
    public static Painter point(final Block block) {
        return (rng, world, paintPos) -> world.setBlockState(paintPos, block.getDefaultState());
    }

    /**
     * A painter that places a block state at the target position.
     *
     * @param state The block state to place.
     */
    public static Painter point(final IBlockState state) {
        return (rng, world, paintPos) -> world.setBlockState(paintPos, state);
    }

    /**
     * A painter that places a block state sampled from a palette at the target position.
     *
     * @param palette The palette to sample a block state from.
     */
    public static Painter point(final BlockPalette palette) {
        return (rng, world, paintPos) -> world.setBlockState(paintPos, palette.pickBlock(rng, world, paintPos));
    }

    /**
     * A painter that paints the blocks enumerated by a brush geometry with blocks sampled from a block palette.
     *
     * @param geometry The geometry providing the positions to paint.
     * @param palette  The block palette providing the block states to paint.
     */
    public static Painter brush(final BrushGeometry geometry, final BlockPalette palette) {
        return (rng, world, paintPos) -> {
            final Iterator<BlockPos> iter = geometry.createBrush(rng, world, paintPos).iterator();
            if (!iter.hasNext()) return false;
            boolean success = false;
            do {
                final BlockPos pos = iter.next();
                if (world.setBlockState(pos, palette.pickBlock(rng, world, pos))) {
                    success = true;
                }
            } while (iter.hasNext());
            return success;
        };
    }

    /**
     * A painter that paints the blocks enumerated by a brush geometry with blocks sampled from a block palette. A block
     * filter additionally restricts the positions to be painted.
     *
     * @param geometry The geometry providing the positions to paint.
     * @param filter   The block filter restricting the positions to paint.
     * @param palette  The block palette providing the block states to paint.
     */
    public static Painter brush(final BrushGeometry geometry, final BlockFilter filter, final BlockPalette palette) {
        return (rng, world, paintPos) -> {
            boolean success = false;
            for (final BlockPos pos : geometry.createBrush(rng, world, paintPos)) {
                if (filter.matchPos(rng, world, pos)) {
                    success = true;
                    world.setBlockState(pos, palette.pickBlock(rng, world, pos));
                }
            }
            return success;
        };
    }

    /**
     * A painter that paints a mob spawner at the target position.
     *
     * @param entityIdProvider A function providing the ID of the entity to spawn.
     */
    public static Painter mobSpawner(final Function<Random, ResourceLocation> entityIdProvider) {
        return (rng, world, paintPos) -> {
            if (!world.setBlockState(paintPos, Blocks.MOB_SPAWNER.getDefaultState())) return false;
            final TileEntity te = world.getTileEntity(paintPos);
            if (te instanceof TileEntityMobSpawner) {
                ((TileEntityMobSpawner) te).getSpawnerBaseLogic().setEntityId(entityIdProvider.apply(rng));
            }
            return true;
        };
    }

    /**
     * A painter that paints a mob spawner at the target position.
     *
     * @param entityId The ID of the entity to spawn.
     */
    public static Painter mobSpawner(final ResourceLocation entityId) {
        return mobSpawner(rng -> entityId);
    }

    /**
     * A painter that paints a mob spawner at the target position. The mob to spawn is randomly picked from the default
     * Minecraft mob spawner pool, which can be manipulated with {@link DungeonHooks}.
     */
    public static Painter mobSpawner() {
        return mobSpawner(DungeonHooks::getRandomDungeonMob);
    }

    /**
     * A painter that sets a loot table for a container that supports it (e.g. chests, shulker boxes, dispensers). This
     * painter does not place any blocks; the loot container must already be placed in the world!
     *
     * @param lootTableId The ID of the loot table to set. See {@link net.minecraft.world.storage.loot.LootTableList}
     *                    for the default Minecraft loot tables.
     */
    public static Painter loot(final ResourceLocation lootTableId) {
        return (rng, world, paintPos) -> {
            final TileEntity te = world.getTileEntity(paintPos);
            if (!(te instanceof TileEntityLockableLoot)) return false;
            ((TileEntityLockableLoot) te).setLootTable(lootTableId, rng.nextLong());
            return true;
        };
    }
}
