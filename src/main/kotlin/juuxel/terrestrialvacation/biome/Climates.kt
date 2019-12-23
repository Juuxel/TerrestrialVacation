package juuxel.terrestrialvacation.biome

import com.terraformersmc.terrestria.init.TerrestriaBiomes
import juuxel.terrestrialvacation.biome.fabric.WeightedBiomePicker
import net.fabricmc.fabric.api.biomes.v1.OverworldClimate
import net.minecraft.world.biome.Biome
import java.util.*

object Climates {
    private val pickers: EnumMap<OverworldClimate, WeightedBiomePicker> = EnumMap(OverworldClimate::class.java)

    fun add(biome: Biome, climate: OverworldClimate, weight: Double) {
        pickers.getOrPut(climate) { WeightedBiomePicker() }.addBiome(biome, weight)
    }

    fun getPicker(climate: OverworldClimate) =
        pickers[climate] ?: throw IllegalArgumentException("No picker found for climate '$climate'")

    fun registerSpecialCases() {
        add(TerrestriaBiomes.CALDERA_RIDGE, OverworldClimate.COOL, 0.25)
        add(TerrestriaBiomes.RAINBOW_RAINFOREST, OverworldClimate.TEMPERATE, 0.25)
    }
}
