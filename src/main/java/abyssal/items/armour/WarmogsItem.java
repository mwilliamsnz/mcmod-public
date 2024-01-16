package abyssal.items.armour;

import abyssal.capability.CombatTimeCapability;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class WarmogsItem extends ModArmourItem {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public WarmogsItem(Type slot, Properties properties) {
        super(ModArmourMaterials.WARMOGS, slot, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = UUID.fromString("2c5f1a30-0f76-11ee-be56-0242ac120002");
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Maximum health", 16, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_40390_) {
        return p_40390_ == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_40390_);
    }

    public void doArmourTick(ItemStack stack, Level level, Player player) {
        // Regenerate HP if out of combat
        if(!level.isClientSide()) {
            player.getCapability(CombatTimeCapability.INSTANCE).ifPresent(ctc -> {
                if(ctc.getTicksOutOfCombat() > 120) {
                    player.heal(player.getMaxHealth() * 0.005f);
                }
            });
        }
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        Inventory inv = player.getInventory();
        int vanillaIndex = slotIndex;
        if (slotIndex >= inv.items.size()) {
            vanillaIndex = slotIndex - inv.items.size();
            if (vanillaIndex >= inv.armor.size()) {
                vanillaIndex -= inv.armor.size();
            } else {
                this.doArmourTick(stack, level, player);
            }
        }
        stack.inventoryTick(level, player, vanillaIndex, selectedIndex == vanillaIndex);
    }

}
