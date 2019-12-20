package juuxel.traversingdimensions.biome

import net.fabricmc.fabric.impl.biome.InternalBiomeUtils
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.layer.type.IdentitySamplingLayer
import net.minecraft.world.biome.layer.util.LayerRandomnessSource

object AddVariantsLayer : IdentitySamplingLayer {
    override fun sample(context: LayerRandomnessSource, value: Int) =
        InternalBiomeUtils.transformBiome(context, Registry.BIOME[value], null)
}
