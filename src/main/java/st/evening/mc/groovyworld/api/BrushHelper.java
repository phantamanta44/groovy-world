package st.evening.mc.groovyworld.api;

import com.google.common.collect.AbstractIterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jspecify.annotations.Nullable;
import st.evening.mc.groovyworld.util.function.FloatDoubleFunction;
import st.evening.mc.groovyworld.util.Plane;
import st.evening.mc.groovyworld.util.ScanBlockPos;
import st.evening.mc.groovyworld.util.collection.SingletonIterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Utilities for constructing brushes.
 */
public enum BrushHelper {
    ;

    /**
     * 32-bit pi. Tasty!
     */
    public static final float PI_F = (float) Math.PI;

    /**
     * A brush that contains no positions.
     *
     * @return The empty brush.
     */
    public static Brush empty() {
        return Collections::emptyIterator;
    }

    /**
     * A brush that contains a single position.
     *
     * @param pos The brush position.
     * @return The new point brush.
     */
    public static Brush point(final BlockPos pos) {
        return () -> new SingletonIterator<>(pos);
    }

    /**
     * Combines a collection of brushes into one big brush.
     *
     * @param brushes The constituent brushes.
     * @return The combined brush.
     */
    public static Brush concat(final Collection<Brush> brushes) {
        return () -> new AbstractIterator<BlockPos>() {
            private final Iterator<Brush> backing = brushes.iterator();
            private @Nullable Iterator<BlockPos> child = null;

            @Override
            protected BlockPos computeNext() {
                while (true) {
                    if (child != null && child.hasNext()) {
                        return child.next();
                    }
                    if (!backing.hasNext()) {
                        child = null;
                        return endOfData();
                    }
                    child = backing.next().iterator();
                }
            }
        };
    }

    /**
     * Combines a list of brushes into one big brush.
     *
     * @param brushes The constituent brushes.
     * @return The combined brush.
     */
    public static Brush concat(final Brush... brushes) {
        return concat(Arrays.asList(brushes));
    }

    /**
     * Creates a brush containing a line of blocks in an axial direction from a given starting point.
     *
     * @param x      The starting X-coordinate.
     * @param y      The starting Y-coordinate.
     * @param z      The starting Z-coordinate.
     * @param dir    The direction of the line.
     * @param length The length of the line.
     * @return The new column brush.
     */
    public static Brush createColumn(final int x, final int y, final int z, final EnumFacing dir, final int length) {
        if (length <= 0) return empty();
        final ScanBlockPos pos = new ScanBlockPos(x, y, z);
        return () -> new Iterator<BlockPos>() {
            private int remaining = length;

            @Override
            public boolean hasNext() {
                return remaining > 0;
            }

            @Override
            public BlockPos next() {
                final BlockPos next = pos.toImmutable();
                pos.setAddOffset(dir, 1);
                remaining--;
                return next;
            }
        };
    }

    /**
     * Creates a brush containing a line of blocks in an axial direction from a given starting point.
     *
     * @param pos    The starting point.
     * @param dir    The direction of the line.
     * @param length The length of the line.
     * @return The new column brush.
     */
    public static Brush createColumn(final BlockPos pos, final EnumFacing dir, final int length) {
        return createColumn(pos.getX(), pos.getY(), pos.getZ(), dir, length);
    }

    /**
     * Creates a brush containing a cuboid of blocks.
     *
     * @param minX The cuboid's lower X-coordinate.
     * @param minY The cuboid's lower Y-coordinate.
     * @param minZ The cuboid's lower Z-coordinate.
     * @param maxX The cuboid's upper X-coordinate.
     * @param maxY The cuboid's upper Y-coordinate.
     * @param maxZ The cuboid's upper Z-coordinate.
     * @return The new cuboid brush.
     */
    public static Brush createCuboid(final int minX, final int minY, final int minZ,
                                     final int maxX, final int maxY, final int maxZ) {
        return () -> new Iterator<BlockPos>() {
            private int x = minX, y = minY, z = minZ;

            @Override
            public boolean hasNext() {
                return y <= maxY;
            }

            @Override
            public BlockPos next() {
                final BlockPos pos = new BlockPos(x, y, z);
                if (x < maxX) {
                    x++;
                } else {
                    x = minX;
                    if (z < maxZ) {
                        z++;
                    } else {
                        z = minZ;
                        y++;
                    }
                }
                return pos;
            }
        };
    }

