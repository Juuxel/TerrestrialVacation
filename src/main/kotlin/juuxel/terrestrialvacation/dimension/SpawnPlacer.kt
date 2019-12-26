package juuxel.terrestrialvacation.dimension

import juuxel.terrestrialvacation.component.DimensionalSpawnPoints
import net.fabricmc.fabric.api.dimension.v1.EntityPlacer
import net.minecraft.block.pattern.BlockPattern
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

/**
 * An [EntityPlacer] that places players at their spawn position and other entities like [TopPlacer].
 */
object SpawnPlacer : EntityPlacer {
    override fun placeEntity(
        teleported: Entity, destination: ServerWorld, portalDir: Direction,
        horizontalOffset: Double, verticalOffset: Double
    ): BlockPattern.TeleportTarget {
        return if (teleported is PlayerEntity) {
            val pos = DimensionalSpawnPoints[teleported, destination.dimension.type] ?: destination.spawnPos
            BlockPattern.TeleportTarget(Vec3d(pos), Vec3d.ZERO, 0)
        } else {
            TopPlacer.placeEntity(teleported, destination, portalDir, horizontalOffset, verticalOffset)
        }
    }
}
