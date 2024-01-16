package abyssal.items.curios;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.SlotContext;

public class CleansingTotemItem extends ModCurioItem {

    public CleansingTotemItem(Properties props) {
        super(props);
    }

    @Override
    public void tickCurio(SlotContext ctx) {
        LivingEntity e = ctx.entity();
        if(e.hasEffect(MobEffects.POISON)) {
            e.removeEffect(MobEffects.POISON);
        }
    }

}
