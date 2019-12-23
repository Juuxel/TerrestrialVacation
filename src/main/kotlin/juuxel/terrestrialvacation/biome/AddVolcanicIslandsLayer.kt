package juuxel.terrestrialvacation.biome

import com.terraformersmc.terrestria.init.TerrestriaBiomes
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.layer.type.IdentitySamplingLayer
import net.minecraft.world.biome.layer.util.LayerRandomnessSource

object AddVolcanicIslandsLayer : IdentitySamplingLayer {
    private val DEEP_OCEAN_ID = Registry.BIOME.getRawId(Biomes.DEEP_OCEAN)
    private val VOLCANIC_SHORE_ID = Registry.BIOME.getRawId(TerrestriaBiomes.VOLCANIC_ISLAND_SHORE)

    override fun sample(context: LayerRandomnessSource, value: Int) =
        if (value == DEEP_OCEAN_ID && context.nextInt(30) == 0) VOLCANIC_SHORE_ID
        else value
}
