package st.evening.mc.groovyworld.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import st.evening.mc.groovyworld.util.function.FloatDoubleFunction;
import st.evening.mc.groovyworld.util.function.FloatFunction;
import st.evening.mc.groovyworld.util.Plane;

/**
 * A library of brush geometries.
 */
public enum BrushGeometries {
    ;

    /**
     * A brush geometry that brushes only the base position.
     */
    public static BrushGeometry point() {
        return (rng, world, brushPos) -> BrushHelper.point(brushPos);
    }

    /**
     * A brush geometry that brushes only a single point at a fixed offset from the base position.
     *
     * @param dx The offset of the point's X-coordinate from the base position.
     * @param dy The offset of the point's Y-coordinate from the base position.
     * @param dz The offset of the point's Z-coordinate from the base position.
     */
    public static BrushGeometry point(final int dx, final int dy, final int dz) {
        return (rng, world, brushPos) -> BrushHelper.point(brushPos.add(dx, dy, dz));
    }

    /**
     * A brush geometry that brushes only a single point at a fixed offset from the base position.
     *
     * @param offset The offset of the point from the base position.
     */
    public static BrushGeometry point(final Vec3i offset) {
        return point(offset.getX(), offset.getY(), offset.getZ());
    }

    /**
     * Transforms a brush geometry by randomly discarding some of the blocks that it brushes.
     *
     * @param keepProbability The probability that a block is kept.
     * @param parent          The brush geometry to transform.
     * @return The dissolved brush geometry.
     */
    public static BrushGeometry dissolveUniform(final float keepProbability, final BrushGeometry parent) {
        return parent.filterBlocks(BlockFilters.random(keepProbability));
    }

    /**
     * Transforms a brush geometry by randomly discarding some of the blocks that it brushes. The probability that a
     * block is kept tends towards zero at a certain distance from the base position, creating a sort of 3D "airbrush"
     * effect.
     *
     * @param maxDist         The distance at which no blocks are kept.
     * @param keepProbability The probability that a block is kept at the base position.
     * @param parent          The brush geometry to transform.
     * @return The dissolved brush geometry.
     */
    public static BrushGeometry dissolveByDistance(final float maxDist, final float keepProbability,
                                                   final BrushGeometry parent) {
        return parent.filterBlocks(pos -> BlockFilters.randomByDistanceSq(pos, maxDist, keepProbability));
    }

    /**
     * A brush geometry that brushes positions by performing a random walk on the block grid from the base position.
     *
     * @param minSteps The minimum number of steps to walk for.
     * @param maxSteps The maximum number of steps to walk for.
     */
    public static BrushGeometry randomWalk(final int minSteps, final int maxSteps) {
        return (rng, world, brushPos) -> BrushGeometryHelper.randomWalk(
                rng, brushPos, RNGHelper.sampleUniformInt(rng, minSteps, maxSteps));
    }

    /**
     * A brush geometry that brushes positions by performing a random walk on the block grid from the base position.
     * Walkable positions are demarcated by a block filter; if the walk reaches an unwalkable position, it ends
     * immediately.
     *
     * @param minSteps The minimum number of steps to walk for.
     * @param maxSteps The maximum number of steps to walk for.
     * @param filter   The walkable block filter.
     */
    public static BrushGeometry randomWalk(final int minSteps, final int maxSteps, final BlockFilter filter) {
        return (rng, world, brushPos) -> BrushGeometryHelper.randomWalk(
                rng, brushPos, RNGHelper.sampleUniformInt(rng, minSteps, maxSteps), world, filter);
    }

    /**
     * A brush geometry that brushes a line of blocks extending in an axial direction from the base position.
     *
     * @param dir    The direction of the line.
     * @param length The length of the line.
     */
    public static BrushGeometry column(final EnumFacing dir, final int length) {
        return (rng, world, brushPos) -> BrushHelper.createColumn(brushPos, dir, length);
    }

