package abyssal.items.armour;

import abyssal.ModAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class MercTreadsItem extends ModArmourItem {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public MercTreadsItem(Type slot, Properties properties) {
        super(ModArmourMaterials.MR_ITEMS, slot, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = UUID.fromString("204d957a-f5a0-4d58-b9c8-d65eaa2d143e");
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Movement speed", 0.015, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.MAGIC_RESIST.get(), new AttributeModifier(uuid, "Magic resistance", 25, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }


}
