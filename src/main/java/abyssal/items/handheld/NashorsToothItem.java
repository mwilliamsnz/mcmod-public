package abyssal.items.handheld;

import abyssal.ModAttributes;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IItemExtension;

import java.util.Collection;
import java.util.UUID;

public class NashorsToothItem extends SwordItem implements IItemExtension {

    private static final UUID NASHORS_AP_UUID = UUID.fromString("a1b50376-1fc0-43ca-85b9-8c16f1c532fc");
    private static final UUID NASHORS_DAMAGE_UUID = UUID.fromString("91c271c5-7307-4aad-bd14-397c6505f3fa");
    private static final AttributeModifier NASHORS_AP = new AttributeModifier(NASHORS_AP_UUID, "Ability power", 90, AttributeModifier.Operation.ADDITION);

    private final ImmutableMultimap<Attribute, AttributeModifier> nashorsModifiers;
    public NashorsToothItem(Tier tier, Properties properties) {
        super(tier, 2, -2.0F, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableListMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND));
        builder.put(ModAttributes.ABILITY_POWER.get(), NASHORS_AP);
        nashorsModifiers = builder.build(); // Does not include damage from AP
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.nashorsModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public void inventoryTick(ItemStack item, Level level, Entity possessor, int p_41407_, boolean p_41408_) {
        if(possessor instanceof Player owner) {
            double ap = owner.getAttributeValue(ModAttributes.ABILITY_POWER.get());
            double bonus = ap/50;
            CompoundTag tag = item.getOrCreateTag();
            ListTag l =  tag.getList("AttributeModifiers",10);
            if(l.isEmpty()) {
                item.addAttributeModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 2, AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
                item.addAttributeModifier(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.0f, AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
                item.addAttributeModifier(ModAttributes.ABILITY_POWER.get(), NASHORS_AP, EquipmentSlot.MAINHAND);
            }
            for(int i = 0; i < l.size(); ++i) {
                CompoundTag attr = l.getCompound(i);
                UUID uuid = attr.getUUID("UUID");
                if(uuid.equals(NASHORS_DAMAGE_UUID)) {
                    l.remove(i);
                    break;
                }
            }
            AttributeModifier dmg = new AttributeModifier(NASHORS_DAMAGE_UUID, "Weapon modifier", bonus, AttributeModifier.Operation.ADDITION);
            item.addAttributeModifier(Attributes.ATTACK_DAMAGE, dmg, EquipmentSlot.MAINHAND);
        }
        super.inventoryTick(item, level, possessor, p_41407_, p_41408_);
    }



    @Override
    public boolean isFoil(ItemStack stack) {
        Collection<AttributeModifier> mods = stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE);
        double tot = 0;
        for(AttributeModifier mod : mods) {
            tot += mod.getAmount();
        }
        return tot >= 10;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return super.onEntitySwing(stack, entity);
    }

    public float getDamage() {
        return 6;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

}
