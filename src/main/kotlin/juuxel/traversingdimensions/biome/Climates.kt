package juuxel.traversingdimensions.biome

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import net.fabricmc.fabric.api.biomes.v1.OverworldClimate
import net.minecraft.world.biome.Biome

object Climates {
    private val climates: Multimap<OverworldClimate, Biome> =
        MultimapBuilder
            .enumKeys(OverworldClimate::class.java)
            .arrayListValues()
            .build<OverworldClimate, Biome>()

    fun hasClimate(biome: Biome, climate: OverworldClimate): Boolean =
        climates[climate]?.contains(biome) == true

    @JvmStatic fun set(biome: Biome, climate: OverworldClimate) {
        climates.put(climate, biome)
    }
}
