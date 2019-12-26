package juuxel.terrestrialvacation.mixin;

import juuxel.terrestrialvacation.component.DimensionalSpawnPoints;
import juuxel.terrestrialvacation.dimension.BiomeRiftDimension;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow
    private BlockPos spawnPosition;

    private PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(method = "setPlayerSpawn", at = @At("HEAD"))
    private void onSetPlayerSpawn_head(BlockPos pos, boolean forced, boolean bed, CallbackInfo info) {
        if (bed && dimension == BiomeRiftDimension.TYPE) {
            DimensionalSpawnPoints.set((PlayerEntity) (Object) this, DimensionType.OVERWORLD, spawnPosition);
            DimensionalSpawnPoints.set((PlayerEntity) (Object) this, BiomeRiftDimension.TYPE, pos);
        }
    }

    @Inject(method = "setPlayerSpawn", at = @At("RETURN"))
    private void onSetPlayerSpawn_return(BlockPos pos, boolean forced, boolean bed, CallbackInfo info) {
        if (bed && dimension == BiomeRiftDimension.TYPE) {
            spawnPosition = DimensionalSpawnPoints.get((PlayerEntity) (Object) this, DimensionType.OVERWORLD, true);
            if (spawnPosition == null) {
                throw new IllegalStateException("[TerrestrialVacation] Could not restore player's overworld spawn point");
            }
        }
    }
}
