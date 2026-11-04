import st.evening.mc.groovyworld.api.BlockFilters
import st.evening.mc.groovyworld.api.BlockPalettes
import st.evening.mc.groovyworld.api.BrushGeometryHelper
import st.evening.mc.groovyworld.api.BrushHelper
import st.evening.mc.groovyworld.api.ChunkFilters
import st.evening.mc.groovyworld.api.FeatureBuilder
import st.evening.mc.groovyworld.api.FeatureDistributions
import st.evening.mc.groovyworld.api.Painters
import st.evening.mc.groovyworld.api.RNGHelper
import st.evening.mc.groovyworld.api.ScanHelper

/*
 * This example script generates big ore veins with flower patches on the surface indicating their location.
 *
 * The ore veins with the default settings given below can be quite large, so they're liable to cause cascading world
 * generation if they happen to generate too close to a chunk border. As such, you probably shouldn't make them generate
 * too frequently.
 */

// chunk filter that prevents generation in the nether or end
def overworldOnly = ~(ChunkFilters.dimension(1) | ChunkFilters.dimension(-1))

// paints a big ore vein
def orePainter = Painters.brush(
        { rng, _, pos -> // custom geometry that brushes a randomly-sized ellipsoid
            BrushHelper.createEllipsoid(pos,
                    RNGHelper.sampleUniform(rng, 8, 14), // WARNING: this is big enough to cause cascading worldgen!
                    RNGHelper.sampleUniform(rng, 5, 7),
                    RNGHelper.sampleUniform(rng, 8, 14))
        },
        BlockFilters.naturalStone(),
        BlockPalettes.blocks(Blocks.REDSTONE_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE))

// paints a flower patch on the surface above the ore vein
def flowerPainter = Painters.brush(
        { rng, world, pos -> // custom geometry that brushes scattered points on the surface above the target position
            // find the corresponding position on the surface
            def surfacePos = ScanHelper.findSurfacePos(rng, world, pos.x, pos.z, ~BlockFilters.surfaceDecoration())
            // brush a cylinder around the position
            def region = BrushHelper.createCylinder(surfacePos.down(3), EnumFacing.UP, 7, 5)
            // filter the cylinder to positions on the ground and randomly delete a few to create a scattered pattern
            return region.filter { p ->
                BrushGeometryHelper.isOnSurface(rng, world, p, BlockFilters.blocks(Blocks.GRASS, Blocks.DIRT))
                        && RNGHelper.bernoulli(rng, 0.25) // only keep 25% of the valid positions
            }
        },
        // note that there's no block filter here because the filtering is already done in the geometry
        BlockPalettes.states(
                blockstate("minecraft:yellow_flower:type=dandelion"),
                blockstate("minecraft:red_flower:type=poppy"),
                blockstate("minecraft:red_flower:type=blue_orchid")))

mods.groovyworld.featureRegistry.addFeature("big_ore_vein", new FeatureBuilder()
        .setChunkFilter(overworldOnly & ChunkFilters.randomOneIn(200)) // only one in every 200 chunks gets a big vein
        .setTriesPerChunk(4, 1) // try four times, but only successfully generate at most one per chunk
        .setDistribution(FeatureDistributions.uniformY(12, 20))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(orePainter.then(flowerPainter)) // flower painter only runs if the ore painter succeeds
        .build())
