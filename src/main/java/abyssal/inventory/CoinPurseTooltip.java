package abyssal.inventory;


import abyssal.components.CoinPurseBundleContents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record CoinPurseTooltip(CoinPurseBundleContents contents) implements TooltipComponent {

}