package juuxel.terrestrialvacation.dimension

import net.fabricmc.fabric.api.dimension.v1.EntityPlacer
import net.minecraft.block.pattern.BlockPattern
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

/**
 * An [EntityPlacer] that places entities on the topmost non-air block.
 */
object TopPlacer : EntityPlacer {
    override fun placeEntity(
        teleported: Entity, destination: ServerWorld, portalDir: Direction,
        horizontalOffset: Double, verticalOffset: Double
    ): BlockPattern.TeleportTarget {
        return BlockPos.PooledMutable.get(teleported.x, destination.height - 1.0, teleported.z).use { pos ->
            while (destination.isAir(pos) && pos.y > 0) {
                pos.setOffset(Direction.DOWN)
            }
            pos.setOffset(Direction.UP) // Move on top of the block

            BlockPattern.TeleportTarget(Vec3d(pos), Vec3d.ZERO, 0)
        }
    }
}
