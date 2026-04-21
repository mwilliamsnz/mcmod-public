package abyssal.items.handheld;

import abyssal.Main;
import abyssal.ModAttributes;
import abyssal.items.AttributeHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NashorsToothItem extends Item {

    private static final Identifier BONUS_AD_LOC = Main.rl("nashors_damage");

    public NashorsToothItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity possessor, EquipmentSlot slot) {
        if(possessor instanceof Player owner) {
            double ap = owner.getAttributeValue(ModAttributes.ABILITY_POWER);
            double bonus = ap/50;
            AttributeModifier dmg = new AttributeModifier(BONUS_AD_LOC, bonus, AttributeModifier.Operation.ADD_VALUE);
            AttributeHelper.addToStack(stack, Attributes.ATTACK_DAMAGE, dmg, EquipmentSlotGroup.MAINHAND);
        }
        super.inventoryTick(stack, level, possessor, slot);
    }

}
