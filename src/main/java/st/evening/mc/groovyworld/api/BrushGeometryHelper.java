package st.evening.mc.groovyworld.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import st.evening.mc.groovyworld.util.function.FloatFunction;
import st.evening.mc.groovyworld.util.ScanBlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utilities for constructing brush geometries.
 */
public enum BrushGeometryHelper {
    ;

    /**
     * Creates a brush by performing a random walk on the block grid from a starting position.
     *
     * @param rng      The RNG to use for direction selection.
     * @param initialX The X-coordinate of the starting position.
     * @param initialY The Y-coordinate of the starting position.
     * @param initialZ The Z-coordinate of the starting position.
     * @param steps    The number of steps to walk for.
     * @return A brush containing the walked positions.
     */
    public static Brush randomWalk(final Random rng, final int initialX, final int initialY, final int initialZ,
                                   final int steps) {
        final ScanBlockPos pos = new ScanBlockPos(initialX, initialY, initialZ);
        final List<BlockPos> brush = new ArrayList<>();
        brush.add(pos.toImmutable());
        EnumFacing dir = null;
        for (int i = steps; i > 0; i--) {
            dir = dir == null ? RNGHelper.sampleUniformDir(rng) : RNGHelper.sampleUniformDirExcluding(rng, dir);
            pos.setAddOffset(RNGHelper.sampleUniformDir(rng), 1);
            brush.add(pos.toImmutable());
        }
        return brush::iterator;
    }

    /**
     * Creates a brush by performing a random walk on the block grid from a starting position.
     *
     * @param rng        The RNG to use for direction selection.
     * @param initialPos The starting position.
     * @param steps      The number of steps to walk for.
     * @return A brush containing the walked positions.
     */
    public static Brush randomWalk(final Random rng, final BlockPos initialPos, final int steps) {
        return randomWalk(rng, initialPos.getX(), initialPos.getY(), initialPos.getZ(), steps);
    }

    /**
     * Creates a brush by performing a random walk on the block grid from a starting position. Walkable positions are
     * demarcated by a block filter; if the walk reaches an unwalkable position, it ends immediately.
     *
     * @param rng      The RNG to use for direction selection.
     * @param initialX The X-coordinate of the starting position.
     * @param initialY The Y-coordinate of the starting position.
     * @param initialZ The Z-coordinate of the starting position.
     * @param steps    The number of steps to walk for.
     * @param world    The world in which the walk is occurring.
     * @param filter   The walkable block filter.
     * @return A brush containing the walked positions.
     */
    public static Brush randomWalk(final Random rng, final int initialX, final int initialY, final int initialZ,
                                   final int steps, final World world, final BlockFilter filter) {
        final ScanBlockPos pos = new ScanBlockPos(initialX, initialY, initialZ);
        if (!filter.matchPos(rng, world, pos)) return BrushHelper.empty();
        final List<BlockPos> brush = new ArrayList<>();
        brush.add(pos.toImmutable());
        EnumFacing dir = null;
        for (int i = steps; i > 0; i--) {
            dir = dir == null ? RNGHelper.sampleUniformDir(rng) : RNGHelper.sampleUniformDirExcluding(rng, dir);
            pos.setAddOffset(dir, 1);
            if (!filter.matchPos(rng, world, pos)) break;
            brush.add(pos.toImmutable());
        }
        return brush::iterator;
    }

    /**
     * Creates a brush by performing a random walk on the block grid from a starting position. Walkable positions are
     * demarcated by a block filter; if the walk reaches an unwalkable position, it ends immediately.
     *
     * @param rng        The RNG to use for direction selection.
     * @param initialPos The starting position.
     * @param steps      The number of steps to walk for.
     * @param world      The world in which the walk is occurring.
     * @param filter     The walkable block filter.
     * @return A brush containing the walked positions.
     */
    public static Brush randomWalk(final Random rng, final BlockPos initialPos,
                                   final int steps, final World world, final BlockFilter filter) {
        return randomWalk(rng, initialPos.getX(), initialPos.getY(), initialPos.getZ(), steps, world, filter);
    }

    /**
     * Checks whether a position is an open space above a surface as specified by a block filter.
     *
     * @param rng            The world generation RNG.
     * @param world          The world to examine.
     * @param pos            The position to examine.
     * @param surfaceMatcher A block matcher for the surface.
     * @return Whether the position is on a surface or not.
     */
    public static boolean isOnSurface(final Random rng, final World world, final BlockPos pos,
                                      final BlockFilter surfaceMatcher) {
        return BlockFilterHelper.isReplaceable(world, pos) && surfaceMatcher.matchPos(rng, world, pos.down());
    }

