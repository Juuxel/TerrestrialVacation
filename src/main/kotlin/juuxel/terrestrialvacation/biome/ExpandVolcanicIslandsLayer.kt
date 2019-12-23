package juuxel.terrestrialvacation.biome

import com.terraformersmc.terrestria.init.TerrestriaBiomes
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.layer.type.CrossSamplingLayer
import net.minecraft.world.biome.layer.util.LayerRandomnessSource

object ExpandVolcanicIslandsLayer : CrossSamplingLayer {
    private val VOLCANIC_SHORE_ID = Registry.BIOME.getRawId(TerrestriaBiomes.VOLCANIC_ISLAND_SHORE)

    override fun sample(context: LayerRandomnessSource, n: Int, e: Int, s: Int, w: Int, center: Int): Int {
        val hasNeighboringShore =
            n == VOLCANIC_SHORE_ID
                || e == VOLCANIC_SHORE_ID
                || s == VOLCANIC_SHORE_ID
                || w == VOLCANIC_SHORE_ID

        return if (hasNeighboringShore && context.nextInt(5) == 0) {
            VOLCANIC_SHORE_ID
        } else {
            center
        }
    }
}
