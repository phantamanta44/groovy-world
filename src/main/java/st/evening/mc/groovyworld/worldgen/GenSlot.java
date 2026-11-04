package st.evening.mc.groovyworld.worldgen;

/**
 * A time slot in which features are generated.
 */
public enum GenSlot {
    EARLIEST(Integer.MIN_VALUE),
    EARLIER(-101),
    EARLY(-1),
    NORMAL(0),
    LATE(1),
    LATER(101),
    LATEST(Integer.MAX_VALUE);

    public final int generatorWeight;

    GenSlot(final int generatorWeight) {
        this.generatorWeight = generatorWeight;
    }
}
