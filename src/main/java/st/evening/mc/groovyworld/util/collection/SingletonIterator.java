package st.evening.mc.groovyworld.util.collection;

import org.jspecify.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingletonIterator<T> implements Iterator<T> {
    private @Nullable T element;

    public SingletonIterator(final T element) {
        this.element = element;
    }

    @Override
    public boolean hasNext() {
        return element != null;
    }

    @Override
    public T next() {
        final T next = element;
        if (next == null) throw new NoSuchElementException();
        element = null;
        return next;
    }
}
