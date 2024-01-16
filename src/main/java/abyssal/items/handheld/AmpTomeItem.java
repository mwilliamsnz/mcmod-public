package abyssal.items.handheld;

import abyssal.ModAttributes;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;

import java.util.UUID;

public class AmpTomeItem extends Item implements IForgeItem {

    private static final UUID TOME_AP_UUID = UUID.fromString("352dbb4f-a0dc-4a99-846e-da02a7b05301");
    private static final AttributeModifier TOME_AP = new AttributeModifier(TOME_AP_UUID, "Ability power", 20, AttributeModifier.Operation.ADDITION);

    private final ImmutableMultimap<Attribute, AttributeModifier> tomeModifiers;
    public AmpTomeItem(Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableListMultimap.builder();
        builder.put(ModAttributes.ABILITY_POWER.get(), TOME_AP);
        tomeModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND ? this.tomeModifiers : super.getAttributeModifiers(slot, stack);
    }

}
