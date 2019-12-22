package juuxel.terrestrialvacation.lib

import juuxel.terrestrialvacation.TerrestrialVacation
import juuxel.terrestrialvacation.block.BiomeRiftPortalBlock
import net.fabricmc.fabric.api.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
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

    fun init() {
        register(Registry.BLOCK, "biome_rift_portal", BIOME_RIFT_PORTAL)
        register(Registry.ITEM, "biome_rift_portal", BlockItem(BIOME_RIFT_PORTAL, Item.Settings().group(ItemGroup.DECORATIONS)))
    }

    private fun <T> register(registry: Registry<in T>, name: String, value: T): T =
        Registry.register(registry, TerrestrialVacation.id(name), value)
}
