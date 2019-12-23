package juuxel.terrestrialvacation.biome

import it.unimi.dsi.fastutil.ints.IntArrayList
import net.minecraft.world.biome.layer.type.DiagonalCrossSamplingLayer
import net.minecraft.world.biome.layer.util.LayerRandomnessSource

object RemoveUnwantedTheHallowBiomesLayer : DiagonalCrossSamplingLayer {
    private val hallowBiomes: IntArrayList = IntArrayList()

    override fun sample(context: LayerRandomnessSource, sw: Int, se: Int, ne: Int, nw: Int, center: Int) =
        if (center in hallowBiomes) {
            System.err.println("FOUND THE HALLOW")
            se
        }
        else center

    fun add(biome: Int) {
        hallowBiomes += biome
    }
}
