package st.evening.mc.groovyworld.grs;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import net.minecraftforge.fml.common.Mod;
import st.evening.mc.groovyworld.GrWConsts;
import st.evening.mc.groovyworld.GroovyWorld;

@Mod.EventBusSubscriber(modid = GrWConsts.MOD_ID)
public class GroovyWorldPlugin implements GroovyPlugin {
    @Override
    public String getModId() {
        return GrWConsts.MOD_ID;
    }

    @Override
    public String getContainerName() {
        return "Groovy World";
    }

    @Override
    public void onCompatLoaded(final GroovyContainer<?> container) {
        container.addProperty(GroovyWorld.INSTANCE.getFeatureRegistry());
    }
}