    /**
     * A brush geometry that brushes a cuboid with bounds offset from the base position.
     *
     * @param minDx The offset of the cuboid's lower X-coordinate from the base position. Usually negative.
     * @param minDy The offset of the cuboid's lower Y-coordinate from the base position. Usually negative.
     * @param minDz The offset of the cuboid's lower Z-coordinate from the base position. Usually negative.
     * @param maxDx The offset of the cuboid's upper X-coordinate from the base position.
     * @param maxDy The offset of the cuboid's upper Y-coordinate from the base position.
     * @param maxDz The offset of the cuboid's upper Z-coordinate from the base position.
     */
    public static BrushGeometry cuboid(final int minDx, final int minDy, final int minDz,
                                       final int maxDx, final int maxDy, final int maxDz) {
        return (rng, world, brushPos) -> BrushHelper.createCuboid(
                brushPos.getX() + minDx, brushPos.getY() + minDy, brushPos.getZ() + minDz,
                brushPos.getX() + maxDx, brushPos.getY() + maxDy, brushPos.getZ() + maxDz);
    }

    /**
     * A brush geometry that brushes a cuboid with bounds extending from the base position in both the positive and
     * negative directions along each axis.
     *
     * @param dx The extent of the cuboid from the base position along the X-axis.
     * @param dy The extent of the cuboid from the base position along the Y-axis.
     * @param dz The extent of the cuboid from the base position along the Z-axis.
     */
    public static BrushGeometry cuboid(final int dx, final int dy, final int dz) {
        return (rng, world, brushPos) -> BrushHelper.createCuboid(
                brushPos.getX() - dx, brushPos.getY() - dy, brushPos.getZ() - dz,
                brushPos.getX() + dx, brushPos.getY() + dy, brushPos.getZ() + dz);
    }

    /**
     * A brush geometry that brushes a cuboid between vertices offset from the base position.
     *
     * @param offset1 The offset of one vertex from the base position.
     * @param offset2 The offset of the diagonally opposite vertex from the base position.
     */
    public static BrushGeometry cuboid(final Vec3i offset1, final Vec3i offset2) {
        return (rng, world, brushPos) -> BrushHelper.createCuboid(brushPos.add(offset1), brushPos.add(offset2));
    }

    /**
     * A brush geometry that brushes the outer surface of a cuboid with bounds offset from the base position.
     *
     * @param minDx The offset of the cuboid's lower X-coordinate from the base position. Usually negative.
     * @param minDy The offset of the cuboid's lower Y-coordinate from the base position. Usually negative.
     * @param minDz The offset of the cuboid's lower Z-coordinate from the base position. Usually negative.
     * @param maxDx The offset of the cuboid's upper X-coordinate from the base position.
     * @param maxDy The offset of the cuboid's upper Y-coordinate from the base position.
     * @param maxDz The offset of the cuboid's upper Z-coordinate from the base position.
     */
    public static BrushGeometry cuboidSurface(final int minDx, final int minDy, final int minDz,
                                              final int maxDx, final int maxDy, final int maxDz) {
        return (rng, world, brushPos) -> BrushHelper.createCuboidSurface(
                brushPos.getX() + minDx, brushPos.getY() + minDy, brushPos.getZ() + minDz,
                brushPos.getX() + maxDx, brushPos.getY() + maxDy, brushPos.getZ() + maxDz);
    }

    /**
     * A brush geometry that brushes the outer surface of a cuboid with bounds extending from the base position in both
     * the positive and negative directions along each axis.
     *
     * @param dx The extent of the cuboid from the base position along the X-axis.
     * @param dy The extent of the cuboid from the base position along the Y-axis.
     * @param dz The extent of the cuboid from the base position along the Z-axis.
     */
    public static BrushGeometry cuboidSurface(final int dx, final int dy, final int dz) {
        return (rng, world, brushPos) -> BrushHelper.createCuboidSurface(
                brushPos.getX() - dx, brushPos.getY() - dy, brushPos.getZ() - dz,
                brushPos.getX() + dx, brushPos.getY() + dy, brushPos.getZ() + dz);
    }

    /**
     * A brush geometry that brushes the surface of a cuboid between vertices offset from the base position.
     *
     * @param offset1 The offset of one vertex from the base position.
     * @param offset2 The offset of the diagonally opposite vertex from the base position.
     */
    public static BrushGeometry cuboidSurface(final Vec3i offset1, final Vec3i offset2) {
        return (rng, world, brushPos) ->
                BrushHelper.createCuboidSurface(brushPos.add(offset1), brushPos.add(offset2));
    }

    /**
     * A brush geometry that brushes a disk on the YZ plane with its center at the base position.
     *
     * @param radius The radius of the disk.
     */
    public static BrushGeometry diskYZ(final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createDiskYZ(brushPos, radius);
    }

    /**
     * A brush geometry that brushes a disk on the XZ plane with its center at the base position.
     *
     * @param radius The radius of the disk.
     */
    public static BrushGeometry diskXZ(final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createDiskXZ(brushPos, radius);
    }

