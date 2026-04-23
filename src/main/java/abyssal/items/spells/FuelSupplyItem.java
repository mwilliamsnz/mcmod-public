package abyssal.items.spells;

import abyssal.spells.SpellFuelQuantity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FuelSupplyItem extends Item {

    public final SpellFuelQuantity quantity;

    public FuelSupplyItem(Properties properties, SpellFuelQuantity quantity) {
        super(properties);
        this.quantity = quantity;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack fuelStack = player.getItemInHand(hand);
        if(quantity.topUp(player)) {
            fuelStack.shrink(1);
            player.getCooldowns().addCooldown(fuelStack, 10);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}
