package abyssal.items.curios;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import top.theillusivec4.curios.api.SlotContext;

public class WaterCurioItem extends ModCurioItem {

    public WaterCurioItem(Properties props) {
        super(props);
    }

    @Override
    public void tickCurio(SlotContext ctx) {
        if(ctx.entity().isUnderWater()) {
            ctx.entity().addEffect(new MobEffectInstance(MobEffects.HUNGER, 25, 2));
            if(ctx.entity().tickCount % 3 != 0) { // "Un-tick" air supply on two out of every three ticks
                int air = ctx.entity().getAirSupply();
                ctx.entity().setAirSupply(Math.min(air + 1, ctx.entity().getMaxAirSupply()));
            }
        }

    }

}
