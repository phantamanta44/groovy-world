package st.evening.mc.groovyworld;

import com.cleanroommc.groovyscript.event.GroovyReloadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import st.evening.mc.groovyworld.worldgen.GenSlot;
import st.evening.mc.groovyworld.worldgen.GrWFeatureGenerator;
import st.evening.mc.groovyworld.worldgen.GrWFeatureRegistry;

@Mod(modid = GrWConsts.MOD_ID, version = GrWConsts.VERSION, useMetadata = true)
public class GroovyWorld {
    public static final Logger LOGGER = LogManager.getLogger(GrWConsts.MOD_ID);

    @Mod.Instance
    @SuppressWarnings("NotNullFieldNotInitialized")
    public static GroovyWorld INSTANCE;

    private final GrWFeatureRegistry featureRegistry = new GrWFeatureRegistry();

    public GrWFeatureRegistry getFeatureRegistry() {
        return featureRegistry;
    }

    @Mod.EventHandler
    public void onPreInit(final FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        for (final GenSlot slot : GenSlot.values()) {
            GameRegistry.registerWorldGenerator(new GrWFeatureGenerator(featureRegistry, slot), slot.generatorWeight);
        }
    }

    @SubscribeEvent
    public void onGroovyReload(final GroovyReloadEvent event) {
        featureRegistry.clearFeatures();
    }
}
