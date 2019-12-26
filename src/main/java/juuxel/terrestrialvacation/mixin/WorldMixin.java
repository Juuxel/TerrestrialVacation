package juuxel.terrestrialvacation.mixin;

import juuxel.terrestrialvacation.dimension.BiomeRiftDimension;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * "Time also exists in the biome rift"
 * -- or in other words, this mixin makes {@code isDay} and {@code isNight}
 * work in the biome rift.
 */
@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow
    @Final
    public Dimension dimension;

    @Shadow
    private int ambientDarkness;

    @Shadow
    public abstract boolean isDay();

    @Inject(method = "isDay", at = @At("RETURN"), cancellable = true)
    private void onIsDay(CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValueZ()) {
            info.setReturnValue(this.dimension.getType() == BiomeRiftDimension.TYPE && this.ambientDarkness < 4);
        }
    }

    @Inject(method = "isNight", at = @At("RETURN"), cancellable = true)
    private void onIsNight(CallbackInfoReturnable<Boolean> info) {
        if (!info.getReturnValueZ()) {
            info.setReturnValue(this.dimension.getType() == BiomeRiftDimension.TYPE && !isDay());
        }
    }
}
