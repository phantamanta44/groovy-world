package st.evening.mc.groovyworld.util.function;

/**
 * A function from floats to objects.
 *
 * @param <T> The return type.
 */
@FunctionalInterface
public interface FloatFunction<T> {
    T apply(float value);
}
