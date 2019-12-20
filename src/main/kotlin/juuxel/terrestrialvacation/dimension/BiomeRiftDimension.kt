package juuxel.terrestrialvacation.dimension

import juuxel.terrestrialvacation.TerrestrialVacation
import juuxel.terrestrialvacation.biome.TerrestrialBiomeSource
import juuxel.terrestrialvacation.lib.Lib
import juuxel.terrestrialvacation.util.orNull
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType
import net.minecraft.block.Blocks
import net.minecraft.block.pattern.BlockPattern
import net.minecraft.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.village.PointOfInterest
import net.minecraft.village.PointOfInterestStorage
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
                val poiStorage = destination.pointOfInterestStorage
                poiStorage.method_22439(destination, teleported.blockPos, 128)

                val pointsOfInterest = poiStorage.method_22383(
                    { it == Lib.BIOME_RIFT_PORTAL_POI },
                    teleported.blockPos,
                    128,
                    PointOfInterestStorage.OccupationStatus.ANY
                )
                val poi = pointsOfInterest.min(
                    Comparator.comparingDouble<PointOfInterest> { it.pos.getSquaredDistance(teleported.blockPos) }
                        .thenComparingInt { it.pos.y }
                ).orNull()

                if (poi == null) {
                    placePortal(destination, teleported.x, teleported.z)
                } else {
                    BlockPattern.TeleportTarget(Vec3d(poi.pos.up()), Vec3d(3.0, 2.0, 0.0), 0)
                }
            }
            .buildAndRegister(TerrestrialVacation.id("biome_rift"))

        fun init() {

        }

        private fun placePortal(world: World, entityX: Double, entityZ: Double): BlockPattern.TeleportTarget {
            return BlockPos.PooledMutable.get(entityX, world.height - 1.0, entityZ).use { pos ->
                while (world.isAir(pos) && pos.y > 0) {
                    pos.setOffset(Direction.DOWN)
                }

                fun checkGround(pos: BlockPos) {
                    if (!world.getBlockState(pos).isFullCube(world, pos)) {
                        world.setBlockState(pos, world.getBiome(pos).surfaceConfig.underMaterial)
                    }
                }
                checkGround(BlockPos(pos.x, pos.y - 1, pos.z))
                checkGround(BlockPos(pos.x + 1, pos.y - 1, pos.z))
                checkGround(BlockPos(pos.x, pos.y - 1, pos.z + 1))
                checkGround(BlockPos(pos.x + 1, pos.y - 1, pos.z + 1))

                val p = BlockPos.Mutable(pos.x - 1, pos.y, pos.z - 1)
                for (x in 0..3) {
                    for (z in 0..3) {
                        if (x == 0 || x == 3 || z == 0 || z == 3) {
                            world.setBlockState(p, Blocks.GRASS_BLOCK.defaultState)
                            p.setOffset(Direction.UP)
                            world.setBlockState(p, BlockTags.SMALL_FLOWERS.getRandom(world.random).defaultState)
                            p.setOffset(Direction.DOWN)
                        } else {
                            world.setBlockState(p, Lib.BIOME_RIFT_PORTAL.defaultState)
                        }
                        p.setOffset(Direction.SOUTH)
                    }
                    p.setOffset(Direction.EAST)
                    p.setOffset(Direction.NORTH, 4)
                }
                for (i in 0..1) {
                    p.setOffset(Direction.UP)
                    for (x in 0..3) {
                        for (z in 0..3) {
                            if (i != 0 || !(x == 0 || x == 3 || z == 0 || z == 3)) {
                                world.setBlockState(p, Blocks.AIR.defaultState)
                            }

                            p.setOffset(Direction.NORTH)
                        }
                        p.setOffset(Direction.WEST)
                        p.setOffset(Direction.SOUTH, 4)
                    }
                }

                BlockPattern.TeleportTarget(Vec3d(pos.setOffset(-1, 1, 0)), Vec3d.ZERO, 0)
            }
        }
    }
}
