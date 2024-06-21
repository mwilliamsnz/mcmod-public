package abyssal.items.armour;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ModTickingArmourItem extends ModArmourItem {

    public ModTickingArmourItem(ArmorMaterial material, Type slot, Properties properties) {
        super(material, slot, properties);
    }

    public abstract void doArmourTick(ItemStack stack, Level level, Entity entity);

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isSelected) {
        if (slotIndex >= 36 && slotIndex <= 39) {
            doArmourTick(stack, level, entity);
        }
    }

    // By 1.21 this method will be removed, simply delete as inventoryTick will work properly by then.
    @Override
    public void onArmorTick(ItemStack stack, Level world, Player entity) {
        doArmourTick(stack, world, entity);
    }


}
