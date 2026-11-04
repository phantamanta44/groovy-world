import net.minecraftforge.event.terraingen.OreGenEvent
import net.minecraftforge.fml.common.eventhandler.Event
import st.evening.mc.groovyworld.api.BlockFilters
import st.evening.mc.groovyworld.api.BlockPalettes
import st.evening.mc.groovyworld.api.BrushGeometries
import st.evening.mc.groovyworld.api.ChunkFilters
import st.evening.mc.groovyworld.api.Distributions
import st.evening.mc.groovyworld.api.FeatureBuilder
import st.evening.mc.groovyworld.api.FeatureDistributions
import st.evening.mc.groovyworld.api.Painters

/*
 * This example script provides a rough approximation of vanilla feature generation.
 */

// disable all vanilla generation
event_manager.listen(EventBusType.ORE_GENERATION) { OreGenEvent.GenerateMinable event ->
    if (event.type != OreGenEvent.GenerateMinable.EventType.CUSTOM) {
        event.result = Event.Result.DENY
    }
}

// chunk filter that prevents generation in the nether or end
def overworldOnly = ~(ChunkFilters.dimension(1) | ChunkFilters.dimension(-1))

////////////////////
// rocks and dirt //
////////////////////

// all the generators here use the same geometry
def dirtGeo = BrushGeometries.vein(7, 9, 1, 0, 0.125, 1.5, 2.5)

mods.groovyworld.featureRegistry.addFeature("dirt", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(10)
        .setDistribution(FeatureDistributions.uniformY(0, 256))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(Painters.brush(dirtGeo, BlockFilters.naturalStone(), BlockPalettes.block(Blocks.DIRT)))
        .build())

mods.groovyworld.featureRegistry.addFeature("gravel", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(8)
        .setDistribution(FeatureDistributions.uniformY(0, 256))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(Painters.brush(dirtGeo, BlockFilters.naturalStone(), BlockPalettes.block(Blocks.GRAVEL)))
        .build())

mods.groovyworld.featureRegistry.addFeature("diorite", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(10)
        .setDistribution(FeatureDistributions.uniformY(0, 80))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(Painters.brush(dirtGeo, BlockFilters.naturalStone(),
                BlockPalettes.state(blockstate("minecraft:stone:variant=diorite"))))
        .build())

mods.groovyworld.featureRegistry.addFeature("granite", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(10)
        .setDistribution(FeatureDistributions.uniformY(0, 80))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(Painters.brush(dirtGeo, BlockFilters.naturalStone(),
                BlockPalettes.state(blockstate("minecraft:stone:variant=granite"))))
        .build())

mods.groovyworld.featureRegistry.addFeature("andesite", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(10)
        .setDistribution(FeatureDistributions.uniformY(0, 80))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(Painters.brush(dirtGeo, BlockFilters.naturalStone(),
                BlockPalettes.state(blockstate("minecraft:stone:variant=andesite"))))
        .build())

mods.groovyworld.featureRegistry.addFeature("silverfish", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(7)
        .setDistribution(FeatureDistributions.uniformY(0, 64))
        .setBlockFilter(BlockFilters.naturalStone() & BlockFilters.biomes( // only in extreme hills biomes
                Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_EDGE, Biomes.EXTREME_HILLS_WITH_TREES,
                Biomes.MUTATED_EXTREME_HILLS, Biomes.MUTATED_EXTREME_HILLS_WITH_TREES))
        .setPainter(Painters.brush(
                BrushGeometries.vein(2, 3, 0.75, 0, 0.5, 0.75, 1.1),
                BlockFilters.naturalStone(),
                BlockPalettes.state(blockstate("minecraft:monster_egg:variant=stone"))))
        .build())

//////////
// ores //
//////////

mods.groovyworld.featureRegistry.addFeature("coal_ore", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(20)
        .setDistribution(FeatureDistributions.uniformY(0, 128))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(Painters.brush(
                BrushGeometries.vein(3, 5, 1, 0, 0.25, 1, 1.5),
                BlockFilters.naturalStone(),
                BlockPalettes.block(Blocks.COAL_ORE)))
        .build())

