package abyssal.spells;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LeapSpell extends Spell {
    protected LeapSpell(Identifier key, SpellFuelQuantity cost) {
        super(key, cost);
    }

    @Override
    public InteractionResult cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        if(player.onGround() || player.onClimbable()) {
            double forceFactor = 1 * (1 + 0.005 * ap) ; // 200 AP to double, 1k AP = 6x
            Vec3 v = player.getLookAngle().normalize().scale(forceFactor);
            player.push(v);
            float volume = Mth.clamp(((float)forceFactor-1)/4 + 1, 1.0f, 2.0f);
            level.playSound(null, player.blockPosition(), SoundEvents.SLIME_JUMP, SoundSource.PLAYERS, volume, 0.8f+level.getRandom().nextFloat()*0.4f);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
