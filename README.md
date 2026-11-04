# Groovy World

A collection of tools for defining custom feature generators (ores, surface decorations, etc.) in [GroovyScript](https://github.com/CleanroomMC/GroovyScript).

The main entry point is `mods.groovyworld.featureRegistry`, an instance of [GrWFeatureRegistry](src/main/java/st/evening/mc/groovyworld/worldgen/GrWFeatureRegistry.java) where you can register [GrWFeature](src/main/java/st/evening/mc/groovyworld/worldgen/GrWFeature.java)s for generation:
```groovy
mods.groovyworld.featureRegistry.addFeature("my_cool_feature") { rng, world, chunkMinX, chunkMinZ ->
    // randomly place one diamond ore block somewhere in the chunk
    world.setBlockState(
            new BlockPos(chunkMinX + rng.nextInt(16), rng.nextInt(64), chunkMinZ + rng.nextInt(16)),
            Blocks.DIAMOND_ORE.getDefaultState())
}
```
Writing a closure with your own world generation logic as in the above is the most straightforward way to build a custom feature, but Groovy World also comes with a batteries-included toolkit that abstracts some of the logic away. The primary tool is the [FeatureBuilder](src/main/java/st/evening/mc/groovyworld/api/FeatureBuilder.java), which lets you assemble a bunch of parts into a working feature generator:
```groovy
mods.groovyworld.featureRegistry.addFeature("my_awesome_feature", new FeatureBuilder()
        // only generate the feature in dimension 0 (the overworld)
        .setChunkFilter(ChunkFilters.dimension(0))
        // try to place the feature 4 times per chunk
        .setTriesPerChunk(4) 
        // distribute the feature uniformly between y=4 and y=32
        .setDistribution(FeatureDistributions.uniformY(4, 32))
        // only generate the feature if there is a stone block at the target position
        .setBlockFilter(BlockFilters.naturalStone())
        // paint a sphere of ore at the target position
        .setPainter(Painters.brush(
                BrushGeometries.sphere(2.5), // paint a sphere of radius 2.5
                BlockFilters.naturalStone(), // only paint the ore over stone blocks
                BlockPalettes.block(Blocks.DIAMOND_ORE, Blocks.GOLD_ORE))) // pick either diamond or gold for each ore
        .build())
```
For more information on the toolkit API, check out the Javadocs in [the API package](src/main/java/st/evening/mc/groovyworld/api). For a few example scripts, check out [the examples directory](examples).
