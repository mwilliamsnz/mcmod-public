package abyssal.items.armour;

import abyssal.ModAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class SpiritVisageItem extends ModArmourItem {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public SpiritVisageItem(Type slot, Properties properties) {
        super(ModArmourMaterials.MR_ITEMS, slot, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = UUID.fromString("e9166b06-4aa8-4be5-be5a-7e513a99eeeb");
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Maximum health", 9, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.MAGIC_RESIST.get(), new AttributeModifier(uuid, "Magic resistance", 60, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_40390_) {
        return p_40390_ == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_40390_);
    }
}
