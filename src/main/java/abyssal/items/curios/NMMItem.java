package abyssal.items.curios;

import abyssal.ModAttributes;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class NMMItem extends ModCurioItem {

    public NMMItem(Properties props) {
        super(props);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getCurioAttributes(SlotContext ctx, UUID uuid) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(ModAttributes.MAGIC_RESIST.get(), new AttributeModifier(uuid, "Magic Resistance", 25, AttributeModifier.Operation.ADDITION));
        return modifiers;
    }

}
