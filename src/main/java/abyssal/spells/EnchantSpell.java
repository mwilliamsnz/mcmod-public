package abyssal.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class EnchantSpell extends Spell {
    protected EnchantSpell(ResourceLocation key) {
        super(key);
    }

    @Override
    public InteractionResultHolder<ItemStack> cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        ItemStack toEnchant;
        if(mainHand == staff) {
            toEnchant = offHand;
        } else if(offHand == staff) {
            toEnchant = mainHand;
        } else {
            return InteractionResultHolder.fail(staff);
        }
        if(!toEnchant.isEnchantable()) {
            return InteractionResultHolder.fail(staff);
        }
        toEnchant.enchant(Enchantments.UNBREAKING, 1);
        level.playSound(player, BlockPos.containing(player.position()), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResultHolder.success(staff);


    }

}
