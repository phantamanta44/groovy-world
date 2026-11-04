package st.evening.mc.groovyworld.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Random;

/**
 * An action that actually places a feature into a world.
 */
@FunctionalInterface
public interface Painter {
    /**
     * Attempts to place a feature into a world at a given position, returning true if the placement is successful and
     * false otherwise. When returning false, the world should not have been modified; equivalently, if the world is
     * modified in any way, this should return true.
     *
     * @param rng      The world generation RNG.
     * @param world    The world where the feature should be placed.
     * @param paintPos The position at which the feature should be placed.
     * @return Whether the feature was successfully placed or not.
     */
    boolean paint(Random rng, World world, BlockPos paintPos);

    /**
     * Translates this painter by adding a fixed offset to the feature's placement position.
     *
     * @param dx The offset on the X-axis.
     * @param dy The offset on the Y-axis.
     * @param dz The offset on the Z-axis.
     * @return The translated painter.
     */
    default Painter offset(final int dx, final int dy, final int dz) {
        return (rng, world, paintPos) -> paint(rng, world, paintPos.add(dx, dy, dz));
    }

    /**
     * Translates this painter by adding a fixed offset to the feature's placement position.
     *
     * @param offset The offset vector.
     * @return The translated painter.
     */
    default Painter offset(final Vec3i offset) {
        return (rng, world, paintPos) -> paint(rng, world, paintPos.add(offset));
    }

    /**
     * Combines this painter with another one. The resulting painter first applies this painter, then the other. Both
     * painters will always run regardless of whether either succeeds or not.
     *
     * @param o The other painter.
     * @return The combined painter.
     */
    default Painter plus(final Painter o) { // note non-short circuiting disjunction!
        return (rng, world, paintPos) -> paint(rng, world, paintPos) | o.paint(rng, world, paintPos);
    }

    /**
     * Sequences this painter with another one. The resulting painter first applies this painter, and if it succeeds, it
     * then applies the other.
     *
     * @param o The other painter.
     * @return The sequenced painter.
     */
    default Painter then(final Painter o) {
        return (rng, world, paintPos) -> paint(rng, world, paintPos) && o.paint(rng, world, paintPos);
    }
}
