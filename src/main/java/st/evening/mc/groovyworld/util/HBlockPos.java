package st.evening.mc.groovyworld.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * A horizontal block position with only X- and Z-coordinates.
 */
public class HBlockPos {
    /**
     * The origin position.
     */
    public static final HBlockPos ZERO = new HBlockPos(0, 0);

    /**
     * The X-coordinate of this position.
     */
    public final int x;

    /**
     * The Z-coordinate of this position.
     */
    public final int z;

    /**
     * Creates a new horizontal block position with the given coordinates.
     *
     * @param x The new position's X-coordinate.
     * @param z The new position's Z-coordinate.
     */
    public HBlockPos(final int x, final int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Creates a new horizontal block position by projecting the given 3D block position onto the XZ plane.
     *
     * @param pos The 3D position to project.
     */
    public HBlockPos(final BlockPos pos) {
        this(pos.getX(), pos.getZ());
    }

    /**
     * Computes a new position at an offset from this one.
     *
     * @param dx The offset on the X-axis.
     * @param dz The offset on the Z-axis.
     * @return The new position.
     */
    public HBlockPos add(final int dx, final int dz) {
        return new HBlockPos(x + dx, z + dz);
    }

    /**
     * Computes a new position north of this one.
     *
     * @param offset The offset to the north.
     * @return The new position.
     */
    public HBlockPos north(final int offset) {
        return new HBlockPos(x, z - offset);
    }

    /**
     * Computes a new position east of this one.
     *
     * @param offset The offset to the east.
     * @return The new position.
     */
    public HBlockPos east(final int offset) {
        return new HBlockPos(x + offset, z);
    }

    /**
     * Computes a new position south of this one.
     *
     * @param offset The offset to the south.
     * @return The new position.
     */
    public HBlockPos south(final int offset) {
        return new HBlockPos(x, z + offset);
    }

    /**
     * Computes a new position west of this one.
     *
     * @param offset The offset to the west.
     * @return The new position.
     */
    public HBlockPos west(final int offset) {
        return new HBlockPos(x - offset, z);
    }

    /**
     * Computes a new position at an offset from this one in a cardinal direction.
     *
     * @param face   The direction to offset towards.
     * @param amount The offset in the given direction.
     * @return The new position.
     */
    public HBlockPos offset(final EnumFacing face, final int amount) {
        switch (face) {
            case NORTH:
                return north(amount);
            case EAST:
                return east(amount);
            case SOUTH:
                return south(amount);
            case WEST:
                return west(amount);
            default:
                return this;
        }
    }

    /**
     * Promotes this horizontal block position to a 3D block position.
     *
     * @param y The Y-coordinate of the new 3D position.
     * @return The new 3D block position.
     */
    public BlockPos toBlockPos(final int y) {
        return new BlockPos(x, y, z);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof HBlockPos)) return false;
        final HBlockPos o = (HBlockPos) obj;
        return x == o.x && z == o.z;
    }

    @Override
    public int hashCode() {
        return x + z * 31;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + z + ")";
    }
}
