package abyssal.items.handheld;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class TitanicHydraItem extends TiamatItem {
    public TitanicHydraItem(Tier tier, Properties properties) {
        super(tier, 5, -3.2F, properties);
    }

    @Override
    public void inventoryTick(ItemStack item, Level level, Entity possessor, int p_41407_, boolean p_41408_) {
        if(possessor instanceof Player owner) {
            double hp = owner.getAttributeValue(Attributes.MAX_HEALTH);
            double bonus = Math.max(0, hp-20)/4 + 5;
            CompoundTag tag = item.getOrCreateTag();
            ListTag l =  tag.getList("AttributeModifiers",10);
            l.clear();
            item.setTag(tag);
            AttributeModifier dmg = new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", bonus, AttributeModifier.Operation.ADDITION);
            item.addAttributeModifier(Attributes.ATTACK_DAMAGE, dmg, EquipmentSlot.MAINHAND);
            item.addAttributeModifier(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3.2F, AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
        }
        if(getAllEnchantments(item).isEmpty()) {
            item.enchant(Enchantments.SWEEPING_EDGE, 1);
        }
        super.inventoryTick(item, level, possessor, p_41407_, p_41408_);
    }

    public float getDamage() {
        return 5;
    }


//    @NotNull
//    public AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
//        Vec3 delta = target.position().subtract(player.position());
//        double x = delta.x;
//        double z = delta.z;
//
//        Vec3 delta2D = new Vec3(delta.x, 0, delta.y);
//        delta2D.
//        return target.getBoundingBox().inflate(3.0D, 0.25D, 3.0D);
//    }

}
