package st.evening.mc.groovyworld.worldgen;

import com.cleanroommc.groovyscript.api.INamed;
import st.evening.mc.groovyworld.GroovyWorld;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class GrWFeatureRegistry implements INamed {
    private final Map<String, GrWFeature> features = new HashMap<>();
    @SuppressWarnings("unchecked")
    private final Map<String, GrWFeature>[] genSlots = new Map[GenSlot.values().length];

    public GrWFeatureRegistry() {
        for (int i = 0; i < genSlots.length; i++) {
            genSlots[i] = new LinkedHashMap<>();
        }
    }

    /**
     * Gets the set of all registered features.
     *
     * @return The set of features.
     */
    public Set<Map.Entry<String, GrWFeature>> getFeatures() {
        return features.entrySet();
    }

    /**
     * Gets the set of all registered features in the given slot.
     *
     * @param slot The slot to get features for.
     * @return The set of features.
     */
    public Set<Map.Entry<String, GrWFeature>> getFeatures(final GenSlot slot) {
        return genSlots[slot.ordinal()].entrySet();
    }

    /**
     * Registers a feature for generation.
     *
     * @param id      The feature's unique ID.
     * @param slot    The slot to register the feature in.
     * @param feature The feature to register.
     */
    public void addFeature(final String id, final GenSlot slot, final GrWFeature feature) {
        if (features.putIfAbsent(id, feature) != null) {
            throw new IllegalStateException("Duplicate feature registration: " + id);
        }
        genSlots[slot.ordinal()].put(id, feature);
        GroovyWorld.LOGGER.info("Registered feature {} in slot {}", id, slot);
    }

    /**
     * Registers a feature for generation in the {@link GenSlot#NORMAL} slot.
     *
     * @param id      The feature's unique ID.
     * @param feature The feature to register.
     */
    public void addFeature(final String id, final GrWFeature feature) {
        addFeature(id, GenSlot.NORMAL, feature);
    }

    /**
     * Removes a feature by its unique ID.
     *
     * @param id The ID of the feature to remove.
     * @return Whether a feature was successfully removed or not.
     */
    public boolean removeFeature(final String id) {
        if (features.remove(id) == null) return false;
        for (int i = 0; i < genSlots.length; i++) {
            if (genSlots[i].remove(id) != null) {
                GroovyWorld.LOGGER.info("Removed feature {} from slot {}", id, GenSlot.values()[i]);
                return true;
            }
        }
        throw new IllegalStateException("Feature was registered but had no slot: " + id);
    }

    /**
     * Removes all registered Groovy World features. You probably don't want to call this.
     */
    public void clearFeatures() {
        GroovyWorld.LOGGER.info("Clearing feature registry");
        features.clear();
        for (final Map<String, GrWFeature> slotFeatures : genSlots) {
            slotFeatures.clear();
        }
    }

    @Override
    public Collection<String> getAliases() {
        return Collections.singletonList("featureRegistry");
    }
}
