package juuxel.terrestrialvacation.dimension

import juuxel.terrestrialvacation.TerrestrialVacation
import juuxel.terrestrialvacation.biome.TerrestrialBiomeSource
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType
import net.minecraft.world.World
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.dimension.OverworldDimension
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig
import net.minecraft.world.gen.chunk.OverworldChunkGenerator
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig

class BiomeRiftDimension(world: World, private val type: DimensionType) : OverworldDimension(world, type) {
    override fun getType() = type

    override fun createChunkGenerator(): ChunkGenerator<out ChunkGeneratorConfig> =
        OverworldChunkGenerator(
            world,
            TerrestrialBiomeSource(TerrestrialVacation.biomes, VanillaLayeredBiomeSourceConfig(world.levelProperties)),
            OverworldChunkGeneratorConfig()
        )

    companion object {
        @JvmField
        val TYPE: DimensionType = FabricDimensionType.builder()
            .factory(::BiomeRiftDimension)
            .biomeAccessStrategy(HorizontalVoronoiBiomeAccessType.INSTANCE)
            .defaultPlacer(TopPlacer)
            .buildAndRegister(TerrestrialVacation.id("biome_rift"))

        fun init() {

        }
    }
}
