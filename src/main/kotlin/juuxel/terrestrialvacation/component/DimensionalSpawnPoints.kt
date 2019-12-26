package juuxel.terrestrialvacation.component

import juuxel.terrestrialvacation.lib.Lib
import nerdhub.cardinal.components.api.component.Component
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.dimension.DimensionType
import org.apache.logging.log4j.LogManager

interface DimensionalSpawnPoints : Component {
    operator fun get(dimension: DimensionType): BlockPos?
    operator fun set(dimension: DimensionType, pos: BlockPos)

    operator fun contains(dimension: DimensionType): Boolean

    companion object {
        operator fun invoke(): DimensionalSpawnPoints = Impl()

        /**
         * Gets the dimensional spawn point of the [player] in the [dimension].
         * Returns `null` if the spawn position is not present.
         */
        @JvmStatic
        operator fun get(player: PlayerEntity, dimension: DimensionType, ignoreOverworldDefault: Boolean = false): BlockPos? {
            val spawnPos = Lib.DIMENSIONAL_SPAWN_POINTS[player][dimension]

            return if (spawnPos == null && dimension === DimensionType.OVERWORLD && !ignoreOverworldDefault) player.spawnPosition
            else spawnPos
        }

        /**
         * Sets the dimensional [spawn point][spawnPos] of the [player] in the [dimension].
         */
        @JvmStatic
        operator fun set(player: PlayerEntity, dimension: DimensionType, spawnPos: BlockPos) {
            Lib.DIMENSIONAL_SPAWN_POINTS[player][dimension] = spawnPos
        }

        @JvmStatic
        fun hasSpawnPointIn(player: PlayerEntity, dimension: DimensionType): Boolean =
            dimension in Lib.DIMENSIONAL_SPAWN_POINTS[player]
    }

    private class Impl : DimensionalSpawnPoints {
        private val spawnPoints: MutableMap<DimensionType, BlockPos> = hashMapOf()

        override fun get(dimension: DimensionType) = spawnPoints[dimension]

        override fun set(dimension: DimensionType, pos: BlockPos) {
            spawnPoints[dimension] = pos
        }

        override fun contains(dimension: DimensionType) = dimension in spawnPoints

        override fun fromTag(tag: CompoundTag) {
            spawnPoints.clear()
            for (key in tag.keys) {
                val id = Identifier.tryParse(key) ?: continue
                val dimension = Registry.DIMENSION[id] ?: continue
                if (!tag.contains(key, NbtType.LONG)) {
                    LOGGER.warn("[TerrestrialVacation] Found invalid value for dimension '$id': ${tag[key]}")
                }

                val pos = BlockPos.fromLong(tag.getLong(key))
                spawnPoints[dimension] = pos
            }
        }

        override fun toTag(tag: CompoundTag) = tag.apply {
            for ((dimension, pos) in spawnPoints) {
                putLong(Registry.DIMENSION.getId(dimension).toString(), pos.asLong())
            }
        }

        companion object {
            private val LOGGER = LogManager.getLogger()
        }
    }
}
