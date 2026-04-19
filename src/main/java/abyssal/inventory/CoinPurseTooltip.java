package abyssal.inventory;


import abyssal.items.curios.CoinPurseBundleContents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record CoinPurseTooltip(CoinPurseBundleContents contents) implements TooltipComponent {

}