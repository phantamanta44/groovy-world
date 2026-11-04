package st.evening.mc.groovyworld.api;

import com.google.common.collect.Iterators;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jspecify.annotations.Nullable;
import st.evening.mc.groovyworld.util.collection.ConcatTransformIterator;
import st.evening.mc.groovyworld.util.collection.PlusIterator;
import st.evening.mc.groovyworld.util.collection.TransformIterator;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A region of blocks in a world. Equivalently, a collection of block positions.
 * <p>
 * Brushes are generally distinguished from collections (e.g. a {@link java.util.List}) by lazy semantics. This ensures
 * that very large brushes with simple geometric representations don't require a very large amount of memory.
 */
@FunctionalInterface
public interface Brush extends Iterable<BlockPos> {
    /**
     * Transforms this brush by applying a map to each position. If the map returns null at a position, that position is
     * deleted entirely.
     *
     * @param f The transformation map.
     * @return The transformed brush.
     */
    default Brush transform(final Function<BlockPos, @Nullable BlockPos> f) {
        return () -> new TransformIterator<>(iterator(), f);
    }

    /**
     * "Expands" this brush by creating a sub-brush at each position then combining them all into one big brush.
     * Comparable to the {@code flatMap} operation on collections.
     *
     * @param f The expansion map.
     * @return The expanded brush.
     */
    default Brush expand(final Function<BlockPos, @Nullable Brush> f) {
        return () -> new ConcatTransformIterator<>(iterator(), pos -> {
            final Brush brush = f.apply(pos);
            return brush != null ? brush.iterator() : null;
        });
    }

    /**
     * Filters the positions in this brush.
     *
     * @param f The filter.
     * @return The filtered brush.
     */
    default Brush filter(final Predicate<? super BlockPos> f) {
        return () -> Iterators.filter(iterator(), f::test);
    }

    /**
     * Translates this brush by a given offset vector.
     *
     * @param dx The offset on the X-axis.
     * @param dy The offset on the Y-axis.
     * @param dz The offset on the Z-axis.
     * @return The translated brush.
     */
    default Brush offset(final int dx, final int dy, final int dz) {
        return transform(pos -> pos.add(dx, dy, dz));
    }

    /**
     * Translates this brush by a given offset vector.
     *
     * @param offset The offset vector.
     * @return The translated brush.
     */
    default Brush offset(final Vec3i offset) {
        return transform(pos -> pos.add(offset));
    }

    /**
     * Combines this brush with another.
     *
     * @param o The other brush.
     * @return The combined brush.
     */
    default Brush plus(final Brush o) {
        return () -> new PlusIterator<>(iterator(), o.iterator());
    }
}
