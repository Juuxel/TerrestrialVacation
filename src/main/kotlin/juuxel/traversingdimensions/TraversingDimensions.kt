package juuxel.traversingdimensions

import dev.vatuu.tesseract.api.DimensionBuilder
import dev.vatuu.tesseract.api.DimensionRegistry
import juuxel.traversingdimensions.biome.TDBiomeSource
import juuxel.traversingdimensions.config.Config
import juuxel.traversingdimensions.util.visit
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
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
                    .visibleSky(true)
                    .fogColour { skyAngle, tickDelta ->
                        // Copied from OverworldDimension

                        var f = MathHelper.cos(skyAngle * 6.2831855f) * 2.0f + 0.5f
                        f = MathHelper.clamp(f, 0.0f, 1.0f)
                        var red = 0.7529412
                        var green = 0.84705883
                        var blue = 1.0
                        red *= f * 0.94 + 0.06
                        green *= f * 0.94 + 0.06
                        blue *= f * 0.91 + 0.09

                        Vec3d(red, green, blue)
                    }
                    .chunkGenerator {
                        OverworldChunkGenerator(
                            it,
                            TDBiomeSource(biomes, VanillaLayeredBiomeSourceConfig(it.levelProperties)),
                            OverworldChunkGeneratorConfig()
                        )
                    }
                    .build(world, type)
            },
            HorizontalVoronoiBiomeAccessType.INSTANCE
        )
    }
}
