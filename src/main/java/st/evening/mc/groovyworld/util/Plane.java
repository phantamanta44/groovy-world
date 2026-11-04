package st.evening.mc.groovyworld.util;

import net.minecraft.util.EnumFacing;

/**
 * One of the three axial planes in 3D space.
 */
public enum Plane {
    /**
     * The YZ plane.
     */
    YZ,

    /**
     * The XZ plane.
     */
    XZ,

    /**
     * The XY plane.
     */
    XY;

    /**
     * Retrieves the plane normal to the given axis.
     *
     * @param axis The axis.
     * @return The plane normal to the given axis.
     */
    public static Plane fromNormalAxis(final EnumFacing.Axis axis) {
        switch (axis) {
            case X:
                return YZ;
            case Y:
                return XZ;
            case Z:
                return XY;
        }
        throw new IllegalStateException(axis.toString());
    }

    /**
     * Retrieves the plane normal to the given direction's axis.
     *
     * @param dir The direction.
     * @return The plane normal to the given direction's axis.
     */
    public static Plane fromNormalDir(final EnumFacing dir) {
        return fromNormalAxis(dir.getAxis());
    }
}
