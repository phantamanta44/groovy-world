package st.evening.mc.groovyworld.util.collection;

import com.google.common.collect.AbstractIterator;
import org.jspecify.annotations.Nullable;

import java.util.Iterator;

public class PlusIterator<T> extends AbstractIterator<T> {
    private Iterator<T> current;
    private @Nullable Iterator<T> second;

    public PlusIterator(final Iterator<T> first, final Iterator<T> second) {
        this.current = first;
        this.second = second;
    }

    @Override
    protected T computeNext() {
        if (current.hasNext()) {
            return current.next();
        }
        if (second != null) {
            current = second;
            second = null;
            if (current.hasNext()) {
                return current.next();
            }
        }
        return endOfData();
    }
}
