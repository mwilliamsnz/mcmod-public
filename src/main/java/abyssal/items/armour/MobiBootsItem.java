package abyssal.items.armour;

import abyssal.capability.CombatTimeCapability;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class MobiBootsItem extends ModTickingArmourItem {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    private final AttributeModifier outOfCombatModifier = new AttributeModifier(UUID.fromString("278c08ec-0f76-11ee-be56-0242ac120002"), "Movement Speed", 0.03f, AttributeModifier.Operation.ADDITION);
    private final AttributeModifier inCombatModifier = new AttributeModifier(UUID.fromString("206f71a2-0f76-11ee-be56-0242ac120002"), "Movement Speed", -0.15f, AttributeModifier.Operation.MULTIPLY_BASE);

    public MobiBootsItem(Type slot, Properties properties) {
        super(ModArmourMaterials.WARMOGS, slot, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.MOVEMENT_SPEED, outOfCombatModifier);
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_40390_) {
        return p_40390_ == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_40390_);
    }

    @Override
    public void doArmourTick(ItemStack stack, Level level, Player player) {
        if(!level.isClientSide()) {
            player.getCapability(CombatTimeCapability.INSTANCE).ifPresent(ctc -> {
                if(ctc.getTicksOutOfCombat() > 100) {
                    applyBonus(stack);
                } else {
                    applyMalus(stack);
                }
            });
        }
    }

    private void applyBonus(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag l = tag.getList("AttributeModifiers", 10);
        l.clear();
        stack.setTag(tag);
        stack.addAttributeModifier(Attributes.MOVEMENT_SPEED, outOfCombatModifier, EquipmentSlot.FEET);
    }

    private void applyMalus(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag l = tag.getList("AttributeModifiers", 10);
        l.clear();
        stack.setTag(tag);
        stack.addAttributeModifier(Attributes.MOVEMENT_SPEED, inCombatModifier, EquipmentSlot.FEET);
    }
}
