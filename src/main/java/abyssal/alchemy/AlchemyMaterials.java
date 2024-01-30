package abyssal.alchemy;

import abyssal.Main;
import abyssal.init.Gems;
import abyssal.init.ModBlocks;
import abyssal.init.ModItems;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AlchemyMaterials {

    static final int MAX_TIER = 7;

    private static final int CHEAP_COST = 9 * 16;
    private static final int STANDARD_COST = CHEAP_COST * 20;
    private static final int DUST_COST = STANDARD_COST / 4;
    private static final int NUGGET_COST = STANDARD_COST / 9;
    private static final int GEM_SMALL_COST = STANDARD_COST;
    private static final int GEM_REGULAR_COST = GEM_SMALL_COST * 4;

    private static final int GEM_LARGE_COST = GEM_REGULAR_COST * 4;

    private static final int GEM_TINY_COST = GEM_SMALL_COST / 4;
    private static final int GEM_POWDER_COST = GEM_TINY_COST / 2;

    static final double VERY_LOW_PURITY = 0.5;
    static final double STANDARD_MIN_PURITY_REQ = 0.25;


    static final double LOW_PURITY = 0.65;
    static final double LOW_PURITY_REQ = 0.55;

    static final double AVERAGE_PURITY = 0.8;
    static final double AVERAGE_PURITY_REQ = 0.75;

    static final double HIGH_PURITY = 0.9;
    static final double HIGH_PURITY_REQ = 0.85;

    static final double VERY_HIGH_PURITY = 0.95;
    static final double VERY_HIGH_PURITY_REQ = 0.85;
    static final double PERFECT_PURITY = 1;
    static final double PERFECT_PURITY_REQ = 0.99;

    static final AlchemyMaterialGroup BLOCKER = new AlchemyMaterialGroup("X X X", new AlchemyMaterialGroup.Tier(0,0,0), Alchemy.Category.BLOCKER, Set.of());

    static int count = 0;

    static List<AlchemyMaterialGroup> list;

    public static List<AlchemyMaterialGroup> makeMaterials() {

        list = new ArrayList<>();

        AlchemyMaterial cobblestone = new AlchemyMaterial(Items.COBBLESTONE, CHEAP_COST);
        AlchemyMaterial cobbled_deepslate = new AlchemyMaterial(Items.COBBLED_DEEPSLATE, CHEAP_COST);

        AlchemyMaterial dirt = new AlchemyMaterial(() -> Items.DIRT, CHEAP_COST, LOW_PURITY, LOW_PURITY_REQ);
        AlchemyMaterial coarse_dirt = new AlchemyMaterial(Items.DIRT, CHEAP_COST, VERY_LOW_PURITY);
        AlchemyMaterial sand = new AlchemyMaterial(Items.SAND, CHEAP_COST);

        AlchemyMaterial netherrack = new AlchemyMaterial(Items.NETHERRACK, CHEAP_COST);

        group("Cobbl", Alchemy.Category.STONE, cobblestone, 1,1,0);
        group("CobDS", Alchemy.Category.STONE, cobbled_deepslate, 1,1,0);
        group("Dirts", Alchemy.Category.EARTH, Set.of(dirt, coarse_dirt), 1,1,0);  // "clean" for UHP dirt
        group("Sand ", Alchemy.Category.EARTH, sand, 1,1,0); // red sand as less pure sand?
        group("Nrack", Alchemy.Category.INFERNAL, netherrack, 1,1,0);

        AlchemyMaterial stone = new AlchemyMaterial(Items.STONE, CHEAP_COST);
        AlchemyMaterial gravel = new AlchemyMaterial(Items.GRAVEL, CHEAP_COST);
        AlchemyMaterial sandstone = new AlchemyMaterial(Items.SANDSTONE, CHEAP_COST * 4);
        AlchemyMaterial red_sandstone = new AlchemyMaterial(Items.RED_SANDSTONE, CHEAP_COST * 4);
        AlchemyMaterial deepslate = new AlchemyMaterial(Items.DEEPSLATE, CHEAP_COST);
        AlchemyMaterial andesite = new AlchemyMaterial(Items.ANDESITE, CHEAP_COST);
        AlchemyMaterial diorite = new AlchemyMaterial(Items.DIORITE, CHEAP_COST); // consider pairing these three, impurities by quartz amount
        AlchemyMaterial granite = new AlchemyMaterial(Items.GRANITE, CHEAP_COST);
        AlchemyMaterial tuff = new AlchemyMaterial(Items.TUFF, CHEAP_COST);
        AlchemyMaterial calcite = new AlchemyMaterial(Items.CALCITE, CHEAP_COST);
        AlchemyMaterial dripstone = new AlchemyMaterial(Items.DRIPSTONE_BLOCK, CHEAP_COST);
        AlchemyMaterial prismarine_shard = new AlchemyMaterial(Items.PRISMARINE_SHARD, CHEAP_COST);
        AlchemyMaterial prismarine = new AlchemyMaterial(Items.PRISMARINE, CHEAP_COST * 4);

        AlchemyMaterial glass = new AlchemyMaterial(Items.GLASS, CHEAP_COST, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial ice = new AlchemyMaterial(Items.ICE, CHEAP_COST);

        AlchemyMaterial clay_ball = new AlchemyMaterial(Items.CLAY_BALL, CHEAP_COST, LOW_PURITY);
        AlchemyMaterial clay = new AlchemyMaterial(Items.CLAY, CHEAP_COST*4, LOW_PURITY);
        AlchemyMaterial mud = new AlchemyMaterial(Items.MUD, CHEAP_COST, VERY_LOW_PURITY);
        AlchemyMaterial red_sand = new AlchemyMaterial(Items.RED_SAND, CHEAP_COST);

        AlchemyMaterial basalt = new AlchemyMaterial(Items.BASALT, CHEAP_COST);
        AlchemyMaterial blackstone = new AlchemyMaterial(Items.BLACKSTONE, CHEAP_COST);

        group("Stone", Alchemy.Category.STONE, stone, 2,0,1);
        group("Gravl", Alchemy.Category.STONE, gravel, 2,0,1);
        group("Sndst", Alchemy.Category.STONE, sandstone, 2,0,1); // smooth sandstone high purity?
        group("Slate", Alchemy.Category.STONE, deepslate, 2,0,1);
        group("Andes", Alchemy.Category.STONE, andesite, 2,0,1);
        group("Diori", Alchemy.Category.STONE, diorite, 2,0,1);
        group("Grani", Alchemy.Category.STONE, granite, 2,0,1);
        group("Tuff ", Alchemy.Category.STONE, tuff, 2,1,1); // tuffer tuffest
        group("Calci", Alchemy.Category.STONE, calcite, 2,1,0);
        group("Drips", Alchemy.Category.STONE, dripstone, 2,1,1);  // throw in the pointy dripstones
        group("Prism", Alchemy.Category.STONE, Set.of(prismarine_shard, prismarine), 2,1,0); // dark pris as potential impurity
        group("RSdst", Alchemy.Category.STONE, red_sandstone, 2,1,1); // see sand
        group("Glass", Alchemy.Category.CRYSTAL, glass, 2,1,0);  // "clear glass" as a purer option, and one less pure? Random stained glass?
        group(" Ice ", Alchemy.Category.CRYSTAL, ice, 2,1,1);
        group("Clay ", Alchemy.Category.EARTH, Set.of(clay, clay_ball), 2,1,1);
        group(" Mud ", Alchemy.Category.EARTH, mud, 2,1,1);  // consider rolling this into something else
        group("RSand", Alchemy.Category.EARTH, red_sand, 2,1,1); // see sand
        group("Baslt", Alchemy.Category.INFERNAL, basalt, 2,1,1);
        group("BlkSt", Alchemy.Category.INFERNAL, blackstone, 2,1,1); // vantablackstone

        Main.LOGGER.info(count + " at rank <= 2");

        AlchemyMaterial raw_copper = new AlchemyMaterial(Items.RAW_COPPER, STANDARD_COST/2, LOW_PURITY);
        AlchemyMaterial copper = new AlchemyMaterial(Items.COPPER_INGOT, STANDARD_COST/2, AVERAGE_PURITY, AVERAGE_PURITY_REQ);
        AlchemyMaterial lapis = new AlchemyMaterial(Items.LAPIS_LAZULI, DUST_COST);
        AlchemyMaterial slime_ball = new AlchemyMaterial(Items.SLIME_BALL, STANDARD_COST/2);
        AlchemyMaterial creeper_jelly = new AlchemyMaterial(ModItems.CREEPER_JELLY, DUST_COST);
        AlchemyMaterial saltpetre = new AlchemyMaterial(ModItems.SALTPETRE, DUST_COST);
        AlchemyMaterial lignite = new AlchemyMaterial(Items.COAL, DUST_COST, LOW_PURITY, LOW_PURITY_REQ);
        AlchemyMaterial coal = new AlchemyMaterial(Items.COAL, DUST_COST, AVERAGE_PURITY, AVERAGE_PURITY_REQ);
        AlchemyMaterial anthracite = new AlchemyMaterial(Items.COAL, DUST_COST, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial charcoal = new AlchemyMaterial(Items.CHARCOAL, DUST_COST);
        AlchemyMaterial obsidian = new AlchemyMaterial(Items.OBSIDIAN, DUST_COST); // maybe crying can go in here somehow
        AlchemyMaterial amethyst_shard = new AlchemyMaterial(Items.AMETHYST_SHARD, DUST_COST);
        AlchemyMaterial amethyst_block = new AlchemyMaterial(Items.AMETHYST_BLOCK, DUST_COST*4);
        AlchemyMaterial emerald = new AlchemyMaterial(Items.EMERALD, STANDARD_COST); // impure version? Standard->dust cost?
        AlchemyMaterial redstone = new AlchemyMaterial(Items.REDSTONE, DUST_COST);
        AlchemyMaterial glowstone_dust = new AlchemyMaterial(Items.GLOWSTONE_DUST, DUST_COST);
        AlchemyMaterial glowstone_block = new AlchemyMaterial(Items.GLOWSTONE, DUST_COST*4, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial sulfur = new AlchemyMaterial(ModItems.SULFUR, DUST_COST);
        AlchemyMaterial soul_sand = new AlchemyMaterial(Items.SOUL_SAND, CHEAP_COST);
        AlchemyMaterial soul_soil = new AlchemyMaterial(Items.SOUL_SOIL, CHEAP_COST); // consider merge with soul sand



        group("Coppr", Alchemy.Category.METAL, Set.of(raw_copper, copper), 3,1,1);
        group("Lapis", Alchemy.Category.STONE, lapis, 3,2,0);
        group("Obsid", Alchemy.Category.CRYSTAL, obsidian, 3,1,1);
        group("Amthy", Alchemy.Category.CRYSTAL, Set.of(amethyst_block, amethyst_shard), 3,1,1);
        group("Emrld", Alchemy.Category.CRYSTAL, emerald, 3,1,1);
        group("Redst", Alchemy.Category.CRYSTAL, redstone, 3,1,1);
        group("Slime", Alchemy.Category.EARTH, slime_ball, 3,1,1);
        group("Jelly", Alchemy.Category.EARTH, creeper_jelly, 3,1,1);
        group("Coal ", Alchemy.Category.EARTH, Set.of(lignite, anthracite, coal), 3,0,1);
        group("Charc", Alchemy.Category.EARTH, charcoal, 3,0,1);
        group("Slptr", Alchemy.Category.EARTH, saltpetre, 3,1,1);
        group("Sulfr", Alchemy.Category.INFERNAL, sulfur, 3,1,1);
        group("Glwst", Alchemy.Category.INFERNAL, Set.of(glowstone_block, glowstone_dust), 3,1,0);
        group("SlSnd", Alchemy.Category.INFERNAL, soul_sand, 3,0,1);
        group("SlSoi", Alchemy.Category.INFERNAL, soul_soil, 3,0,1); // soul mud as impure version, bigger slow

        Main.LOGGER.info(count + " at rank <= 3");

        AlchemyMaterial poor_iron = new AlchemyMaterial(ModItems.POOR_IRON, NUGGET_COST*3, LOW_PURITY);
        AlchemyMaterial raw_iron = new AlchemyMaterial(Items.RAW_IRON, STANDARD_COST, AVERAGE_PURITY, AVERAGE_PURITY_REQ);
        AlchemyMaterial iron_nugget = new AlchemyMaterial(Items.IRON_NUGGET, NUGGET_COST, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial iron_ingot = new AlchemyMaterial(Items.IRON_INGOT, STANDARD_COST, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial nether_brass_nugget = new AlchemyMaterial(ModItems.NETHER_BRASS_NUGGET, NUGGET_COST);
        AlchemyMaterial nether_brass_ingot = new AlchemyMaterial(ModItems.NETHER_BRASS_INGOT, STANDARD_COST);
        AlchemyMaterial ender_pearl = new AlchemyMaterial(Items.ENDER_PEARL, STANDARD_COST);
        AlchemyMaterial nether_quartz = new AlchemyMaterial(Items.QUARTZ, DUST_COST, LOW_PURITY);
        AlchemyMaterial pure_quartz = new AlchemyMaterial(ModItems.PURE_QUARTZ, DUST_COST, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial prismatic_powder = new AlchemyMaterial(ModItems.PRISMATIC_POWDER, CHEAP_COST*2);
        AlchemyMaterial prismarine_crystals = new AlchemyMaterial(Items.PRISMARINE_CRYSTALS, CHEAP_COST*2, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial blaze_powder = new AlchemyMaterial(Items.BLAZE_POWDER, DUST_COST);


        group("Irons", Alchemy.Category.METAL, Set.of(poor_iron, raw_iron, iron_nugget, iron_ingot), 4,1,0); // refined iron
        group("NBras", Alchemy.Category.METAL, Set.of(nether_brass_ingot, nether_brass_nugget), 4,1,0);
        group("Ender", Alchemy.Category.CRYSTAL, ender_pearl, 4,1,0); // impure version
        group("Quart", Alchemy.Category.CRYSTAL, Set.of(nether_quartz, pure_quartz), 4,0,1);
        group("PrsCr", Alchemy.Category.CRYSTAL, Set.of(prismarine_crystals, prismatic_powder), 4,0,1);
        group("Blaze", Alchemy.Category.INFERNAL, blaze_powder, 4,0,1);


        Main.LOGGER.info(count + " at rank <= 4");

        AlchemyMaterial silver_nugget = new AlchemyMaterial(ModItems.SILVER_NUGGET, STANDARD_COST, VERY_HIGH_PURITY, VERY_HIGH_PURITY_REQ);
        AlchemyMaterial silver_ingot = new AlchemyMaterial(ModItems.SILVER_INGOT, STANDARD_COST, VERY_HIGH_PURITY, VERY_HIGH_PURITY_REQ);
        AlchemyMaterial raw_silver = new AlchemyMaterial(ModItems.RAW_SILVER, STANDARD_COST, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial alchemical_gold_ingot = new AlchemyMaterial(ModItems.ALCHEMICAL_GOLD_INGOT, STANDARD_COST, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial alchemical_gold_nugget = new AlchemyMaterial(ModItems.ALCHEMICAL_GOLD_NUGGET, NUGGET_COST, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial fools_gold = new AlchemyMaterial(ModItems.FOOLS_GOLD, STANDARD_COST);
        AlchemyMaterial garnet_powder = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.POWDER, Gems.GemType.GARNET), GEM_POWDER_COST, LOW_PURITY);
        AlchemyMaterial tiny_garnet = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.TINY, Gems.GemType.GARNET), GEM_TINY_COST, AVERAGE_PURITY, AVERAGE_PURITY_REQ);
        AlchemyMaterial small_garnet = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.SMALL, Gems.GemType.GARNET), GEM_SMALL_COST, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial garnet = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.GARNET), GEM_REGULAR_COST, VERY_HIGH_PURITY, VERY_HIGH_PURITY_REQ);
        AlchemyMaterial large_garnet = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.LARGE, Gems.GemType.GARNET), GEM_LARGE_COST, PERFECT_PURITY, PERFECT_PURITY_REQ);
        AlchemyMaterial onyx_powder = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.POWDER, Gems.GemType.ONYX), GEM_POWDER_COST, LOW_PURITY);
        AlchemyMaterial tiny_onyx = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.TINY, Gems.GemType.ONYX), GEM_TINY_COST, AVERAGE_PURITY, AVERAGE_PURITY_REQ);
        AlchemyMaterial small_onyx = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.SMALL, Gems.GemType.ONYX), GEM_SMALL_COST, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial onyx = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.ONYX), GEM_REGULAR_COST, VERY_HIGH_PURITY, VERY_HIGH_PURITY_REQ);
        AlchemyMaterial large_onyx = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.LARGE, Gems.GemType.ONYX), GEM_LARGE_COST, PERFECT_PURITY, PERFECT_PURITY_REQ);
        AlchemyMaterial end_stone = new AlchemyMaterial(Items.END_STONE, CHEAP_COST);
        AlchemyMaterial purpur = new AlchemyMaterial(Items.PURPUR_BLOCK, CHEAP_COST);
        AlchemyMaterial uu_matter = new AlchemyMaterial(ModItems.UU_MATTER, STANDARD_COST);

        group("Silvr", Alchemy.Category.METAL, Set.of(raw_silver, silver_ingot, silver_nugget), 5,1,1);  // some silver-containing alloy for impure, better silver also an option
        group("AlchG", Alchemy.Category.METAL, Set.of(alchemical_gold_ingot, alchemical_gold_nugget, fools_gold), 5,2,0);
        group("EndSt", Alchemy.Category.STONE, end_stone, 5,0,1);
        group("Purpr", Alchemy.Category.STONE, purpur, 5,0,1);
        group("Grnet", Alchemy.Category.CRYSTAL, Set.of(garnet_powder, tiny_garnet, small_garnet, garnet, large_garnet), 5,1,1);
        group("Onyx ", Alchemy.Category.CRYSTAL, Set.of(onyx_powder, tiny_onyx, small_onyx, onyx, large_onyx), 5,1,1);
        group("UUMat", Alchemy.Category.EARTH, uu_matter, 5,0,2); // "dead matter" less pure

        Main.LOGGER.info(count + " at rank <= 5");

        AlchemyMaterial gold_nugget = new AlchemyMaterial(Items.GOLD_NUGGET, NUGGET_COST*2, PERFECT_PURITY, PERFECT_PURITY_REQ);
        AlchemyMaterial gold_ingot = new AlchemyMaterial(Items.GOLD_INGOT, STANDARD_COST*2, PERFECT_PURITY, PERFECT_PURITY_REQ);
        AlchemyMaterial raw_gold = new AlchemyMaterial(Items.RAW_GOLD, STANDARD_COST*2, VERY_HIGH_PURITY, VERY_HIGH_PURITY_REQ);
        AlchemyMaterial abyssal_stone = new AlchemyMaterial(() -> ModBlocks.ABYSSAL_STONE.get().asItem(), CHEAP_COST);
        AlchemyMaterial diamond_powder = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.POWDER, Gems.GemType.DIAMOND), GEM_POWDER_COST, LOW_PURITY);
        AlchemyMaterial tiny_diamond = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.TINY, Gems.GemType.DIAMOND), GEM_TINY_COST, AVERAGE_PURITY, AVERAGE_PURITY_REQ);
        AlchemyMaterial small_diamond = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.SMALL, Gems.GemType.DIAMOND), GEM_SMALL_COST, HIGH_PURITY, HIGH_PURITY_REQ);
        AlchemyMaterial diamond = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.REGULAR, Gems.GemType.DIAMOND), GEM_REGULAR_COST, VERY_HIGH_PURITY, VERY_HIGH_PURITY_REQ);
        AlchemyMaterial large_diamond = new AlchemyMaterial(() -> Gems.gem(Gems.GemSize.LARGE, Gems.GemType.DIAMOND), GEM_LARGE_COST, PERFECT_PURITY, PERFECT_PURITY_REQ);
        AlchemyMaterial ghast_tear = new AlchemyMaterial(Items.GHAST_TEAR, STANDARD_COST, AVERAGE_PURITY, LOW_PURITY_REQ);
        AlchemyMaterial aether_wisp = new AlchemyMaterial(ModItems.AETHER_WISP, STANDARD_COST, VERY_HIGH_PURITY, VERY_HIGH_PURITY_REQ);
        AlchemyMaterial crying_obsidian = new AlchemyMaterial(Items.CRYING_OBSIDIAN, STANDARD_COST);
        AlchemyMaterial explosion_temp_placeholder = new AlchemyMaterial(Items.TNT, STANDARD_COST, VERY_LOW_PURITY);

        group("Golds", Alchemy.Category.METAL, Set.of(gold_ingot, gold_nugget, raw_gold), 6,1,0);
        group("Abyss", Alchemy.Category.STONE, abyssal_stone, 6,0,2);
        group("Diamd", Alchemy.Category.CRYSTAL, Set.of(diamond_powder, tiny_diamond, small_diamond, diamond, large_diamond), 6,1,0);
        group("Ghast", Alchemy.Category.INFERNAL, ghast_tear, 6,0,1);
        group("AWisp", Alchemy.Category.INFERNAL, aether_wisp, 6,0,1); // lower purity option needed. Ghost summon?
        group("Cryin", Alchemy.Category.INFERNAL, crying_obsidian, 6,0,1); // maybe put with obs or remove
        group("Boom!", Alchemy.Category.INFERNAL, explosion_temp_placeholder, 6,1,0);  // TODO make this one explode when precipitated

        Main.LOGGER.info(count + " at rank <= 6");

        AlchemyMaterial warm_ingot = new AlchemyMaterial(ModItems.WARM_INGOT, STANDARD_COST*4);
        AlchemyMaterial plutonium_ingot = new AlchemyMaterial(ModItems.PLUTONIUM_INGOT, STANDARD_COST*4, VERY_HIGH_PURITY, VERY_HIGH_PURITY_REQ);
        AlchemyMaterial super_soil = new AlchemyMaterial(() -> ModBlocks.SUPER_SOIL.get().asItem(), CHEAP_COST, PERFECT_PURITY, PERFECT_PURITY_REQ);
        AlchemyMaterial prism = new AlchemyMaterial(() -> ModBlocks.PRISM.get().asItem(), DUST_COST, PERFECT_PURITY, PERFECT_PURITY_REQ);
        AlchemyMaterial blankest_slate = new AlchemyMaterial(ModItems.BLANKEST_SLATE, STANDARD_COST, PERFECT_PURITY, PERFECT_PURITY_REQ);
        AlchemyMaterial inferno_essence = new AlchemyMaterial(ModItems.INFERNO_ESSENCE, STANDARD_COST, PERFECT_PURITY, PERFECT_PURITY_REQ);

        group("Pluto", Alchemy.Category.METAL, Set.of(warm_ingot, plutonium_ingot), 7,0,0);
        group("Blank", Alchemy.Category.STONE, blankest_slate, 7,0,1);
        group("IllPr", Alchemy.Category.CRYSTAL, prism, 7,0,1);
        group("EdenS", Alchemy.Category.EARTH, super_soil, 7,0,1);
        group("Infer", Alchemy.Category.INFERNAL, inferno_essence, 7,0,1);
        group("Boom2", Alchemy.Category.INFERNAL, explosion_temp_placeholder, 7,0,0);  // TODO make this one explode too when precipitated

        Main.LOGGER.info(count + " at rank <= 7");
        return list;
    }

    private static void group(String name, Alchemy.Category category, AlchemyMaterial material, int tierBase, int tierUp, int tierDown) {
        group(name, category, material, new AlchemyMaterialGroup.Tier(tierBase,tierUp,tierDown));
    }

    private static void group(String name, Alchemy.Category category, AlchemyMaterial material, AlchemyMaterialGroup.Tier tier) {
        group(name, category, Set.of(material), tier);
    }

    private static void group(String name, Alchemy.Category category, Set<AlchemyMaterial> materials, int tierBase, int tierUp, int tierDown) {
        group(name, category, materials, new AlchemyMaterialGroup.Tier(tierBase,tierUp,tierDown));
    }


    private static void group(String name, Alchemy.Category category, Set<AlchemyMaterial> materials, AlchemyMaterialGroup.Tier tier) {
        AlchemyMaterialGroup m = new AlchemyMaterialGroup(name, tier, category, materials);
        list.add(m);
        count++;
    }
    

}
