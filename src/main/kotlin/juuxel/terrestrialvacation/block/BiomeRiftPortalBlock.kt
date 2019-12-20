package juuxel.terrestrialvacation.block

import juuxel.terrestrialvacation.dimension.BiomeRiftDimension
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityContext
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class BiomeRiftPortalBlock(settings: Settings) : Block(settings) {
    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        if (!world.isClient && world.dimension.type !== BiomeRiftDimension.TYPE) {
            FabricDimensions.teleport(entity, BiomeRiftDimension.TYPE)
        }
    }

    override fun getOutlineShape(state: BlockState, view: BlockView, pos: BlockPos, ePos: EntityContext) = SHAPE

    companion object {
        private val SHAPE: VoxelShape = createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0)
    }
}
