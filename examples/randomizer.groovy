import net.minecraft.item.ItemBlock
import st.evening.mc.groovyworld.api.BlockFilters
import st.evening.mc.groovyworld.api.BlockPalettes
import st.evening.mc.groovyworld.api.BrushGeometries
import st.evening.mc.groovyworld.api.ChunkFilters
import st.evening.mc.groovyworld.api.FeatureBuilder
import st.evening.mc.groovyworld.api.FeatureDistributions
import st.evening.mc.groovyworld.api.Painters
import st.evening.mc.groovyworld.api.RNGHelper

/*
 * This example script generates random ore features for all the modded ores it can find in the ore dictionary.
 *
 * This is not terribly practical because the ore generation settings will be completely different whenever a modpack
 * update changes the ore dictionary, but it's interesting as a demonstration nonetheless.
 *
 * If you really want to play the game with this for some reason, you could modify the script to write a "lockfile"
 * containing the ore generation settings on the first startup to pin them in case of any pack changes.
 */

// chunk filter that prevents generation in the nether or end
def overworldOnly = ~(ChunkFilters.dimension(1) | ChunkFilters.dimension(-1))

// seeded RNG for determinism
def featureRng = new Random(1464914439174506881)

// sort the ore names alphabetically for determinism
def oreNames = new ArrayList<String>(ore_dict.streamOreNames())
oreNames.sort(Comparator.naturalOrder())

genFeatures:
for (oreName in oreNames) {
    // make sure it's an ore and that it's not a nether or end ore or one of thermal foundation's liquid ores
    if (!oreName.startsWith("ore")
            || oreName.startsWith("Nether", 3)
            || oreName.startsWith("End", 3)
            || oreName.startsWith("Clathrate", 3)) {
        continue genFeatures
    }

    // make sure it's not a vanilla ore
    def oreStacks = ore_dict.getItems(oreName)
    if (oreStacks.isEmpty()) continue genFeatures // sanity check
    if (oreStacks.any { oreStack -> oreStack.registryName.namespace == "minecraft" }) continue genFeatures

    // get the block to generate
    def oreStack = oreStacks[0]
    if (!(oreStack.item instanceof ItemBlock)) continue genFeatures // sanity check
    // a bit sketchy, but there's not really a clean way to get the block state placed by a given block item stack
    def orePalette = BlockPalettes.state(oreStack.item.block.getStateFromMeta(oreStack.metadata))

    // generate a height range for the feature
    def minY = RNGHelper.sampleUniformInt(featureRng, 0, 32)
    def maxY = RNGHelper.sampleUniformInt(featureRng, minY + 16, 64)

    // generate a geometry for the feature
    def minLength = RNGHelper.sampleUniform(featureRng, 1, 2)
    def maxLength = RNGHelper.sampleUniform(featureRng, minLength + 0.5, 3)
    def minVert = RNGHelper.sampleUniformFloat(featureRng, 0, 0.3)
    def maxVert = RNGHelper.sampleUniformFloat(featureRng, minVert, 0.8)
    def minRadius = RNGHelper.sampleUniform(featureRng, 0.5, 1)
    def maxRadius = RNGHelper.sampleUniform(featureRng, minRadius + 0.5, 2)
    def geo = BrushGeometries.vein(minLength, maxLength, 0.75, minVert, maxVert, minRadius, maxRadius)

    // build the feature using the generated values
    log("Generating $oreName with y=[$minY,$maxY] l=[$minLength,$maxLength] v=[$minVert,$maxVert] r=[$minRadius, $maxRadius]")
    mods.groovyworld.featureRegistry.addFeature("random_$oreName", new FeatureBuilder()
            .setChunkFilter(overworldOnly)
            .setDistribution(FeatureDistributions.uniformY(minY, maxY))
            .setBlockFilter(BlockFilters.naturalStone())
            .setPainter(Painters.brush(geo, BlockFilters.naturalStone(), orePalette))
            .build())
}
