package juuxel.terrestrialvacation

import juuxel.terrestrialvacation.biome.Climates
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
        Registry.BIOME.visit { id, biome ->
            if (isValidBaseBiomeId(id)) {
                _biomes += biome
            }
        }

        Lib.init()
        BiomeRiftDimension.init()
        Climates.registerSpecialCases()
    }

    fun isValidBaseBiomeId(id: Identifier?): Boolean =
        id != null
            && (id.namespace == "traverse" || id.namespace == "terrestria")
            && id !in Config.INSTANCE.blacklistedBaseBiomes

    fun isOtherBiome(id: Identifier?): Boolean =
        id != null && id.namespace != "traverse" && id.namespace != "terrestria"
}
