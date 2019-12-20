package juuxel.terrestrialvacation.biome

import com.terraformersmc.traverse.biome.TraverseBiomes
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.layer.type.ParentedLayer
import net.minecraft.world.biome.layer.util.IdentityCoordinateTransformer
import net.minecraft.world.biome.layer.util.LayerSampleContext
import net.minecraft.world.biome.layer.util.LayerSampler

object SetSpawnBiomeLayer : ParentedLayer, IdentityCoordinateTransformer {
    override fun sample(context: LayerSampleContext<*>, parent: LayerSampler, x: Int, z: Int) =
        if (x == 0 && z == 0) Registry.BIOME.getRawId(TraverseBiomes.MEADOW)
        else parent.sample(x, z)
}
