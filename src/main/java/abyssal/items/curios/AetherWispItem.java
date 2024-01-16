package abyssal.items.curios;

import abyssal.ModAttributes;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class AetherWispItem extends ModCurioItem {

    public AetherWispItem(Properties props) {
        super(props);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getCurioAttributes(SlotContext ctx, UUID uuid) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(ModAttributes.ABILITY_POWER.get(), new AttributeModifier(uuid, "Ability Power", 30, AttributeModifier.Operation.ADDITION));
        modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid, "Movement Speed", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
        return modifiers;
    }

}
