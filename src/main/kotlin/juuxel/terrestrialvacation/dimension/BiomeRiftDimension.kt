package juuxel.terrestrialvacation.dimension

import juuxel.terrestrialvacation.TerrestrialVacation
import juuxel.terrestrialvacation.biome.TerrestrialBiomeSource
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType
import net.minecraft.block.pattern.BlockPattern
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType
import net.minecraft.world.biome.source.VanillaLayeredBiomeSourceConfig
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.dimension.OverworldDimension
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig
import net.minecraft.world.gen.chunk.OverworldChunkGenerator
import net.minecraft.world.gen.chunk.OverworldChunkGeneratorConfig

class BiomeRiftDimension(world: World, private val type: DimensionType) : OverworldDimension(world, type) {
    override fun getType() = type

    override fun createChunkGenerator(): ChunkGenerator<out ChunkGeneratorConfig> =
        OverworldChunkGenerator(
            world,
            TerrestrialBiomeSource(TerrestrialVacation.biomes, VanillaLayeredBiomeSourceConfig(world.levelProperties)),
            OverworldChunkGeneratorConfig()
        )

    companion object {
        @JvmField
        val TYPE: DimensionType = FabricDimensionType.builder()
            .factory(::BiomeRiftDimension)
            .biomeAccessStrategy(HorizontalVoronoiBiomeAccessType.INSTANCE)
            .defaultPlacer { teleported, destination, _, _, _ ->
                BlockPos.PooledMutable.get(teleported.x, destination.height - 1.0, teleported.z).use { pos ->
                    while (destination.isAir(pos) && pos.y > 0) {
                        pos.setOffset(Direction.DOWN)
                    }

                    BlockPattern.TeleportTarget(Vec3d(pos), Vec3d.ZERO, 0)
                }
            }
            .buildAndRegister(TerrestrialVacation.id("biome_rift"))

        fun init() {

        }
    }
}
