package juuxel.traversingdimensions

import dev.vatuu.tesseract.api.DimensionBuilder
import dev.vatuu.tesseract.api.DimensionRegistry
import juuxel.traversingdimensions.biome.TDBiomeSource
import juuxel.traversingdimensions.config.Config
import juuxel.traversingdimensions.util.visit
import net.minecraft.SharedConstants
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig
import net.minecraft.world.gen.chunk.OverworldChunkGenerator
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig
import java.util.function.BiFunction

object TraversingDimensions {
    const val ID: String = "traversing_dimensions"

    fun id(path: String): Identifier =
        Identifier(ID, path)

    fun init() {
        val config = Config.load()

        SharedConstants.isDevelopment = true
        val biomes = ArrayList<Biome>()

        Registry.BIOME.visit { id, biome ->
            if ((id.namespace == "traverse" || id.namespace == "terrestria") && id !in config.blacklistedBiomes) {
                biomes += biome
            }
        }

        DimensionRegistry.getInstance().registerDimensionType(
            id("dimension"),
            true,
            BiFunction { world, type ->
                DimensionBuilder.create()
                    .chunkGenerator { OverworldChunkGenerator(it, TDBiomeSource(biomes, VanillaLayeredBiomeSourceConfig(it.levelProperties)), OverworldChunkGeneratorConfig()) }
                    .build(world, type)
            },
            HorizontalVoronoiBiomeAccessType.INSTANCE
        )
    }
}
