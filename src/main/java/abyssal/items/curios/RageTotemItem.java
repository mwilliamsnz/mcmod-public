package abyssal.items.curios;

import abyssal.capability.CombatTimeCapability;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.SlotContext;

public class RageTotemItem extends ModCurioItem {

    public RageTotemItem(Properties props) {
        super(props);
    }

    @Override
    public void tickCurio(SlotContext ctx) {
        LivingEntity e = ctx.entity();
        if(!e.level().isClientSide()) {
            e.getCapability(CombatTimeCapability.INSTANCE).ifPresent(ctc -> {
                if(ctc.getTicksInCombat() > 0 ) { // && e.getHealth() > 1.5
                    // Work out best way to directly hurt without giving invulnerability frames
                    e.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 15));
                    e.addEffect(new MobEffectInstance(MobEffects.POISON, 15, 1));
                }
            });
        }
    }

}
