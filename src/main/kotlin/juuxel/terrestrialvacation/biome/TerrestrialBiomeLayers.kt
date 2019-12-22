package juuxel.terrestrialvacation.biome

import juuxel.terrestrialvacation.util.stack
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.layer.*
import net.minecraft.world.biome.layer.util.CachingLayerContext
import net.minecraft.world.biome.layer.util.LayerFactory
import net.minecraft.world.biome.layer.util.LayerSampleContext
import net.minecraft.world.biome.layer.util.LayerSampler
import net.minecraft.world.biome.source.BiomeLayerSampler
import java.util.function.LongFunction

object TerrestrialBiomeLayers {
    private fun <R : LayerSampler, T : LayerSampleContext<R>> create(contextProvider: LongFunction<T>, biomeList: List<Biome>): LayerFactory<R> {
        var layer: LayerFactory<R> = ContinentLayer.INSTANCE.create(contextProvider.apply(1L))
        layer = ScaleLayer.FUZZY.create(contextProvider.apply(100L), layer)
        layer = IncreaseEdgeCurvatureLayer.INSTANCE.create(contextProvider.apply(1L), layer)
        layer = ScaleLayer.NORMAL.create(contextProvider.apply(2000L), layer)
        layer = IncreaseEdgeCurvatureLayer.INSTANCE.stack(3, contextProvider, 20L, layer)
        layer = AddIslandLayer.INSTANCE.create(contextProvider.apply(1L), layer)
        layer = ScaleLayer.NORMAL.stack(3, contextProvider, 10L, layer)

        var oceanTemperature: LayerFactory<R> = OceanTemperatureLayer.INSTANCE.create(contextProvider.apply(10L))
        oceanTemperature = ScaleLayer.NORMAL.stack(6, contextProvider, 2001L, oceanTemperature)

//        layer = AddColdClimatesLayer.INSTANCE.create(contextProvider.apply(2L), layer)
        layer = IncreaseEdgeCurvatureLayer.INSTANCE.create(contextProvider.apply(3L), layer)
//        layer = AddClimateLayers.AddTemperateBiomesLayer.INSTANCE.create(contextProvider.apply(2L), layer)
//        layer = AddClimateLayers.AddCoolBiomesLayer.INSTANCE.create(contextProvider.apply(2L), layer)
//        layer = AddClimateLayers.AddSpecialBiomesLayer.INSTANCE.create(contextProvider.apply(2L), layer)
        layer = ScaleLayer.NORMAL.stack(2, contextProvider, 2002L, layer)
        layer = IncreaseEdgeCurvatureLayer.INSTANCE.create(contextProvider.apply(4L), layer)
        layer = AddDeepOceanLayer.INSTANCE.create(contextProvider.apply(4L), layer)

        var biomes: LayerFactory<R> = AddBiomesLayer(biomeList).create(contextProvider.apply(20L), layer)
        biomes = ScaleLayer.NORMAL.stack(2, contextProvider, 1000L, biomes)
        biomes = AddVariantsLayer.create(contextProvider.apply(30L), biomes)
        biomes = EaseBiomeEdgeLayer.INSTANCE.create(contextProvider.apply(1000L), biomes)
        biomes = AddHillsLayer.INSTANCE.create(contextProvider.apply(100L), biomes, ScaleLayer.NORMAL.stack(2, contextProvider, 1000L, biomes))
        biomes = AddEdgeBiomesLayer.INSTANCE.create(contextProvider.apply(100L), biomes)

        for (i in 0..3) {
            biomes = ScaleLayer.NORMAL.create(contextProvider.apply(1000L + i), biomes)
            if (i == 0) {
                biomes = IncreaseEdgeCurvatureLayer.INSTANCE.create(contextProvider.apply(3L), biomes)
            } else if (i == 1) {
                biomes = AddEdgeBiomesLayer.INSTANCE.create(contextProvider.apply(1000L), biomes)
            }
        }

        var rivers: LayerFactory<R> = SimpleLandNoiseLayer.INSTANCE.create(contextProvider.apply(200L), layer)
        rivers = ScaleLayer.NORMAL.stack(4, contextProvider, 1000L, rivers)
        rivers = NoiseToRiverLayer.INSTANCE.create(contextProvider.apply(250L), rivers)
        rivers = ScaleLayer.NORMAL.stack(3, contextProvider, 3000L, rivers)
        rivers = SmoothenShorelineLayer.INSTANCE.create(contextProvider.apply(1000L), rivers)

        biomes = SmoothenShorelineLayer.INSTANCE.create(contextProvider.apply(1000L), biomes)
        biomes = AddRiversLayer.INSTANCE.create(contextProvider.apply(102L), biomes, rivers)
        biomes = IncreaseEdgeCurvatureLayer.INSTANCE.stack(3, contextProvider, 7L, biomes)
        biomes = ApplyOceanTemperatureLayer.INSTANCE.create(contextProvider.apply(100L), biomes, oceanTemperature)
        return biomes
    }

    fun build(biomes: List<Biome>, seed: Long): BiomeLayerSampler =
        BiomeLayerSampler(create(LongFunction { CachingLayerContext(25, seed, it) }, biomes))
}
