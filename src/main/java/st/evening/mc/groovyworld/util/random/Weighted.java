package st.evening.mc.groovyworld.util.random;

/**
 * An object annotated with an integer weight. Useful for weighted random sampling.
 *
 * @param <T> The type of the annotated object.
 */
public class Weighted<T> {
    private final T value;
    private final int weight;

    /**
     * Creates a new weighted object.
     *
     * @param value  The object.
     * @param weight The object's weight.
     */
    public Weighted(final T value, final int weight) {
        this.value = value;
        this.weight = weight;
    }

    /**
     * @return The annotated object.
     */
    public T getValue() {
        return value;
    }

    /**
     * @return The object's weight.
     */
    public int getWeight() {
        return weight;
    }
}
