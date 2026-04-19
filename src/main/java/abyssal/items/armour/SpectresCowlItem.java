package abyssal.items.armour;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpectresCowlItem extends ModTickingArmourItem {

    public SpectresCowlItem(Properties properties) {
        super(properties);
    }

    @Override
    public void doArmourTick(ItemStack stack, Level level, Entity entity) {
        // TODO capability for tracking time remaining stored from damage taken
    }
}
