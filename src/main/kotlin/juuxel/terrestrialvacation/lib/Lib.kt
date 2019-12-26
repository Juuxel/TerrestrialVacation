package juuxel.terrestrialvacation.lib

import juuxel.terrestrialvacation.TerrestrialVacation
import juuxel.terrestrialvacation.block.BiomeRiftPortalBlock
import juuxel.terrestrialvacation.component.DimensionalSpawnPoints
import nerdhub.cardinal.components.api.ComponentRegistry
import nerdhub.cardinal.components.api.ComponentType
import nerdhub.cardinal.components.api.event.EntityComponentCallback
import nerdhub.cardinal.components.api.util.EntityComponents
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.registry.Registry

object Lib {
    val BIOME_RIFT_PORTAL: Block = BiomeRiftPortalBlock(
        FabricBlockSettings.of(Material.STONE)
            .ticksRandomly()
            .strength(2f, 2f)
            .sounds(BlockSoundGroup.STONE)
            .build()
    )

    val DIMENSIONAL_SPAWN_POINTS: ComponentType<DimensionalSpawnPoints> = ComponentRegistry.INSTANCE.registerIfAbsent(
        TerrestrialVacation.id("spawn_points"), DimensionalSpawnPoints::class.java
    )

    fun init() {
        register(Registry.BLOCK, "biome_rift_portal", BIOME_RIFT_PORTAL)
        register(Registry.ITEM, "biome_rift_portal", BlockItem(BIOME_RIFT_PORTAL, Item.Settings().group(ItemGroup.DECORATIONS)))

        EntityComponentCallback.event(PlayerEntity::class.java).register(EntityComponentCallback { entity, components ->
            components[DIMENSIONAL_SPAWN_POINTS] = DimensionalSpawnPoints()
        })

        EntityComponents.setRespawnCopyStrategy(DIMENSIONAL_SPAWN_POINTS, RespawnCopyStrategy.ALWAYS_COPY)
    }

    private fun <T> register(registry: Registry<in T>, name: String, value: T): T =
        Registry.register(registry, TerrestrialVacation.id(name), value)
}
