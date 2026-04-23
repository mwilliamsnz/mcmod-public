package abyssal.items;

import abyssal.Main;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import top.theillusivec4.curios.api.CurioAttributeModifiers;
import top.theillusivec4.curios.api.CuriosDataComponents;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.concurrent.atomic.AtomicBoolean;

public class AttributeHelper {

    public static void addToStack(ItemStack stack, Holder<Attribute> attribute, AttributeModifier mod, EquipmentSlotGroup slot) {
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, stack.getAttributeModifiers().withModifierAdded(attribute, mod, slot));
    }

    public static void removeFromStack(ItemStack stack, Holder<Attribute> attribute,  AttributeModifier mod, EquipmentSlotGroup slot) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        AtomicBoolean found = new AtomicBoolean(false);
        stack.getAttributeModifiers().forEach(slot, (atr, m) -> {
            if(m.id().equals(mod.id())) {
                found.set(true);
            } else {
                builder.add(atr, m, slot);
            }
        });
        if(found.get()) {
            stack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
        }
    }


    private static String replaceSuffix(String id, String newSuffix) {
        if (id == null || newSuffix == null) {
            throw new IllegalArgumentException("Attribute ID and newSuffix must not be null.");
        }

        int lastUnderscoreIndex = id.lastIndexOf('_');
        if (lastUnderscoreIndex == -1 || lastUnderscoreIndex == id.length() - 1) {
            throw new IllegalArgumentException("Attribute ID must contain a base and a suffix separated by an underscore.");
        }

        String base = id.substring(0, lastUnderscoreIndex);
        return base + "_" + newSuffix;
    }

    public static void relabelCurioModifiers(ItemStack stack, ISlotType slot, String suffix) {
        if(slot == null) {
            return;
        }
        CurioAttributeModifiers.Builder builder = CurioAttributeModifiers.builder();
        ICurioItem.forEachModifier(stack, slot, (atr, m) -> {
            String newPath = replaceSuffix(m.id().getPath(), suffix);
            AttributeModifier newMod = new AttributeModifier(
                    Identifier.fromNamespaceAndPath(m.id().getNamespace(), newPath),
                    m.amount(), m.operation());
            builder.addModifier(atr, newMod);
        });
        stack.set(CuriosDataComponents.ATTRIBUTE_MODIFIERS, builder.build());
    }

}