    /**
     * A brush geometry that brushes a disk on the XY plane with its center at the base position.
     *
     * @param radius The radius of the disk.
     */
    public static BrushGeometry diskXY(final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createDiskXY(brushPos, radius);
    }

    /**
     * A brush geometry that brushes a disk on one of the axial planes with its center at the base position.
     *
     * @param plane  The axial plane in which the disk lies.
     * @param radius The radius of the disk.
     */
    public static BrushGeometry disk(final Plane plane, final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createDisk(brushPos, plane, radius);
    }

    /**
     * A brush geometry that brushes a sphere with its center at the base position.
     *
     * @param radius The radius of the sphere.
     */
    public static BrushGeometry sphere(final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createSphere(brushPos, radius);
    }

    /**
     * A brush geometry that brushes an axis-aligned ellipsoid with its center at the base position.
     *
     * @param radiusX The radius of the ellipsoid on the X-axis.
     * @param radiusY The radius of the ellipsoid on the Y-axis.
     * @param radiusZ The radius of the ellipsoid on the Z-axis.
     */
    public static BrushGeometry ellipsoid(final double radiusX, final double radiusY, final double radiusZ) {
        return (rng, world, brushPos) -> BrushHelper.createEllipsoid(brushPos, radiusX, radiusY, radiusZ);
    }

    /**
     * A brush geometry that brushes a solid of revolution with its axis of revolution extending upwards from the base
     * position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param length    The length that the solid extends up from the base position.
     * @param radiusMap The radius map.
     */
    public static BrushGeometry revolutionUp(final int length, final FloatDoubleFunction radiusMap) {
        return (rng, world, brushPos) -> BrushHelper.createRevolutionUp(brushPos, length, radiusMap);
    }

    /**
     * A brush geometry that brushes a solid of revolution with its axis of revolution extending downwards from the base
     * position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param length    The length that the solid extends down from the base position.
     * @param radiusMap The radius map.
     */
    public static BrushGeometry revolutionDown(final int length, final FloatDoubleFunction radiusMap) {
        return (rng, world, brushPos) -> BrushHelper.createRevolutionDown(brushPos, length, radiusMap);
    }

    /**
     * A brush geometry that brushes a solid of revolution with its axis of revolution extending northwards from the
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param length    The length that the solid extends north from the base position.
     * @param radiusMap The radius map.
     */
    public static BrushGeometry revolutionNorth(final int length, final FloatDoubleFunction radiusMap) {
        return (rng, world, brushPos) -> BrushHelper.createRevolutionNorth(brushPos, length, radiusMap);
    }

    /**
     * A brush geometry that brushes a solid of revolution with its axis of revolution extending eastwards from the base
     * position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param length    The length that the solid extends east from the base position.
     * @param radiusMap The radius map.
     */
    public static BrushGeometry revolutionEast(final int length, final FloatDoubleFunction radiusMap) {
        return (rng, world, brushPos) -> BrushHelper.createRevolutionEast(brushPos, length, radiusMap);
    }

    /**
     * A brush geometry that brushes a solid of revolution with its axis of revolution extending southwards from the
     * base position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param length    The length that the solid extends south from the base position.
     * @param radiusMap The radius map.
     */
    public static BrushGeometry revolutionSouth(final int length, final FloatDoubleFunction radiusMap) {
        return (rng, world, brushPos) -> BrushHelper.createRevolutionSouth(brushPos, length, radiusMap);
    }

    /**
     * A brush geometry that brushes a solid of revolution with its axis of revolution extending westwards from the base
     * position. The radius of the cross-section at a point along the axis is given by a "radius map", which maps a
     * progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a radius in blocks.
     *
     * @param length    The length that the solid extends west from the base position.
     * @param radiusMap The radius map.
     */
    public static BrushGeometry revolutionWest(final int length, final FloatDoubleFunction radiusMap) {
        return (rng, world, brushPos) -> BrushHelper.createRevolutionWest(brushPos, length, radiusMap);
    }

