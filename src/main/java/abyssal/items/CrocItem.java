package abyssal.items;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class CrocItem extends Item {

    private static final Random random = new Random();
    public CrocItem(Properties props) {
        super(props);
    }

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
            DamageSource s = level.damageSources().cactus(); // TODO
            eater.hurt(s, dmg);
        }
        return itemstack;
    }
}
