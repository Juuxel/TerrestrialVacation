package juuxel.terrestrialvacation.lib

import com.google.common.collect.ImmutableSet
import juuxel.terrestrialvacation.TerrestrialVacation
import juuxel.terrestrialvacation.block.BiomeRiftPortalBlock
import juuxel.terrestrialvacation.mixin.PointOfInterestTypeAccessor
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.registry.Registry

object Lib {
    val BIOME_RIFT_PORTAL: Block = BiomeRiftPortalBlock(
        FabricBlockSettings.of(Material.PORTAL)
            .noCollision()
            .ticksRandomly()
            .strength(-1.0f, -1.0f)
            .sounds(BlockSoundGroup.GLASS)
            .lightLevel(11)
            .dropsNothing()
            .build()
    )

    val BIOME_RIFT_PORTAL_POI = PointOfInterestTypeAccessor.of(
        TerrestrialVacation.id("biome_rift_portal").toString(),
        ImmutableSet.copyOf(BIOME_RIFT_PORTAL.stateManager.states),
        0, 1
    )

    fun init() {
        register(Registry.BLOCK, "biome_rift_portal", BIOME_RIFT_PORTAL)
        register(Registry.POINT_OF_INTEREST_TYPE, "biome_rift_portal", BIOME_RIFT_PORTAL_POI)
    }

    private fun <T> register(registry: Registry<in T>, name: String, value: T): T =
        Registry.register(registry, TerrestrialVacation.id(name), value)
}
