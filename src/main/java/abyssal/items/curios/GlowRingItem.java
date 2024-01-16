package abyssal.items.curios;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import top.theillusivec4.curios.api.SlotContext;

public class GlowRingItem extends ModCurioItem {

    public GlowRingItem(Properties props) {
        super(props);
    }

    @Override
    public void tickCurio(SlotContext ctx) {
        ctx.entity().addEffect(new MobEffectInstance(MobEffects.GLOWING, 10, 0, false, false));
        ctx.entity().addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 10, 0, false, false));
//        ctx.entity()
    }

}
