package abyssal.spells;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LeapSpell extends Spell {
    protected LeapSpell(ResourceLocation key) {
        super(key);
    }

    @Override
    public InteractionResultHolder<ItemStack> cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        if(player.onGround() || player.onClimbable()) {
            double forceFactor = 3 * (1 + 0.005 * ap) ; // 200 AP to double, 1k AP = 6x
            Vec3 v = player.getLookAngle().normalize().scale(forceFactor);
            player.push(v.x, v.y, v.z);
            return InteractionResultHolder.success(staff);
        }
        return InteractionResultHolder.fail(staff);
    }
}
