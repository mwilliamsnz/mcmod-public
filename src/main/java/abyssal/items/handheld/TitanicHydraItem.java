package abyssal.items.handheld;

import abyssal.Main;
import abyssal.items.AttributeHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class TitanicHydraItem extends TiamatItem {

    private static final ResourceLocation BONUS_AD_LOC = Main.rl("hydra_damage");

    public TitanicHydraItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity possessor, EquipmentSlot slot) {
        if(possessor instanceof Player owner) {
            double hp = owner.getAttributeValue(Attributes.MAX_HEALTH);
            double bonus = Math.max(0, hp-20)/4;

            AttributeModifier dmg = new AttributeModifier(BONUS_AD_LOC, bonus, AttributeModifier.Operation.ADD_VALUE);
            AttributeHelper.addToStack(stack, Attributes.ATTACK_DAMAGE, dmg, EquipmentSlotGroup.MAINHAND);
        }
//        if(getAllEnchantments(stack, level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)).isEmpty()) {
//            stack.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SWEEPING_EDGE), 1);
//        }
        super.inventoryTick(stack, level, possessor, slot);
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(Enchantments.SWEEPING_EDGE);
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
