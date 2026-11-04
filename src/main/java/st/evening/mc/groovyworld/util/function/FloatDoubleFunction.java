package st.evening.mc.groovyworld.util.function;

/**
 * A function from floats to doubles.
 */
@FunctionalInterface
public interface FloatDoubleFunction {
    double applyAsDouble(float value);
}
