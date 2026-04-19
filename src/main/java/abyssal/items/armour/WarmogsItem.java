package abyssal.items.armour;

import abyssal.Main;
import abyssal.init.ModAttachmentTypes;
import abyssal.items.AttributeHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WarmogsItem extends ModTickingArmourItem {

    private static final AttributeModifier BONUS_SPEED = new AttributeModifier(Main.rl("warmogs_extra_speed"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    public WarmogsItem(Properties properties) {
        super(properties);
    }

    @Override
    public void doArmourTick(ItemStack stack, Level level, Entity entity) {
        // Regenerate HP if out of combat
        if(!level.isClientSide() && entity instanceof Player player) {
            if(player.getData(ModAttachmentTypes.NO_COMBAT_TIME) > 120 && player.getMaxHealth() >= 20 + 26) {
                player.heal(player.getMaxHealth() * 0.005f);
                ensureBonusActive(stack);
            } else {
                ensureBonusInactive(stack);
            }
        }
    }

    private void ensureBonusActive(ItemStack stack) {
        AttributeHelper.addToStack(stack, Attributes.MOVEMENT_SPEED, BONUS_SPEED, EquipmentSlotGroup.CHEST);
    }

    private void ensureBonusInactive(ItemStack stack) {
        AttributeHelper.removeFromStack(stack, Attributes.MOVEMENT_SPEED, BONUS_SPEED, EquipmentSlotGroup.CHEST);
    }

}
