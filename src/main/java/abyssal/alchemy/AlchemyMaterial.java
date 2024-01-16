package abyssal.alchemy;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record AlchemyMaterial(int cost, double inherentPurity, double requiredPurity, Supplier<ItemStack> item) {

    // Supplier is unpacked in Alchemy#populateReverseBoard
    public AlchemyMaterial(int cost, double inherentPurity, double requiredPurity, Supplier<ItemStack> item) {
        this.item = item;
        this.cost = cost;
        this.inherentPurity = inherentPurity;
        this.requiredPurity = requiredPurity;

        if(this.requiredPurity > this.inherentPurity) {
            throw new IllegalArgumentException("Material is inherently more impure than required purity for production");
        }
    }

    // RegistryObjects can act as the supplier here e.g. new AlchemyMaterial(ModItems.SALTPETRE, ...)
    public AlchemyMaterial(Supplier<Item> itemSupplier, int cost, double inherentPurity, double requiredPurity) {
        this(cost, inherentPurity, requiredPurity, () -> new ItemStack(itemSupplier.get()));
    }

    public AlchemyMaterial(Supplier<Item> item, int cost) {
        this(item, cost, AlchemyMaterials.AVERAGE_PURITY);
    }

    public AlchemyMaterial(Supplier<Item> item, int cost, double inherentPurity) {
        this(item, cost, inherentPurity, AlchemyMaterials.STANDARD_MIN_PURITY_REQ);
    }

    public AlchemyMaterial(Item vanillaItem, int cost) {
        this(vanillaItem, cost, AlchemyMaterials.AVERAGE_PURITY);
    }

    public AlchemyMaterial(Item vanillaItem, int cost, double inherentPurity) {
        this(vanillaItem, cost, inherentPurity, AlchemyMaterials.STANDARD_MIN_PURITY_REQ);
    }

    public AlchemyMaterial(Item vanillaItem, int cost, double inherentPurity, double requiredPurity) {
        this(()-> vanillaItem, cost, inherentPurity, requiredPurity);
    }



}
