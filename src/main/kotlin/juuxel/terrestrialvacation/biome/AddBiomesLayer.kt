package juuxel.terrestrialvacation.biome

import net.fabricmc.fabric.api.biomes.v1.OverworldClimate
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.layer.type.IdentitySamplingLayer
import net.minecraft.world.biome.layer.util.LayerRandomnessSource

object AddBiomesLayer : IdentitySamplingLayer {
    override fun sample(context: LayerRandomnessSource, value: Int): Int =
        when (value) {
            1 -> Registry.BIOME.getRawId(Climates.getPicker(OverworldClimate.DRY).pickRandom(context))
            2 -> Registry.BIOME.getRawId(Climates.getPicker(OverworldClimate.TEMPERATE).pickRandom(context))
            3 -> Registry.BIOME.getRawId(Climates.getPicker(OverworldClimate.COOL).pickRandom(context))
            4 -> Registry.BIOME.getRawId(Climates.getPicker(OverworldClimate.SNOWY).pickRandom(context))
            else -> value
        }
}
