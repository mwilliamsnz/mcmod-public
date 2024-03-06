package abyssal.spells;

import abyssal.Main;
import abyssal.items.spells.SpellFuelStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public class SpellFuelQuantity {

    final SpellFuelType type;
    int quantity;

    public static final SpellFuelQuantity NONE = new SpellFuelQuantity(SpellFuelTypes.FUEL_NONE, 0);


    public SpellFuelQuantity(SpellFuelType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    public  SpellFuelQuantity add(int q) {
        this.quantity += q;
        return this;
    }

    public boolean depleteIfSatisfied(Player player) {
        if(player instanceof ServerPlayer p) {
            if(!canAfford(p)) {
                return false;
            }
            final int[] remaining = {quantity};
            CuriosApi.getCuriosInventory(player).ifPresent((itemHandler)-> {
                int slots = itemHandler.getEquippedCurios().getSlots();
                Main.LOGGER.info("Slots: " + slots);
                for (int i = 0; i < slots; i++) {
                    ItemStack s = itemHandler.getEquippedCurios().getStackInSlot(i);
                    Main.LOGGER.info(i + ": " + s);
                    remaining[0] = withdrawFromStack(s, remaining[0]);
                    if (remaining[0] <= 0) {
                        break;
                    }
                }
            });
            if (remaining[0] <= 0) {
                return true;
            }
            for(int i = 0; i < p.getInventory().getContainerSize(); i++) {
                ItemStack s = p.getInventory().getItem(i);
                remaining[0] = withdrawFromStack(s, remaining[0]);
                if (remaining[0] <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private int withdrawFromStack(ItemStack s, int remaining) {
        if(s.getItem() instanceof SpellFuelStorage fs) {
            SpellFuelQuantity q = fs.getSpellFuelQuantity(s);
            if(q.type == type && q.quantity > 0) {
                int d = Math.min(remaining, q.quantity);
                fs.changeSpellFuelQuantity(s, -d);
                remaining -= d;
            }
        }
        return remaining;
    }

    public boolean canAfford(ServerPlayer player) {
        final int[] remaining = {quantity};

        CuriosApi.getCuriosInventory(player).ifPresent((itemHandler)-> {
            int slots = itemHandler.getEquippedCurios().getSlots();
            Main.LOGGER.info("Slots: " + slots);
            for (int i = 0; i < slots; i++) {
                ItemStack s = itemHandler.getEquippedCurios().getStackInSlot(i);
                if(s.getItem() instanceof SpellFuelStorage fs) {
                    SpellFuelQuantity q = fs.getSpellFuelQuantity(s);
                    if(q.type != type) {
                        continue;
                    }
                    if(q.quantity > 0) {
                        remaining[0] -= q.quantity;
                        if (remaining[0] <= 0) {
                            break;
                        }
                    }
                }
            }
        });
        if (remaining[0] <= 0) {
            return true;
        }
        for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack s = player.getInventory().getItem(i);
            if(s.getItem() instanceof SpellFuelStorage fs) {
                SpellFuelQuantity q = fs.getSpellFuelQuantity(s);
                if(q.type != type) {
                    continue;
                }
                if(q.quantity > 0) {
                    remaining[0] -= q.quantity;
                    if (remaining[0] <= 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
