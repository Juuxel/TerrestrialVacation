package juuxel.terrestrialvacation.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.village.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(PointOfInterestType.class)
public interface PointOfInterestTypeAccessor {
    @SuppressWarnings("PublicStaticMixinMember")
    @Invoker("<init>")
    static PointOfInterestType of(String id, Set<BlockState> workstationStates, int ticketCount, int locationDistance) {
        throw new AssertionError("static @Invoker body called");
    }
}
