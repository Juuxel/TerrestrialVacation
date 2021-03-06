package juuxel.terrestrialvacation.biome

import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.source.BiomeLayerSampler
import net.minecraft.world.biome.source.BiomeSource
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig

class TerrestrialBiomeSource(biomes: List<Biome>, config: VanillaLayeredBiomeSourceConfig) : BiomeSource(biomes.toSet()) {
    private val biomeSampler: BiomeLayerSampler = TerrestrialBiomeLayers.build(config.seed)

    override fun getBiomeForNoiseGen(biomeX: Int, biomeY: Int, biomeZ: Int): Biome =
        biomeSampler.sample(biomeX, biomeZ)
}
