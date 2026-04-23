package abyssal.data;

import abyssal.Main;
import abyssal.init.Gems;
import abyssal.init.ModBlocks;
import abyssal.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

import static abyssal.Main.rl;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        makeOverrides();
        makeGemBlocks();
        makeMetalConversions();
        makeComponents();
        makeTools();
        makeCurios();
        makeSpellItems();
        makeUUs();
        makeSmeltingBlasting();
        makeCoalLikes();

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ModBlocks.ELDER_PINE_DOOR.get())
                .pattern("pp")
                .pattern("pp")
                .pattern("pp")
                .define('p', ModBlocks.ELDER_PINE_PLANKS.get())
                .unlockedBy("has_item", has(ModBlocks.ELDER_PINE_PLANKS.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ModBlocks.POWDER_BARREL.get())
                .pattern(" g ")
                .pattern("gbg")
                .pattern(" g ")
                .define('g', Items.GUNPOWDER)
                .define('b', Tags.Items.BARRELS_WOODEN)
                .unlockedBy("has_item", has(Items.GUNPOWDER))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ModBlocks.POWDER_BARREL_KNOCK.get())
                .pattern(" g ")
                .pattern("gbg")
                .pattern(" g ")
                .define('g', Tags.Items.GUNPOWDERS)
                .define('b', ModBlocks.POWDER_BARREL.get())
                .unlockedBy("has_item", has(ModBlocks.POWDER_BARREL.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ModBlocks.POWDER_BARREL_FRAG.get())
                .pattern("nnn")
                .pattern("nbn")
                .pattern("nnn")
                .define('n', Items.IRON_NUGGET)
                .define('b', ModBlocks.POWDER_BARREL.get())
                .unlockedBy("has_item", has(ModBlocks.POWDER_BARREL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, ModBlocks.LAPIDARY.get())
                .pattern(" i ")
                .pattern("ldl")
                .pattern("ddd")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('d', Blocks.COBBLED_DEEPSLATE)
                .define('l', Items.LAPIS_LAZULI)
                .unlockedBy("has_item", has(Items.LAPIS_LAZULI))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, ModBlocks.HARMONISER.get())
                .pattern("grg")
                .pattern("fcf")
                .pattern("dgd")
                .define('g', ModTags.Items.INGOTS_GOLDLIKE)
                .define('f', ModItems.ALCHEMICAL_FILTER.get())
                .define('d', Blocks.COBBLED_DEEPSLATE)
                .define('c', Items.CAULDRON)
                .define('r', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET))
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.PHILO_STONE.get())
                .pattern(" g ")
                .pattern("pbp")
                .pattern("sis")
                .define('g', Items.GOLD_INGOT) // intentionally not goldlikes
                .define('b', ModItems.BLANKEST_SLATE.get())
                .define('p', ModBlocks.PRISM.get())
                .define('s', ModBlocks.SUPER_SOIL.get())
                .define('i', ModItems.INFERNO_ESSENCE.get())
                .unlockedBy("has_item", has(ModItems.BLANKEST_SLATE.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, ModBlocks.ELDER_PINE_PLANKS.get(),4)
                .requires(ModBlocks.ELDER_PINE_LOG.get())
                .unlockedBy("has_item", has(ModBlocks.ELDER_PINE_LOG.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, Items.PAPER, 3)
                .requires(ModBlocks.REED.get(), 3)
                .unlockedBy("has_item", has(ModBlocks.REED.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.DECORATIONS, ModItems.FISH_PAINTING.get())
                .requires(Items.PAINTING)
                .requires(ItemTags.FISHES)
                .unlockedBy("has_item", has(Items.PAINTING))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.FOOD, ModItems.CROC.get())
                .requires(Items.SUGAR)
                .requires(Items.SWEET_BERRIES)
                .requires(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.GARNET))
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.GARNET)))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.FOOD, ModItems.BAT_WING_SOUP.get())
                .requires(Items.BOWL)
                .requires(ModItems.BAT_WING.get())
                .unlockedBy("has_item", has(ModItems.BAT_WING.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.FOOD, ModBlocks.CORN_SEED.get(), 3)
                .requires(ModItems.CORN.get())
                .unlockedBy("has_item", has(ModBlocks.CORN_SEED.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, Blocks.BIRCH_LOG)
                .requires(ModBlocks.MOSSY_BIRCH.get())
                .unlockedBy("has_item", has(ModBlocks.MOSSY_BIRCH.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_BIRCH.get())
                .requires(Blocks.BIRCH_LOG)
                .requires(Blocks.MOSS_CARPET)
                .unlockedBy("has_item", has(ModBlocks.MOSSY_BIRCH.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, Blocks.OAK_LOG)
                .requires(ModBlocks.MOSSY_OAK.get())
                .unlockedBy("has_item", has(ModBlocks.MOSSY_OAK.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_OAK.get())
                .requires(Blocks.OAK_LOG)
                .requires(Blocks.MOSS_CARPET)
                .unlockedBy("has_item", has(ModBlocks.MOSSY_OAK.get()))
                .save(output);

    }

    private void armour(TagKey<Item> material, Item head, Item chest, Item legs, Item boots, ItemLike unlock) {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, head)
                .pattern("mmm")
                .pattern("m m")
                .define('m', material)
                .unlockedBy("has_item", has(unlock))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, chest)
                .pattern("m m")
                .pattern("mmm")
                .pattern("mmm")
                .define('m', material)
                .unlockedBy("has_item", has(unlock))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, legs)
                .pattern("mmm")
                .pattern("m m")
                .pattern("m m")
                .define('m', material)
                .unlockedBy("has_item", has(unlock))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, boots)
                .pattern("m m")
                .pattern("m m")
                .define('m', material)
                .unlockedBy("has_item", has(unlock))
                .save(output);

    }

    private void makeOverrides() {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, Items.IRON_CHAIN, 4)
                .pattern("n")
                .pattern("i")
                .pattern("n")
                .define('n', Items.IRON_NUGGET)
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, Items.GOLDEN_APPLE)
                .pattern(" n ")
                .pattern("nan")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('a', Items.APPLE)
                .unlockedBy("has_item", has(Items.APPLE))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, Items.GOLDEN_CARROT)
                .requires(Items.GOLD_NUGGET)
                .requires(Items.CARROT)
                .unlockedBy("has_item", has(Items.CARROT))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, Items.GLISTERING_MELON_SLICE)
                .requires(Items.GOLD_NUGGET)
                .requires(Items.MELON_SLICE)
                .unlockedBy("has_item", has(Items.MELON_SLICE))
                .save(output);

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, Items.CLOCK)
                .pattern(" n ")
                .pattern("nmn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('m', ModItems.INFERNAL_MECHANISM.get())
                .unlockedBy("has_item", has(Items.GOLD_NUGGET))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, Items.GOLDEN_PICKAXE)
                .pattern(" n ")
                .pattern("ntn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('t', Items.STONE_PICKAXE)
                .unlockedBy("has_item", has(Items.GOLD_NUGGET))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, Items.GOLDEN_AXE)
                .pattern(" n ")
                .pattern("ntn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('t', Items.STONE_AXE)
                .unlockedBy("has_item", has(Items.GOLD_NUGGET))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, Items.GOLDEN_SWORD)
                .pattern(" n ")
                .pattern("ntn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('t', Items.STONE_SWORD)
                .unlockedBy("has_item", has(Items.GOLD_NUGGET))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, Items.GOLDEN_SHOVEL)
                .pattern(" n ")
                .pattern("ntn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('t', Items.STONE_SHOVEL)
                .unlockedBy("has_item", has(Items.GOLD_NUGGET))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, Items.GOLDEN_HOE)
                .pattern(" n ")
                .pattern("ntn")
                .pattern(" n ")
                .define('n', Items.GOLD_NUGGET)
                .define('t', Items.STONE_HOE)
                .unlockedBy("has_item", has(Items.GOLD_NUGGET))
                .save(output);

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .pattern("n")
                .pattern("p")
                .define('n', Items.GOLD_NUGGET)
                .define('p', ItemTags.WOODEN_PRESSURE_PLATES)
                .unlockedBy("has_item", has(Items.GOLD_NUGGET))
                .save(output);

        templateCopy(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Items.NETHERRACK);
        templateCopy(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE);
        templateCopy(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.SANDSTONE);
        templateCopy(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE);
        templateCopy(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, Items.MOSSY_COBBLESTONE);
        templateCopy(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLED_DEEPSLATE);
        templateCopy(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.END_STONE);
        templateCopy(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLESTONE);
        templateCopy(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.PRISMARINE);
        templateCopy(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, Items.BLACKSTONE);
        templateCopy(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, Items.NETHERRACK);
        templateCopy(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.PURPUR_BLOCK);
        templateCopy(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, Items.COBBLED_DEEPSLATE);
        templateCopy(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
        templateCopy(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
        templateCopy(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
        templateCopy(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, Items.TERRACOTTA);
    }

    private void makeGemBlocks() {
        for(Gems.GemType gem : Gems.GemType.values()) {
            if(!Gems.isVanillaGemBlock(Gems.GemBlockType.SLATE, gem)) {
                Item gemItem = Gems.gem(Gems.GemSize.SMALL, gem);
                ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.SLATE, gem))
                        .pattern(" g ")
                        .pattern("gSg")
                        .pattern(" g ")
                        .define('g', gemItem)
                        .define('S', Blocks.COBBLED_DEEPSLATE)
                        .unlockedBy("has_item", has(gemItem))
                        .save(output);
            }
            if(!Gems.isVanillaGemBlock(Gems.GemBlockType.SILVERED, gem)) {
                if(gem != Gems.GemType.NONE) {
                    Item gemItem = Gems.gem(Gems.GemSize.SMALL, gem);
                    ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.SILVERED, gem))
                            .pattern("sgs")
                            .pattern("gSg")
                            .pattern("sgs")
                            .define('g', gemItem)
                            .define('s', ModItems.SILVER_INGOT.get())
                            .define('S', Blocks.COBBLED_DEEPSLATE)
                            .unlockedBy("has_item", has(gemItem))
                            .save(output, rl(Gems.gemBlockName(Gems.GemBlockType.SILVERED, gem)) + "_combined");
                    ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.SILVERED, gem))
                            .pattern(" g ")
                            .pattern("gGg")
                            .pattern(" g ")
                            .define('g', gemItem)
                            .define('G', Gems.gemBlock(Gems.GemBlockType.SILVERED, Gems.GemType.NONE))
                            .unlockedBy("has_item", has(gemItem))
                            .save(output, rl(Gems.gemBlockName(Gems.GemBlockType.SILVERED, gem)) + "_only_gem");
                }
                ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.SILVERED, gem))
                        .pattern("s s")
                        .pattern(" G ")
                        .pattern("s s")
                        .define('s', ModItems.SILVER_INGOT.get())
                        .define('G', Gems.gemBlock(Gems.GemBlockType.SLATE, gem))
                        .unlockedBy("has_item", has(ModItems.SILVER_INGOT.get()))
                        .save(output, Main.rl(Gems.gemBlockName(Gems.GemBlockType.SILVERED, gem)) + "_only_ingot");
            }
            if(!Gems.isVanillaGemBlock(Gems.GemBlockType.GILDED, gem)) {
                if(gem != Gems.GemType.NONE) {
                    Item gemItem = Gems.gem(Gems.GemSize.SMALL, gem);
                    ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.GILDED, gem))
                            .pattern("sgs")
                            .pattern("gSg")
                            .pattern("sgs")
                            .define('g', gemItem)
                            .define('s', ModItems.ALCHEMICAL_GOLD_INGOT.get())
                            .define('S', Blocks.COBBLED_DEEPSLATE)
                            .unlockedBy("has_item", has(gemItem))
                            .save(output, rl(Gems.gemBlockName(Gems.GemBlockType.GILDED, gem)) + "_combined");
                    ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.GILDED, gem))
                            .pattern(" g ")
                            .pattern("gGg")
                            .pattern(" g ")
                            .define('g', gemItem)
                            .define('G', Gems.gemBlock(Gems.GemBlockType.GILDED, Gems.GemType.NONE))
                            .unlockedBy("has_item", has(gemItem))
                            .save(output, rl(Gems.gemBlockName(Gems.GemBlockType.GILDED, gem)) + "_only_gem");
                }
                ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, Gems.gemBlock(Gems.GemBlockType.GILDED, gem))
                        .pattern("s s")
                        .pattern(" G ")
                        .pattern("s s")
                        .define('s', Items.GOLD_INGOT)
                        .define('G', Gems.gemBlock(Gems.GemBlockType.SLATE, gem))
                        .unlockedBy("has_item", has(Items.GOLD_INGOT))
                        .save(output, Main.rl(Gems.gemBlockName(Gems.GemBlockType.GILDED, gem)) + "_only_ingot");
            }
        }
    }

    private void makeMetalConversions() {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.SILVER_INGOT.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.SILVER_NUGGET.get())
                .unlockedBy("has_item", has(ModItems.SILVER_NUGGET.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, ModBlocks.SILVER_BLOCK.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_item", has(ModItems.SILVER_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.NETHER_BRASS_INGOT.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.NETHER_BRASS_NUGGET.get())
                .unlockedBy("has_item", has(ModItems.NETHER_BRASS_NUGGET.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, ModBlocks.NETHER_BRASS_BLOCK.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.NETHER_BRASS_INGOT.get())
                .unlockedBy("has_item", has(ModItems.NETHER_BRASS_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.ALCHEMICAL_GOLD_INGOT.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.ALCHEMICAL_GOLD_NUGGET.get())
                .unlockedBy("has_item", has(ModItems.ALCHEMICAL_GOLD_NUGGET.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, ModBlocks.ALCHEMICAL_GOLD_BLOCK.get())
                .pattern("nnn")
                .pattern("nnn")
                .pattern("nnn")
                .define('n', ModItems.ALCHEMICAL_GOLD_INGOT.get())
                .unlockedBy("has_item", has(ModItems.ALCHEMICAL_GOLD_INGOT.get()))
                .save(output);


        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.SILVER_NUGGET.get(), 9)
                .requires(ModItems.SILVER_INGOT.get())
                .unlockedBy("has_item", has(ModItems.SILVER_INGOT.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 9)
                .requires(ModBlocks.SILVER_BLOCK.get())
                .unlockedBy("has_item", has(ModBlocks.SILVER_BLOCK.get()))
                .save(output, rl("silver_ingot") + "_from_block");
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.NETHER_BRASS_NUGGET.get(), 9)
                .requires(ModItems.NETHER_BRASS_INGOT.get())
                .unlockedBy("has_item", has(ModItems.NETHER_BRASS_INGOT.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.NETHER_BRASS_INGOT.get(), 9)
                .requires(ModBlocks.NETHER_BRASS_BLOCK.get())
                .unlockedBy("has_item", has(ModBlocks.NETHER_BRASS_BLOCK.get()))
                .save(output, rl("nether_brass_ingot") + "_from_block");
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.ALCHEMICAL_GOLD_NUGGET.get(), 9)
                .requires(ModItems.ALCHEMICAL_GOLD_INGOT.get())
                .unlockedBy("has_item", has(ModItems.ALCHEMICAL_GOLD_INGOT.get()))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.ALCHEMICAL_GOLD_INGOT.get(), 9)
                .requires(ModBlocks.ALCHEMICAL_GOLD_BLOCK.get())
                .unlockedBy("has_item", has(ModBlocks.ALCHEMICAL_GOLD_BLOCK.get()))
                .save(output, rl("alchemical_gold_ingot") + "_from_block");

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.GOLD_COIN.get(), 32)
                .pattern(" i ")
                .pattern("i i")
                .pattern(" i ")
                .define('i', Items.GOLD_INGOT)
                .unlockedBy("has_item", has(Items.GOLD_INGOT))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.SILVER_COIN.get(), 16)
                .pattern("ii")
                .pattern("ii")
                .define('i', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_item", has(ModItems.SILVER_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.COPPER_COIN.get(), 16)
                .pattern(" i ")
                .pattern("i i")
                .pattern(" i ")
                .define('i', Items.COPPER_INGOT)
                .unlockedBy("has_item", has(Items.COPPER_INGOT))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, Items.GOLD_INGOT, 1)
                .requires(ModItems.GOLD_COIN.get(), 8)
                .unlockedBy("has_item", has(ModItems.GOLD_COIN.get()))
                .save(output, rl("gold_ingot") + "_from_coins");
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 1)
                .requires(ModItems.SILVER_COIN.get(), 4)
                .unlockedBy("has_item", has(ModItems.SILVER_COIN.get()))
                .save(output, rl("silver_ingot") + "_from_coins");
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, Items.COPPER_INGOT, 1)
                .requires(ModItems.COPPER_COIN.get(), 4)
                .unlockedBy("has_item", has(ModItems.COPPER_COIN.get()))
                .save(output, rl("copper_ingot") + "_from_coins");
    }

    private void makeComponents() {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, Items.BLAZE_ROD)
                .pattern("s")
                .pattern("i")
                .pattern("p")
                .define('s', ModItems.SULFUR.get())
                .define('i', ModItems.NETHER_BRASS_INGOT.get())
                .define('p', Items.BLAZE_POWDER)
                .unlockedBy("has_item", has(Items.BLAZE_POWDER))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.INFERNAL_MECHANISM.get())
                .pattern("rbr")
                .pattern("bgb")
                .pattern("rbr")
                .define('b', ModItems.NETHER_BRASS_INGOT.get())
                .define('r', Items.REDSTONE)
                .define('g', Items.GOLD_NUGGET)
                .unlockedBy("has_item", has(ModItems.NETHER_BRASS_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.ALCHEMICAL_FILTER.get())
                .pattern("dgd")
                .pattern("gdg")
                .pattern("dgd")
                .define('d', Gems.gem(Gems.GemSize.TINY, Gems.GemType.GARNET))
                .define('g', ModTags.Items.NUGGETS_GOLDLIKE)
                .unlockedBy("has_item", has(Items.GOLD_NUGGET))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.DIAMOND_INGOT.get())
                .pattern(" d ")
                .pattern("did")
                .pattern(" d ")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('d', Gems.gem(Gems.GemSize.TINY, Gems.GemType.DIAMOND))
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.TINY, Gems.GemType.DIAMOND)))
                .save(output);

        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, Items.GUNPOWDER, 3)
                .requires(Items.CHARCOAL)
                .requires(ModItems.SALTPETRE.get())
                .requires(ModItems.SULFUR.get())
                .unlockedBy("has_item", has(Items.GUNPOWDER))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.PRISMATIC_POWDER.get())
                .requires(Items.PRISMARINE_CRYSTALS)
                .unlockedBy("has_item", has(Items.PRISMARINE_CRYSTALS))
                .save(output);
    }
    private void makeTools() {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.WARMOGS.get())
                .pattern("l l")
                .pattern("rbr")
                .pattern("lgl")
                .define('b', ModItems.REJUVENATION_BEAD.get())
                .define('r', ModItems.RUBY_CRYSTAL.get())
                .define('l', ItemTags.LOGS)
                .define('g', ModItems.GIANTS_BELT.get())
                .unlockedBy("has_item", has(ModItems.REJUVENATION_BEAD.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.SPECTRES_COWL.get())
                .pattern(" n ")
                .pattern("prp")
                .define('n', ModItems.NULL_MAGIC_MANTLE.get())
                .define('p', Items.PHANTOM_MEMBRANE)
                .define('r', ModItems.RUBY_CRYSTAL.get())
                .unlockedBy("has_item", has(ModItems.NULL_MAGIC_MANTLE.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.SPIRIT_VISAGE.get())
                .pattern("s")
                .pattern("c")
                .pattern("r")
                .define('s', ModItems.SPECTRES_COWL.get())
                .define('c', ModItems.SILVER_CHESTPLATE.get())
                .define('r', ModItems.RUBY_CRYSTAL.get())
                .unlockedBy("has_item", has(ModItems.SPECTRES_COWL.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.MERC_TREADS.get())
                .pattern(" n ")
                .pattern("dgd")
                .pattern(" b ")
                .define('n', ModItems.NULL_MAGIC_MANTLE.get())
                .define('d', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND))
                .define('b', Items.LEATHER_BOOTS)
                .define('g', Items.GOLD_INGOT)
                .unlockedBy("has_item", has(ModItems.NULL_MAGIC_MANTLE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.MOBI_BOOTS.get())
                .pattern("m m")
                .pattern("b b")
                .define('b', ModItems.NETHER_BRASS_INGOT.get())
                .define('m', ModItems.INFERNAL_MECHANISM.get())
                .unlockedBy("has_item", has(ModItems.NETHER_BRASS_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.NASHORS_TOOTH.get())
                .pattern("ab")
                .pattern("ab")
                .pattern("ar")
                .define('b', ModItems.NETHER_BRASS_INGOT.get())
                .define('r', Items.BLAZE_ROD)
                .define('a', Items.AMETHYST_SHARD)
                .unlockedBy("has_item", has(ModItems.NETHER_BRASS_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ModItems.NETHER_BRASS_AXE.get())
                .pattern("bb")
                .pattern("br")
                .pattern(" s")
                .define('b', ModItems.NETHER_BRASS_INGOT.get())
                .define('r', Items.BLAZE_ROD)
                .define('s', Items.STICK)
                .unlockedBy("has_item", has(ModItems.NETHER_BRASS_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ModItems.CHARRING_AXE.get())
                .pattern("bbo")
                .pattern("br ")
                .pattern(" r ")
                .define('b', ModItems.NETHER_BRASS_INGOT.get())
                .define('r', Items.BLAZE_ROD)
                .define('o', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.ONYX))
                .unlockedBy("has_item", has(ModItems.NETHER_BRASS_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.TIAMAT.get())
                .pattern("igi")
                .pattern("iri")
                .pattern(" s ")
                .define('i', ModItems.NETHER_BRASS_INGOT.get())
                .define('g', Items.GOLD_NUGGET)
                .define('r', Items.BLAZE_ROD)
                .define('s', Items.STICK)
                .unlockedBy("has_item", has(ModItems.NETHER_BRASS_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.TITANIC_HYDRA.get())
                .pattern("i i")
                .pattern("rtr")
                .pattern(" i ")
                .define('t', ModItems.TIAMAT.get())
                .define('r', ModItems.RUBY_CRYSTAL.get())
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_item", has(ModItems.RUBY_CRYSTAL.get()))
                .save(output);



        armour(ModTags.Items.INGOTS_SILVER, ModItems.SILVER_HELMET.get(), ModItems.SILVER_CHESTPLATE.get(), ModItems.SILVER_LEGGINGS.get(), ModItems.SILVER_BOOTS.get(), ModItems.SILVER_INGOT.get());
        armour(ItemTags.WOOL, ModItems.CLOTH_HELMET.get(), ModItems.CLOTH_CHESTPLATE.get(), ModItems.CLOTH_LEGGINGS.get(), ModItems.CLOTH_BOOTS.get(), Blocks.WHITE_WOOL);
//        armour(Items.WHITE_WOOL, Items.LEATHER_HELMET, ModItems.LEATHER_CHESTPLATE.get(), ModItems.LEATHER_LEGGINGS.get(), ModItems.LEATHER_BOOTS.get());
    }

    private void makeCurios() {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.RUBY_CRYSTAL.get())
                .pattern("lrl")
                .pattern("dgd")
                .pattern("lrl")
                .define('l', Blocks.GLASS)
                .define('g', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET))
                .define('d', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND))
                .define('r', Items.REDSTONE)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.GOLD_RING.get())
                .pattern(" g ")
                .pattern("g g")
                .pattern(" g ")
                .define('g', Items.GOLD_NUGGET)
                .unlockedBy("has_item", has(Items.GOLD_NUGGET))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.SILVER_RING.get())
                .pattern(" s ")
                .pattern("s s")
                .pattern(" s ")
                .define('s', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_item", has(ModItems.SILVER_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.BELT.get())
                .pattern(" l ")
                .pattern("l l")
                .pattern(" g ")
                .define('g', Items.GOLD_NUGGET)
                .define('l', Items.LEATHER)
                .unlockedBy("has_item", has(Items.GOLD_NUGGET))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.ANCHOR_BELT.get())
                .pattern(" b ")
                .pattern(" c ")
                .pattern("iii")
                .define('i', Items.IRON_INGOT)
                .define('c', Items.IRON_CHAIN)
                .define('b', ModItems.BELT.get())
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.SKULL_BELT.get())
                .requires(ModItems.BELT.get())
                .requires(ModItems.WACKY_SKULL.get())
                .unlockedBy("has_item", has(ModItems.WACKY_SKULL.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.GIANTS_BELT.get())
                .pattern(" l ")
                .pattern("l l")
                .pattern("grg")
                .define('g', Items.GOLD_INGOT)
                .define('l', Items.LEATHER)
                .define('r', ModItems.RUBY_CRYSTAL.get())
                .unlockedBy("has_item", has(ModItems.RUBY_CRYSTAL.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ModItems.COIN_PURSE.get())
                .pattern(" s ")
                .pattern("lnl")
                .pattern("lll")
                .define('n', Items.GOLD_NUGGET)
                .define('l', Items.LEATHER)
                .define('s', Items.STRING)
                .unlockedBy("has_item", has(Items.LEATHER))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ModItems.COIN_PURSE.get())
                .pattern(" s ")
                .pattern("lnl")
                .pattern("lll")
                .define('n', Items.GOLD_NUGGET)
                .define('l', ItemTags.WOOL)
                .define('s', Items.STRING)
                .unlockedBy("has_item", has(Items.WHITE_WOOL))
                .save(output, rl("coin_purse_wool").toString());
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.GLOW_RING.get())
                .pattern("dsd")
                .pattern("srs")
                .pattern("dsd")
                .define('d', Items.GLOWSTONE_DUST)
                .define('s', Items.GLOW_INK_SAC)
                .define('r', ModItems.GOLD_RING.get())
                .unlockedBy("has_item", has(Items.GLOW_INK_SAC))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.DAMAGE_RING.get())
                .pattern(" d ")
                .pattern("drd")
                .pattern(" d ")
                .define('d', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND))
                .define('r', ModItems.SILVER_RING.get())
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.CHAMPIONS_RING.get())
                .pattern(" d ")
                .pattern("oro")
                .pattern(" o ")
                .define('d', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.DIAMOND))
                .define('r', ModItems.GOLD_RING.get())
                .define('o', Blocks.OBSIDIAN)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.DIAMOND)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TRANSPORTATION, ModItems.LIGHT_TRAVEL_RING.get())
                .pattern("f f")
                .pattern("srs")
                .pattern("fgf")
                .define('r', ModItems.GOLD_RING.get())
                .define('g', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET))
                .define('s', ModItems.SILVER_INGOT.get())
                .define('f', Items.FEATHER)
                .unlockedBy("has_item", has(ModItems.GOLD_RING.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.DORANS_RING.get())
                .pattern("plp")
                .pattern("lrl")
                .pattern("plp" )
                .define('r', ModItems.SILVER_RING.get())
                .define('p', Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND))
                .define('l', Items.LAPIS_LAZULI)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.GLASS_CANNON_RING.get())
                .pattern("sgs")
                .pattern("gxg")
                .pattern("sgs")
                .define('s', ModItems.SILVER_INGOT.get())
                .define('g', Blocks.GLASS)
                .define('x', ModBlocks.POWDER_BARREL.get())
                .unlockedBy("has_item", has(ModItems.SILVER_INGOT.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ModItems.LUCK_CHARM.get())
                .pattern("lgl")
                .pattern("gfg")
                .pattern("lgl")
                .define('f', Items.RABBIT_FOOT)
                .define('l', ModItems.FOUR_LEAF_CLOVER.get())
                .define('g', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.GARNET))
                .unlockedBy("has_item", has(Items.RABBIT_FOOT))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.TOXIC_TOTEM.get())
                .pattern(" i ")
                .pattern("gLg")
                .pattern(" m ")
                .define('i', Items.SPIDER_EYE)
                .define('L', Items.JUNGLE_LOG)
                .define('g', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.ONYX))
                .define('m', Items.GOLD_NUGGET)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.SMALL, Gems.GemType.ONYX)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.CLEANSING_TOTEM.get())
                .pattern(" i ")
                .pattern("gLg")
                .pattern(" m ")
                .define('i', Items.HONEYCOMB)
                .define('L', Items.MANGROVE_LOG)
                .define('g', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND))
                .define('m', ModItems.SILVER_INGOT.get())
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.RAGE_TOTEM.get())
                .pattern(" i ")
                .pattern("gLg")
                .pattern(" m ")
                .define('i', Items.BLAZE_POWDER)
                .define('L', Items.SPRUCE_LOG)
                .define('g', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.GARNET))
                .define('m', Items.COPPER_INGOT)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.SMALL, Gems.GemType.GARNET)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ModItems.FISH_NECKLACE.get())
                .pattern("sss")
                .pattern("b b")
                .pattern("ofo")
                .define('s', Items.STRING)
                .define('b', Items.BONE_MEAL)
                .define('f', ItemTags.FISHES)
                .define('o', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.ONYX))
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.SMALL, Gems.GemType.ONYX)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ModItems.CLOCKWORK_AMULET.get())
                .pattern("s s")
                .pattern("mom")
                .pattern("ggg")
                .define('m', ModItems.INFERNAL_MECHANISM.get())
                .define('o', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.ONYX))
                .define('s', Items.STRING)
                .define('g', Items.GOLD_NUGGET)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.ONYX)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.NULL_MAGIC_MANTLE.get())
                .pattern("ngn")
                .pattern("psp")
                .pattern("nnn")
                .define('g', Items.GOLD_NUGGET)
                .define('p', Items.PHANTOM_MEMBRANE)
                .define('n', ModItems.SILVER_NUGGET.get())
                .define('s', Items.STRING)
                .unlockedBy("has_item", has(Items.PHANTOM_MEMBRANE))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.REJUVENATION_BEAD.get())
                .pattern("sd ")
                .pattern("ded")
                .pattern(" ds")
                .define('e', Items.EMERALD)
                .define('d', Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND))
                .define('s', Items.STRING)
                .unlockedBy("has_item", has(Items.EMERALD))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.REJUVENATION_BELT.get())
                .pattern("rbr")
                .pattern(" r ")
                .define('r', ModItems.REJUVENATION_BEAD.get())
                .define('b', ModItems.BELT.get())
                .unlockedBy("has_item", has(ModItems.REJUVENATION_BEAD.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.REJUVENATION_NECKLACE.get())
                .pattern(" s ")
                .pattern("r r")
                .pattern(" r ")
                .define('r', ModItems.REJUVENATION_BEAD.get())
                .define('s', Items.STRING)
                .unlockedBy("has_item", has(ModItems.REJUVENATION_BEAD.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.REJUVENATION_RING.get())
                .pattern("b")
                .pattern("r")
                .define('b', ModItems.REJUVENATION_BEAD.get())
                .define('r', ModItems.GOLD_RING.get())
                .unlockedBy("has_item", has(ModItems.REJUVENATION_BEAD.get()))
                .save(output);
    }

    private void makeSpellItems() {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.AMP_TOME.get())
                .pattern("gsg")
                .pattern("sbs")
                .pattern("gsg")
                .define('g', Gems.gem(Gems.GemSize.TINY, Gems.GemType.GARNET))
                .define('s', ModItems.SILVER_NUGGET.get())
                .define('b', Items.ENCHANTED_BOOK)
                .unlockedBy("has_item", has(Items.ENCHANTED_BOOK))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.DECORATIONS, ModBlocks.AMP_BOOKSHELF.get())
                .pattern("aaa")
                .pattern("scs")
                .pattern("aaa")
                .define('a', ModItems.AMP_TOME.get())
                .define('s', ModItems.SILVER_INGOT.get())
                .define('c', Blocks.CHISELED_BOOKSHELF)
                .unlockedBy("has_item", has(ModItems.AMP_TOME.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.SKELETON_STAFF.get())
                .pattern("sgs")
                .pattern(" b ")
                .pattern(" s ")
                .define('g', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.GARNET))
                .define('s', ModItems.WACKY_SKULL.get())
                .define('b', Items.BONE)
                .unlockedBy("has_item", has(ModItems.WACKY_SKULL.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.ENCHANTING_STAFF.get())
                .pattern("lDl")
                .pattern(" s ")
                .pattern(" s ")
                .define('s', ModItems.SILVER_INGOT.get())
                .define('l', Items.LAPIS_LAZULI)
                .define('D', Items.DIAMOND)
                .unlockedBy("has_item", has(ModItems.SILVER_INGOT.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ModItems.PORTAL_LIGHTER.get())
                .pattern("ofo")
                .pattern(" i ")
                .pattern(" i ")
                .define('i', Tags.Items.INGOTS_GOLD)
                .define('o', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.ONYX))
                .define('f', Items.FLINT_AND_STEEL)
                .unlockedBy("has_item", has(Items.FLINT_AND_STEEL))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ModItems.PORTAL_LIGHTER.get())
                .pattern("ofo")
                .pattern(" i ")
                .pattern(" i ")
                .define('i', Tags.Items.INGOTS_GOLD)
                .define('o', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.ONYX))
                .define('f', Items.FIRE_CHARGE)
                .unlockedBy("has_item", has(Items.FIRE_CHARGE))
                .save(output, rl("portal_lighter_from_fire_charge").toString());
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ModItems.MOVER.get())
                .pattern("sri")
                .pattern(" i ")
                .pattern(" i ")
                .define('s', ModItems.WACKY_SKULL.get())
                .define('r', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET))
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_item", has(ModItems.WACKY_SKULL.get()))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ModItems.RECALL_STAFF.get())
                .pattern("dod")
                .pattern("did")
                .define('d', Blocks.DEEPSLATE)
                .define('o', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.ONYX))
                .define('i', Items.IRON_INGOT)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.ONYX)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ModItems.DOWSING_ROD.get())
                .pattern("nd ")
                .pattern("ogo")
                .pattern(" in")
                .define('i', Tags.Items.INGOTS_GOLD)
                .define('o', Gems.gem(Gems.GemSize.TINY, Gems.GemType.ONYX))
                .define('g', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.GARNET))
                .define('d', Items.DIAMOND)
                .define('n', Items.GOLD_NUGGET)
                .unlockedBy("has_item", has(Items.DIAMOND))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.SAPPHIRE_CRYSTAL.get())
                .pattern("glg")
                .pattern("sds")
                .pattern("glg")
                .define('g', Blocks.GLASS)
                .define('d', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.DIAMOND))
                .define('s', ModItems.SILVER_INGOT.get())
                .define('l', Items.LAPIS_LAZULI)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.DIAMOND)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.RESONATING_STAR.get())
                .pattern("nrn")
                .pattern("sgs")
                .pattern("nrn")
                .define('r', Items.REDSTONE)
                .define('g', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.GARNET))
                .define('s', ModItems.SALTPETRE.get())
                .define('n', Items.GOLD_NUGGET)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.SMALL, Gems.GemType.GARNET)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.REVERBERATING_STAR.get())
                .pattern(" g ")
                .pattern("vxv")
                .pattern(" i ")
                .define('i', Items.GOLD_INGOT)
                .define('g', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET))
                .define('x', ModItems.RESONATING_STAR.get())
                .define('v', ModItems.CREEPER_JELLY)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.SMALL, Gems.GemType.GARNET)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.SMOLDERING_TABLET.get())
                .pattern("ioi")
                .pattern("cdc")
                .pattern("bbb")
                .define('d', Blocks.DEEPSLATE)
                .define('o', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.ONYX))
                .define('c', Items.COAL)
                .define('i', Items.IRON_INGOT)
                .define('b', Items.BLAZE_POWDER)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.DIAMOND)))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.OMNISTONE.get())
                .pattern("nbl")
                .pattern("gdo")
                .pattern("eba")
                .define('b', Blocks.OBSIDIAN)
                .define('o', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.ONYX))
                .define('g', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET))
                .define('d', Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.DIAMOND))
                .define('a', Items.AMETHYST_SHARD)
                .define('n', Items.GOLD_NUGGET)
                .define('e', Items.EMERALD)
                .define('l', Items.LAPIS_LAZULI)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.DIAMOND)))
                .save(output);


        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.GLOWING_PASTE.get())
                .requires(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND))
                .requires(Items.GLOWSTONE)
                .requires(Items.LAPIS_LAZULI)
                .requires(Items.GLOW_INK_SAC)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND)))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.GRAINS_OF_FORCE.get())
                .requires(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.GARNET))
                .requires(Items.REDSTONE)
                .requires(ModItems.SALTPETRE)
                .requires(ModItems.CREEPER_JELLY)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND)))
                .save(output);
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, ModItems.INCENDIARY_POWDER.get())
                .requires(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.ONYX))
                .requires(ModItems.SULFUR)
                .requires(Items.COAL)
                .requires(Items.BLAZE_POWDER)
                .unlockedBy("has_item", has(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND)))
                .save(output);
    }

    private void makeCoalLikes() {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.DECORATIONS, Blocks.TORCH, 3)
                .pattern("c")
                .pattern("s")
                .define('c', ModItems.LIGNITE.get())
                .define('s', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(ModItems.LIGNITE.get()))
                .save(output, rl("torch_lignite").toString());
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.DECORATIONS, Blocks.TORCH, 5)
                .pattern("c")
                .pattern("s")
                .define('c', ModItems.ANTHRACITE.get())
                .define('s', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_item", has(ModItems.ANTHRACITE.get()))
                .save(output, rl("torch_anthracite").toString());

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.DECORATIONS, Blocks.SOUL_TORCH, 3)
                .pattern("c")
                .pattern("s")
                .pattern("S")
                .define('c', ModItems.LIGNITE.get())
                .define('s', Tags.Items.RODS_WOODEN)
                .define('S', Blocks.SOUL_SOIL)
                .unlockedBy("has_item", has(ModItems.LIGNITE.get()))
                .save(output, rl("soul_torch_lignite").toString());
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.DECORATIONS, Blocks.SOUL_TORCH, 5)
                .pattern("c")
                .pattern("s")
                .pattern("S")
                .define('c', ModItems.ANTHRACITE.get())
                .define('s', Tags.Items.RODS_WOODEN)
                .define('S', Blocks.SOUL_SOIL)
                .unlockedBy("has_item", has(ModItems.ANTHRACITE.get()))
                .save(output, rl("soul_torch_anthracite").toString());


        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, Items.FIRE_CHARGE, 2)
                .requires(Items.GUNPOWDER)
                .requires(Items.BLAZE_POWDER)
                .requires(ModItems.LIGNITE.get())
                .unlockedBy("has_item", has(ModItems.LIGNITE.get()))
                .save(output, rl("fire_charge_lignite").toString());
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, Items.FIRE_CHARGE, 4)
                .requires(Items.GUNPOWDER)
                .requires(Items.BLAZE_POWDER)
                .requires(ModItems.ANTHRACITE.get())
                .unlockedBy("has_item", has(ModItems.ANTHRACITE.get()))
                .save(output, rl("fire_charge_anthracite").toString());
    }

    private void makeSmeltingBlasting() {
        standardSmeltable(ModBlocks.SILVER_ORE.get(), ModItems.SILVER_INGOT.get(), 0.7f);
        standardSmeltable(ModBlocks.DEEPSLATE_SILVER_ORE.get(), ModItems.SILVER_INGOT.get(), 0.7f);
        standardSmeltable(ModItems.RAW_SILVER.get(), ModItems.SILVER_INGOT.get(), 0.7f);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.POOR_IRON), RecipeCategory.MISC, CookingBookCategory.MISC,
                        new ItemStackTemplate(Items.IRON_NUGGET, 2), 0.2f, 200)
                .unlockedBy("has_item", has(ModItems.POOR_IRON))
                .save(output, BuiltInRegistries.ITEM.getKey(ModItems.POOR_IRON.get()) + "_smelt");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItems.POOR_IRON), RecipeCategory.MISC, CookingBookCategory.MISC,
                        new ItemStackTemplate(Items.IRON_NUGGET, 2), 0.2f, 50)
                .unlockedBy("has_item", has(ModItems.POOR_IRON))
                .save(output, BuiltInRegistries.ITEM.getKey(ModItems.POOR_IRON.get()) + "_blast");

    }


    private void makeUUs() {
        uu(Blocks.ACACIA_LOG);
        uu(Blocks.BIRCH_LOG);
        uu(Blocks.CHERRY_LOG);
        uu(Blocks.DARK_OAK_LOG);
        uu(Blocks.JUNGLE_LOG);
        uu(Blocks.MANGROVE_LOG);
        uu(Blocks.OAK_LOG);
        uu(Blocks.SPRUCE_LOG);
        uu(Blocks.CACTUS);
        uu(Blocks.BAMBOO);
        uu(Blocks.CRIMSON_STEM);
        uu(Blocks.WARPED_STEM);
        uu(Blocks.MOSS_BLOCK, 32);
        uu(Blocks.ACACIA_LEAVES, 32);
        uu(Blocks.AZALEA_LEAVES, 32);
        uu(Blocks.BIRCH_LEAVES, 32);
        uu(Blocks.CHERRY_LEAVES, 32);
        uu(Blocks.DARK_OAK_LEAVES, 32);
        uu(Blocks.FLOWERING_AZALEA_LEAVES, 32);
        uu(Blocks.JUNGLE_LEAVES, 32);
        uu(Blocks.MANGROVE_LEAVES, 32);
        uu(Blocks.OAK_LEAVES, 32);
        uu(Blocks.SPRUCE_LEAVES, 32);
        uu(ModBlocks.ELDER_PINE_LOG.get(), 4);
    }

    private void uu(RecipeOutput out, Item inOut) {
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, inOut, 17)
                .requires(ModItems.UU_MATTER.get())
                .requires(inOut)
                .unlockedBy("has_item", has(ModItems.UU_MATTER.get()))
                .save(out, BuiltInRegistries.ITEM.getKey(inOut) + "_uu");
    }

    private void uu(Block inOut) {
        uu(inOut, 16);
    }

    private void uu(Block inOut, int count) {
        ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.BUILDING_BLOCKS, inOut, count+1)
                .requires(ModItems.UU_MATTER.get())
                .requires(inOut)
                .unlockedBy("has_item", has(ModItems.UU_MATTER.get()))
                .save(output, BuiltInRegistries.BLOCK.getKey(inOut) + "_uu");
    }

    private void templateCopy(Item template, ItemLike material) {
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, template, 2)
                .define('#', ModItems.DIAMOND_INGOT.get())
                .define('d', Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND))
                .define('C', material)
                .define('S', template)
                .pattern("dSd")
                .pattern("dCd")
                .pattern("d#d")
                .unlockedBy(getHasName(template), has(template))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, template, 2)
                .define('p', Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND))
                .define('C', ModItems.BLANKEST_SLATE.get())
                .define('S', template)
                .pattern("pSp")
                .pattern("pCp")
                .pattern("ppp")
                .unlockedBy(getHasName(template), has(template))
                .save(output, rl("blank_slate_") + BuiltInRegistries.ITEM.getKey(template).getPath());
    }



    private void standardSmeltableOnly(Item in, Item out, float xp) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(in), RecipeCategory.MISC, CookingBookCategory.MISC,
                        out, xp, 200)
                .unlockedBy("has_item", has(in))
                .save(output, BuiltInRegistries.ITEM.getKey(in) + "_smelt");
    }

    private void standardSmeltable(Item in, Item out, float xp) {
        standardSmeltableOnly(in, out, xp);
        standardBlastable(in, out, xp);
    }

    private void standardSmeltable(Block in, Item out, float xp) {
        standardSmeltable(in.asItem(), out, xp);
    }

    private void standardBlastable(Item in, Item out, float xp) {
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(in), RecipeCategory.MISC, CookingBookCategory.MISC,
                        out, xp, 100)
                .unlockedBy("has_item", has(in))
                .save(output, BuiltInRegistries.ITEM.getKey(in) + "_blast");
    }

    private void standardBlastable(Block in, Item out, float xp) {
        standardBlastable(in.asItem(), out, xp);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new ModRecipeProvider(provider, output);
        }

        @Override
        public String getName() {
            return Main.MOD_ID;
        }
    }
}
