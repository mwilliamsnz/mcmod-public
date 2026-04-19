package abyssal.items.armour;

import abyssal.Main;
import abyssal.init.ModAttachmentTypes;
import abyssal.items.AttributeHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MobiBootsItem extends ModTickingArmourItem {

    public static final AttributeModifier OUT_OF_COMBAT_MODIFIER = new AttributeModifier(Main.rl("mobis_speed"), 0.03f, AttributeModifier.Operation.ADD_VALUE);
    public static final AttributeModifier IN_COMBAT_MODIFIER = new AttributeModifier(Main.rl("mobis_slow"), -0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    public MobiBootsItem(Properties properties) {
        super(properties);
    }

    @Override
    public void doArmourTick(ItemStack stack, Level level, Entity entity) {
        if(!level.isClientSide()) {
            if(entity.getData(ModAttachmentTypes.NO_COMBAT_TIME) > 100) {
                applyBonus(stack);
            } else {
                applyMalus(stack);
            }
        }
    }

    private void applyBonus(ItemStack stack) {
        AttributeHelper.removeFromStack(stack, Attributes.MOVEMENT_SPEED, IN_COMBAT_MODIFIER, EquipmentSlotGroup.FEET);
        AttributeHelper.addToStack(stack, Attributes.MOVEMENT_SPEED, OUT_OF_COMBAT_MODIFIER, EquipmentSlotGroup.FEET);
    }

    private void applyMalus(ItemStack stack) {
        AttributeHelper.removeFromStack(stack, Attributes.MOVEMENT_SPEED, OUT_OF_COMBAT_MODIFIER, EquipmentSlotGroup.FEET);
        AttributeHelper.addToStack(stack, Attributes.MOVEMENT_SPEED, IN_COMBAT_MODIFIER, EquipmentSlotGroup.FEET);
    }
}
