package abyssal.spells;

import abyssal.Main;
import abyssal.components.SpellBatteryComponent;
import abyssal.init.ModDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public record SpellFuelQuantity (SpellFuelType type, int quantity) {

    public static final MapCodec<SpellFuelQuantity> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    SpellFuelType.CODEC.fieldOf("type").forGetter(SpellFuelQuantity::type),
                    Codec.intRange(-10000000, 10000000).fieldOf("quantity").forGetter(SpellFuelQuantity::quantity)
            ).apply(i, SpellFuelQuantity::new));

    public static final SpellFuelQuantity NONE = new SpellFuelQuantity(SpellFuelTypes.FUEL_NONE, 0);

    public SpellFuelQuantity add(int q) {
        return new SpellFuelQuantity(type, quantity + q);
    }

    public boolean depleteIfSatisfied(Player player) {
        if(player instanceof ServerPlayer p) {
            if(!canAfford(p)) {
                return false;
            }
            final int[] remaining = {quantity};
            CuriosApi.getCuriosInventory(player).ifPresent((itemHandler)-> {
                int slots = itemHandler.getEquippedCurios().getSlots();
                for (int i = 0; i < slots; i++) {
                    ItemStack s = itemHandler.getEquippedCurios().getStackInSlot(i);
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

    public boolean topUp(Player player) {
        final int[] remaining = {quantity};
        if(player instanceof ServerPlayer p) {
            CuriosApi.getCuriosInventory(player).ifPresent((itemHandler)-> {
                int slots = itemHandler.getEquippedCurios().getSlots();
                for (int i = 0; i < slots; i++) {
                    ItemStack s = itemHandler.getEquippedCurios().getStackInSlot(i);
                    remaining[0] = topUpStack(s, remaining[0]);
                    if (remaining[0] <= 0) {
                        break;
                    }
                }
            });
            if (remaining[0] <= 0) {
                return quantity != remaining[0];
            }
            for(int i = 0; i < p.getInventory().getContainerSize(); i++) {
                ItemStack s = p.getInventory().getItem(i);
                remaining[0] = topUpStack(s, remaining[0]);
                if (remaining[0] <= 0) {
                    return quantity != remaining[0];
                }
            }
        }
        return quantity != remaining[0];
    }

    private int withdrawFromStack(ItemStack s, int remaining) {
        if(s.has(ModDataComponents.SPELL_BATTERY)) {
            SpellBatteryComponent batt = s.get(ModDataComponents.SPELL_BATTERY);
            int stored = batt.stored();
            SpellFuelQuantity capacity = batt.capacity();

            if((capacity.type == type || capacity.type == SpellFuelTypes.FUEL_COLOURLESS) && stored > 0) {
                int d = Math.min(remaining, stored);
                SpellBatteryComponent newBatt = new SpellBatteryComponent(capacity, stored - d);
                s.set(ModDataComponents.SPELL_BATTERY, newBatt);
                remaining -= d;
            }
        }
        return remaining;
    }

    private int topUpStack(ItemStack s, int remaining) {
        if(s.has(ModDataComponents.SPELL_BATTERY)) {
            SpellBatteryComponent batt = s.get(ModDataComponents.SPELL_BATTERY);
            int stored = batt.stored();
            SpellFuelQuantity capacity = batt.capacity();

            if((capacity.type == type || type == SpellFuelTypes.FUEL_COLOURLESS) && capacity.quantity > stored) {
                int d = Math.min(remaining, capacity.quantity - stored);
                SpellBatteryComponent newBatt = new SpellBatteryComponent(capacity, stored + d);
                remaining -= d;
            }
        }
        return remaining;
    }

    public boolean canAfford(ServerPlayer player) {
        final int[] remaining = {quantity};
        Main.LOGGER.info("Checking if we can afford quantity " + remaining[0] + " of " + type.id());
        CuriosApi.getCuriosInventory(player).ifPresent((itemHandler)-> {
            int slots = itemHandler.getEquippedCurios().getSlots();
            for (int i = 0; i < slots; i++) {
                ItemStack s = itemHandler.getEquippedCurios().getStackInSlot(i);
                if(s.has(ModDataComponents.SPELL_BATTERY)) {
                    SpellBatteryComponent batt = s.get(ModDataComponents.SPELL_BATTERY);
                    int stored = batt.stored();
                    SpellFuelQuantity capacity = batt.capacity();

                    if(!capacity.type.equals(type) && !capacity.type.equals(SpellFuelTypes.FUEL_COLOURLESS)) {
                        continue;
                    }

                    if(stored > 0) {
                        remaining[0] -= stored;
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
            if(s.has(ModDataComponents.SPELL_BATTERY)) {
                SpellBatteryComponent batt = s.get(ModDataComponents.SPELL_BATTERY);
                int stored = batt.stored();
                SpellFuelQuantity capacity = batt.capacity();

                Main.LOGGER.info(stored + "/" + capacity.quantity() + " of " + capacity.type.id());
                if(!capacity.type.equals(type) && !capacity.type.equals(SpellFuelTypes.FUEL_COLOURLESS)) {
                    continue;
                }

                if(stored > 0) {
                    remaining[0] -= stored;
                    if (remaining[0] <= 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
