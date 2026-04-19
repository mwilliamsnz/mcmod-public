package abyssal.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LeapSpell extends Spell {
    protected LeapSpell(ResourceLocation key, SpellFuelQuantity cost) {
        super(key, cost);
    }

    @Override
    public InteractionResult cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        if(player.onGround() || player.onClimbable()) {
            double forceFactor = 3 * (1 + 0.005 * ap) ; // 200 AP to double, 1k AP = 6x
            Vec3 v = player.getLookAngle().normalize().scale(forceFactor);
            player.push(v.x, v.y, v.z);
            level.playSound(player, BlockPos.containing(player.position()), SoundEvents.SLIME_JUMP, SoundSource.PLAYERS, 1.0F, 0.8f+level.random.nextFloat()*0.4f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
