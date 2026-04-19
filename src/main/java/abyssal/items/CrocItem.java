package abyssal.items;

import abyssal.Main;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.Random;

public class CrocItem extends Item {

    private static final Random random = new Random();
    public CrocItem(Properties props) {
        super(props);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity eater) {
        ItemStack itemstack = super.finishUsingItem(stack, level, eater);
        if(!level.isClientSide()) {
            int dmg = 0;
            while(true) {
                int roll = random.nextInt(1,5);
                dmg += roll;
                if(roll < 4) {
                    break;
                }
                dmg += 3;
            }
            DamageSource s = level.damageSources().cactus();
            Optional<Holder.Reference<DamageType>> crocDamageType = level.damageSources().damageTypes.get(Main.rl("abyssal_croc"));
            if(crocDamageType.isPresent()) {
                s = new DamageSource(crocDamageType.get());
            }
            eater.hurt(s, dmg);
        }
        return itemstack;
    }
}
