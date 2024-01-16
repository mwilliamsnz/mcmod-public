package abyssal.items.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class GlassCannonRingItem extends ModCurioItem {

    public GlassCannonRingItem(Properties props) {
        super(props);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getCurioAttributes(SlotContext ctx, UUID uuid) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Attack Damage", 0.30, AttributeModifier.Operation.MULTIPLY_BASE));
        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Max Health", -6, AttributeModifier.Operation.ADDITION));
        return modifiers;
    }


    public void onUnequipCurio(SlotContext ctx){
        ctx.entity().setHealth(ctx.entity().getHealth()); // Clamp
    }
    public void onEquipCurio(SlotContext ctx){
        ctx.entity().setHealth(ctx.entity().getHealth()); // Clamp
    }

}
