package juuxel.traversingdimensions

import juuxel.traversingdimensions.config.Config
import juuxel.traversingdimensions.dimension.TheDimension
import juuxel.traversingdimensions.util.visit
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome

object TraversingDimensions {
    const val ID: String = "traversing_dimensions"

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
