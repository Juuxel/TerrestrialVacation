package juuxel.traversingdimensions.biome

import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.layer.type.IdentitySamplingLayer
import net.minecraft.world.biome.layer.util.LayerRandomnessSource

class AddBiomesLayer(private val biomes: List<Biome>) : IdentitySamplingLayer {
    override fun sample(context: LayerRandomnessSource, value: Int): Int =
        if (value == 1) Registry.BIOME.getRawId(biomes[context.nextInt(biomes.size)])
        else value
}
