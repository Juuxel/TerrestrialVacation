package juuxel.terrestrialvacation.block

import juuxel.terrestrialvacation.dimension.BiomeRiftDimension
import juuxel.terrestrialvacation.dimension.TopPlacer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import java.util.*

class BiomeRiftPortalBlock(settings: Settings) : Block(settings) {
    override fun onUse(
        state: BlockState, world: World, pos: BlockPos,
        player: PlayerEntity, hand: Hand, hit: BlockHitResult
    ): ActionResult {
        if (!world.isClient) {
            if (world.dimension.type !== BiomeRiftDimension.TYPE) {
                FabricDimensions.teleport(player, BiomeRiftDimension.TYPE)
            } else {
                FabricDimensions.teleport(player, DimensionType.OVERWORLD, TopPlacer)
            }
        }
        return ActionResult.SUCCESS
    }

    @Environment(EnvType.CLIENT)
    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        for (i in 1..(random.nextInt(3) + 3)) {
            world.addParticle(
                ParticleTypes.PORTAL,
                pos.x + random.nextDouble(),
                pos.y + 1.0,
                pos.z + random.nextDouble(),
                0.0, 1.0, 0.0
            )
        }
    }
}
