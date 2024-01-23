package abyssal.items.armour;

import abyssal.capability.CombatTimeCapability;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

    private static final UUID SPEED_UUID = UUID.fromString("f7c3a88d-9e64-4b9a-b7ef-35f453bb6d2c");
    private static final AttributeModifier BONUS_SPEED = new AttributeModifier(SPEED_UUID, "Movement Speed", 0.1, AttributeModifier.Operation.MULTIPLY_BASE);


    public WarmogsItem(Type slot, Properties properties) {
        super(ModArmourMaterials.WARMOGS, slot, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = UUID.fromString("2c5f1a30-0f76-11ee-be56-0242ac120002");
        UUID uuid2 = UUID.fromString("8ba56079-206f-48f6-85c9-fde0faa4c030");
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Maximum health", 15, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(uuid2, "Movement speed", 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
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
                if(ctc.getTicksOutOfCombat() > 120 && player.getMaxHealth() >= 20 + 26) {
                    player.heal(player.getMaxHealth() * 0.005f);

                    CompoundTag tag = stack.getOrCreateTag();
                    ListTag l =  tag.getList("AttributeModifiers",10);
                    boolean hasAlready = false;
                    for(int i = 0; i < l.size(); ++i) {
                        CompoundTag attr = l.getCompound(i);
                        UUID uuid = attr.getUUID("UUID");
                        if(uuid.equals(SPEED_UUID)) {
                            hasAlready = true;
                            break;
                        }
                    }
                    if(!hasAlready) {
                        stack.addAttributeModifier(Attributes.MOVEMENT_SPEED, BONUS_SPEED, EquipmentSlot.CHEST);
                    }
                } else {
                    CompoundTag tag = stack.getOrCreateTag();
                    ListTag l =  tag.getList("AttributeModifiers",10);
                    l.clear(); // Removing just the speed seems to remove them all anyway, so may as well just clear. See nashor's tooth.
                    defaultModifiers.forEach(((attribute, attributeModifier) -> {
                        stack.addAttributeModifier(attribute, attributeModifier, EquipmentSlot.CHEST);
                    }));
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
