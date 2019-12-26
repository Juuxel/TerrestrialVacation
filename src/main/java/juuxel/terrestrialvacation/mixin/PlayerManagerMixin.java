package juuxel.terrestrialvacation.mixin;

import juuxel.terrestrialvacation.component.DimensionalSpawnPoints;
import juuxel.terrestrialvacation.dimension.BiomeRiftDimension;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Objects;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @ModifyVariable(method = "respawnPlayer", at = @At("HEAD"), argsOnly = true, index = 2)
    private DimensionType changeDimension(DimensionType dimension, ServerPlayerEntity player, DimensionType dimensionAgain, boolean alive) {
        boolean respawningInBiomeRift = !alive && player.dimension == BiomeRiftDimension.TYPE && dimension == DimensionType.OVERWORLD;
        boolean hasRiftSpawn = DimensionalSpawnPoints.hasSpawnPointIn(player, BiomeRiftDimension.TYPE);
        return respawningInBiomeRift && hasRiftSpawn ? BiomeRiftDimension.TYPE : dimension;
    }

    @ModifyVariable(method = "respawnPlayer", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getSpawnPosition()Lnet/minecraft/util/math/BlockPos;", ordinal = 0), ordinal = 0)
    private BlockPos getDimensionalSpawnPoint(BlockPos pos, ServerPlayerEntity player, DimensionType dimension, boolean alive) {
        return Objects.requireNonNull(
                DimensionalSpawnPoints.get(player, dimension, false),
                () -> "[TerrestrialVacation] Spawn point not found for dimension '" + Registry.DIMENSION.getId(dimension) + '\''
        );
    }
}
