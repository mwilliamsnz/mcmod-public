package abyssal.items.armour;

import abyssal.ModAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class RabadonsItem extends ModArmourItem {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public RabadonsItem(Type slot, Properties properties) {
        super(ModArmourMaterials.MR_ITEMS, slot, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = UUID.fromString("fa8fb625-4681-4e27-87a0-fa23c5f52380");
        UUID uuid2 = UUID.fromString("44905fc2-8bbf-4612-b675-e28b67aad1de");
        builder.put(ModAttributes.ABILITY_POWER.get(), new AttributeModifier(uuid, "Ability power", 120, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.ABILITY_POWER.get(), new AttributeModifier(uuid2, "Ability power multiplier", 0.4, AttributeModifier.Operation.MULTIPLY_BASE));
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_40390_) {
        return p_40390_ == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_40390_);
    }

}
