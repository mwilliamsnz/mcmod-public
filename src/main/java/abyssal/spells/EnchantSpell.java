package abyssal.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.List;

public class EnchantSpell extends Spell {
    protected EnchantSpell(Identifier key, SpellFuelQuantity cost) {
        super(key, cost);
    }

    @Override
    public InteractionResult cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        ItemStack toEnchant;
        if(mainHand == staff) {
            toEnchant = offHand;
        } else if(offHand == staff) {
            toEnchant = mainHand;
        } else {
            return InteractionResult.FAIL;
        }
        if(!toEnchant.isEnchantable()) {
            return InteractionResult.FAIL;
        }
        Registry<Enchantment> reg = level.registryAccess().lookup(Registries.ENCHANTMENT).get();
        List<ResourceKey<Enchantment>> available = List.of(Enchantments.UNBREAKING, Enchantments.EFFICIENCY,
                Enchantments.POWER, Enchantments.PUNCH, Enchantments.PIERCING,
                Enchantments.SHARPNESS, Enchantments.BANE_OF_ARTHROPODS, Enchantments.SMITE, Enchantments.LOOTING, Enchantments.KNOCKBACK,
                Enchantments.PROTECTION, Enchantments.BLAST_PROTECTION, Enchantments.PROJECTILE_PROTECTION, Enchantments.FIRE_PROTECTION,
                Enchantments.RESPIRATION, Enchantments.FEATHER_FALLING, Enchantments.AQUA_AFFINITY,
                Enchantments.DENSITY,
                Enchantments.LURE, Enchantments.LUCK_OF_THE_SEA);
        List<Holder.Reference<Enchantment>> possible = available.stream()
                .map(reg::getOrThrow)
                .filter(toEnchant::isPrimaryItemFor).toList();
        Holder.Reference<Enchantment> e = possible.get(level.getRandom().nextInt(possible.size()));
        toEnchant.enchant(e, 1);
        level.playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS;


    }

}