    /**
     * A brush geometry that brushes a solid of revolution with its axis of revolution extending in an axial direction
     * from the base position. The radius of the cross-section at a point along the axis is given by a "radius map",
     * which maps a progress fraction along the axis (i.e. from zero at the base to one at the maximum extent) to a
     * radius in blocks.
     *
     * @param dir       The direction in which the solid extends outwards.
     * @param length    The length that the solid extends out from the base position.
     * @param radiusMap The radius map.
     */
    public static BrushGeometry revolution(final EnumFacing dir,
                                           final int length, final FloatDoubleFunction radiusMap) {
        return (rng, world, brushPos) -> BrushHelper.createRevolution(brushPos, dir, length, radiusMap);
    }

    /**
     * A brush geometry that brushes a cylinder extending upwards from the base position.
     *
     * @param length The length that the cylinder extends up from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     */
    public static BrushGeometry cylinderUp(final int length, final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createCylinderUp(brushPos, length, radius);
    }

    /**
     * A brush geometry that brushes a cylinder extending downwards from the base position.
     *
     * @param length The length that the cylinder extends down from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     */
    public static BrushGeometry cylinderDown(final int length, final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createCylinderDown(brushPos, length, radius);
    }

    /**
     * A brush geometry that brushes a cylinder extending northwards from the base position.
     *
     * @param length The length that the cylinder extends north from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     */
    public static BrushGeometry cylinderNorth(final int length, final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createCylinderNorth(brushPos, length, radius);
    }

    /**
     * A brush geometry that brushes a cylinder extending eastwards from the base position.
     *
     * @param length The length that the cylinder extends east from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     */
    public static BrushGeometry cylinderEast(final int length, final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createCylinderEast(brushPos, length, radius);
    }

    /**
     * A brush geometry that brushes a cylinder extending southwards from the base position.
     *
     * @param length The length that the cylinder extends south from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     */
    public static BrushGeometry cylinderSouth(final int length, final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createCylinderSouth(brushPos, length, radius);
    }

    /**
     * A brush geometry that brushes a cylinder extending westwards from the base position.
     *
     * @param length The length that the cylinder extends west from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     */
    public static BrushGeometry cylinderWest(final int length, final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createCylinderWest(brushPos, length, radius);
    }

    /**
     * A brush geometry that brushes a cylinder extending in an axial direction from the base position.
     *
     * @param dir    The direction in which the cylinder extends outwards.
     * @param length The length that the cylinder extends out from the base position.
     * @param radius The cross-sectional radius of the cylinder.
     */
    public static BrushGeometry cylinder(final EnumFacing dir, final int length, final double radius) {
        return (rng, world, brushPos) -> BrushHelper.createCylinder(brushPos, dir, length, radius);
    }

    /**
     * Filters a brush geometry to only blocks that are on top of an upwards-facing surface.
     *
     * @param surfaceMatcher A block filter for the surface.
     * @param parent         The brush geometry to filter.
     * @return The filtered brush geometry.
     */
    public static BrushGeometry onSurface(final BlockFilter surfaceMatcher, final BrushGeometry parent) {
        return parent.filterBlocks((rng, world, pos) ->
                BrushGeometryHelper.isOnSurface(rng, world, pos, surfaceMatcher));
    }

    /**
     * Filters a brush geometry to only blocks that are on top of an upwards-facing solid surface.
     *
     * @param parent The brush geometry to filter.
     * @return The filtered brush geometry.
     */
    public static BrushGeometry onSurface(final BrushGeometry parent) {
        return parent.filterBlocks(BrushGeometryHelper::isOnSurface);
    }

    /**
     * Filters a brush geometry to only blocks that are on top of an upwards-facing surface, then randomly discards some
     * of the blocks to create a "scatter" effect. Useful for generating ground decorations like flowers.
     *
     * @param keepProbability The probability that a block is kept.
     * @param surfaceMatcher  A block filter for the surface.
     * @param parent          The brush geometry to transform.
     * @return The transformed brush geometry.
     */
    public static BrushGeometry onSurfaceScatterUniform(final float keepProbability,
                                                        final BlockFilter surfaceMatcher, final BrushGeometry parent) {
        return dissolveUniform(keepProbability, onSurface(surfaceMatcher, parent));
    }

    /**
     * Filters a brush geometry to only blocks that are on top of an upwards-facing solid surface, then randomly
     * discards some of the blocks to create a "scatter" effect. Useful for generating ground decorations like flowers.
     *
     * @param keepProbability The probability that a block is kept.
     * @param parent          The brush geometry to transform.
     * @return The transformed brush geometry.
     */
    public static BrushGeometry onSurfaceScatterUniform(final float keepProbability, final BrushGeometry parent) {
        return dissolveUniform(keepProbability, onSurface(parent));
    }

