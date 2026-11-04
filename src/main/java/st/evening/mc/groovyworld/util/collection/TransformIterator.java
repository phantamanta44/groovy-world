package st.evening.mc.groovyworld.util.collection;

import com.google.common.collect.AbstractIterator;
import org.jspecify.annotations.Nullable;

import java.util.Iterator;
import java.util.function.Function;

public class TransformIterator<A, B> extends AbstractIterator<B> {
    private final Iterator<A> backing;
    private final Function<A, @Nullable B> transform;

    public TransformIterator(final Iterator<A> backing, final Function<A, @Nullable B> transform) {
        this.backing = backing;
        this.transform = transform;
    }

    @Override
    protected B computeNext() {
        while (backing.hasNext()) {
            final B value = transform.apply(backing.next());
            if (value != null) return value;
        }
        return endOfData();
    }
}
