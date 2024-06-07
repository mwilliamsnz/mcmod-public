package abyssal.data;

import abyssal.Main;
import abyssal.init.Gems;
import abyssal.init.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(DataGenerator dataGenerator, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator.getPackOutput(), provider, Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        Gems.forAllGemBlocks((gemBlock) -> {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(gemBlock);
            this.tag(ModTags.Blocks.GEM_SLATES).add(gemBlock);
        });

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.LEAF_LITTER.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.SUPER_SOIL.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.GRASS_SUPER_SOIL.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.GOLD_GRAVEL.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.ABYSSAL_STONE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.ENKATITE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.NITRE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DEEPSLATE_NITRE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.SULFUR.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DEEPSLATE_SULFUR.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.LAPIDARY.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.SILVER_BLOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.NETHER_BRASS_BLOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.ALCHEMICAL_GOLD_BLOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.HARMONISER.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CHARRED_LOG.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.PRISM.get());


        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.ELDER_PINE_LOG.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.ELDER_PINE_PLANKS.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.ELDER_PINE_DOOR.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.IVY.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.MOSSY_BIRCH.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.MOSSY_OAK.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.POWDER_BARREL.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.POWDER_BARREL_FRAG.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.POWDER_BARREL_KNOCK.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.SHRUB.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.CHARRED_LOG.get());
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.AMP_BOOKSHELF.get());


        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.POOR_IRON_ORE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DEEPSLATE_POOR_IRON_ORE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.SILVER_ORE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DEEPSLATE_SILVER_ORE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.GARNET_CLUSTER.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DEEPSLATE_GARNET_CLUSTER.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.ONYX_CLUSTER.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DEEPSLATE_ONYX_CLUSTER.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.NETHER_ONYX_CLUSTER.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.GEM_CLUSTER.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DEEPSLATE_GEM_CLUSTER.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.SILVER_ORE.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.DEEPSLATE_SILVER_ORE.get());
        this.tag(ModTags.Blocks.SILVER_ORES).add(ModBlocks.SILVER_ORE.get());
        this.tag(ModTags.Blocks.SILVER_ORES).add(ModBlocks.DEEPSLATE_SILVER_ORE.get());

        this.tag(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.SILVER_BLOCK.get(), ModBlocks.NETHER_BRASS_BLOCK.get(), ModBlocks.ALCHEMICAL_GOLD_BLOCK.get());

        this.tag(BlockTags.LOGS).add(ModBlocks.ELDER_PINE_LOG.get());
        this.tag(BlockTags.LOGS_THAT_BURN).add(ModBlocks.ELDER_PINE_LOG.get());
        this.tag(BlockTags.LOGS_THAT_BURN).add(ModBlocks.MOSSY_OAK.get(), ModBlocks.MOSSY_BIRCH.get());
        this.tag(BlockTags.LOGS).add(ModBlocks.MOSSY_OAK.get(),ModBlocks.MOSSY_BIRCH.get());
        this.tag(BlockTags.OVERWORLD_NATURAL_LOGS).add(ModBlocks.MOSSY_OAK.get(), ModBlocks.MOSSY_BIRCH.get());
        this.tag(BlockTags.BIRCH_LOGS).add(ModBlocks.MOSSY_BIRCH.get());
        this.tag(BlockTags.OAK_LOGS).add(ModBlocks.MOSSY_OAK.get());
        this.tag(BlockTags.LEAVES).add(ModBlocks.THIN_LEAVES.get());
        this.tag(BlockTags.REPLACEABLE).add(ModBlocks.BRUSH.get());
        this.tag(BlockTags.REPLACEABLE).add(ModBlocks.ALPINE_PLANT.get());

        this.tag(BlockTags.DIRT).add(ModBlocks.LEAF_LITTER.get());
        this.tag(BlockTags.DIRT).add(ModBlocks.SUPER_SOIL.get());
        this.tag(BlockTags.DIRT).add(ModBlocks.GRASS_SUPER_SOIL.get());

        this.tag(ModTags.Blocks.GRASS_SPREADERS).add(ModBlocks.GRASS_SUPER_SOIL.get());
        this.tag(ModTags.Blocks.GRASS_SPREADERS).add(Blocks.GRASS_BLOCK);

        this.tag(Tags.Blocks.GRAVEL).add(ModBlocks.GOLD_GRAVEL.get());
        this.tag(BlockTags.BAMBOO_PLANTABLE_ON).add(ModBlocks.GOLD_GRAVEL.get());

        this.tag(Tags.Blocks.BOOKSHELVES).add(ModBlocks.AMP_BOOKSHELF.get());
        this.tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(ModBlocks.AMP_BOOKSHELF.get());
        this.tag(BlockTags.ENCHANTMENT_POWER_PROVIDER).remove(Blocks.BOOKSHELF);

        this.tag(ModTags.Blocks.CHARRING_AXE_DESTROYS).add(Blocks.VINE, ModBlocks.IVY.get());

    }
}