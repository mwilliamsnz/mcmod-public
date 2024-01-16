package abyssal.items.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class DamageCurioItem extends ModCurioItem {

    private final float damage;

    public DamageCurioItem(Properties props, float damage) {
        super(props);
        this.damage = damage;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getCurioAttributes(SlotContext ctx, UUID uuid) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Damage", damage, AttributeModifier.Operation.ADDITION));
        return modifiers;
    }

}
