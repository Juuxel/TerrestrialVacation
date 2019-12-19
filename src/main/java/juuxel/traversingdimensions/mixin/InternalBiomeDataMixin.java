package juuxel.traversingdimensions.mixin;

import juuxel.traversingdimensions.biome.Climates;
import net.fabricmc.fabric.api.biomes.v1.OverworldClimate;
import net.fabricmc.fabric.impl.biome.InternalBiomeData;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InternalBiomeData.class)
public class InternalBiomeDataMixin {
    @Inject(method = "addOverworldContinentalBiome", at = @At("HEAD"))
    private static void onAddOverworldContinentalBiome(OverworldClimate climate, Biome biome, double weight, CallbackInfo info) {
        Climates.set(biome, climate);
    }
}
