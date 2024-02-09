package abyssal.data;

import abyssal.Main;
import abyssal.init.Gems;
import abyssal.init.ModBlocks;
import abyssal.init.ModItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        makeOverrides(recipeOutput);
        makeGemBlocks(recipeOutput);
        makeMetalConversions(recipeOutput);
        makeComponents(recipeOutput);
        makeTools(recipeOutput);
        makeCurios(recipeOutput);
        makeSpellItems(recipeOutput);
        makeUUs(recipeOutput);
        makeSmeltingBlasting(recipeOutput);
        makeCoalLikes(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.ELDER_PINE_DOOR.get())
                .pattern("pp")
                .pattern("pp")
                .pattern("pp")
                .define('p', ModBlocks.ELDER_PINE_PLANKS.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.ELDER_PINE_PLANKS.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "elder_pine_door"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.POWDER_BARREL.get())
                .pattern(" g ")
                .pattern("gbg")
                .pattern(" g ")
                .define('g', Items.GUNPOWDER)
                .define('b', Tags.Items.BARRELS_WOODEN)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GUNPOWDER))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "powder_barrel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.POWDER_BARREL_KNOCK.get())
                .pattern(" g ")
                .pattern("gbg")
                .pattern(" g ")
                .define('g', Tags.Items.GUNPOWDER)
                .define('b', ModBlocks.POWDER_BARREL.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.POWDER_BARREL.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "powder_barrel_knock"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.POWDER_BARREL_FRAG.get())
                .pattern("nnn")
                .pattern("nbn")
                .pattern("nnn")
                .define('n', Items.IRON_NUGGET)
                .define('b', ModBlocks.POWDER_BARREL.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.POWDER_BARREL.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "powder_barrel_frag"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.LAPIDARY.get())
                .pattern(" i ")
                .pattern("ldl")
                .pattern("ddd")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('d', Blocks.COBBLED_DEEPSLATE)
                .define('l', Items.LAPIS_LAZULI)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LAPIS_LAZULI))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "lapidary_table"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.HARMONISER.get())
                .pattern("grg")
                .pattern("fcf")
                .pattern("dgd")
                .define('g', ModTags.Items.INGOTS_GOLDLIKE)
                .define('f', ModItems.ALCHEMICAL_FILTER.get())
                .define('d', Blocks.COBBLED_DEEPSLATE)
                .define('c', Items.CAULDRON)
                .define('r', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET))
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "alchemy"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PHILO_STONE.get())
                .pattern(" g ")
                .pattern("pbp")
                .pattern("sis")
                .define('g', Items.GOLD_INGOT) // intentionally not goldlikes
                .define('b', ModItems.BLANKEST_SLATE.get())
                .define('p', ModBlocks.PRISM.get())
                .define('s', ModBlocks.SUPER_SOIL.get())
                .define('i', ModItems.INFERNO_ESSENCE.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BLANKEST_SLATE.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "magnum_opus"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ELDER_PINE_PLANKS.get(),4)
                .requires(ModBlocks.ELDER_PINE_LOG.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.ELDER_PINE_LOG.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "elder_pine_planks"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PAPER, 3)
                .requires(ModBlocks.REED.get(), 3)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.REED.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "reed_paper"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModItems.FISH_PAINTING.get())
                .requires(Items.PAINTING)
                .requires(ItemTags.FISHES)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PAINTING))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "fish_painting"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CROC.get())
                .requires(Items.SUGAR)
                .requires(Items.SWEET_BERRIES)
                .requires(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.GARNET))
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.GARNET)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "croc"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.BAT_WING_SOUP.get())
                .requires(Items.BOWL)
                .requires(ModItems.BAT_WING.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BAT_WING.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "bat_wing_soup"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModBlocks.CORN_SEED.get(), 3)
                .requires(ModItems.CORN.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.CORN_SEED.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "corn"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.BIRCH_LOG)
                .requires(ModBlocks.MOSSY_BIRCH.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.MOSSY_BIRCH.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "birch_remove_moss"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_BIRCH.get())
                .requires(Blocks.BIRCH_LOG)
                .requires(Blocks.MOSS_CARPET)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.MOSSY_BIRCH.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "birch_add_moss"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_LOG)
                .requires(ModBlocks.MOSSY_OAK.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.MOSSY_OAK.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "oak_remove_moss"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_OAK.get())
                .requires(Blocks.OAK_LOG)
                .requires(Blocks.MOSS_CARPET)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.MOSSY_OAK.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "oak_add_moss"));

    }

    private void armour(RecipeOutput out, TagKey<Item> material, Item head, Item chest, Item legs, Item boots, ItemLike unlock) {

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, head)
                .pattern("mmm")
                .pattern("m m")
                .define('m', material)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(unlock))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chest)
                .pattern("m m")
                .pattern("mmm")
                .pattern("mmm")
                .define('m', material)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(unlock))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, legs)
                .pattern("mmm")
                .pattern("m m")
                .pattern("m m")
                .define('m', material)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(unlock))
                .save(out);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots)
                .pattern("m m")
                .pattern("m m")
                .define('m', material)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(unlock))
                .save(out);

    }

    private void makeOverrides(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHAIN, 4)
                .pattern("n")
                .pattern("i")
                .pattern("n")
                .define('n', Items.IRON_NUGGET)
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(recipeOutput, new ResourceLocation("minecraft", "chain"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.GOLDEN_APPLE)
                .pattern(" n ")
                .pattern("nan")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('a', Items.APPLE)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.APPLE))
                .save(recipeOutput, new ResourceLocation("minecraft", "golden_apple"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GOLDEN_CARROT)
                .requires(Items.GOLD_NUGGET)
                .requires(Items.CARROT)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CARROT))
                .save(recipeOutput, new ResourceLocation("minecraft", "golden_carrot"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GLISTERING_MELON_SLICE)
                .requires(Items.GOLD_NUGGET)
                .requires(Items.MELON_SLICE)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.MELON_SLICE))
                .save(recipeOutput, new ResourceLocation("minecraft", "glistering_melon_slice"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.CLOCK)
                .pattern(" n ")
                .pattern("nmn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('m', ModItems.INFERNAL_MECHANISM.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
                .save(recipeOutput, new ResourceLocation("minecraft", "clock"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.GOLDEN_PICKAXE)
                .pattern(" n ")
                .pattern("ntn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('t', Items.STONE_PICKAXE)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
                .save(recipeOutput, new ResourceLocation("minecraft", "golden_pickaxe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.GOLDEN_AXE)
                .pattern(" n ")
                .pattern("ntn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('t', Items.STONE_AXE)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
                .save(recipeOutput, new ResourceLocation("minecraft", "golden_axe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.GOLDEN_SWORD)
                .pattern(" n ")
                .pattern("ntn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('t', Items.STONE_SWORD)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
                .save(recipeOutput, new ResourceLocation("minecraft", "golden_sword"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.GOLDEN_SHOVEL)
                .pattern(" n ")
                .pattern("ntn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('t', Items.STONE_SHOVEL)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
                .save(recipeOutput, new ResourceLocation("minecraft", "golden_shovel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.GOLDEN_HOE)
                .pattern(" n ")
                .pattern("ntn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('t', Items.STONE_HOE)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
                .save(recipeOutput, new ResourceLocation("minecraft", "golden_hoe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .pattern("n")
                .pattern("p")
                .define('n', Items.GOLD_NUGGET)
                .define('p', ItemTags.WOODEN_PRESSURE_PLATES)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
                .save(recipeOutput, new ResourceLocation("minecraft", "light_weighted_pressure_plate"));

        templateCopy(recipeOutput, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Items.NETHERRACK);
        templateCopy(recipeOutput, Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE);
        templateCopy(recipeOutput, Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.SANDSTONE);
        templateCopy(recipeOutput, Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE);
        templateCopy(recipeOutput, Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, Items.MOSSY_COBBLESTONE);
        templateCopy(recipeOutput, Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLED_DEEPSLATE);
        templateCopy(recipeOutput, Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.END_STONE);
        templateCopy(recipeOutput, Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE);
        templateCopy(recipeOutput, Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.PRISMARINE);
        templateCopy(recipeOutput, Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, Items.BLACKSTONE);
        templateCopy(recipeOutput, Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, Items.NETHERRACK);
        templateCopy(recipeOutput, Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.PURPUR_BLOCK);
        templateCopy(recipeOutput, Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLED_DEEPSLATE);
        templateCopy(recipeOutput, Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
        templateCopy(recipeOutput, Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
        templateCopy(recipeOutput, Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
        templateCopy(recipeOutput, Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
    }

    private void makeGemBlocks(RecipeOutput recipeOutput) {
        for(Gems.GemType gem : Gems.GemType.values()) {
            if(!Gems.isVanillaGemBlock(Gems.GemBlockType.SLATE, gem)) {
                Item gemItem = Gems.gem(Gems.GemSize.SMALL, gem);
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.SLATE, gem))
                        .pattern(" g ")
                        .pattern("gSg")
                        .pattern(" g ")
                        .define('g', gemItem)
                        .define('S', Blocks.COBBLED_DEEPSLATE)
                        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(gemItem))
                        .save(recipeOutput);
            }
            if(!Gems.isVanillaGemBlock(Gems.GemBlockType.SILVERED, gem)) {
                if(gem != Gems.GemType.NONE) {
                    Item gemItem = Gems.gem(Gems.GemSize.SMALL, gem);
                    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.SILVERED, gem))
                            .pattern("sgs")
                            .pattern("gSg")
                            .pattern("sgs")
                            .define('g', gemItem)
                            .define('s', ModItems.SILVER_INGOT.get())
                            .define('S', Blocks.COBBLED_DEEPSLATE)
                            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(gemItem))
                            .save(recipeOutput, new ResourceLocation(Main.MOD_ID, Gems.gemBlockName(Gems.GemBlockType.SILVERED, gem) + "_combined"));
                    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.SILVERED, gem))
                            .pattern(" g ")
                            .pattern("gGg")
                            .pattern(" g ")
                            .define('g', gemItem)
                            .define('G', Gems.gemBlock(Gems.GemBlockType.SILVERED, Gems.GemType.NONE))
                            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(gemItem))
                            .save(recipeOutput, new ResourceLocation(Main.MOD_ID, Gems.gemBlockName(Gems.GemBlockType.SILVERED, gem) + "_only_gem"));
                }
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.SILVERED, gem))
                        .pattern("s s")
                        .pattern(" G ")
                        .pattern("s s")
                        .define('s', ModItems.SILVER_INGOT.get())
                        .define('G', Gems.gemBlock(Gems.GemBlockType.SLATE, gem))
                        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SILVER_INGOT.get()))
                        .save(recipeOutput, new ResourceLocation(Main.MOD_ID, Gems.gemBlockName(Gems.GemBlockType.SILVERED, gem) + "_only_ingot"));
            }
            if(!Gems.isVanillaGemBlock(Gems.GemBlockType.GILDED, gem)) {
                if(gem != Gems.GemType.NONE) {
                    Item gemItem = Gems.gem(Gems.GemSize.SMALL, gem);
                    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.GILDED, gem))
                            .pattern("sgs")
                            .pattern("gSg")
                            .pattern("sgs")
                            .define('g', gemItem)
                            .define('s', ModItems.ALCHEMICAL_GOLD_INGOT.get())
                            .define('S', Blocks.COBBLED_DEEPSLATE)
                            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(gemItem))
                            .save(recipeOutput, new ResourceLocation(Main.MOD_ID, Gems.gemBlockName(Gems.GemBlockType.GILDED, gem) + "_combined"));
                    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.GILDED, gem))
                            .pattern(" g ")
                            .pattern("gGg")
                            .pattern(" g ")
                            .define('g', gemItem)
                            .define('G', Gems.gemBlock(Gems.GemBlockType.GILDED, Gems.GemType.NONE))
                            .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(gemItem))
                            .save(recipeOutput, new ResourceLocation(Main.MOD_ID, Gems.gemBlockName(Gems.GemBlockType.GILDED, gem) + "_only_gem"));
                }
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.GILDED, gem))
                        .pattern("s s")
                        .pattern(" G ")
                        .pattern("s s")
                        .define('s', Items.GOLD_INGOT)
                        .define('G', Gems.gemBlock(Gems.GemBlockType.SLATE, gem))
                        .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT))
                        .save(recipeOutput, new ResourceLocation(Main.MOD_ID, Gems.gemBlockName(Gems.GemBlockType.GILDED, gem) + "_only_ingot"));
            }
        }
    }

    private void makeMetalConversions(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILVER_INGOT.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.SILVER_NUGGET.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SILVER_NUGGET.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "silver_ingot"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SILVER_BLOCK.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SILVER_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "silver_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NETHER_BRASS_INGOT.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.NETHER_BRASS_NUGGET.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHER_BRASS_NUGGET.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "nether_brass_ingot"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.NETHER_BRASS_BLOCK.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.NETHER_BRASS_INGOT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHER_BRASS_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "nether_brass_block"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ALCHEMICAL_GOLD_INGOT.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.ALCHEMICAL_GOLD_NUGGET.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ALCHEMICAL_GOLD_NUGGET.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "alchemical_gold_ingot"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ALCHEMICAL_GOLD_BLOCK.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.ALCHEMICAL_GOLD_INGOT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ALCHEMICAL_GOLD_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "alchemical_gold_block"));


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILVER_NUGGET.get(), 9)
                .requires(ModItems.SILVER_INGOT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SILVER_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "silver_nugget"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 9)
                .requires(ModBlocks.SILVER_BLOCK.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.SILVER_BLOCK.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "silver_ingot_from_block"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.NETHER_BRASS_NUGGET.get(), 9)
                .requires(ModItems.NETHER_BRASS_INGOT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHER_BRASS_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "nether_brass_nugget"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.NETHER_BRASS_INGOT.get(), 9)
                .requires(ModBlocks.NETHER_BRASS_BLOCK.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.NETHER_BRASS_BLOCK.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "nether_brass_ingot_from_block"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ALCHEMICAL_GOLD_NUGGET.get(), 9)
                .requires(ModItems.ALCHEMICAL_GOLD_INGOT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ALCHEMICAL_GOLD_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "alchemical_gold_nugget"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ALCHEMICAL_GOLD_INGOT.get(), 9)
                .requires(ModBlocks.ALCHEMICAL_GOLD_BLOCK.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.ALCHEMICAL_GOLD_BLOCK.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "alchemical_gold_ingot_from_block"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GOLD_COIN.get(), 32)
                .pattern(" i ")
                .pattern("i i")
                .pattern(" i ")
                .define('i', Items.GOLD_INGOT)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_INGOT))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "gold_coin"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILVER_COIN.get(), 16)
                .pattern(" i ")
                .pattern("i i")
                .pattern(" i ")
                .define('i', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SILVER_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "silver_coin"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COPPER_COIN.get(), 16)
                .pattern(" i ")
                .pattern("i i")
                .pattern(" i ")
                .define('i', Items.COPPER_INGOT)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.COPPER_INGOT))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "copper_coin"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GOLD_INGOT, 1)
                .requires(ModItems.GOLD_COIN.get(), 8)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GOLD_COIN.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "gold_coin_reverse"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 1)
                .requires(ModItems.SILVER_COIN.get(), 4)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SILVER_COIN.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "silver_coin_reverse"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COPPER_INGOT, 1)
                .requires(ModItems.COPPER_COIN.get(), 4)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.COPPER_COIN.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "copper_coin_reverse"));
    }

    private void makeComponents(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BLAZE_ROD)
                .pattern("s")
                .pattern("i")
                .pattern("p")
                .define('s', ModItems.SULFUR.get())
                .define('i', ModItems.NETHER_BRASS_INGOT.get())
                .define('p', Items.BLAZE_POWDER)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.BLAZE_POWDER))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "blaze_rod_from_ingot"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.INFERNAL_MECHANISM.get())
                .pattern("rbr")
                .pattern("bgb")
                .pattern("rbr")
                .define('b', ModItems.NETHER_BRASS_INGOT.get())
                .define('r', Items.REDSTONE)
                .define('g', Items.GOLD_NUGGET)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHER_BRASS_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "infernal_mechanism"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ALCHEMICAL_FILTER.get())
                .pattern("dgd")
                .pattern("gdg")
                .pattern("dgd")
                .define('d', Gems.gem(Gems.GemSize.POWDER, Gems.GemType.GARNET))
                .define('g', ModTags.Items.NUGGETS_GOLDLIKE)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "alchemical_filter"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIAMOND_INGOT.get())
                .pattern("ddd")
                .pattern("did")
                .pattern("ddd")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('d', Gems.gem(Gems.GemSize.TINY, Gems.GemType.DIAMOND))
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.TINY, Gems.GemType.DIAMOND)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "diamond_ingot"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GUNPOWDER, 3)
                .requires(Items.CHARCOAL)
                .requires(ModItems.SALTPETRE.get())
                .requires(ModItems.SULFUR.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GUNPOWDER))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "gunpowder"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PRISMATIC_POWDER.get())
                .requires(Items.PRISMARINE_CRYSTALS)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PRISMARINE_CRYSTALS))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "prismatic_powder"));
    }
    private void makeTools(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.WARMOGS.get())
                .pattern("l l")
                .pattern("rbr")
                .pattern("lgl")
                .define('b', ModItems.REJUVENATION_BEAD.get())
                .define('r', ModItems.RUBY_CRYSTAL.get())
                .define('l', ItemTags.LOGS)
                .define('g', ModItems.GIANTS_BELT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.REJUVENATION_BEAD.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "warmogs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SPECTRES_COWL.get())
                .pattern(" n ")
                .pattern("prp")
                .define('n', ModItems.NULL_MAGIC_MANTLE.get())
                .define('p', Items.PHANTOM_MEMBRANE)
                .define('r', ModItems.RUBY_CRYSTAL.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NULL_MAGIC_MANTLE.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "spectres_cowl"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SPIRIT_VISAGE.get())
                .pattern("s")
                .pattern("c")
                .pattern("r")
                .define('s', ModItems.SPECTRES_COWL.get())
                .define('c', ModItems.SILVER_CHESTPLATE.get())
                .define('r', ModItems.RUBY_CRYSTAL.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SPECTRES_COWL.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "spirit_visage"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MERC_TREADS.get())
                .pattern(" n ")
                .pattern("dgd")
                .pattern(" b ")
                .define('n', ModItems.NULL_MAGIC_MANTLE.get())
                .define('d', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND))
                .define('b', Items.LEATHER_BOOTS)
                .define('g', Items.GOLD_INGOT)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NULL_MAGIC_MANTLE.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "merc_treads"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.MOBI_BOOTS.get())
                .pattern("m m")
                .pattern("b b")
                .define('b', ModItems.NETHER_BRASS_INGOT.get())
                .define('m', ModItems.INFERNAL_MECHANISM.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHER_BRASS_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "mobi_boots"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.NASHORS_TOOTH.get())
                .pattern("ab")
                .pattern("ab")
                .pattern("ar")
                .define('b', ModItems.NETHER_BRASS_INGOT.get())
                .define('r', Items.BLAZE_ROD)
                .define('a', Items.AMETHYST_SHARD)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHER_BRASS_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "nashors_tooth"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.NETHER_BRASS_AXE.get())
                .pattern("bb")
                .pattern("br")
                .pattern(" s")
                .define('b', ModItems.NETHER_BRASS_INGOT.get())
                .define('r', Items.BLAZE_ROD)
                .define('s', Items.STICK)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHER_BRASS_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "nether_brass_axe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.CHARRING_AXE.get())
                .pattern("bbo")
                .pattern("br ")
                .pattern(" r ")
                .define('b', ModItems.NETHER_BRASS_INGOT.get())
                .define('r', Items.BLAZE_ROD)
                .define('o', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.ONYX))
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHER_BRASS_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "charring_axe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TIAMAT.get())
                .pattern("igi")
                .pattern("iri")
                .pattern(" s ")
                .define('i', ModItems.NETHER_BRASS_INGOT.get())
                .define('g', Items.GOLD_NUGGET)
                .define('r', Items.BLAZE_ROD)
                .define('s', Items.STICK)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHER_BRASS_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "tiamat"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TITANIC_HYDRA.get())
                .pattern("i i")
                .pattern("rtr")
                .pattern(" i ")
                .define('t', ModItems.TIAMAT.get())
                .define('r', ModItems.RUBY_CRYSTAL.get())
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RUBY_CRYSTAL.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "titanic_hydra"));



        armour(recipeOutput, ModTags.Items.INGOTS_SILVER, ModItems.SILVER_HELMET.get(), ModItems.SILVER_CHESTPLATE.get(), ModItems.SILVER_LEGGINGS.get(), ModItems.SILVER_BOOTS.get(), ModItems.SILVER_INGOT.get());
        armour(recipeOutput, ItemTags.WOOL, ModItems.CLOTH_HELMET.get(), ModItems.CLOTH_CHESTPLATE.get(), ModItems.CLOTH_LEGGINGS.get(), ModItems.CLOTH_BOOTS.get(), Blocks.WHITE_WOOL);
