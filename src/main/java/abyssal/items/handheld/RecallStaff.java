package abyssal.items.handheld;

import abyssal.Main;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class RecallStaff extends Item {


    public RecallStaff(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);

    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int time) {
        Main.LOGGER.info(time + ", " + user.getUseItemRemainingTicks());
        SoundSource soundsource = user instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
        if (time >= this.getUseDuration(stack)) {
            level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.PORTAL_TRAVEL, soundsource, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
            user.moveTo(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, user.getOnPos()).getCenter());
        } else {
            level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.GLASS_BREAK, soundsource, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        Main.LOGGER.info("finished");
        SoundSource soundsource = user instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
        level.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.PORTAL_TRAVEL, soundsource, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        user.moveTo(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, user.getOnPos()).getCenter());
        return stack;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack); // Why not
        return i == 0 ? 200 : 200 - 40 * i;
    }

}
