package abyssal.alchemy;

import abyssal.Main;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AlchemyState {

    public double quantity;
    public double impurityQuantity;
    public Alchemy.BoardPosition position;

    public int temperature;

    public AlchemyState(Alchemy.BoardPosition position, double quantity, int temperature, double impurityQuantity) {
        this.position = position;
        this.quantity = quantity;
        this.temperature = temperature;
        this.impurityQuantity = impurityQuantity;
    }

    public static AlchemyState fromStack(ItemStack stack, int temperature) {
        Alchemy.BoardPosition pos = Alchemy.positionOf(stack.getItem());
        AlchemyMaterial m = Alchemy.material(stack.getItem());
        if(pos == null || m == null) {
            throw new IllegalArgumentException("ItemStack cannot be converted to AlchemyMaterial");
        }

        double qty = m.cost() * stack.getCount();
        double impQty = (qty / m.inherentPurity()) - qty;

        if(qty == 0) {
            throw new IllegalArgumentException("ItemStack cannot be priced as AlchemyMaterial");
        }
        return new AlchemyState(pos, qty, temperature, impQty);
    }

    public boolean canPrecipitate() {
        return getMaterialHere().getCategory() != Alchemy.Category.BLOCKER;
    }
    public List<ItemStack> getPrecipitationItemStacks() {
        if(!canPrecipitate()) {
            Main.LOGGER.info("Getting precipitate for a blocker!");
            return List.of();
        }
        if(quantity + impurityQuantity == 0) {
            return List.of();
        }
        double purity = quantity / (quantity + impurityQuantity);
        double remainingQuantity = quantity;
        ArrayList<ItemStack> outputs = new ArrayList<>();
        while(true) {
            AlchemyMaterial m = getMaterialHere().bestUnderConditions(remainingQuantity, purity);
            if(m == null) {
                break;
            }
            int count = (int) (remainingQuantity / m.cost());
            ItemStack output = m.item().get().copyWithCount(count);
            outputs.add(output);
            remainingQuantity -= count * m.cost();
        }
        return outputs;
    }

    public AlchemyMaterialGroup getMaterialHere() {
        return Alchemy.materialGroupAt(position);
    }

    public void apply(AlchemyReagent reagent) {
        Alchemy.Category category = getMaterialHere().getCategory();
        AlchemyReagentEffect effect = reagent.getEffect(temperature, category);
        Alchemy.BoardPosition candidatePosition = position.add(effect.dx, effect.dy);
        Alchemy.Category destinationCategory = Alchemy.materialGroupAt(candidatePosition).getCategory();
        if(destinationCategory != Alchemy.Category.BLOCKER) {
            double loss = (1 - ( destinationCategory == category ? effect.intraEfficiency : effect.interEfficiency)) * quantity;
            impurityQuantity += loss;
            quantity -= loss;
            position = candidatePosition;
        }

        temperature += effect.dt;
    }

    @Override
    public String toString() {
        return "[" + getMaterialHere().name + " at " + this.position + ", useful fuel " + quantity + ", total fuel " + (quantity+impurityQuantity) + ", purity " + (quantity/(quantity+impurityQuantity)) + "]";
    }

}
