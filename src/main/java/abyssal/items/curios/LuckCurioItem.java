package abyssal.items.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class LuckCurioItem extends ModCurioItem {

    private final int luckLevel;

    public LuckCurioItem(Properties props, int luckLevel) {
        super(props);
        this.luckLevel = luckLevel;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getCurioAttributes(SlotContext ctx, UUID uuid) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(Attributes.LUCK, new AttributeModifier(uuid, "Luck", luckLevel, AttributeModifier.Operation.ADDITION));
        return modifiers;
    }

}
