import st.evening.mc.groovyworld.api.BlockFilters
import st.evening.mc.groovyworld.api.BlockPalettes
import st.evening.mc.groovyworld.api.BrushGeometries
import st.evening.mc.groovyworld.api.ChunkFilters
import st.evening.mc.groovyworld.api.FeatureBuilder
import st.evening.mc.groovyworld.api.FeatureDistributions
import st.evening.mc.groovyworld.api.Painters
import st.evening.mc.groovyworld.worldgen.GenSlot

/*
 * This example script generates small dungeon-like structures with mob spawners and loot chests.
 */

// chunk filter that prevents generation in the nether or end
def overworldOnly = ~(ChunkFilters.dimension(1) | ChunkFilters.dimension(-1))

// paints the walls, floor, and ceiling of the room
def wallPainter = Painters.brush(
        BrushGeometries.cuboidSurface(-5, -1, -5, 5, 4, 5),
        BlockPalettes.states(
                blockstate("minecraft:stonebrick:variant=stonebrick"),
                blockstate("minecraft:stonebrick:variant=mossy_stonebrick")))

// paints the air inside the room (i.e. clears out any terrain that's there)
def spacePainter = Painters.brush(BrushGeometries.cuboid(-4, 0, -4, 4, 3, 4), BlockPalettes.air())

// paints the mob spawner
def spawnerPainter = Painters.mobSpawner()

// paints the chest
def chestPainter = Painters.brush(BrushGeometries.point(0, 1, 0), BlockPalettes.block(Blocks.CHEST))

// paints loot into the chest
def lootPainter = Painters.loot(resource("minecraft:chests/simple_dungeon")).offset(0, 1, 0)

mods.groovyworld.featureRegistry.addFeature("dungeon", GenSlot.EARLY, new FeatureBuilder()
        .setChunkFilter(overworldOnly & ChunkFilters.randomOneIn(100)) // only one in every 100 chunks gets a dungeon
        .setDistribution(FeatureDistributions.uniformY(8, 56))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(wallPainter + spacePainter + spawnerPainter + chestPainter + lootPainter)
        .build())