    /**
     * Filters a brush geometry to only blocks that are on top of an upwards-facing surface, then randomly discards some
     * of the blocks to create a "scatter" effect. The probability that a block is kept tends towards zero at a certain
     * distance from the base position, creating a sort of 3D "airbrush" effect.
     *
     * @param maxDist         The distance at which no blocks are kept.
     * @param keepProbability The probability that a block is kept at the base position.
     * @param surfaceMatcher  A block filter for the surface.
     * @param parent          The brush geometry to transform.
     * @return The transformed brush geometry.
     */
    public static BrushGeometry onSurfaceScatterByDistance(final float maxDist, final float keepProbability,
                                                           final BlockFilter surfaceMatcher,
                                                           final BrushGeometry parent) {
        return dissolveByDistance(maxDist, keepProbability, onSurface(surfaceMatcher, parent));
    }

    /**
     * Filters a brush geometry to only blocks that are on top of an upwards-facing solid surface, then randomly
     * discards some of the blocks to create a "scatter" effect. The probability that a block is kept tends towards zero
     * at a certain distance from the base position, creating a sort of 3D "airbrush" effect.
     *
     * @param maxDist         The distance at which no blocks are kept.
     * @param keepProbability The probability that a block is kept at the base position.
     * @param parent          The brush geometry to transform.
     * @return The transformed brush geometry.
     */
    public static BrushGeometry onSurfaceScatterByDistance(final float maxDist, final float keepProbability,
                                                           final BrushGeometry parent) {
        return dissolveByDistance(maxDist, keepProbability, onSurface(parent));
    }

    /**
     * A brush geometry that brushes a "vein" of blocks using a sub-brush. In particular, it picks a random vector in an
     * arbitrary orientation in 3D space and applies the sub-brush at intervals along a line segment extending outwards
     * along that vector in both the positive and negative directions from the base position. The sub-brush is
     * parametric in a fractional "size" value that varies sinusoidally from zero at the outer edges of the vein to one
     * at the center (i.e. at the base position). This is somewhat similar to how vanilla ore generation works.
     *
     * @param minLength      The minimum length of the vein.
     * @param maxLength      The maximum length of the vein.
     * @param stepInterval   The interval, in blocks, at which the sub-brush should be applied.
     * @param minVerticality The minimum verticality of the vein (from 0 for flat to 1 for straight upwards).
     * @param maxVerticality The maximum verticality of the vein.
     * @param stepBrush      The "size"-parametric sub-brush to apply at intervals.
     */
    public static BrushGeometry vein(final double minLength, final double maxLength, final double stepInterval,
                                     final float minVerticality, final float maxVerticality,
                                     final FloatFunction<BrushGeometry> stepBrush) {
        return (rng, world, brushPos) -> BrushGeometryHelper.createVein(rng, world, brushPos,
                RNGHelper.sampleUniform(rng, minLength, maxLength),
                stepInterval,
                BrushHelper.PI_F * 2F * rng.nextFloat(), // pitch is strictly positive; -pitch = +pi yaw
                BrushHelper.PI_F * 0.5F * RNGHelper.sampleUniformFloat(rng, minVerticality, maxVerticality),
                stepBrush);
    }

    /**
     * A brush geometry that brushes a "vein" of spheres. This is a specialization of
     * {@link #vein(double, double, double, float, float, FloatFunction)} that brushes spheres along the vein with
     * radius varying with the "size" parameter. This is somewhat similar to how vanilla ore generation works.
     *
     * @param minLength      The minimum length of the vein.
     * @param maxLength      The maximum length of the vein.
     * @param stepInterval   The interval, in blocks, at which the sub-brush should be applied.
     * @param minVerticality The minimum verticality of the vein (from 0 for flat to 1 for straight upwards).
     * @param maxVerticality The maximum verticality of the vein.
     * @param minStepRadius  The radius of the spheres at the outer edges of the vein.
     * @param maxStepRadius  The radius of the spheres at the center of the vein.
     */
    public static BrushGeometry vein(final double minLength, final double maxLength, final double stepInterval,
                                     final float minVerticality, final float maxVerticality,
                                     final double minStepRadius, final double maxStepRadius) {
        final double dr = maxStepRadius - minStepRadius;
        return vein(minLength, maxLength, stepInterval, minVerticality, maxVerticality,
                f -> sphere(Math.max(minStepRadius + f * dr, 0.5D)));
    }
}
