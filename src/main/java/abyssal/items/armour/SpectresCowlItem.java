package abyssal.items.armour;

import abyssal.ModAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class SpectresCowlItem extends ModTickingArmourItem {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public SpectresCowlItem(Type slot, Properties properties) {
        super(ModArmourMaterials.MR_ITEMS, slot, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = UUID.fromString("ce455a18-f733-470a-9a56-2e8f43ea38ea");
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Maximum health", 5, AttributeModifier.Operation.ADDITION));
        builder.put(ModAttributes.MAGIC_RESIST.get(), new AttributeModifier(uuid, "Magic resistance", 25, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_40390_) {
        return p_40390_ == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_40390_);
    }

    @Override
    public void doArmourTick(ItemStack stack, Level level, Entity entity) {
        // TODO capability for tracking time remaining stored from damage taken
    }
}
