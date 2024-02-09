package abyssal.init;

import abyssal.Main;
import abyssal.items.*;
import abyssal.items.armour.*;
import abyssal.items.curios.*;
import abyssal.items.handheld.*;
import abyssal.items.handheld.spells.BasicStaff;
import abyssal.items.handheld.spells.DualSpellBook;
import abyssal.spells.Spells;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Supplier;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);
    public static final DeferredRegister<Item> OVERRIDE_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

    public static final Map<ResourceKey<CreativeModeTab>, List<RegistryObject<Item>>> itemTabs = new HashMap<>();
    public static final List<RegistryObject<Item>> tab1Items = new ArrayList<>();
    public static final List<RegistryObject<Item>> tab2Items = new ArrayList<>();

    static Item.Properties defaultItemProperties() {
        return new Item.Properties();
    }

    static Item.Properties defaultCurioProperties() {
        return new Item.Properties().stacksTo(1);
    }

    public static final RegistryObject<Item> CORN = register("corn", () -> new Item(defaultItemProperties().food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.5f).build())));
    public static final RegistryObject<Item> CROC = register("croc", () -> new CrocItem(defaultItemProperties().food((new FoodProperties.Builder()).nutrition(8).saturationMod(2f).build())));
    public static final RegistryObject<Item> BAT_WING = register("bat_wing", () -> new Item(defaultItemProperties().food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.5f).meat().build())));

    private static final Supplier<MobEffectInstance> batSoupEffect = () -> new MobEffectInstance(MobEffects.WITHER, 200, 1);
    public static final RegistryObject<Item> BAT_WING_SOUP = register("bat_wing_soup", () -> new BowlFoodItem(defaultItemProperties().food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.6f).effect(batSoupEffect, 0.1F).build())));
    public static final RegistryObject<Item> FISH_PAINTING = register("fish_painting", () -> new FishPaintingItem(defaultItemProperties()));
    public static final RegistryObject<Item> MOVER = registerCurio("mover", () -> new SpawnerMoverItem(defaultItemProperties().stacksTo(1)));
    public static final RegistryObject<Item> BOOSTER = registerCurio("booster", () -> new RocketItem(defaultItemProperties().stacksTo(1), 3.0f));
    public static final RegistryObject<Item> SKELETON_WAND = registerCurio("skeleton_wand", () -> new SkeletonPlacerItem(defaultItemProperties().stacksTo(1)));
    public static final RegistryObject<Item> RECALL_STAFF = registerCurio("recall_staff", () -> new RecallStaff(defaultItemProperties().stacksTo(1)));

    public static final RegistryObject<Item> DOWSING_ROD = registerCurio("dowsing_rod", () -> new DowsingRodItem(defaultItemProperties().stacksTo(1)));
    public static final RegistryObject<Item> DEBUG_DOWSING_ROD = registerCurio("debug_dowsing_rod", () -> new DebugDowsingRodItem(defaultItemProperties().stacksTo(1)));
    public static final RegistryObject<Item> PORTAL_LIGHTER = registerCurio("portal_lighter", () -> new PortalLighterItem(defaultItemProperties().stacksTo(1)));

    public static final RegistryObject<Item> AMP_TOME = registerCurio("amp_tome", () -> new AmpTomeItem(defaultItemProperties().stacksTo(1)));
    public static final RegistryObject<Item> LOST_CHAPTER = registerCurio("lost_chapter", () -> new DualSpellBook(defaultItemProperties().stacksTo(1), 40, UUID.randomUUID())); // TODO generate UUIDs
    public static final RegistryObject<Item> FIENDISH_CODEX = registerCurio("fiendish_codex", () -> new DualSpellBook(defaultItemProperties().stacksTo(1), 35, UUID.randomUUID()));

    public static final RegistryObject<Item> SKELETON_STAFF = registerCurio("skeleton_staff", () -> new BasicStaff(defaultItemProperties().stacksTo(1), 0, UUID.randomUUID(), Spells.SUMMON_SKELETON));
    public static final RegistryObject<Item> ENCHANTING_STAFF = registerCurio("enchanting_staff", () -> new BasicStaff(defaultItemProperties().stacksTo(1), 0, UUID.randomUUID(), Spells.ENCHANT));

    public static final RegistryObject<Item> WACKY_SKULL = registerCurio("wacky_skull", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> RUBY_CRYSTAL = registerCurio("ruby_crystal", () -> new RubyCrystalItem(defaultCurioProperties()));
    public static final RegistryObject<Item> RAGE_TOTEM = registerCurio("rage_totem", () -> new RageTotemItem(defaultCurioProperties()));
    public static final RegistryObject<Item> CLEANSING_TOTEM = registerCurio("cleansing_totem", () -> new CleansingTotemItem(defaultCurioProperties()));
    public static final RegistryObject<Item> TOXIC_TOTEM = registerCurio("toxic_totem", () -> new ModCurioItem(defaultCurioProperties()));
    public static final RegistryObject<Item> GIANTS_BELT = registerCurio("giants_belt", () -> new GiantsBeltItem(defaultCurioProperties()));
    public static final RegistryObject<Item> REJUVENATION_BEAD = registerCurio("rejuvenation_bead", () -> new RejuvenationBeadItem(defaultCurioProperties(), 0.005f));
    public static final RegistryObject<Item> REJUVENATION_NECKLACE = registerCurio("rejuvenation_necklace", () -> new RejuvenationBeadItem(defaultCurioProperties(), 0.015f));
    public static final RegistryObject<Item> REJUVENATION_RING = registerCurio("rejuvenation_ring", () -> new RejuvenationBeadItem(defaultCurioProperties(), 0.005f));
    public static final RegistryObject<Item> REJUVENATION_BELT = registerCurio("rejuvenation_belt", () -> new RejuvenationBeadItem(defaultCurioProperties(), 0.015f));
    public static final RegistryObject<Item> BELT = registerCurio("belt", () -> new ModCurioItem(defaultCurioProperties()));
    public static final RegistryObject<Item> COIN_PURSE = registerCurio("coin_purse", () -> new CoinPurseItem(defaultCurioProperties()));
    public static final RegistryObject<Item> SKULL_BELT = registerCurio("skull_belt", () -> new ModCurioItem(defaultCurioProperties()));
    public static final RegistryObject<Item> ANCHOR_BELT = registerCurio("anchor_belt", () -> new AnchorBeltItem(defaultCurioProperties()));
    public static final RegistryObject<Item> TIAMAT = registerCurio("tiamat", () -> new TiamatItem(ModItemTiers.NETHER_BRASS, 3, -3.2F, defaultItemProperties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> TITANIC_HYDRA = registerCurio("titanic_hydra", () -> new TitanicHydraItem(ModItemTiers.HYDRA, defaultItemProperties().stacksTo(1)));
    public static final RegistryObject<Item> NASHORS_TOOTH = registerCurio("nashors_tooth", () -> new NashorsToothItem(ModItemTiers.NETHER_BRASS, defaultItemProperties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> NETHER_BRASS_AXE = registerCurio("nether_brass_axe", () -> new CharAxe(ModItemTiers.NETHER_BRASS, 6, -3.2f, defaultItemProperties().stacksTo(1).fireResistant(), false, 3));
    public static final RegistryObject<Item> CHARRING_AXE = registerCurio("charring_axe", () -> new CharAxe(ModItemTiers.NETHER_BRASS, 7, -3.2f, defaultItemProperties().stacksTo(1).fireResistant(), true, 5));
    public static final RegistryObject<Item> GOLD_RING = registerCurio("gold_ring", () -> new ModCurioItem(defaultCurioProperties()));
    public static final RegistryObject<Item> SILVER_RING = registerCurio("silver_ring", () -> new ModCurioItem(defaultCurioProperties()));
    public static final RegistryObject<Item> GLOW_RING = registerCurio("glow_ring", () -> new GlowRingItem(defaultCurioProperties()));
    public static final RegistryObject<Item> DAMAGE_RING = registerCurio("damage_ring", () -> new DamageCurioItem(defaultCurioProperties(), 1));
    public static final RegistryObject<Item> CHAMPIONS_RING = registerCurio("champions_ring", () -> new DamageCurioItem(defaultCurioProperties(), 2));
    public static final RegistryObject<Item> DORANS_RING = registerCurio("dorans_ring", () -> new DoransRingItem(defaultCurioProperties()));
    public static final RegistryObject<Item> LIGHT_TRAVEL_RING = registerCurio("light_travel_ring", () -> new LightTravelRingItem(defaultCurioProperties()));
    public static final RegistryObject<Item> GLASS_CANNON_RING = registerCurio("glass_cannon_ring", () -> new GlassCannonRingItem(defaultCurioProperties()));
    public static final RegistryObject<Item> FOUR_LEAF_CLOVER = registerCurio("four_leaf_clover", () -> new LuckCurioItem(defaultCurioProperties(), 1));
    public static final RegistryObject<Item> LUCK_CHARM = registerCurio("luck_charm", () -> new LuckCurioItem(defaultCurioProperties(), 2));
    public static final RegistryObject<Item> FISH_NECKLACE = registerCurio("fishbone_necklace", () -> new WaterCurioItem(defaultCurioProperties()));
    public static final RegistryObject<Item> CLOCKWORK_AMULET = registerCurio("clockwork_amulet", () -> new ModCurioItem(defaultCurioProperties().fireResistant()));
    public static final RegistryObject<Item> CREEPER_JELLY = register("nitro", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> SALTPETRE = register("saltpetre", () -> new BoneMealItem(defaultItemProperties()));
    public static final RegistryObject<Item> SULFUR = register("sulfur", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> GOLD_COIN = register("gold_coin", () -> new PiglinCurrencyItem(defaultItemProperties()));
    public static final RegistryObject<Item> SILVER_COIN = register("silver_coin", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> COPPER_COIN = register("copper_coin", () -> new Item(defaultItemProperties()));

    public static final RegistryObject<Item> RAW_SILVER = register("raw_silver", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> POOR_IRON = register("poor_iron", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> SILVER_INGOT = register("silver_ingot", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> SILVER_NUGGET = register("silver_nugget", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> DIAMOND_INGOT = register("diamond_ingot", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> NETHER_BRASS_NUGGET = register("nether_brass_nugget", () -> new Item(defaultItemProperties().fireResistant()));
    public static final RegistryObject<Item> NETHER_BRASS_INGOT = register("nether_brass_ingot", () -> new Item(defaultItemProperties().fireResistant()));
    public static final RegistryObject<Item> ALCHEMICAL_GOLD_NUGGET = register("alchemical_gold_nugget", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> ALCHEMICAL_GOLD_INGOT = register("alchemical_gold_ingot", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> PLUTONIUM_INGOT = register("plutonium_ingot", () -> new RadioactiveItem(defaultItemProperties(), 1));
    public static final RegistryObject<Item> WARM_INGOT = register("warm_ingot", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> FOOLS_GOLD = register("fools_gold", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> PRISMATIC_POWDER = register("prismatic_powder", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> UU_MATTER = register("uumatter", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> AETHER_WISP = register("aether_wisp", () -> new AetherWispItem(defaultItemProperties()));
    public static final RegistryObject<Item> PURE_QUARTZ = register("pure_quartz", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> LIGNITE = register("lignite", () -> new BasicFuelItem(defaultItemProperties(),1200));
    public static final RegistryObject<Item> ANTHRACITE = register("anthracite", () -> new BasicFuelItem(defaultItemProperties(), 2000));
    public static final RegistryObject<Item> BLANKEST_SLATE = register("blankest_slate", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> INFERNO_ESSENCE = register("inferno_essence", () -> new Item(defaultItemProperties().fireResistant()));
    public static final RegistryObject<Item> PHILO_STONE = register("magnum_opus", () -> new Item(defaultItemProperties()));
    public static final RegistryObject<Item> ELIXIR = register("elixir", () -> new ElixirItem(defaultItemProperties().stacksTo(1)));
    public static final RegistryObject<Item> INFERNAL_MECHANISM = register("infernal_mechanism", () -> new Item(defaultItemProperties().fireResistant()));
    public static final RegistryObject<Item> ALCHEMICAL_FILTER = register("alchemical_filter", () -> new Item(defaultItemProperties()));

    public static final RegistryObject<Item> ICHOR_BUCKET = register("ichor_bucket", () -> new BucketItem(ModFluids.ICHOR_FLUID, defaultItemProperties().stacksTo(1)));

    public static final RegistryObject<Item> WARMOGS = registerCurio("warmogs", () -> new WarmogsItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MOBI_BOOTS = registerCurio("mobi_boots", () -> new MobiBootsItem(ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> NULL_MAGIC_MANTLE = registerCurio("null_magic_mantle", () -> new NMMItem(defaultCurioProperties()));
    public static final RegistryObject<Item> SPECTRES_COWL = registerCurio("spectres_cowl", () -> new SpectresCowlItem(ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> SPIRIT_VISAGE = registerCurio("spirit_visage", () -> new SpiritVisageItem(ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MERC_TREADS = registerCurio("merc_treads", () -> new MercTreadsItem(ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> RABADONS = registerCurio("rabadons", () -> new RabadonsItem(ArmorItem.Type.HELMET, defaultItemProperties()));

    public static final RegistryObject<Item> SILVER_HELMET = register("silver_helmet", () -> new ModArmourItem(ModArmourMaterials.SILVER, ArmorItem.Type.HELMET, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final RegistryObject<Item> SILVER_CHESTPLATE = register("silver_chestplate", () -> new ModArmourItem(ModArmourMaterials.SILVER, ArmorItem.Type.CHESTPLATE, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final RegistryObject<Item> SILVER_LEGGINGS = register("silver_leggings", () -> new ModArmourItem(ModArmourMaterials.SILVER, ArmorItem.Type.LEGGINGS, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final RegistryObject<Item> SILVER_BOOTS = register("silver_boots", () -> new ModArmourItem(ModArmourMaterials.SILVER, ArmorItem.Type.BOOTS, new Item.Properties()), CreativeModeTabs.COMBAT);

    public static final RegistryObject<Item> CLOTH_HELMET = register("cloth_helmet", () -> new ModDyeableArmourItem(ModArmourMaterials.CLOTH, ArmorItem.Type.HELMET, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final RegistryObject<Item> CLOTH_CHESTPLATE = register("cloth_chestplate", () -> new ModDyeableArmourItem(ModArmourMaterials.CLOTH, ArmorItem.Type.CHESTPLATE, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final RegistryObject<Item> CLOTH_LEGGINGS = register("cloth_leggings", () -> new ModDyeableArmourItem(ModArmourMaterials.CLOTH, ArmorItem.Type.LEGGINGS, new Item.Properties()), CreativeModeTabs.COMBAT);
    public static final RegistryObject<Item> CLOTH_BOOTS = register("cloth_boots", () -> new ModDyeableArmourItem(ModArmourMaterials.CLOTH, ArmorItem.Type.BOOTS, new Item.Properties()), CreativeModeTabs.COMBAT);

    public static final RegistryObject<Item> LEATHER_HELMET = OVERRIDE_ITEMS.register("leather_helmet", () -> new DyeableArmorItem(ModArmourMaterials.MOD_LEATHER, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> LEATHER_CHESTPLATE = OVERRIDE_ITEMS.register("leather_chestplate", () -> new DyeableArmorItem(ModArmourMaterials.MOD_LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> LEATHER_LEGGINGS = OVERRIDE_ITEMS.register("leather_leggings", () -> new DyeableArmorItem(ModArmourMaterials.MOD_LEATHER, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> LEATHER_BOOTS = OVERRIDE_ITEMS.register("leather_boots", () -> new DyeableArmorItem(ModArmourMaterials.MOD_LEATHER, ArmorItem.Type.BOOTS, new Item.Properties()));


    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> supplier) {
        RegistryObject<T> r = ITEMS.register(name, supplier);
        tab1Items.add((RegistryObject<Item>) r);
        return r;
    }

    private static <T extends Item> RegistryObject<T> registerCurio(String name, Supplier<T> supplier) {
        RegistryObject<T> r = ITEMS.register(name, supplier);
        tab2Items.add((RegistryObject<Item>) r);
        return r;
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> supplier, ResourceKey<CreativeModeTab> tab) {
        RegistryObject<T> r = ITEMS.register(name, supplier);
        itemTabs.putIfAbsent(tab, new ArrayList<>());
        List<RegistryObject<Item>> l = itemTabs.get(tab);
        l.add((RegistryObject<Item>) r);
        return r;
    }

    public static final List<RegistryObject<Item>> HANDHELD_ITEMS = List.of(
            MOVER, BOOSTER, SKELETON_WAND, DOWSING_ROD, DEBUG_DOWSING_ROD, RECALL_STAFF,
            SKELETON_STAFF, ENCHANTING_STAFF,
            PORTAL_LIGHTER,
            TIAMAT, TITANIC_HYDRA, NASHORS_TOOTH,
            NETHER_BRASS_AXE, CHARRING_AXE
    );

}
