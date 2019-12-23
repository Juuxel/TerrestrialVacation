package juuxel.terrestrialvacation

import juuxel.terrestrialvacation.biome.Climates
import juuxel.terrestrialvacation.biome.RemoveUnwantedTheHallowBiomesLayer
import juuxel.terrestrialvacation.config.Config
import juuxel.terrestrialvacation.dimension.BiomeRiftDimension
import juuxel.terrestrialvacation.lib.Lib
import juuxel.terrestrialvacation.util.visit
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome

object TerrestrialVacation {
    const val ID: String = "terrestrial_vacation"

    internal val biomes: List<Biome> get() = _biomes
    private val _biomes: MutableList<Biome> = ArrayList()

    fun id(path: String): Identifier =
        Identifier(ID, path)

    fun init() {
        Registry.BIOME.visit { id, biome, rawId ->
            if (isValidBaseBiomeId(id)) {
                _biomes += biome
                Climates.checkSpecialCase(id, biome)
            } else if (id.namespace == "thehallow") {
                RemoveUnwantedTheHallowBiomesLayer.add(rawId)
            }
        }

        Lib.init()
        BiomeRiftDimension.init()
    }

    fun isValidBaseBiomeId(id: Identifier?): Boolean =
        id != null
            && isTerraformerBiome(id)
            && id !in Config.INSTANCE.blacklistedBaseBiomes

    fun isTerraformerBiome(id: Identifier?): Boolean =
        id != null
            && (id.namespace == "traverse" || id.namespace == "terrestria")

    fun isOtherBiome(id: Identifier?): Boolean =
        id != null && id.namespace != "traverse" && id.namespace != "terrestria"
}
