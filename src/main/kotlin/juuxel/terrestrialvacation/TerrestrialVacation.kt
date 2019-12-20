package juuxel.terrestrialvacation

import juuxel.terrestrialvacation.config.Config
import juuxel.terrestrialvacation.dimension.TheDimension
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
        val config = Config.load()
        Registry.BIOME.visit { id, biome ->
            if ((id.namespace == "traverse" || id.namespace == "terrestria") && id !in config.blacklistedBiomes) {
                _biomes += biome
            }
        }

        TheDimension.init()
    }
}
