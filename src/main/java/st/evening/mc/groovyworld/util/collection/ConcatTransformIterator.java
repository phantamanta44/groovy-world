package st.evening.mc.groovyworld.util.collection;

import com.google.common.collect.AbstractIterator;
import org.jspecify.annotations.Nullable;

import java.util.Iterator;
import java.util.function.Function;

public class ConcatTransformIterator<I, T> extends AbstractIterator<T> {
    private final Iterator<I> backing;
    private final Function<I, @Nullable Iterator<T>> transform;
    private @Nullable Iterator<T> child = null;

    public ConcatTransformIterator(final Iterator<I> backing, final Function<I, @Nullable Iterator<T>> transform) {
        this.backing = backing;
        this.transform = transform;
    }

    @Override
    protected T computeNext() {
        while (true) {
            if (child != null && child.hasNext()) {
                return child.next();
            }
            if (!backing.hasNext()) {
                child = null;
                return endOfData();
            }
            child = transform.apply(backing.next());
        }
    }
}
