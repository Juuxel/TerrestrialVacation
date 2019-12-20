package juuxel.terrestrialvacation.dimension

import juuxel.terrestrialvacation.TerrestrialVacation
import juuxel.terrestrialvacation.biome.TDBiomeSource
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType
import net.minecraft.block.pattern.BlockPattern
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Heightmap
import net.minecraft.world.World
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.dimension.OverworldDimension
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig
import net.minecraft.world.gen.chunk.OverworldChunkGenerator
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig

class TheDimension(world: World, private val type: DimensionType) : OverworldDimension(world, type) {
    override fun getType() = type

    override fun createChunkGenerator(): ChunkGenerator<out ChunkGeneratorConfig> =
        OverworldChunkGenerator(
            world,
            TDBiomeSource(TerrestrialVacation.biomes, VanillaLayeredBiomeSourceConfig(world.levelProperties)),
            OverworldChunkGeneratorConfig()
        )

    companion object {
        val TYPE: DimensionType = FabricDimensionType.builder()
            .defaultPlacer { teleported, destination, _, _, _ ->
                val pos = Vec3d(teleported.x, destination.getTopY(Heightmap.Type.MOTION_BLOCKING, teleported.blockPos.x, teleported.blockPos.z).toDouble(), teleported.z)
                BlockPattern.TeleportTarget(pos, Vec3d.ZERO, 0)
            }
            .biomeAccessStrategy(HorizontalVoronoiBiomeAccessType.INSTANCE)
            .factory(::TheDimension)
            .buildAndRegister(TerrestrialVacation.id("biome_rift"))

        fun init() {

        }
    }
}
