package st.evening.mc.groovyworld.worldgen;

/**
 * A time slot in which features are generated.
 */
public enum GenSlot {
    EARLIEST(Integer.MIN_VALUE, 7677383011749354414L),
    EARLIER(-101, 616916820135870785L),
    EARLY(-1, 7849728955063353807L),
    NORMAL(0, 5521302890793641190L),
    LATE(1, 4885791873880115456L),
    LATER(101, 5985226099245194364L),
    LATEST(Integer.MAX_VALUE, 8561105100579255666L);

    public final int generatorWeight;
    public final long seedModifier;

    GenSlot(final int generatorWeight, final long seedModifier) {
        this.generatorWeight = generatorWeight;
        this.seedModifier = seedModifier;
    }
}
