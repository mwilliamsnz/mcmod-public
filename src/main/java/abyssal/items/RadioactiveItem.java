package abyssal.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadioactiveItem extends Item {

    private final int unitsPerItem;

    public RadioactiveItem(Properties properties, int unitsPerItem) {
        super(properties);
        this.unitsPerItem = unitsPerItem;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        if(entity instanceof Player p) {
            int count = stack.getCount() * unitsPerItem;
            int delta = count * count / 2 + 1;


            int currentDuration = 0;
            int currentAmplifier = 0;
            if(p.hasEffect(MobEffects.WITHER)) {
                MobEffectInstance instance = p.getEffect(MobEffects.WITHER);
                currentDuration = instance.getDuration();
                currentAmplifier = instance.getAmplifier();
                p.removeEffect(MobEffects.WITHER);
            }

            int newAmplifier = Math.max((int)Math.sqrt(count) - 1, currentAmplifier);
            p.addEffect(new MobEffectInstance(MobEffects.WITHER, currentDuration + delta, newAmplifier, false, false, true));


            if(count >= 16) {
                stack.shrink(count);
                level.explode(null, entity.getX(),entity.getY(),entity.getZ(), 6f + count/4f, Level.ExplosionInteraction.TNT);
            }
        }


    }
}