//        armour(recipeOutput, Items.WHITE_WOOL, Items.LEATHER_HELMET, ModItems.LEATHER_CHESTPLATE.get(), ModItems.LEATHER_LEGGINGS.get(), ModItems.LEATHER_BOOTS.get());
    }

    private void makeCurios(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RUBY_CRYSTAL.get())
                .pattern("lrl")
                .pattern("dgd")
                .pattern("lrl")
                .define('l', Blocks.GLASS)
                .define('g', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET))
                .define('d', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND))
                .define('r', Items.REDSTONE)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "ruby_crystal"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GOLD_RING.get())
                .pattern(" g ")
                .pattern("g g")
                .pattern(" g ")
                .define('g', Items.GOLD_NUGGET)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "gold_ring"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SILVER_RING.get())
                .pattern(" s ")
                .pattern("s s")
                .pattern(" s ")
                .define('s', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SILVER_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "silver_ring"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BELT.get())
                .pattern(" l ")
                .pattern("l l")
                .pattern(" g ")
                .define('g', Items.GOLD_NUGGET)
                .define('l', Items.LEATHER)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GOLD_NUGGET))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "belt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ANCHOR_BELT.get())
                .pattern(" b ")
                .pattern(" c ")
                .pattern("iii")
                .define('i', Items.IRON_INGOT)
                .define('c', Items.CHAIN)
                .define('b', ModItems.BELT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "anchor_belt"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.SKULL_BELT.get())
                .requires(ModItems.BELT.get())
                .requires(ModItems.WACKY_SKULL.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WACKY_SKULL.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "skull_belt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GIANTS_BELT.get())
                .pattern(" l ")
                .pattern("l l")
                .pattern("grg")
                .define('g', Items.GOLD_INGOT)
                .define('l', Items.LEATHER)
                .define('r', ModItems.RUBY_CRYSTAL.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RUBY_CRYSTAL.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "giants_belt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COIN_PURSE.get())
                .pattern(" s ")
                .pattern("lnl")
                .pattern("lll")
                .define('n', Items.GOLD_NUGGET)
                .define('l', Items.LEATHER)
                .define('s', Items.STRING)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEATHER))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "coin_purse_leather"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COIN_PURSE.get())
                .pattern(" s ")
                .pattern("lnl")
                .pattern("lll")
                .define('n', Items.GOLD_NUGGET)
                .define('l', ItemTags.WOOL)
                .define('s', Items.STRING)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WHITE_WOOL))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "coin_purse_wool"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GLOW_RING.get())
                .pattern("dsd")
                .pattern("srs")
                .pattern("dsd")
                .define('d', Items.GLOWSTONE_DUST)
                .define('s', Items.GLOW_INK_SAC)
                .define('r', ModItems.GOLD_RING.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GLOW_INK_SAC))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "glow_ring"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DAMAGE_RING.get())
                .pattern(" d ")
                .pattern("drd")
                .pattern(" d ")
                .define('d', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND))
                .define('r', ModItems.SILVER_RING.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "damage_ring"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CHAMPIONS_RING.get())
                .pattern(" d ")
                .pattern("oro")
                .pattern(" o ")
                .define('d', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.DIAMOND))
                .define('r', ModItems.GOLD_RING.get())
                .define('o', Blocks.OBSIDIAN)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.DIAMOND)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "champions_ring"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModItems.LIGHT_TRAVEL_RING.get())
                .pattern("f f")
                .pattern("srs")
                .pattern("fgf")
                .define('r', ModItems.GOLD_RING.get())
                .define('g', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET))
                .define('s', ModItems.SILVER_INGOT.get())
                .define('f', Items.FEATHER)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GOLD_RING.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "light_travel_ring"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DORANS_RING.get())
                .pattern("plp")
                .pattern("lrl")
                .pattern("plp" )
                .define('r', ModItems.SILVER_RING.get())
                .define('p', Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND))
                .define('l', Items.LAPIS_LAZULI)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "dorans_ring"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.GLASS_CANNON_RING.get())
                .pattern("sgs")
                .pattern("gxg")
                .pattern("sgs")
                .define('s', ModItems.SILVER_INGOT.get())
                .define('g', Blocks.GLASS)
                .define('x', ModBlocks.POWDER_BARREL.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SILVER_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "glass_cannon_ring"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LUCK_CHARM.get())
                .pattern("lgl")
                .pattern("gfg")
                .pattern("lgl")
                .define('f', Items.RABBIT_FOOT)
                .define('l', ModItems.FOUR_LEAF_CLOVER.get())
                .define('g', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.GARNET))
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.RABBIT_FOOT))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "luck_charm"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TOXIC_TOTEM.get())
                .pattern(" i ")
                .pattern("gLg")
                .pattern(" m ")
                .define('i', Items.SPIDER_EYE)
                .define('L', Items.JUNGLE_LOG)
                .define('g', Gems.gem(Gems.GemSize.TINY, Gems.GemType.ONYX))
                .define('m', Items.GOLD_NUGGET)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.TINY, Gems.GemType.ONYX)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "toxic_totem"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.CLEANSING_TOTEM.get())
                .pattern(" i ")
                .pattern("gLg")
                .pattern(" m ")
                .define('i', Items.HONEYCOMB)
                .define('L', Items.MANGROVE_LOG)
                .define('g', Gems.gem(Gems.GemSize.TINY, Gems.GemType.DIAMOND))
                .define('m', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.TINY, Gems.GemType.DIAMOND)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "cleansing_totem"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.RAGE_TOTEM.get())
                .pattern(" i ")
                .pattern("gLg")
                .pattern(" m ")
                .define('i', Items.BLAZE_POWDER)
                .define('L', Items.SPRUCE_LOG)
                .define('g', Gems.gem(Gems.GemSize.TINY, Gems.GemType.GARNET))
                .define('m', Items.COPPER_INGOT)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.TINY, Gems.GemType.GARNET)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "rage_totem"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.FISH_NECKLACE.get())
                .pattern("sss")
                .pattern("b b")
                .pattern("ofo")
                .define('s', Items.STRING)
                .define('b', Items.BONE_MEAL)
                .define('f', ItemTags.FISHES)
                .define('o', Gems.gem(Gems.GemSize.TINY, Gems.GemType.ONYX))
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.TINY, Gems.GemType.ONYX)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "fish_necklace"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.CLOCKWORK_AMULET.get())
                .pattern("s s")
                .pattern("mom")
                .pattern("ggg")
                .define('m', ModItems.INFERNAL_MECHANISM.get())
                .define('o', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.ONYX))
                .define('s', Items.STRING)
                .define('g', Items.GOLD_NUGGET)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.ONYX)))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "clockwork_amulet"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.NULL_MAGIC_MANTLE.get())
                .pattern("ngn")
                .pattern("psp")
                .pattern("nnn")
                .define('g', Items.GOLD_NUGGET)
                .define('p', Items.PHANTOM_MEMBRANE)
                .define('n', ModItems.SILVER_NUGGET.get())
                .define('s', Items.STRING)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.PHANTOM_MEMBRANE))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "null_magic_mantle"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.REJUVENATION_BEAD.get())
                .pattern("sd ")
                .pattern("ded")
                .pattern(" ds")
                .define('e', Items.EMERALD)
                .define('d', Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND))
                .define('s', Items.STRING)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.EMERALD))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "rejuvenation_bead"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.REJUVENATION_BELT.get())
                .pattern("rbr")
                .pattern(" r ")
                .define('r', ModItems.REJUVENATION_BEAD.get())
                .define('b', ModItems.BELT.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.REJUVENATION_BEAD.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "rejuvenation_belt"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.REJUVENATION_NECKLACE.get())
                .pattern(" s ")
                .pattern("r r")
                .pattern(" r ")
                .define('r', ModItems.REJUVENATION_BEAD.get())
                .define('s', Items.STRING)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.REJUVENATION_BEAD.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "rejuvenation_necklace"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.REJUVENATION_RING.get())
                .pattern("b")
                .pattern("r")
                .define('b', ModItems.REJUVENATION_BEAD.get())
                .define('r', ModItems.GOLD_RING.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.REJUVENATION_BEAD.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "rejuvenation_ring"));
    }

    private void makeSpellItems(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.AMP_TOME.get())
                .pattern("gsg")
                .pattern("sbs")
                .pattern("gsg")
                .define('g', Gems.gem(Gems.GemSize.TINY, Gems.GemType.GARNET))
                .define('s', ModItems.SILVER_INGOT.get())
                .define('b', Items.ENCHANTED_BOOK)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ENCHANTED_BOOK))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "amp_tome"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SKELETON_STAFF.get())
                .pattern("sgs")
                .pattern(" b ")
                .pattern(" s ")
                .define('g', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.GARNET))
                .define('s', ModItems.WACKY_SKULL.get())
                .define('b', Items.BONE)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WACKY_SKULL.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "skeleton_staff"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ENCHANTING_STAFF.get())
                .pattern("lDl")
                .pattern(" s ")
                .pattern(" s ")
                .define('s', ModItems.SILVER_INGOT.get())
                .define('l', Items.LAPIS_LAZULI)
                .define('D', Items.DIAMOND)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SILVER_INGOT.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "enchanting_staff"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.PORTAL_LIGHTER.get())
                .pattern("ofo")
                .pattern(" i ")
                .pattern(" i ")
                .define('i', Tags.Items.INGOTS_GOLD)
                .define('o', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.ONYX))
                .define('f', Items.FLINT_AND_STEEL)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.FLINT_AND_STEEL))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "portal_lighter"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.PORTAL_LIGHTER.get())
                .pattern("ofo")
                .pattern(" i ")
                .pattern(" i ")
                .define('i', Tags.Items.INGOTS_GOLD)
                .define('o', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.ONYX))
                .define('f', Items.FIRE_CHARGE)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.FIRE_CHARGE))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "portal_lighter_from_fire_charge"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.MOVER.get())
                .pattern("sri")
                .pattern(" i ")
                .pattern(" i ")
                .define('s', ModItems.WACKY_SKULL.get())
                .define('r', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET))
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WACKY_SKULL.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "spawner_mover"));
    }

    private void makeCoalLikes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Blocks.TORCH, 3)
                .pattern("c")
                .pattern("s")
                .define('c', ModItems.LIGNITE.get())
                .define('s', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.LIGNITE.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "lignite_torch"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Blocks.TORCH, 5)
                .pattern("c")
                .pattern("s")
                .define('c', ModItems.ANTHRACITE.get())
                .define('s', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ANTHRACITE.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "anthracite_torch"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Blocks.SOUL_TORCH, 3)
                .pattern("c")
                .pattern("s")
                .pattern("S")
                .define('c', ModItems.LIGNITE.get())
                .define('s', Tags.Items.RODS_WOODEN)
                .define('S', Blocks.SOUL_SOIL)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.LIGNITE.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "lignite_soul_torch"));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Blocks.SOUL_TORCH, 5)
                .pattern("c")
                .pattern("s")
                .pattern("S")
                .define('c', ModItems.ANTHRACITE.get())
                .define('s', Tags.Items.RODS_WOODEN)
                .define('S', Blocks.SOUL_SOIL)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ANTHRACITE.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "anthracite_soul_torch"));


        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Items.FIRE_CHARGE, 2)
                .requires(Items.GUNPOWDER)
                .requires(Items.BLAZE_POWDER)
                .requires(ModItems.LIGNITE.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.LIGNITE.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "lignite_fire_charge"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, Items.FIRE_CHARGE, 4)
                .requires(Items.GUNPOWDER)
                .requires(Items.BLAZE_POWDER)
                .requires(ModItems.ANTHRACITE.get())
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ANTHRACITE.get()))
                .save(recipeOutput, new ResourceLocation(Main.MOD_ID, "anthracite_fire_charge"));
    }

    private void makeSmeltingBlasting(RecipeOutput recipeOutput) {
        standardSmeltable(recipeOutput, ModBlocks.SILVER_ORE.get(), ModItems.SILVER_INGOT.get(), 0.7f);
        standardSmeltable(recipeOutput, ModBlocks.DEEPSLATE_SILVER_ORE.get(), ModItems.SILVER_INGOT.get(), 0.7f);
        standardSmeltable(recipeOutput, ModItems.RAW_SILVER.get(), ModItems.SILVER_INGOT.get(), 0.7f);
    }


    private void makeUUs(RecipeOutput recipeOutput) {
        uu(recipeOutput, Blocks.ACACIA_LOG);
        uu(recipeOutput, Blocks.BIRCH_LOG);
        uu(recipeOutput, Blocks.CHERRY_LOG);
        uu(recipeOutput, Blocks.DARK_OAK_LOG);
        uu(recipeOutput, Blocks.JUNGLE_LOG);
        uu(recipeOutput, Blocks.MANGROVE_LOG);
        uu(recipeOutput, Blocks.OAK_LOG);
        uu(recipeOutput, Blocks.SPRUCE_LOG);
        uu(recipeOutput, Blocks.CACTUS);
        uu(recipeOutput, Items.BAMBOO);
        uu(recipeOutput, Blocks.CRIMSON_STEM);
        uu(recipeOutput, Blocks.WARPED_STEM);
        uu(recipeOutput, Blocks.MOSS_BLOCK, 32);
        uu(recipeOutput, Blocks.ACACIA_LEAVES, 32);
        uu(recipeOutput, Blocks.AZALEA_LEAVES, 32);
        uu(recipeOutput, Blocks.BIRCH_LEAVES, 32);
        uu(recipeOutput, Blocks.CHERRY_LEAVES, 32);
        uu(recipeOutput, Blocks.DARK_OAK_LEAVES, 32);
        uu(recipeOutput, Blocks.FLOWERING_AZALEA_LEAVES, 32);
        uu(recipeOutput, Blocks.JUNGLE_LEAVES, 32);
        uu(recipeOutput, Blocks.MANGROVE_LEAVES, 32);
        uu(recipeOutput, Blocks.OAK_LEAVES, 32);
        uu(recipeOutput, Blocks.SPRUCE_LEAVES, 32);
        uu(recipeOutput, ModBlocks.ELDER_PINE_LOG.get(), 4);
    }

    private void uu(RecipeOutput out, Item inOut) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, inOut, 17)
                .requires(ModItems.UU_MATTER.get())
                .requires(inOut)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.UU_MATTER.get()))
                .save(out, ForgeRegistries.ITEMS.getKey(inOut) + "_uu");
    }

    private void uu(RecipeOutput out, Block inOut) {
        uu(out, inOut, 16);
    }

    private void uu(RecipeOutput out, Block inOut, int count) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, inOut, count+1)
                .requires(ModItems.UU_MATTER.get())
                .requires(inOut)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.UU_MATTER.get()))
                .save(out, ForgeRegistries.BLOCKS.getKey(inOut) + "_uu");
    }

    private void templateCopy(RecipeOutput out, Item template, ItemLike material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, template, 2)
                .define('#', ModItems.DIAMOND_INGOT.get())
                .define('d', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND))
                .define('C', material)
                .define('S', template)
                .pattern("dSd")
                .pattern("dCd")
                .pattern("d#d")
                .unlockedBy(getHasName(template), has(template))
                .save(out);
    }



    private void standardSmeltableOnly(RecipeOutput output, Item in, Item out, float xp) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(in), RecipeCategory.MISC,
                        out, xp, 200)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(in))
                .save(output, ForgeRegistries.ITEMS.getKey(in));
    }

    private void standardSmeltable(RecipeOutput output, Item in, Item out, float xp) {
        standardSmeltableOnly(output, in, out, xp);
        standardBlastable(output, in, out, xp);
    }

    private void standardSmeltable(RecipeOutput output, Block in, Item out, float xp) {
        standardSmeltable(output, in.asItem(), out, xp);
    }

    private void standardBlastable(RecipeOutput output, Item in, Item out, float xp) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(in), RecipeCategory.MISC,
                        out, xp, 100)
                .unlockedBy("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(in))
                .save(output, ForgeRegistries.ITEMS.getKey(in) + "_blast");
    }

    private void standardBlastable(RecipeOutput output, Block in, Item out, float xp) {
        standardBlastable(output, in.asItem(), out, xp);
    }
}
