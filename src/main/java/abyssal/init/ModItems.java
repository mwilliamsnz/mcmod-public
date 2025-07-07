package abyssal.init;

import abyssal.Main;
import abyssal.items.*;
import abyssal.items.armour.*;
import abyssal.items.curios.CoinPurseItem;
import abyssal.items.handheld.*;
import abyssal.items.spells.BasicStaff;
import abyssal.items.spells.DualSpellBook;
import abyssal.items.spells.FuelStorageItem;
import abyssal.spells.SpellFuelTypes;
import abyssal.spells.Spells;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Supplier;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Main.MOD_ID);
    public static final DeferredRegister<Item> OVERRIDE_ITEMS = DeferredRegister.createItems("minecraft");

    public static final Map<ResourceKey<CreativeModeTab>, List<Supplier<Item>>> itemTabs = new HashMap<>();
    public static final List<Supplier<Item>> tab1Items = new ArrayList<>();
    public static final List<Supplier<Item>> tab2Items = new ArrayList<>();

    static Item.Properties defaultItemProperties() {
        return new Item.Properties();
    }

    static Item.Properties defaultCurioProperties() {
        return new Item.Properties().stacksTo(1);
    }

    public static final Supplier<Item> CORN = register("corn", () -> new Item(defaultItemProperties().food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.5f).build())));
    public static final Supplier<Item> CROC = register("croc", () -> new CrocItem(defaultItemProperties().food((new FoodProperties.Builder()).nutrition(8).saturationMod(2f).build())));
    public static final Supplier<Item> BAT_WING = register("bat_wing", () -> new Item(defaultItemProperties().food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.5f).meat().build())));

    private static final Supplier<MobEffectInstance> batSoupEffect = () -> new MobEffectInstance(MobEffects.WITHER, 200, 1);
    public static final Supplier<Item> BAT_WING_SOUP = register("bat_wing_soup", () -> new BowlFoodItem(defaultItemProperties().food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.6f).effect(batSoupEffect, 0.1F).build())));
    public static final Supplier<Item> FISH_PAINTING = register("fish_painting", () -> new FishPaintingItem(defaultItemProperties()));
    public static final Supplier<Item> MOVER = registerCurio("mover", () -> new SpawnerMoverItem(defaultItemProperties().stacksTo(1)));
    public static final Supplier<Item> BOOSTER = registerCurio("booster", () -> new RocketItem(defaultItemProperties().stacksTo(1), 3.0f));
    public static final Supplier<Item> SKELETON_WAND = registerCurio("skeleton_wand", () -> new SkeletonPlacerItem(defaultItemProperties().stacksTo(1)));
    public static final Supplier<Item> RECALL_STAFF = registerCurio("recall_staff", () -> new RecallStaff(defaultItemProperties().stacksTo(1)));

    public static final Supplier<Item> DOWSING_ROD = registerCurio("dowsing_rod", () -> new DowsingRodItem(defaultItemProperties().stacksTo(1)));
    public static final Supplier<Item> DEBUG_DOWSING_ROD = registerCurio("debug_dowsing_rod", () -> new DebugDowsingRodItem(defaultItemProperties().stacksTo(1)));
    public static final Supplier<Item> PORTAL_LIGHTER = registerCurio("portal_lighter", () -> new PortalLighterItem(defaultItemProperties().stacksTo(1)));

    public static final Supplier<Item> AMP_TOME = registerCurio("amp_tome", () -> new AmpTomeItem(defaultItemProperties().rarity(Rarity.UNCOMMON)));
    public static final Supplier<Item> LOST_CHAPTER = registerCurio("lost_chapter", () -> new DualSpellBook(defaultItemProperties().stacksTo(1).rarity(Rarity.RARE), 40, UUID.randomUUID())); // TODO generate UUIDs
    public static final Supplier<Item> FIENDISH_CODEX = registerCurio("fiendish_codex", () -> new DualSpellBook(defaultItemProperties().stacksTo(1).rarity(Rarity.RARE), 35, UUID.randomUUID()));

    public static final Supplier<Item> SKELETON_STAFF = registerCurio("skeleton_staff", () -> new BasicStaff(defaultItemProperties().stacksTo(1), 0, UUID.randomUUID(), Spells.SUMMON_SKELETON));
    public static final Supplier<Item> ENCHANTING_STAFF = registerCurio("enchanting_staff", () -> new BasicStaff(defaultItemProperties().stacksTo(1), 0, UUID.randomUUID(), Spells.ENCHANT));

    public static final Supplier<Item> WACKY_SKULL = registerCurio("wacky_skull", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> SAPPHIRE_CRYSTAL = registerCurio("sapphire_crystal", () -> new FuelStorageItem(defaultCurioProperties().durability(250), SpellFuelTypes.FUEL_GENERIC));
    public static final Supplier<Item> KINDLEGEM = registerCurio("kindlegem", () -> new FuelStorageItem(defaultCurioProperties().durability(250), SpellFuelTypes.FUEL_FIRE));
    public static final Supplier<Item> DARK_CUBE = registerCurio("dark_cube", () -> new FuelStorageItem(defaultCurioProperties().durability(250), SpellFuelTypes.FUEL_EVIL));
    public static final Supplier<Item> RADIANT_PRISM = registerCurio("radiant_prism", () -> new FuelStorageItem(defaultCurioProperties().durability(250), SpellFuelTypes.FUEL_LIGHT));
    public static final Supplier<Item> ENERGISED_ORB = registerCurio("energised_orb", () -> new FuelStorageItem(defaultCurioProperties().durability(250), SpellFuelTypes.FUEL_FORCE));
    public static final Supplier<Item> OMNISTONE = registerCurio("omnistone", () -> new FuelStorageItem(defaultCurioProperties().durability(250), SpellFuelTypes.FUEL_COLOURLESS));
    public static final Supplier<Item> RUBY_CRYSTAL = registerCurio("ruby_crystal", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> RAGE_TOTEM = registerCurio("rage_totem", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> CLEANSING_TOTEM = registerCurio("cleansing_totem", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> TOXIC_TOTEM = registerCurio("toxic_totem", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> GIANTS_BELT = registerCurio("giants_belt", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> REJUVENATION_BEAD = registerCurio("rejuvenation_bead", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> REJUVENATION_NECKLACE = registerCurio("rejuvenation_necklace", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> REJUVENATION_RING = registerCurio("rejuvenation_ring", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> REJUVENATION_BELT = registerCurio("rejuvenation_belt", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> BELT = registerCurio("belt", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> COIN_PURSE = registerCurio("coin_purse", () -> new CoinPurseItem(defaultCurioProperties()));
    public static final Supplier<Item> SKULL_BELT = registerCurio("skull_belt", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> ANCHOR_BELT = registerCurio("anchor_belt", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> TIAMAT = registerCurio("tiamat", () -> new TiamatItem(ModItemTiers.NETHER_BRASS, 3, -3.2F, defaultItemProperties().stacksTo(1).fireResistant()));
    public static final Supplier<Item> TITANIC_HYDRA = registerCurio("titanic_hydra", () -> new TitanicHydraItem(ModItemTiers.HYDRA, defaultItemProperties().stacksTo(1)));
    public static final Supplier<Item> NASHORS_TOOTH = registerCurio("nashors_tooth", () -> new NashorsToothItem(ModItemTiers.NETHER_BRASS, defaultItemProperties().stacksTo(1).fireResistant()));
    public static final Supplier<Item> NETHER_BRASS_AXE = registerCurio("nether_brass_axe", () -> new CharAxe(ModItemTiers.NETHER_BRASS, 6, -3.2f, defaultItemProperties().stacksTo(1).fireResistant(), false, 3));
    public static final Supplier<Item> CHARRING_AXE = registerCurio("charring_axe", () -> new CharAxe(ModItemTiers.NETHER_BRASS, 7, -3.2f, defaultItemProperties().stacksTo(1).fireResistant(), true, 5));
    public static final Supplier<Item> GOLD_RING = registerCurio("gold_ring", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> SILVER_RING = registerCurio("silver_ring", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> GLOW_RING = registerCurio("glow_ring", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> DAMAGE_RING = registerCurio("damage_ring", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> CHAMPIONS_RING = registerCurio("champions_ring", () -> new Item(defaultCurioProperties().rarity(Rarity.RARE)));
    public static final Supplier<Item> DORANS_RING = registerCurio("dorans_ring", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> LIGHT_TRAVEL_RING = registerCurio("light_travel_ring", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> GLASS_CANNON_RING = registerCurio("glass_cannon_ring", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> FOUR_LEAF_CLOVER = registerCurio("four_leaf_clover", () -> new Item(defaultCurioProperties().rarity(Rarity.UNCOMMON)));
    public static final Supplier<Item> LUCK_CHARM = registerCurio("luck_charm", () -> new Item(defaultCurioProperties().rarity(Rarity.RARE)));
    public static final Supplier<Item> FISH_NECKLACE = registerCurio("fishbone_necklace", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> CLOCKWORK_AMULET = registerCurio("clockwork_amulet", () -> new Item(defaultCurioProperties().fireResistant()));
    public static final Supplier<Item> CREEPER_JELLY = register("nitro", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> SALTPETRE = register("saltpetre", () -> new BoneMealItem(defaultItemProperties()));
    public static final Supplier<Item> SULFUR = register("sulfur", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> GOLD_COIN = register("gold_coin", () -> new PiglinCurrencyItem(defaultItemProperties()));
    public static final Supplier<Item> SILVER_COIN = register("silver_coin", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> COPPER_COIN = register("copper_coin", () -> new Item(defaultItemProperties()));

    public static final Supplier<Item> RAW_SILVER = register("raw_silver", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> POOR_IRON = register("poor_iron", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> SILVER_INGOT = register("silver_ingot", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> SILVER_NUGGET = register("silver_nugget", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> DIAMOND_INGOT = register("diamond_ingot", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> NETHER_BRASS_NUGGET = register("nether_brass_nugget", () -> new Item(defaultItemProperties().fireResistant()));
    public static final Supplier<Item> NETHER_BRASS_INGOT = register("nether_brass_ingot", () -> new Item(defaultItemProperties().fireResistant()));
    public static final Supplier<Item> ALCHEMICAL_GOLD_NUGGET = register("alchemical_gold_nugget", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> ALCHEMICAL_GOLD_INGOT = register("alchemical_gold_ingot", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> PLUTONIUM_INGOT = register("plutonium_ingot", () -> new RadioactiveItem(defaultItemProperties().rarity(Rarity.EPIC), 1));
    public static final Supplier<Item> WARM_INGOT = register("warm_ingot", () -> new Item(defaultItemProperties().rarity(Rarity.UNCOMMON)));
    public static final Supplier<Item> FOOLS_GOLD = register("fools_gold", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> PRISMATIC_POWDER = register("prismatic_powder", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> UU_MATTER = register("uumatter", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> AETHER_WISP = register("aether_wisp", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> PURE_QUARTZ = register("pure_quartz", () -> new Item(defaultItemProperties()));
    public static final Supplier<Item> LIGNITE = register("lignite", () -> new BasicFuelItem(defaultItemProperties(),1200));
    public static final Supplier<Item> ANTHRACITE = register("anthracite", () -> new BasicFuelItem(defaultItemProperties(), 2000));
    public static final Supplier<Item> BLANKEST_SLATE = register("blankest_slate", () -> new Item(defaultItemProperties().rarity(Rarity.EPIC)));
    public static final Supplier<Item> INFERNO_ESSENCE = register("inferno_essence", () -> new Item(defaultItemProperties().rarity(Rarity.EPIC).fireResistant()));
    public static final Supplier<Item> PHILO_STONE = register("magnum_opus", () -> new Item(defaultItemProperties().rarity(Rarity.EPIC)));
    public static final Supplier<Item> ELIXIR = register("elixir", () -> new ElixirItem(defaultItemProperties().rarity(Rarity.EPIC).stacksTo(1)));
    public static final Supplier<Item> INFERNAL_MECHANISM = register("infernal_mechanism", () -> new Item(defaultItemProperties().fireResistant()));
    public static final Supplier<Item> ALCHEMICAL_FILTER = register("alchemical_filter", () -> new Item(defaultItemProperties()));

    public static final Supplier<Item> WARMOGS = registerCurio("warmogs", () -> new WarmogsItem(ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Supplier<Item> MOBI_BOOTS = registerCurio("mobi_boots", () -> new MobiBootsItem(ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));

    public static final Supplier<Item> NULL_MAGIC_MANTLE = registerCurio("null_magic_mantle", () -> new Item(defaultCurioProperties()));
    public static final Supplier<Item> SPECTRES_COWL = registerCurio("spectres_cowl", () -> new SpectresCowlItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final Supplier<Item> SPIRIT_VISAGE = registerCurio("spirit_visage", () -> new SpiritVisageItem(ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Supplier<Item> MERC_TREADS = registerCurio("merc_treads", () -> new MercTreadsItem(ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final Supplier<Item> RABADONS = registerCurio("rabadons", () -> new RabadonsItem(ArmorItem.Type.HELMET, defaultItemProperties().rarity(Rarity.UNCOMMON)));

    public static final Supplier<Item> SILVER_HELMET = register("silver_helmet", () -> new ModArmourItem(ModArmourMaterials.SILVER, ArmorItem.Type.HELMET, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final Supplier<Item> SILVER_CHESTPLATE = register("silver_chestplate", () -> new ModArmourItem(ModArmourMaterials.SILVER, ArmorItem.Type.CHESTPLATE, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final Supplier<Item> SILVER_LEGGINGS = register("silver_leggings", () -> new ModArmourItem(ModArmourMaterials.SILVER, ArmorItem.Type.LEGGINGS, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final Supplier<Item> SILVER_BOOTS = register("silver_boots", () -> new ModArmourItem(ModArmourMaterials.SILVER, ArmorItem.Type.BOOTS, new Item.Properties()), CreativeModeTabs.COMBAT);

    public static final Supplier<Item> CLOTH_HELMET = register("cloth_helmet", () -> new ModDyeableArmourItem(ModArmourMaterials.CLOTH, ArmorItem.Type.HELMET, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final Supplier<Item> CLOTH_CHESTPLATE = register("cloth_chestplate", () -> new ModDyeableArmourItem(ModArmourMaterials.CLOTH, ArmorItem.Type.CHESTPLATE, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final Supplier<Item> CLOTH_LEGGINGS = register("cloth_leggings", () -> new ModDyeableArmourItem(ModArmourMaterials.CLOTH, ArmorItem.Type.LEGGINGS, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final Supplier<Item> CLOTH_BOOTS = register("cloth_boots", () -> new ModDyeableArmourItem(ModArmourMaterials.CLOTH, ArmorItem.Type.BOOTS, new Item.Properties()), CreativeModeTabs.COMBAT);

    public static final Supplier<Item> LEATHER_HELMET = OVERRIDE_ITEMS.register("leather_helmet", () -> new DyeableArmorItem(ModArmourMaterials.MOD_LEATHER, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final Supplier<Item> LEATHER_CHESTPLATE = OVERRIDE_ITEMS.register("leather_chestplate", () -> new DyeableArmorItem(ModArmourMaterials.MOD_LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final Supplier<Item> LEATHER_LEGGINGS = OVERRIDE_ITEMS.register("leather_leggings", () -> new DyeableArmorItem(ModArmourMaterials.MOD_LEATHER, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final Supplier<Item> LEATHER_BOOTS = OVERRIDE_ITEMS.register("leather_boots", () -> new DyeableArmorItem(ModArmourMaterials.MOD_LEATHER, ArmorItem.Type.BOOTS, new Item.Properties()));


    private static <T extends Item> Supplier<T> register(String name, Supplier<T> supplier) {
        Supplier<T> r = ITEMS.register(name, supplier);
        tab1Items.add((Supplier<Item>) r);
        return r;
    }

    private static <T extends Item> Supplier<T> registerCurio(String name, Supplier<T> supplier) {
        Supplier<T> r = ITEMS.register(name, supplier);
        tab2Items.add((Supplier<Item>) r);
        return r;
    }

    private static <T extends Item> Supplier<T> register(String name, Supplier<T> supplier, ResourceKey<CreativeModeTab> tab) {
        Supplier<T> r = ITEMS.register(name, supplier);
        itemTabs.putIfAbsent(tab, new ArrayList<>());
        List<Supplier<Item>> l = itemTabs.get(tab);
        l.add((Supplier<Item>) r);
        return r;
    }

    public static final List<Supplier<Item>> HANDHELD_ITEMS = List.of(
            MOVER, BOOSTER, SKELETON_WAND, DOWSING_ROD, DEBUG_DOWSING_ROD, RECALL_STAFF,
            SKELETON_STAFF, ENCHANTING_STAFF,
            PORTAL_LIGHTER,
            TIAMAT, TITANIC_HYDRA, NASHORS_TOOTH,
            NETHER_BRASS_AXE, CHARRING_AXE
    );

}