mods.groovyworld.featureRegistry.addFeature("iron_ore", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(20)
        .setDistribution(FeatureDistributions.uniformY(0, 64))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(Painters.brush(
                BrushGeometries.vein(2, 3, 0.75, 0, 0.5, 0.75, 1.1),
                BlockFilters.naturalStone(),
                BlockPalettes.block(Blocks.IRON_ORE)))
        .build())

mods.groovyworld.featureRegistry.addFeature("gold_ore", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(2)
        .setDistribution(FeatureDistributions.uniformY(0, 32))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(Painters.brush(
                BrushGeometries.vein(2, 3, 0.75, 0, 0.5, 0.75, 1.1),
                BlockFilters.naturalStone(),
                BlockPalettes.block(Blocks.GOLD_ORE)))
        .build())

mods.groovyworld.featureRegistry.addFeature("redstone_ore", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(8)
        .setDistribution(FeatureDistributions.uniformY(0, 16))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(Painters.brush(
                BrushGeometries.vein(2, 2, 0.6, 0, 0.5, 0.75, 1),
                BlockFilters.naturalStone(),
                BlockPalettes.block(Blocks.REDSTONE_ORE)))
        .build())

mods.groovyworld.featureRegistry.addFeature("diamond_ore", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(1)
        .setDistribution(FeatureDistributions.uniformY(0, 16))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(Painters.brush(
                BrushGeometries.vein(2, 2, 0.6, 0, 0.5, 0.75, 1),
                BlockFilters.naturalStone(),
                BlockPalettes.block(Blocks.DIAMOND_ORE)))
        .build())

mods.groovyworld.featureRegistry.addFeature("lapis_ore", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(1)
        .setDistribution(FeatureDistributions.randomY(Distributions.triangularInt(16, 16))) // vanilla gen isn't uniform
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(Painters.brush(
                BrushGeometries.vein(2, 2, 0.6, 0, 0.5, 0.7, 0.95),
                BlockFilters.naturalStone(),
                BlockPalettes.block(Blocks.LAPIS_ORE)))
        .build())

mods.groovyworld.featureRegistry.addFeature("gold_ore_mesa", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(20)
        .setDistribution(FeatureDistributions.uniformY(32, 80))
        .setBlockFilter(BlockFilters.naturalStone() & BlockFilters.biomes( // only in mesa biomes
                Biomes.MESA, Biomes.MESA_ROCK, Biomes.MESA_CLEAR_ROCK,
                Biomes.MUTATED_MESA, Biomes.MUTATED_MESA_ROCK, Biomes.MUTATED_MESA_CLEAR_ROCK))
        .setPainter(Painters.brush(
                BrushGeometries.vein(2, 3, 0.75, 0, 0.5, 0.75, 1.1),
                BlockFilters.naturalStone(),
                BlockPalettes.block(Blocks.GOLD_ORE)))
        .build())

mods.groovyworld.featureRegistry.addFeature("emerald_ore", new FeatureBuilder()
        .setChunkFilter(overworldOnly)
        .setTriesPerChunk(20)
        .setDistribution(FeatureDistributions.uniformY(32, 80))
        .setBlockFilter(BlockFilters.naturalStone() & BlockFilters.biomes( // only in extreme hills biomes
                Biomes.EXTREME_HILLS, Biomes.EXTREME_HILLS_EDGE, Biomes.EXTREME_HILLS_WITH_TREES,
                Biomes.MUTATED_EXTREME_HILLS, Biomes.MUTATED_EXTREME_HILLS_WITH_TREES))
        .setPainter(Painters.brush(
                BrushGeometries.point(), BlockFilters.naturalStone(), BlockPalettes.block(Blocks.EMERALD_ORE)))
        .build())

mods.groovyworld.featureRegistry.addFeature("nether_quartz", new FeatureBuilder()
        .setChunkFilter(ChunkFilters.dimension(-1)) // only in the nether
        .setTriesPerChunk(16)
        .setDistribution(FeatureDistributions.uniformY(10, 118))
        .setBlockFilter(BlockFilters.block(Blocks.NETHERRACK))
        .setPainter(Painters.brush(
                BrushGeometries.vein(3, 4, 1, 0, 0.25, 0.95, 1.4),
                BlockFilters.block(Blocks.NETHERRACK),
                BlockPalettes.block(Blocks.GOLD_ORE)))
        .build())
