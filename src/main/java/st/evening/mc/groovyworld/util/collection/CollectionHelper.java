package st.evening.mc.groovyworld.util.collection;

import java.util.Collection;

public enum CollectionHelper {
    ;

    public static <T> boolean containsAny(final Collection<T> collection, final Collection<T> query) {
        for (final T element : query) {
            if (collection.contains(element)) return true;
        }
        return false;
    }

    public static <T> boolean containsAny(final Collection<T> collection, final T[] query) {
        for (final T element : query) {
            if (collection.contains(element)) return true;
        }
        return false;
    }
}
