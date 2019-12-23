package juuxel.terrestrialvacation.mixin;

import juuxel.terrestrialvacation.TerrestrialVacation;
import juuxel.terrestrialvacation.biome.Climates;
import juuxel.terrestrialvacation.config.Config;
import net.fabricmc.fabric.api.biomes.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biomes.v1.OverworldClimate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverworldBiomes.class)
public class OverworldBiomesMixin {
    @Inject(method = "addContinentalBiome", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onAddContinentalBiome(Biome biome, OverworldClimate climate, double weight, CallbackInfo info) {
        Identifier id = Registry.BIOME.getId(biome);
        if (TerrestrialVacation.INSTANCE.isValidBaseBiomeId(id)) {
            Climates.INSTANCE.add(biome, climate, weight);
            if (Config.get().isOverworldGenerationDisabled()) {
                info.cancel();
            }
        }
    }
}
