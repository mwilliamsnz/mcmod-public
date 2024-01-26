package abyssal.items.armour;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ModTickingArmourItem extends ModArmourItem {

    public ModTickingArmourItem(ArmorMaterial material, Type slot, Properties properties) {
        super(material, slot, properties);
    }

    public abstract void doArmourTick(ItemStack stack, Level level, Player player);

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
