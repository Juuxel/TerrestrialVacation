package juuxel.traversingdimensions.biome

import net.fabricmc.fabric.api.biomes.v1.OverworldClimate
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.layer.type.IdentitySamplingLayer
import net.minecraft.world.biome.layer.util.LayerRandomnessSource

class AddBiomesLayer(biomes: List<Biome>) : IdentitySamplingLayer {
    private val dryBiomes: List<Biome> = biomes.filter { Climates.hasClimate(it, OverworldClimate.DRY) }
    private val temperateBiomes: List<Biome> = biomes.filter { Climates.hasClimate(it, OverworldClimate.TEMPERATE) }
    private val coolBiomes: List<Biome> = biomes.filter { Climates.hasClimate(it, OverworldClimate.COOL) }
    private val snowyBiomes: List<Biome> = biomes.filter { Climates.hasClimate(it, OverworldClimate.SNOWY) }

    override fun sample(context: LayerRandomnessSource, value: Int): Int =
        when (value) {
            1 -> Registry.BIOME.getRawId(dryBiomes[context.nextInt(dryBiomes.size)])
            2 -> Registry.BIOME.getRawId(temperateBiomes[context.nextInt(temperateBiomes.size)])
            3 -> Registry.BIOME.getRawId(coolBiomes[context.nextInt(coolBiomes.size)])
            4 -> Registry.BIOME.getRawId(snowyBiomes[context.nextInt(snowyBiomes.size)])
            else -> value
        }
}
