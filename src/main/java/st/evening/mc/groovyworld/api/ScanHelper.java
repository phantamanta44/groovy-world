package st.evening.mc.groovyworld.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import st.evening.mc.groovyworld.util.HBlockPos;
import st.evening.mc.groovyworld.util.ScanBlockPos;

import java.util.Random;

/**
 * Utilities for scanning for blocks in a world.
 */
public enum ScanHelper {
    ;

    /**
     * Scans downwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The Y-coordinate of the matching block, or the starting Y-coordinate if no match was found.
     */
    public static int scanYDown(final Random rng, final World world, final int x, final int y, final int z,
                                final int maxDistExclusive, final BlockFilter matcher) {
        final ScanBlockPos pos = new ScanBlockPos(x, 0, z);
        for (int i = 1; i < maxDistExclusive; i++) {
            pos.setY(y - i);
            if (matcher.matchPos(rng, world, pos)) {
                return pos.getY();
            }
        }
        return y;
    }

    /**
     * Scans downwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The Y-coordinate of the matching block, or the starting Y-coordinate if no match was found.
     */
    public static int scanYDown(final Random rng, final World world, final BlockPos pos,
                                final int maxDistExclusive, final BlockFilter matcher) {
        return scanYDown(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans downwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanDown(final Random rng, final World world,
                                              final int x, final int y, final int z,
                                              final int maxDistExclusive, final BlockFilter matcher) {
        final int scanY = scanYDown(rng, world, x, y, z, maxDistExclusive, matcher);
        return scanY != y ? new BlockPos(x, scanY, z) : null;
    }

    /**
     * Scans downwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanDown(final Random rng, final World world, final BlockPos pos,
                                              final int maxDistExclusive, final BlockFilter matcher) {
        return scanDown(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans upwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The Y-coordinate of the matching block, or the starting Y-coordinate if no match was found.
     */
    public static int scanYUp(final Random rng, final World world, final int x, final int y, final int z,
                              final int maxDistExclusive, final BlockFilter matcher) {
        final ScanBlockPos pos = new ScanBlockPos(x, 0, z);
        for (int i = 1; i < maxDistExclusive; i++) {
            pos.setY(y + i);
            if (matcher.matchPos(rng, world, pos)) {
                return pos.getY();
            }
        }
        return y;
    }

    /**
     * Scans downwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The Y-coordinate of the matching block, or the starting Y-coordinate if no match was found.
     */
    public static int scanYUp(final Random rng, final World world, final BlockPos pos,
                              final int maxDistExclusive, final BlockFilter matcher) {
        return scanYUp(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans upwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanUp(final Random rng, final World world,
                                            final int x, final int y, final int z,
                                            final int maxDistExclusive, final BlockFilter matcher) {
        final int scanY = scanYUp(rng, world, x, y, z, maxDistExclusive, matcher);
        return scanY != y ? new BlockPos(x, scanY, z) : null;
    }

    /**
     * Scans upwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanUp(final Random rng, final World world, final BlockPos pos,
                                            final int maxDistExclusive, final BlockFilter matcher) {
        return scanUp(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans northwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The Z-coordinate of the matching block, or the starting Z-coordinate if no match was found.
     */
    public static int scanZNorth(final Random rng, final World world, final int x, final int y, final int z,
                                 final int maxDistExclusive, final BlockFilter matcher) {
        final ScanBlockPos pos = new ScanBlockPos(x, y, 0);
        for (int i = 1; i < maxDistExclusive; i++) {
            pos.setZ(z - i);
            if (matcher.matchPos(rng, world, pos)) {
                return pos.getZ();
            }
        }
        return z;
    }

    /**
     * Scans northwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The Z-coordinate of the matching block, or the starting Z-coordinate if no match was found.
     */
    public static int scanZNorth(final Random rng, final World world, final BlockPos pos,
                                 final int maxDistExclusive, final BlockFilter matcher) {
        return scanZNorth(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans northwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanNorth(final Random rng, final World world,
                                               final int x, final int y, final int z,
                                               final int maxDistExclusive, final BlockFilter matcher) {
        final int scanZ = scanZNorth(rng, world, x, y, z, maxDistExclusive, matcher);
        return scanZ != z ? new BlockPos(x, y, scanZ) : null;
    }

    /**
     * Scans northwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanNorth(final Random rng, final World world, final BlockPos pos,
                                               final int maxDistExclusive, final BlockFilter matcher) {
        return scanNorth(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans southwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The Z-coordinate of the matching block, or the starting Z-coordinate if no match was found.
     */
    public static int scanZSouth(final Random rng, final World world, final int x, final int y, final int z,
                                 final int maxDistExclusive, final BlockFilter matcher) {
        final ScanBlockPos pos = new ScanBlockPos(x, y, 0);
        for (int i = 1; i < maxDistExclusive; i++) {
            pos.setZ(z + i);
            if (matcher.matchPos(rng, world, pos)) {
                return pos.getZ();
            }
        }
        return z;
    }

    /**
     * Scans southwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The Z-coordinate of the matching block, or the starting Z-coordinate if no match was found.
     */
    public static int scanZSouth(final Random rng, final World world, final BlockPos pos,
                                 final int maxDistExclusive, final BlockFilter matcher) {
        return scanZSouth(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans southwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanSouth(final Random rng, final World world,
                                               final int x, final int y, final int z,
                                               final int maxDistExclusive, final BlockFilter matcher) {
        final int scanZ = scanZSouth(rng, world, x, y, z, maxDistExclusive, matcher);
        return scanZ != z ? new BlockPos(x, y, scanZ) : null;
    }

    /**
     * Scans southwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanSouth(final Random rng, final World world, final BlockPos pos,
                                               final int maxDistExclusive, final BlockFilter matcher) {
        return scanSouth(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans westwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The X-coordinate of the matching block, or the starting X-coordinate if no match was found.
     */
    public static int scanXWest(final Random rng, final World world, final int x, final int y, final int z,
                                final int maxDistExclusive, final BlockFilter matcher) {
        final ScanBlockPos pos = new ScanBlockPos(0, y, z);
        for (int i = 1; i < maxDistExclusive; i++) {
            pos.setX(x - i);
            if (matcher.matchPos(rng, world, pos)) {
                return pos.getX();
            }
        }
        return x;
    }

    /**
     * Scans westwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The X-coordinate of the matching block, or the starting X-coordinate if no match was found.
     */
    public static int scanXWest(final Random rng, final World world, final BlockPos pos,
                                final int maxDistExclusive, final BlockFilter matcher) {
        return scanXWest(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans westwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanWest(final Random rng, final World world,
                                              final int x, final int y, final int z,
                                              final int maxDistExclusive, final BlockFilter matcher) {
        final int scanX = scanXWest(rng, world, x, y, z, maxDistExclusive, matcher);
        return scanX != x ? new BlockPos(scanX, y, z) : null;
    }

    /**
     * Scans westwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanWest(final Random rng, final World world, final BlockPos pos,
                                              final int maxDistExclusive, final BlockFilter matcher) {
        return scanWest(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans eastwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The X-coordinate of the matching block, or the starting X-coordinate if no match was found.
     */
    public static int scanXEast(final Random rng, final World world, final int x, final int y, final int z,
                                final int maxDistExclusive, final BlockFilter matcher) {
        final ScanBlockPos pos = new ScanBlockPos(0, y, z);
        for (int i = 1; i < maxDistExclusive; i++) {
            pos.setX(x + i);
            if (matcher.matchPos(rng, world, pos)) {
                return pos.getX();
            }
        }
        return x;
    }

    /**
     * Scans eastwards from a starting position for the coordinates of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The X-coordinate of the matching block, or the starting X-coordinate if no match was found.
     */
    public static int scanXEast(final Random rng, final World world, final BlockPos pos,
                                final int maxDistExclusive, final BlockFilter matcher) {
        return scanXEast(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans eastwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanEast(final Random rng, final World world,
                                              final int x, final int y, final int z,
                                              final int maxDistExclusive, final BlockFilter matcher) {
        final int scanX = scanXEast(rng, world, x, y, z, maxDistExclusive, matcher);
        return scanX != x ? new BlockPos(scanX, y, z) : null;
    }

    /**
     * Scans eastwards from a starting position for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scanEast(final Random rng, final World world, final BlockPos pos,
                                              final int maxDistExclusive, final BlockFilter matcher) {
        return scanEast(rng, world, pos.getX(), pos.getY(), pos.getZ(), maxDistExclusive, matcher);
    }

    /**
     * Scans a line in an axial direction for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param x                The X-coordinate of the starting position.
     * @param y                The Y-coordinate of the starting position.
     * @param z                The Z-coordinate of the starting position.
     * @param dir              The direction to scan in.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scan(final Random rng, final World world, final int x, final int y, final int z,
                                          final EnumFacing dir, final int maxDistExclusive, final BlockFilter matcher) {
        switch (dir) {
            case DOWN:
                return scanDown(rng, world, x, y, z, maxDistExclusive, matcher);
            case UP:
                return scanUp(rng, world, x, y, z, maxDistExclusive, matcher);
            case NORTH:
                return scanNorth(rng, world, x, y, z, maxDistExclusive, matcher);
            case SOUTH:
                return scanSouth(rng, world, x, y, z, maxDistExclusive, matcher);
            case WEST:
                return scanWest(rng, world, x, y, z, maxDistExclusive, matcher);
            case EAST:
                return scanEast(rng, world, x, y, z, maxDistExclusive, matcher);
            default:
                throw new IllegalStateException(dir.toString());
        }
    }

    /**
     * Scans a line in an axial direction for the position of a matching block.
     *
     * @param rng              The world generation RNG.
     * @param world            The world to scan in.
     * @param pos              The starting position.
     * @param dir              The direction to scan in.
     * @param maxDistExclusive The maximum distance to scan, exclusive.
     * @param matcher          The matcher for the scan target.
     * @return The position of the matching block, or {@code null} if no match was found.
     */
    public static @Nullable BlockPos scan(final Random rng, final World world, final BlockPos pos,
                                          final EnumFacing dir, final int maxDistExclusive, final BlockFilter matcher) {
        return scan(rng, world, pos.getX(), pos.getY(), pos.getZ(), dir, maxDistExclusive, matcher);
    }

    /**
     * Finds a Y-coordinate above all non-air blocks at a given horizontal position.
     *
     * @param world The world to examine.
     * @param x     The X-coordinate of the position.
     * @param z     The Z-coordinate of the position.
     * @return The upper bound Y-coordinate.
     */
    public static int findUpperBoundY(final World world, final int x, final int z) {
        return world.getChunk(x >> 4, z >> 4).getTopFilledSegment() + 16;
    }

    /**
     * Finds a Y-coordinate above all non-air blocks at a given horizontal position.
     *
     * @param world The world to examine.
     * @param pos   The horizontal position.
     * @return The upper bound Y-coordinate.
     */
    public static int findUpperBoundY(final World world, final HBlockPos pos) {
        return findUpperBoundY(world, pos.x, pos.z);
    }

    /**
     * Finds a position above all non-air blocks at a given horizontal position.
     *
     * @param world The world to examine.
     * @param x     The X-coordinate of the position.
     * @param z     The Z-coordinate of the position.
     * @return The upper bound position.
     */
    public static BlockPos findUpperBoundPos(final World world, final int x, final int z) {
        return new BlockPos(x, findUpperBoundY(world, x, z), z);
    }

    /**
     * Finds a position above all non-air blocks at a given horizontal position.
     *
     * @param world The world to examine.
     * @param pos   The horizontal position.
     * @return The upper bound position.
     */
    public static BlockPos findUpperBoundPos(final World world, final HBlockPos pos) {
        return pos.toBlockPos(findUpperBoundY(world, pos.x, pos.z));
    }

    /**
     * Finds the Y-coordinate of the surface of the world, as defined by a block filter, at a given horizontal position.
     *
     * @param rng     The world generation RNG.
     * @param world   The world to scan in.
     * @param x       The X-coordinate of the position.
     * @param z       The Z-coordinate of the position.
     * @param matcher The matcher for the surface.
     * @return The Y-coordinate of the surface, or {@link Integer#MIN_VALUE} if it cannot be found.
     */
    public static int findSurfaceY(final Random rng, final World world, final int x, final int z,
                                   final BlockFilter matcher) {
        final int upperY = findUpperBoundY(world, x, z);
        final int surfaceY = scanYDown(rng, world, x, upperY, z, upperY, matcher);
        return surfaceY != upperY ? surfaceY : Integer.MIN_VALUE;
    }

    /**
     * Finds the Y-coordinate of the surface of the world, as defined by a block filter, at a given horizontal position.
     *
     * @param rng     The world generation RNG.
     * @param world   The world to scan in.
     * @param pos     The horizontal position.
     * @param matcher The matcher for the surface.
     * @return The Y-coordinate of the surface, or {@link Integer#MIN_VALUE} if it cannot be found.
     */
    public static int findSurfaceY(final Random rng, final World world, final HBlockPos pos,
                                   final BlockFilter matcher) {
        return findSurfaceY(rng, world, pos.x, pos.z, matcher);
    }

    /**
     * Finds the position of the surface of the world, as defined by a block filter, at a given horizontal position.
     *
     * @param rng     The world generation RNG.
     * @param world   The world to scan in.
     * @param x       The X-coordinate of the position.
     * @param z       The Z-coordinate of the position.
     * @param matcher The matcher for the surface.
     * @return The position of the surface, or {@code null} if it cannot be found.
     */
    public static @Nullable BlockPos findSurfacePos(final Random rng, final World world, final int x, final int z,
                                                    final BlockFilter matcher) {
        final int surfaceY = findSurfaceY(rng, world, x, z, matcher);
        return surfaceY != Integer.MIN_VALUE ? new BlockPos(x, surfaceY, z) : null;
    }

    /**
     * Finds the position of the surface of the world, as defined by a block filter, at a given horizontal position.
     *
     * @param rng     The world generation RNG.
     * @param world   The world to scan in.
     * @param pos     The horizontal position.
     * @param matcher The matcher for the surface.
     * @return The position of the surface, or {@code null} if it cannot be found.
     */
    public static @Nullable BlockPos findSurfacePos(final Random rng, final World world, final HBlockPos pos,
                                                    final BlockFilter matcher) {
        final int surfaceY = findSurfaceY(rng, world, pos, matcher);
        return surfaceY != Integer.MIN_VALUE ? pos.toBlockPos(surfaceY) : null;
    }
}