    /**
     * Checks whether a position is an open space above a solid surface.
     *
     * @param rng   The world generation RNG.
     * @param world The world to examine.
     * @param pos   The position to examine.
     * @return Whether the position is on a solid surface or not.
     */
    public static boolean isOnSurface(final Random rng, final World world, final BlockPos pos) {
        return BlockFilterHelper.isReplaceable(world, pos) && !BlockFilterHelper.isReplaceable(world, pos.down());
    }

    /**
     * Generates a "vein" of blocks using a sub-brush. In particular, it picks a random vector in an arbitrary
     * orientation in 3D space and applies the sub-brush at intervals along a line segment extending outwards along that
     * vector in both the positive and negative directions from the base position. The sub-brush is parametric in a
     * fractional "size" value that varies sinusoidally from zero at the outer edges of the vein to one at the center
     * (i.e. at the base position). This is somewhat similar to how vanilla ore generation works.
     *
     * @param rng          The world generation RNG.
     * @param world        The world in which the vein is being generated.
     * @param centerX      The X-coordinate of the center of the vein.
     * @param centerY      The Y-coordinate of the center of the vein.
     * @param centerZ      The Z-coordinate of the center of the vein.
     * @param veinLength   The length of the vein.
     * @param stepInterval The interval, in blocks, at which the sub-brush should be applied.
     * @param veinYaw      The horizontal heading of the vein in radians.
     * @param veinPitch    The vertical heading of the vein in radians.
     * @param stepBrush    The "size"-parametric sub-brush to apply at intervals.
     * @return The new vein brush.
     */
    public static Brush createVein(final Random rng, final World world,
                                   final int centerX, final int centerY, final int centerZ,
                                   final double veinLength, final double stepInterval,
                                   final float veinYaw, final float veinPitch,
                                   final FloatFunction<BrushGeometry> stepBrush) {
        final double veinLengthY = veinLength * MathHelper.sin(veinPitch);
        final double veinLengthXZ = veinLength * MathHelper.cos(veinPitch);
        final double veinLengthX = veinLengthXZ * MathHelper.sin(veinYaw);
        final double veinLengthZ = veinLengthXZ * MathHelper.cos(veinYaw);

        double veinX = centerX + 0.5D * (1D - veinLengthX); // reassociation of (x+1/2) - veinLengthX/2
        double veinY = centerY + 0.5D * (1D - veinLengthY);
        double veinZ = centerZ + 0.5D * (1D - veinLengthZ);
        final ScanBlockPos stepPos = new ScanBlockPos(veinX, veinY, veinZ);
        float phi = 0F;

        int steps = MathHelper.ceil((float) (veinLength * 2D / stepInterval));
        final double dx = veinLengthX / steps, dy = veinLengthY / steps, dz = veinLengthZ / steps;
        final float dp = BrushHelper.PI_F / steps;

        final List<Brush> brushes = new ArrayList<>();
        while (true) {
            brushes.add(stepBrush.apply(MathHelper.sin(phi)).createBrush(rng, world, stepPos));
            if (--steps <= 0) break;
            veinX += dx;
            veinY += dy;
            veinZ += dz;
            stepPos.setPos(veinX, veinY, veinZ);
            phi += dp;
        }
        return BrushHelper.concat(brushes);
    }

    /**
     * Generates a "vein" of blocks using a sub-brush. In particular, it picks a random vector in an arbitrary
     * orientation in 3D space and applies the sub-brush at intervals along a line segment extending outwards along that
     * vector in both the positive and negative directions from the base position. The sub-brush is parametric in a
     * fractional "size" value that varies sinusoidally from zero at the outer edges of the vein to one at the center
     * (i.e. at the base position). This is somewhat similar to how vanilla ore generation works.
     *
     * @param rng          The world generation RNG.
     * @param world        The world in which the vein is being generated.
     * @param centerPos    The center of the vein.
     * @param veinLength   The length of the vein.
     * @param stepInterval The interval, in blocks, at which the sub-brush should be applied.
     * @param veinYaw      The horizontal heading of the vein in radians.
     * @param veinPitch    The vertical heading of the vein in radians.
     * @param stepBrush    The "size"-parametric sub-brush to apply at intervals.
     * @return The new vein brush.
     */
    public static Brush createVein(final Random rng, final World world, final BlockPos centerPos,
                                   final double veinLength, final double stepInterval,
                                   final float veinYaw, final float veinPitch,
                                   final FloatFunction<BrushGeometry> stepBrush) {
        return createVein(rng, world, centerPos.getX(), centerPos.getY(), centerPos.getZ(),
                veinLength, stepInterval, veinYaw, veinPitch, stepBrush);
    }
}
