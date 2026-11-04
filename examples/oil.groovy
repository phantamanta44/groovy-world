import st.evening.mc.groovyworld.api.BlockFilters
import st.evening.mc.groovyworld.api.BlockPalettes
import st.evening.mc.groovyworld.api.BrushHelper
import st.evening.mc.groovyworld.api.ChunkFilters
import st.evening.mc.groovyworld.api.FeatureBuilder
import st.evening.mc.groovyworld.api.FeatureDistributions
import st.evening.mc.groovyworld.api.Painter
import st.evening.mc.groovyworld.api.Painters
import st.evening.mc.groovyworld.api.RNGHelper
import st.evening.mc.groovyworld.api.ScanHelper

/*
 * This example script generates BuildCraft-style oil wells with geysers reaching up to the surface.
 *
 * A funny thing about Thermal Foundation oil is that you can't actually swim in it, so falling into one of these
 * geysers would be quite bad.
 */

// chunk filter that prevents generation in the nether or end
def overworldOnly = ~(ChunkFilters.dimension(1) | ChunkFilters.dimension(-1))

// block palette for the oil itself
def oilPalette = BlockPalettes.block(block("thermalfoundation:fluid_crude_oil"))

// block palette for the outer shell of the well, which should be a solid block that contains the oil
def wellWallPalette = BlockPalettes.builder()
        .addState(80, blockstate("thermalfoundation:ore_fluid:type=crude_oil_gravel"))
        .addBlock(20, Blocks.COAL_ORE)
        .build()

// paints an underground oil well
Painter wellPainter = { rng, world, paintPos ->
    // generate the dimensions of the well
    def radiusXZ = RNGHelper.sampleUniform(rng, 4, 8)
    def radiusY = RNGHelper.sampleUniform(rng, 3, 10)
    // convenient constants for the check inside the loop
    def rxz2 = (radiusXZ - 1) * (radiusXZ - 1)
    def ry2 = (radiusY - 1) * (radiusY - 1)
    // place the blocks for the well
    for (def pos in BrushHelper.createEllipsoid(paintPos, radiusXZ, radiusY, radiusXZ)) {
        // check whether it's a block on the outer shell or not
        def dx = pos.x - paintPos.x, dy = pos.y - paintPos.y, dz = pos.z - paintPos.z
        if (dx * dx / rxz2 + dy * dy / ry2 + dz * dz / rxz2 < 1) {
            // it's inside; place oil
            world.setBlockState(pos, oilPalette.pickBlock(rng, world, pos))
        } else {
            // it's outside; place stone to contain the oil
            world.setBlockState(pos, wellWallPalette.pickBlock(rng, world, pos))
        }
    }
    return true
}

// paints an oil geyser going up to the surface
Painter geyserPainter = Painters.brush(
        { rng, world, pos ->
            // find the Y-coordinate at the surface above the well
            def surfaceY = ScanHelper.findSurfaceY(rng, world, pos.x, pos.z, ~BlockFilters.replaceable())
            // compute the height of the geyser, which should get it up to the surface and a bit over it
            def colHeight = surfaceY - pos.y + RNGHelper.sampleUniformInt(rng, 3, 8)
            // paint a column of blocks of that height
            return BrushHelper.createColumn(pos, EnumFacing.UP, colHeight)
        },
        // no block filter: the geyser will cut through anything in its path
        oilPalette)

mods.groovyworld.featureRegistry.addFeature("oil_well", new FeatureBuilder()
        .setChunkFilter(overworldOnly & ChunkFilters.randomOneIn(250)) // only one in every 250 chunks gets an oil well
        .setTriesPerChunk(4, 1) // try four times, but only successfully generate at most one per chunk
        .setDistribution(FeatureDistributions.uniformY(16, 32))
        .setBlockFilter(BlockFilters.naturalStone())
        .setPainter(wellPainter.then(geyserPainter)) // geyser painter only runs if the well painter succeeds
        .build())
