package abyssal.items.handheld;

import abyssal.data.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;

public class ModItemTiers {

    public static final ToolMaterial NETHER_BRASS = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 450,
            4.0F, 2.0F, 5, ModTags.Items.NETHER_BRASS_TOOL_MATERIALS);

    public static final ToolMaterial HYDRA = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 800,
            4.0F, 2.0F, 0, ItemTags.IRON_TOOL_MATERIALS);

}
