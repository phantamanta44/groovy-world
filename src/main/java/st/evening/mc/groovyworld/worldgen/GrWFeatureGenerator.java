package st.evening.mc.groovyworld.worldgen;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GrWFeatureGenerator implements IWorldGenerator {
    private final GrWFeatureRegistry registry;
    private final GenSlot slot;

    public GrWFeatureGenerator(final GrWFeatureRegistry registry, final GenSlot slot) {
        this.registry = registry;
        this.slot = slot;
    }

    @Override
    public void generate(final Random random, final int chunkX, final int chunkZ, final World world,
                         final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {
        final Set<Map.Entry<String, GrWFeature>> features = registry.getFeatures(slot);
        if (features.isEmpty()) return;
        final int minX = (chunkX << 4) + 8, minZ = (chunkZ << 4) + 8;
        for (final Map.Entry<String, GrWFeature> entry : features) {
            try {
                entry.getValue().generate(random, world, minX, minZ);
            } catch (final Exception e) {
                final CrashReport report = CrashReport.makeCrashReport(e, "Generating groovy world feature");
                final CrashReportCategory category = report.makeCategory("GrW feature generator");
                category.addCrashSection("Slot", slot);
                category.addCrashSection("Feature", entry.getKey());
                category.addCrashSection("Dimension", world.provider.getDimension());
                category.addCrashSection("Chunk X", chunkX);
                category.addCrashSection("Chunk Z", chunkZ);
                throw new ReportedException(report);
            }
        }
    }
}