    /**
     * Creates a brush containing the cuboid of blocks between two vertices.
     *
     * @param pos1 One vertex of the cuboid.
     * @param pos2 The diagonally opposite vertex of the cuboid.
     * @return The new cuboid brush.
     */
    public static Brush createCuboid(final BlockPos pos1, final BlockPos pos2) {
        final int minX, maxX, minY, maxY, minZ, maxZ;
        if (pos1.getX() < pos2.getX()) {
            minX = pos1.getX();
            maxX = pos2.getX();
        } else {
            minX = pos2.getX();
            maxX = pos1.getX();
        }
        if (pos1.getY() < pos2.getY()) {
            minY = pos1.getY();
            maxY = pos2.getY();
        } else {
            minY = pos2.getY();
            maxY = pos1.getY();
        }
        if (pos1.getZ() < pos2.getZ()) {
            minZ = pos1.getZ();
            maxZ = pos2.getZ();
        } else {
            minZ = pos2.getZ();
            maxZ = pos1.getZ();
        }
        return createCuboid(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Creates a brush containing the outer surface of a cuboid of blocks.
     *
     * @param minX The cuboid's lower X-coordinate.
     * @param minY The cuboid's lower Y-coordinate.
     * @param minZ The cuboid's lower Z-coordinate.
     * @param maxX The cuboid's upper X-coordinate.
     * @param maxY The cuboid's upper Y-coordinate.
     * @param maxZ The cuboid's upper Z-coordinate.
     * @return The new cuboid surface brush.
     */
    public static Brush createCuboidSurface(final int minX, final int minY, final int minZ,
                                            final int maxX, final int maxY, final int maxZ) {
        return () -> new Iterator<BlockPos>() {
            private int x = minX, y = minY, z = minZ;

            @Override
            public boolean hasNext() {
                return y <= maxY;
            }

            @Override
            public BlockPos next() {
                final BlockPos pos = new BlockPos(x, y, z);
                if (x < maxX) {
                    if (y == minY || y == maxY || z == minZ || z == maxZ) {
                        x++;
                    } else {
                        x = maxX;
                    }
                } else {
                    x = minX;
                    if (z < maxZ) {
                        z++;
                    } else {
                        z = minZ;
                        y++;
                    }
                }
                return pos;
            }
        };
    }

    /**
     * Creates a brush containing the outer surface of the cuboid of blocks between two vertices.
     *
     * @param pos1 One vertex of the cuboid.
     * @param pos2 The diagonally opposite vertex of the cuboid.
     * @return The new cuboid surface brush.
     */
    public static Brush createCuboidSurface(final BlockPos pos1, final BlockPos pos2) {
        final int minX, maxX, minY, maxY, minZ, maxZ;
        if (pos1.getX() < pos2.getX()) {
            minX = pos1.getX();
            maxX = pos2.getX();
        } else {
            minX = pos2.getX();
            maxX = pos1.getX();
        }
        if (pos1.getY() < pos2.getY()) {
            minY = pos1.getY();
            maxY = pos2.getY();
        } else {
            minY = pos2.getY();
            maxY = pos1.getY();
        }
        if (pos1.getZ() < pos2.getZ()) {
            minZ = pos1.getZ();
            maxZ = pos2.getZ();
        } else {
            minZ = pos2.getZ();
            maxZ = pos1.getZ();
        }
        return createCuboidSurface(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Creates a brush containing a disk on the YZ plane.
     *
     * @param centerX The X-coordinate of the center of the disk.
     * @param centerY The Y-coordinate of the center of the disk.
     * @param centerZ The Z-coordinate of the center of the disk.
     * @param radius  The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDiskYZ(final double centerX, final double centerY, final double centerZ,
                                     final double radius) {
        final double r2 = radius * radius;
        return createCuboid(
                MathHelper.floor(centerX), MathHelper.floor(centerY - radius), MathHelper.floor(centerZ - radius),
                MathHelper.floor(centerX), MathHelper.floor(centerY + radius), MathHelper.floor(centerZ + radius))
                .filter(pos -> {
                    final double dy = pos.getY() + 0.5D - centerY;
                    final double dz = pos.getZ() + 0.5D - centerZ;
                    return dy * dy + dz * dz <= r2;
                });
    }

    /**
     * Creates a brush containing a disk on the YZ plane.
     *
     * @param center The center of the disk.
     * @param radius The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDiskYZ(final Vec3d center, final double radius) {
        return createDiskYZ(center.x, center.y, center.z, radius);
    }

    /**
     * Creates a brush containing a disk on the YZ plane.
     *
     * @param center The center of the disk.
     * @param radius The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDiskYZ(final BlockPos center, final double radius) {
        return createDiskYZ(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, radius);
    }

    /**
     * Creates a brush containing a disk on the XZ plane.
     *
     * @param centerX The X-coordinate of the center of the disk.
     * @param centerY The Y-coordinate of the center of the disk.
     * @param centerZ The Z-coordinate of the center of the disk.
     * @param radius  The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDiskXZ(final double centerX, final double centerY, final double centerZ,
                                     final double radius) {
        final double r2 = radius * radius;
        return createCuboid(
                MathHelper.floor(centerX - radius), MathHelper.floor(centerY), MathHelper.floor(centerZ - radius),
                MathHelper.floor(centerX + radius), MathHelper.floor(centerY), MathHelper.floor(centerZ + radius))
                .filter(pos -> {
                    final double dx = pos.getX() + 0.5D - centerX;
                    final double dz = pos.getZ() + 0.5D - centerZ;
                    return dx * dx + dz * dz <= r2;
                });
    }

    /**
     * Creates a brush containing a disk on the XZ plane.
     *
     * @param center The center of the disk.
     * @param radius The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDiskXZ(final Vec3d center, final double radius) {
        return createDiskXZ(center.x, center.y, center.z, radius);
    }

    /**
     * Creates a brush containing a disk on the XZ plane.
     *
     * @param center The center of the disk.
     * @param radius The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDiskXZ(final BlockPos center, final double radius) {
        return createDiskXZ(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, radius);
    }

    /**
     * Creates a brush containing a disk on the XY plane.
     *
     * @param centerX The X-coordinate of the center of the disk.
     * @param centerY The Y-coordinate of the center of the disk.
     * @param centerZ The Z-coordinate of the center of the disk.
     * @param radius  The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDiskXY(final double centerX, final double centerY, final double centerZ,
                                     final double radius) {
        final double r2 = radius * radius;
        return createCuboid(
                MathHelper.floor(centerX - radius), MathHelper.floor(centerY - radius), MathHelper.floor(centerZ),
                MathHelper.floor(centerX + radius), MathHelper.floor(centerY + radius), MathHelper.floor(centerZ))
                .filter(pos -> {
                    final double dx = pos.getX() + 0.5D - centerX;
                    final double dy = pos.getY() + 0.5D - centerY;
                    return dx * dx + dy * dy <= r2;
                });
    }

    /**
     * Creates a brush containing a disk on the XY plane.
     *
     * @param center The center of the disk.
     * @param radius The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDiskXY(final Vec3d center, final double radius) {
        return createDiskXY(center.x, center.y, center.z, radius);
    }

    /**
     * Creates a brush containing a disk on the XY plane.
     *
     * @param center The center of the disk.
     * @param radius The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDiskXY(final BlockPos center, final double radius) {
        return createDiskXY(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, radius);
    }

    /**
     * Creates a brush containing a disk on one of the axial planes.
     *
     * @param centerX The X-coordinate of the center of the disk.
     * @param centerY The Y-coordinate of the center of the disk.
     * @param centerZ The Z-coordinate of the center of the disk.
     * @param plane   The axial plane the disk lies in.
     * @param radius  The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDisk(final double centerX, final double centerY, final double centerZ,
                                   final Plane plane, final double radius) {
        switch (plane) {
            case YZ:
                return createDiskYZ(centerX, centerY, centerZ, radius);
            case XZ:
                return createDiskXZ(centerX, centerY, centerZ, radius);
            case XY:
                return createDiskXY(centerX, centerY, centerZ, radius);
        }
        throw new IllegalStateException(plane.toString());
    }

    /**
     * Creates a brush containing a disk on one of the axial planes.
     *
     * @param center The center of the disk.
     * @param plane  The axial plane the disk lies in.
     * @param radius The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDisk(final Vec3d center, final Plane plane, final double radius) {
        return createDisk(center.x, center.y, center.z, plane, radius);
    }

    /**
     * Creates a brush containing a disk on one of the axial planes.
     *
     * @param center The center of the disk.
     * @param plane  The axial plane in which the disk lies.
     * @param radius The radius of the disk.
     * @return The new disk brush.
     */
    public static Brush createDisk(final BlockPos center, final Plane plane, final double radius) {
        return createDisk(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, plane, radius);
    }

    /**
     * Creates a brush containing a sphere.
     *
     * @param centerX The X-coordinate of the center of the sphere.
     * @param centerY The Y-coordinate of the center of the sphere.
     * @param centerZ The Z-coordinate of the center of the sphere.
     * @param radius  The radius of the sphere.
     * @return The new sphere brush.
     */
    public static Brush createSphere(final double centerX, final double centerY, final double centerZ,
                                     final double radius) {
        final double r2 = radius * radius;
        return createCuboid(
                MathHelper.floor(centerX - radius),
                MathHelper.floor(centerY - radius),
                MathHelper.floor(centerZ - radius),
                MathHelper.floor(centerX + radius),
                MathHelper.floor(centerY + radius),
                MathHelper.floor(centerZ + radius))
                .filter(p -> p.distanceSqToCenter(centerX, centerY, centerZ) <= r2);
    }

    /**
     * Creates a brush containing a sphere.
     *
     * @param center The center of the sphere.
     * @param radius The radius of the sphere.
     * @return The new sphere brush.
     */
    public static Brush createSphere(final Vec3d center, final double radius) {
        return createSphere(center.x, center.y, center.z, radius);
    }

    /**
     * Creates a brush containing a sphere.
     *
     * @param center The center of the sphere.
     * @param radius The radius of the sphere.
     * @return The new sphere brush.
     */
    public static Brush createSphere(final BlockPos center, final double radius) {
        return createSphere(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, radius);
    }

    /**
     * Creates a brush containing an axis-aligned ellipsoid.
     *
     * @param centerX The X-coordinate of the center of the ellipsoid.
     * @param centerY The Y-coordinate of the center of the ellipsoid.
     * @param centerZ The Z-coordinate of the center of the ellipsoid.
     * @param radiusX The radius of the ellipsoid on the X-axis.
     * @param radiusY The radius of the ellipsoid on the Y-axis.
     * @param radiusZ The radius of the ellipsoid on the Z-axis.
     * @return The new ellipsoid brush.
     */
    public static Brush createEllipsoid(final double centerX, final double centerY, final double centerZ,
                                        final double radiusX, final double radiusY, final double radiusZ) {
        final double irx2 = 1D / (radiusX * radiusX), iry2 = 1D / (radiusY * radiusY), irz2 = 1D / (radiusZ * radiusZ);
        return createCuboid(
                MathHelper.floor(centerX - radiusX),
                MathHelper.floor(centerY - radiusY),
                MathHelper.floor(centerZ - radiusZ),
                MathHelper.floor(centerX + radiusX),
                MathHelper.floor(centerY + radiusY),
                MathHelper.floor(centerZ + radiusZ))
                .filter(p -> {
                    final double dx = p.getX() + 0.5D - centerX;
                    final double dy = p.getY() + 0.5D - centerY;
                    final double dz = p.getZ() + 0.5D - centerZ;
                    return dx * dx * irx2 + dy * dy * iry2 + dz * dz * irz2 <= 1;
                });
    }

    /**
     * Creates a brush containing an axis-aligned ellipsoid.
     *
     * @param center  The center of the ellipsoid.
     * @param radiusX The radius of the ellipsoid on the X-axis.
     * @param radiusY The radius of the ellipsoid on the Y-axis.
     * @param radiusZ The radius of the ellipsoid on the Z-axis.
     * @return The new ellipsoid brush.
     */
    public static Brush createEllipsoid(final Vec3d center,
                                        final double radiusX, final double radiusY, final double radiusZ) {
        return createEllipsoid(center.x, center.y, center.z, radiusX, radiusY, radiusZ);
    }

    /**
     * Creates a brush containing an axis-aligned ellipsoid.
     *
     * @param center  The center of the ellipsoid.
     * @param radiusX The radius of the ellipsoid on the X-axis.
     * @param radiusY The radius of the ellipsoid on the Y-axis.
     * @param radiusZ The radius of the ellipsoid on the Z-axis.
     * @return The new ellipsoid brush.
     */
    public static Brush createEllipsoid(final BlockPos center,
                                        final double radiusX, final double radiusY, final double radiusZ) {
        return createEllipsoid(
                center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, radiusX, radiusY, radiusZ);
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending upwards from a given base
     * position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param baseX     The X-coordinate of the base position.
     * @param baseY     The Y-coordinate of the base position.
     * @param baseZ     The Z-coordinate of the base position.
     * @param length    The length that the solid extends up from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionUp(final int baseX, final int baseY, final int baseZ,
                                           final int length, final FloatDoubleFunction radiusMap) {
        return createColumn(baseX, baseY, baseZ, EnumFacing.UP, length).expand(pos ->
                createDiskXZ(pos, radiusMap.applyAsDouble((pos.getY() - baseY) / (float) length)));
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending upwards from a given base
     * position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param pos       The base position.
     * @param length    The length that the solid extends up from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionUp(final BlockPos pos,
                                           final int length, final FloatDoubleFunction radiusMap) {
        return createRevolutionUp(pos.getX(), pos.getY(), pos.getZ(), length, radiusMap);
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending downwards from a given
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param baseX     The X-coordinate of the base position.
     * @param baseY     The Y-coordinate of the base position.
     * @param baseZ     The Z-coordinate of the base position.
     * @param length    The length that the solid extends down from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionDown(final int baseX, final int baseY, final int baseZ,
                                             final int length, final FloatDoubleFunction radiusMap) {
        return createColumn(baseX, baseY, baseZ, EnumFacing.DOWN, length).expand(pos ->
                createDiskXZ(pos, radiusMap.applyAsDouble((baseY - pos.getY()) / (float) length)));
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending downwards from a given
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param pos       The base position.
     * @param length    The length that the solid extends down from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionDown(final BlockPos pos,
                                             final int length, final FloatDoubleFunction radiusMap) {
        return createRevolutionDown(pos.getX(), pos.getY(), pos.getZ(), length, radiusMap);
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending northwards from a given
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param baseX     The X-coordinate of the base position.
     * @param baseY     The Y-coordinate of the base position.
     * @param baseZ     The Z-coordinate of the base position.
     * @param length    The length that the solid extends north from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionNorth(final int baseX, final int baseY, final int baseZ,
                                              final int length, final FloatDoubleFunction radiusMap) {
        return createColumn(baseX, baseY, baseZ, EnumFacing.NORTH, length).expand(pos ->
                createDiskXZ(pos, radiusMap.applyAsDouble((baseZ - pos.getZ()) / (float) length)));
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending northwards from a given
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param pos       The base position.
     * @param length    The length that the solid extends north from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionNorth(final BlockPos pos,
                                              final int length, final FloatDoubleFunction radiusMap) {
        return createRevolutionNorth(pos.getX(), pos.getY(), pos.getZ(), length, radiusMap);
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending eastwards from a given
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param baseX     The X-coordinate of the base position.
     * @param baseY     The Y-coordinate of the base position.
     * @param baseZ     The Z-coordinate of the base position.
     * @param length    The length that the solid extends east from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionEast(final int baseX, final int baseY, final int baseZ,
                                             final int length, final FloatDoubleFunction radiusMap) {
        return createColumn(baseX, baseY, baseZ, EnumFacing.EAST, length).expand(pos ->
                createDiskXZ(pos, radiusMap.applyAsDouble((pos.getX() - baseX) / (float) length)));
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending eastwards from a given
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param pos       The base position.
     * @param length    The length that the solid extends east from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionEast(final BlockPos pos,
                                             final int length, final FloatDoubleFunction radiusMap) {
        return createRevolutionEast(pos.getX(), pos.getY(), pos.getZ(), length, radiusMap);
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending southwards from a given
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param baseX     The X-coordinate of the base position.
     * @param baseY     The Y-coordinate of the base position.
     * @param baseZ     The Z-coordinate of the base position.
     * @param length    The length that the solid extends south from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionSouth(final int baseX, final int baseY, final int baseZ,
                                              final int length, final FloatDoubleFunction radiusMap) {
        return createColumn(baseX, baseY, baseZ, EnumFacing.SOUTH, length).expand(pos ->
                createDiskXZ(pos, radiusMap.applyAsDouble((pos.getZ() - baseZ) / (float) length)));
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending southwards from a given
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param pos       The base position.
     * @param length    The length that the solid extends south from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionSouth(final BlockPos pos,
                                              final int length, final FloatDoubleFunction radiusMap) {
        return createRevolutionSouth(pos.getX(), pos.getY(), pos.getZ(), length, radiusMap);
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending westwards from a given
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param baseX     The X-coordinate of the base position.
     * @param baseY     The Y-coordinate of the base position.
     * @param baseZ     The Z-coordinate of the base position.
     * @param length    The length that the solid extends west from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionWest(final int baseX, final int baseY, final int baseZ,
                                             final int length, final FloatDoubleFunction radiusMap) {
        return createColumn(baseX, baseY, baseZ, EnumFacing.WEST, length).expand(pos ->
                createDiskXZ(pos, radiusMap.applyAsDouble((baseX - pos.getX()) / (float) length)));
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending westwards from a given
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param pos       The base position.
     * @param length    The length that the solid extends west from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolutionWest(final BlockPos pos,
                                             final int length, final FloatDoubleFunction radiusMap) {
        return createRevolutionWest(pos.getX(), pos.getY(), pos.getZ(), length, radiusMap);
    }

    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending in an axial direction from
     * a given base position. The radius of the cross-section at a point along the axis is given by a "radius map",
     * which maps a progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a
     * radius in blocks.
     *
     * @param baseX     The X-coordinate of the base position.
     * @param baseY     The Y-coordinate of the base position.
     * @param baseZ     The Z-coordinate of the base position.
     * @param dir       The direction in which the solid extends outwards.
     * @param length    The length that the solid extends out from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolution(final int baseX, final int baseY, final int baseZ, final EnumFacing dir,
                                         final int length, final FloatDoubleFunction radiusMap) {
        switch (dir) {
            case UP:
                return createRevolutionUp(baseX, baseY, baseZ, length, radiusMap);
            case DOWN:
                return createRevolutionDown(baseX, baseY, baseZ, length, radiusMap);
            case NORTH:
                return createRevolutionNorth(baseX, baseY, baseZ, length, radiusMap);
            case EAST:
                return createRevolutionEast(baseX, baseY, baseZ, length, radiusMap);
            case SOUTH:
                return createRevolutionSouth(baseX, baseY, baseZ, length, radiusMap);
            case WEST:
                return createRevolutionWest(baseX, baseY, baseZ, length, radiusMap);
        }
        throw new IllegalStateException(dir.toString());
    }


    /**
     * Creates a brush containing a solid of revolution with its axis of revolution extending in an axial direction from
     * a given base position. The radius of the cross-section at a point along the axis is given by a "radius map",
     * which maps a progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a
     * radius in blocks.
     *
     * @param pos       The base position.
     * @param dir       The direction in which the solid extends outwards.
     * @param length    The length that the solid extends out from the base position.
     * @param radiusMap The radius map.
     * @return The new revolved brush.
     */
    public static Brush createRevolution(final BlockPos pos, final EnumFacing dir,
                                         final int length, final FloatDoubleFunction radiusMap) {
        return createRevolution(pos.getX(), pos.getY(), pos.getZ(), dir, length, radiusMap);
    }

    /**
     * Creates a brush containing a cylinder extending upwards from a base position.
     *
     * @param baseX  The X-coordinate of the base position.
     * @param baseY  The Y-coordinate of the base position.
     * @param baseZ  The Z-coordinate of the base position.
     * @param length The length that the cylinder extends up from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderUp(final int baseX, final int baseY, final int baseZ,
                                         final int length, final double radius) {
        return createRevolutionUp(baseX, baseY, baseZ, length, f -> radius);
    }

    /**
     * Creates a brush containing a cylinder extending upwards from a base position.
     *
     * @param pos    The base position.
     * @param length The length that the cylinder extends up from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderUp(final BlockPos pos,
                                         final int length, final double radius) {
        return createCylinderUp(pos.getX(), pos.getY(), pos.getZ(), length, radius);
    }

    /**
     * Creates a brush containing a cylinder extending downwards from a base position.
     *
     * @param baseX  The X-coordinate of the base position.
     * @param baseY  The Y-coordinate of the base position.
     * @param baseZ  The Z-coordinate of the base position.
     * @param length The length that the cylinder extends down from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderDown(final int baseX, final int baseY, final int baseZ,
                                           final int length, final double radius) {
        return createRevolutionDown(baseX, baseY, baseZ, length, f -> radius);
    }

    /**
     * Creates a brush containing a cylinder extending downwards from a base position.
     *
     * @param pos    The base position.
     * @param length The length that the cylinder extends down from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderDown(final BlockPos pos,
                                           final int length, final double radius) {
        return createCylinderDown(pos.getX(), pos.getY(), pos.getZ(), length, radius);
    }

    /**
     * Creates a brush containing a cylinder extending northwards from a base position.
     *
     * @param baseX  The X-coordinate of the base position.
     * @param baseY  The Y-coordinate of the base position.
     * @param baseZ  The Z-coordinate of the base position.
     * @param length The length that the cylinder extends north from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderNorth(final int baseX, final int baseY, final int baseZ,
                                            final int length, final double radius) {
        return createRevolutionNorth(baseX, baseY, baseZ, length, f -> radius);
    }

    /**
     * Creates a brush containing a cylinder extending northwards from a base position.
     *
     * @param pos    The base position.
     * @param length The length that the cylinder extends north from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderNorth(final BlockPos pos,
                                            final int length, final double radius) {
        return createCylinderNorth(pos.getX(), pos.getY(), pos.getZ(), length, radius);
    }

    /**
     * Creates a brush containing a cylinder extending eastwards from a base position.
     *
     * @param baseX  The X-coordinate of the base position.
     * @param baseY  The Y-coordinate of the base position.
     * @param baseZ  The Z-coordinate of the base position.
     * @param length The length that the cylinder extends east from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderEast(final int baseX, final int baseY, final int baseZ,
                                           final int length, final double radius) {
        return createRevolutionEast(baseX, baseY, baseZ, length, f -> radius);
    }

    /**
     * Creates a brush containing a cylinder extending eastwards from a base position.
     *
     * @param pos    The base position.
     * @param length The length that the cylinder extends east from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderEast(final BlockPos pos,
                                           final int length, final double radius) {
        return createCylinderEast(pos.getX(), pos.getY(), pos.getZ(), length, radius);
    }

    /**
     * Creates a brush containing a cylinder extending southwards from a base position.
     *
     * @param baseX  The X-coordinate of the base position.
     * @param baseY  The Y-coordinate of the base position.
     * @param baseZ  The Z-coordinate of the base position.
     * @param length The length that the cylinder extends south from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderSouth(final int baseX, final int baseY, final int baseZ,
                                            final int length, final double radius) {
        return createRevolutionSouth(baseX, baseY, baseZ, length, f -> radius);
    }

    /**
     * Creates a brush containing a cylinder extending southwards from a base position.
     *
     * @param pos    The base position.
     * @param length The length that the cylinder extends south from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderSouth(final BlockPos pos,
                                            final int length, final double radius) {
        return createCylinderSouth(pos.getX(), pos.getY(), pos.getZ(), length, radius);
    }

    /**
     * Creates a brush containing a cylinder extending westwards from a base position.
     *
     * @param baseX  The X-coordinate of the base position.
     * @param baseY  The Y-coordinate of the base position.
     * @param baseZ  The Z-coordinate of the base position.
     * @param length The length that the cylinder extends west from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderWest(final int baseX, final int baseY, final int baseZ,
                                           final int length, final double radius) {
        return createRevolutionWest(baseX, baseY, baseZ, length, f -> radius);
    }

    /**
     * Creates a brush containing a cylinder extending westwards from a base position.
     *
     * @param pos    The base position.
     * @param length The length that the cylinder extends west from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinderWest(final BlockPos pos,
                                           final int length, final double radius) {
        return createCylinderWest(pos.getX(), pos.getY(), pos.getZ(), length, radius);
    }

    /**
     * Creates a brush containing a cylinder extending in an axial direction from a base position.
     *
     * @param baseX  The X-coordinate of the base position.
     * @param baseY  The Y-coordinate of the base position.
     * @param baseZ  The Z-coordinate of the base position.
     * @param dir    The direction in which the cylinder extends outwards.
     * @param length The length that the cylinder extends out from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinder(final int baseX, final int baseY, final int baseZ, final EnumFacing dir,
                                       final int length, final double radius) {
        return createRevolution(baseX, baseY, baseZ, dir, length, f -> radius);
    }

    /**
     * Creates a brush containing a cylinder extending in an axial direction from a base position.
     *
     * @param pos    The base position.
     * @param dir    The direction in which the cylinder extends outwards.
     * @param length The length that the cylinder extends out from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     * @return The new cylinder brush.
     */
    public static Brush createCylinder(final BlockPos pos, final EnumFacing dir,
                                       final int length, final double radius) {
        return createCylinder(pos.getX(), pos.getY(), pos.getZ(), dir, length, radius);
    }
}
