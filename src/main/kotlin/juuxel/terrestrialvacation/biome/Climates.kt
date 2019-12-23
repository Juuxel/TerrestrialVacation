package juuxel.terrestrialvacation.biome

import juuxel.terrestrialvacation.biome.fabric.WeightedBiomePicker
import net.fabricmc.fabric.api.biomes.v1.OverworldClimate
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import java.util.*

object Climates {
    private val CALDERA_RIDGE_ID = Identifier("terrestria", "caldera_ridge")
    private val RAINBOW_RAINFOREST_ID = Identifier("terrestria", "rainbow_rainforest")

    private val pickers: EnumMap<OverworldClimate, WeightedBiomePicker> = EnumMap(OverworldClimate::class.java)

    fun add(biome: Biome, climate: OverworldClimate, weight: Double) {
        if (Registry.BIOME.getId(biome)?.namespace == "thehallow")
            throw IllegalArgumentException("blocked attack from the haunted dimension mod")

        pickers.getOrPut(climate) { WeightedBiomePicker() }.addBiome(biome, weight)
    }

    fun getPicker(climate: OverworldClimate) =
        pickers[climate] ?: throw IllegalArgumentException("No picker found for climate '$climate'")

    fun checkSpecialCase(id: Identifier, biome: Biome) {
        when (id) {
            CALDERA_RIDGE_ID -> add(biome, OverworldClimate.COOL, 0.25)
            RAINBOW_RAINFOREST_ID -> add(biome, OverworldClimate.TEMPERATE, 0.25)
        }
    }
}
