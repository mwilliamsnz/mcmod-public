package abyssal.items.curios;

import abyssal.ModAttributes;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class DoransRingItem extends ModCurioItem {

    public DoransRingItem(Properties props) {
        super(props);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getCurioAttributes(SlotContext ctx, UUID uuid) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(ModAttributes.ABILITY_POWER.get(), new AttributeModifier(uuid, "Ability Power", 15, AttributeModifier.Operation.ADDITION));
        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Max Health", 1, AttributeModifier.Operation.ADDITION));
        return modifiers;
    }


    public void onUnequipCurio(SlotContext ctx){
        ctx.entity().setHealth(ctx.entity().getHealth()); // Clamp
    }

}
