package abyssal.items.armour;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ModTickingArmourItem extends Item {

    public ModTickingArmourItem(Properties properties) {
        super(properties);
    }

    public abstract void doArmourTick(ItemStack stack, Level level, Entity entity);

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        if (slot != null && slot.isArmor()) {
            doArmourTick(stack, level, entity);
        }
    }

}
