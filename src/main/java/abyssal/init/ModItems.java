package abyssal.init;

import abyssal.Main;
import abyssal.ModAttributes;
import abyssal.components.DescComponent;
import abyssal.components.RestoreFuelConsumeEffect;
import abyssal.components.SpellBatteryComponent;
import abyssal.components.SpellRefuelComponent;
import abyssal.items.*;
import abyssal.items.armour.MobiBootsItem;
import abyssal.items.armour.WarmogsItem;
import abyssal.items.curios.CoinPurseBundleContents;
import abyssal.items.curios.CoinPurseItem;
import abyssal.items.handheld.*;
import abyssal.items.spells.*;
import abyssal.spells.SpellFuelQuantity;
import abyssal.spells.SpellFuelTypes;
import abyssal.spells.Spells;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Function;

import static abyssal.Main.rl;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Main.MOD_ID);
    public static final DeferredRegister.Items OVERRIDE_ITEMS = DeferredRegister.createItems("minecraft");

    public static final Map<ResourceKey<CreativeModeTab>, List<DeferredItem<Item>>> itemTabs = new HashMap<>();
    public static final List<DeferredItem<Item>> tab1Items = new ArrayList<>();
    public static final List<DeferredItem<Item>> tab2Items = new ArrayList<>();

    public static final float ONE_REJUV_BEAD_REGEN = 0.25f;

    public static Item.Properties defaultPs(Identifier l) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, l));
    }

    public static Item.Properties descPsStacking(Identifier l) {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, l)).component(ModDataComponents.DESC, new DescComponent(l));
    }

    // public for InitGems
    public static Item.Properties noStackPs(Identifier l) {
        return new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, l));
    }

    public static Item.Properties descPs(Identifier l) {
        return new Item.Properties().stacksTo(1).setId(ResourceKey.create(Registries.ITEM, l)).component(ModDataComponents.DESC, new DescComponent(l));
    }

    private static final Consumable BAT_WING_SOUP_CONSUME = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.WITHER, 200, 1)
            ), 0.1f)).build();
    private static final Consumable ELIXIR_CONSUME = Consumables.defaultDrink()
            .onConsume(new RemoveStatusEffectsConsumeEffect(HolderSet.direct(MobEffects.BLINDNESS, MobEffects.POISON,
                    MobEffects.WITHER, MobEffects.WEAKNESS, MobEffects.HUNGER, MobEffects.BAD_OMEN, MobEffects.SLOWNESS,
                    MobEffects.MINING_FATIGUE, MobEffects.NAUSEA)))
            .onConsume(new ApplyStatusEffectsConsumeEffect(List.of(
                    new MobEffectInstance(MobEffects.REGENERATION, 500, 1),
                    new MobEffectInstance(MobEffects.RESISTANCE, 500, 0)
            ))).build();

    private static Consumable baseFuel(SoundEvent sound) {
        return Consumable.builder().consumeSeconds(1.6F).animation(ItemUseAnimation.BLOCK)
                .sound(Holder.direct(sound)).hasConsumeParticles(false)
                .onConsume(new RestoreFuelConsumeEffect())
                .build();
    }

    private static final Consumable REFUEL_LIGHT = baseFuel(SoundEvents.SLIME_SQUISH);
    private static final Consumable REFUEL_FIRE = baseFuel(SoundEvents.BLAZE_BURN);
    private static final Consumable REFUEL_FORCE = baseFuel(SoundEvents.FIREWORK_ROCKET_BLAST);


    public static final DeferredItem<Item> CORN = register("corn",
            l -> new Item(defaultPs(l).food((new FoodProperties.Builder()).nutrition(3).saturationModifier(0.5f).build())));
    public static final DeferredItem<Item> CROC = register("croc",
            l -> new CrocItem(defaultPs(l).food((new FoodProperties.Builder()).nutrition(8).saturationModifier(2f).build())));
    public static final DeferredItem<Item> BAT_WING = register("bat_wing",
            l -> new Item(defaultPs(l).food((new FoodProperties.Builder()).nutrition(2).saturationModifier(0.5f).build())));

    public static final DeferredItem<Item> BAT_WING_SOUP = register("bat_wing_soup",
            l -> new Item(defaultPs(l).food((new FoodProperties.Builder()).nutrition(8).saturationModifier(0.6f).build())
            .component(DataComponents.CONSUMABLE, BAT_WING_SOUP_CONSUME).usingConvertsTo(Items.BOWL)));
    public static final DeferredItem<Item> ELIXIR = register("elixir",
            l -> new Item(defaultPs(l).rarity(Rarity.EPIC).stacksTo(1)
                    .component(DataComponents.CONSUMABLE, ELIXIR_CONSUME).usingConvertsTo(Items.GLASS_BOTTLE)));
    public static final DeferredItem<Item> FISH_PAINTING = register("fish_painting",
            l -> new FishPaintingItem(EntityType.PAINTING, defaultPs(l)));
    public static final DeferredItem<Item> MOVER = registerCurio("mover",
            l -> new SpawnerMoverItem(descPs(l)));
    public static final DeferredItem<Item> BOOSTER = registerCurio("booster",
            l -> new RocketItem(defaultPs(l).stacksTo(1), 3.0f));
    public static final DeferredItem<Item> SKELETON_WAND = registerCurio("skeleton_wand",
            l -> new SkeletonPlacerItem(defaultPs(l).stacksTo(1)));
    public static final DeferredItem<Item> RECALL_STAFF = registerCurio("recall_staff",
            l -> new RecallStaff(descPs(l)));

    public static final DeferredItem<Item> DOWSING_ROD = registerCurio("dowsing_rod",
            l -> new BasicStaff(descPs(l), Spells.DOWSING));
    public static final DeferredItem<Item> DEBUG_DOWSING_ROD = registerCurio("debug_dowsing_rod",
            l -> new DebugDowsingRodItem(defaultPs(l).stacksTo(1)));
    public static final DeferredItem<Item> PORTAL_LIGHTER = registerCurio("portal_lighter",
            l -> new PortalLighterItem(descPs(l)));

    public static final DeferredItem<Item> AMP_TOME = registerCurio("amp_tome", l -> new Item(defaultPs(l).rarity(Rarity.UNCOMMON)
            .attributes(ItemAttributeModifiers.builder()
                    .add(ModAttributes.ABILITY_POWER, new AttributeModifier(rl("amp_tome_ap"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
                    .build())));
    public static final DeferredItem<Item> LOST_CHAPTER = registerCurio("lost_chapter", l -> new DualSpellBook(defaultPs(l).stacksTo(1).rarity(Rarity.RARE)
            .attributes(ItemAttributeModifiers.builder()
                    .add(ModAttributes.ABILITY_POWER, new AttributeModifier(rl("lost_chapter_ap"), 40, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
                    .build())));
    public static final DeferredItem<Item> FIENDISH_CODEX = registerCurio("fiendish_codex", l -> new DualSpellBook(defaultPs(l).stacksTo(1).rarity(Rarity.RARE)
            .attributes(ItemAttributeModifiers.builder()
                    .add(ModAttributes.ABILITY_POWER, new AttributeModifier(rl("fiendish_codex_ap"), 25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
                    .build())));

    public static final DeferredItem<Item> SKELETON_STAFF = registerCurio("skeleton_staff", l -> new BasicStaff(defaultPs(l).stacksTo(1), Spells.SUMMON_SKELETON));
    public static final DeferredItem<Item> ENCHANTING_STAFF = registerCurio("enchanting_staff", l -> new BasicStaff(defaultPs(l).stacksTo(1), Spells.ENCHANT));
    public static final DeferredItem<Item> KINDLEGEM = registerCurio("kindlegem", l -> new BatteryBarItem(noStackPs(l)
            .component(ModDataComponents.SPELL_BATTERY, new SpellBatteryComponent(SpellFuelTypes.FUEL_FIRE, 250, 250))));
    public static final DeferredItem<Item> SMOLDERING_TABLET = registerCurio("smoldering_tablet", l -> new BatteryBarItem(noStackPs(l)
            .component(ModDataComponents.SPELL_BATTERY, new SpellBatteryComponent(SpellFuelTypes.FUEL_FIRE, 250, 250))));
    public static final DeferredItem<Item> RESONATING_STAR = registerCurio("resonating_star", l -> new BatteryBarItem(noStackPs(l)
            .component(ModDataComponents.SPELL_BATTERY, new SpellBatteryComponent(SpellFuelTypes.FUEL_FORCE, 100, 100))));
    public static final DeferredItem<Item> REVERBERATING_STAR = registerCurio("reverberating_star", l -> new BatteryBarItem(noStackPs(l)
            .component(ModDataComponents.SPELL_BATTERY, new SpellBatteryComponent(SpellFuelTypes.FUEL_FORCE, 250, 250))));
    public static final DeferredItem<Item> OMNISTONE = registerCurio("omnistone", l -> new BatteryBarItem(noStackPs(l)
            .component(ModDataComponents.SPELL_BATTERY, new SpellBatteryComponent(SpellFuelTypes.FUEL_COLOURLESS, 600, 600))));

    public static final DeferredItem<Item> GLOWING_PASTE = register("glowing_paste", l
            -> new Item(defaultPs(l).stacksTo(16).component(DataComponents.CONSUMABLE, REFUEL_LIGHT)
            .component(ModDataComponents.SPELL_FUEL_RECHARGE.get(), new SpellRefuelComponent(new SpellFuelQuantity(SpellFuelTypes.FUEL_LIGHT, 20)))));
    public static final DeferredItem<Item> GRAINS_OF_FORCE = register("grains_of_force", l
            -> new Item(defaultPs(l).stacksTo(16).component(DataComponents.CONSUMABLE, REFUEL_FORCE)
            .component(ModDataComponents.SPELL_FUEL_RECHARGE.get(), new SpellRefuelComponent(new SpellFuelQuantity(SpellFuelTypes.FUEL_FORCE, 20)))));
    public static final DeferredItem<Item> INCENDIARY_POWDER = register("incendiary_powder", l
            -> new Item(defaultPs(l).stacksTo(16).component(DataComponents.CONSUMABLE, REFUEL_FIRE)
            .component(ModDataComponents.SPELL_FUEL_RECHARGE.get(), new SpellRefuelComponent(new SpellFuelQuantity(SpellFuelTypes.FUEL_FIRE, 20)))));

    public static final DeferredItem<Item> WACKY_SKULL = registerCurio("wacky_skull", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> SAPPHIRE_CRYSTAL = registerCurio("sapphire_crystal", l -> new BatteryBarItem(noStackPs(l)
            .component(ModDataComponents.SPELL_BATTERY, new SpellBatteryComponent(SpellFuelTypes.FUEL_LIGHT, 250, 250))));
    public static final DeferredItem<Item> RUBY_CRYSTAL = registerCurio("ruby_crystal", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> RAGE_TOTEM = registerCurio("rage_totem", l -> new Item(descPs(l)));
    public static final DeferredItem<Item> CLEANSING_TOTEM = registerCurio("cleansing_totem", l -> new Item(descPs(l)));
    public static final DeferredItem<Item> TOXIC_TOTEM = registerCurio("toxic_totem", l -> new Item(descPs(l)));
    public static final DeferredItem<Item> GIANTS_BELT = registerCurio("giants_belt", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> REJUVENATION_BEAD = registerCurio("rejuvenation_bead", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> REJUVENATION_NECKLACE = registerCurio("rejuvenation_necklace", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> REJUVENATION_RING = registerCurio("rejuvenation_ring", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> REJUVENATION_BELT = registerCurio("rejuvenation_belt", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> BELT = registerCurio("belt", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> COIN_PURSE = registerCurio("coin_purse", l -> new CoinPurseItem(descPs(l).component(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, CoinPurseBundleContents.EMPTY)));
    public static final DeferredItem<Item> SKULL_BELT = registerCurio("skull_belt", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> ANCHOR_BELT = registerCurio("anchor_belt", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> TIAMAT = registerCurio("tiamat",
            l -> new TiamatItem(defaultPs(l).stacksTo(1).fireResistant().sword(ModItemTiers.NETHER_BRASS, 3, -3.2f)));
    public static final DeferredItem<Item> TITANIC_HYDRA = registerCurio("titanic_hydra",
            l -> new TitanicHydraItem(descPs(l).stacksTo(1).sword(ModItemTiers.NETHER_BRASS, 2, -3.2f)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -3.2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.SWEEPING_DAMAGE_RATIO, new AttributeModifier(rl("hydra_sweep"), 0.25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .build())));
    public static final DeferredItem<Item> NASHORS_TOOTH = registerCurio("nashors_tooth",
            l -> new NashorsToothItem(descPs(l).fireResistant().sword(ModItemTiers.NETHER_BRASS, 2, -1.0f)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .add(ModAttributes.ABILITY_POWER, new AttributeModifier(rl("nashors_ap"), 80, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HAND)
                            .build())));
    public static final DeferredItem<Item> NETHER_BRASS_AXE = registerCurio("nether_brass_axe",
            l -> new CharAxe(ModItemTiers.NETHER_BRASS, 6, -3.2f, descPs(l).fireResistant(), false, 3));
    public static final DeferredItem<Item> CHARRING_AXE = registerCurio("charring_axe",
            l -> new CharAxe(ModItemTiers.NETHER_BRASS, 7, -3.2f, descPs(l).fireResistant(), true, 5));
    public static final DeferredItem<Item> GOLD_RING = registerCurio("gold_ring", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> SILVER_RING = registerCurio("silver_ring", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> GLOW_RING = registerCurio("glow_ring", l -> new Item(descPs(l)));
    public static final DeferredItem<Item> DAMAGE_RING = registerCurio("damage_ring", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> CHAMPIONS_RING = registerCurio("champions_ring", l -> new Item(noStackPs(l).rarity(Rarity.RARE)));
    public static final DeferredItem<Item> DORANS_RING = registerCurio("dorans_ring", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> LIGHT_TRAVEL_RING = registerCurio("light_travel_ring", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> GLASS_CANNON_RING = registerCurio("glass_cannon_ring", l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> FOUR_LEAF_CLOVER = registerCurio("four_leaf_clover", l -> new Item(noStackPs(l).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> LUCK_CHARM = registerCurio("luck_charm", l -> new Item(noStackPs(l).rarity(Rarity.RARE)));
    public static final DeferredItem<Item> FISH_NECKLACE = registerCurio("fishbone_necklace", l -> new Item(descPs(l)));
    public static final DeferredItem<Item> CLOCKWORK_AMULET = registerCurio("clockwork_amulet", l -> new Item(descPs(l).fireResistant()));
    public static final DeferredItem<Item> CREEPER_JELLY = register("nitro", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> SALTPETRE = register("saltpetre", l -> new BoneMealItem(descPsStacking(l)));
    public static final DeferredItem<Item> SULFUR = register("sulfur", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> GOLD_COIN = register("gold_coin", l -> new PiglinCurrencyItem(defaultPs(l)));
    public static final DeferredItem<Item> SILVER_COIN = register("silver_coin", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> COPPER_COIN = register("copper_coin", l -> new Item(defaultPs(l)));

    public static final DeferredItem<Item> RAW_SILVER = register("raw_silver", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> POOR_IRON = register("poor_iron", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> SILVER_INGOT = register("silver_ingot", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> SILVER_NUGGET = register("silver_nugget", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> DIAMOND_INGOT = register("diamond_ingot", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> NETHER_BRASS_NUGGET = register("nether_brass_nugget", l -> new Item(defaultPs(l).fireResistant()));
    public static final DeferredItem<Item> NETHER_BRASS_INGOT = register("nether_brass_ingot", l -> new Item(defaultPs(l).fireResistant()));
    public static final DeferredItem<Item> ALCHEMICAL_GOLD_NUGGET = register("alchemical_gold_nugget", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> ALCHEMICAL_GOLD_INGOT = register("alchemical_gold_ingot", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> PLUTONIUM_INGOT = register("plutonium_ingot", l -> new RadioactiveItem(defaultPs(l).rarity(Rarity.EPIC), 1));
    public static final DeferredItem<Item> WARM_INGOT = register("warm_ingot", l -> new Item(defaultPs(l).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> FOOLS_GOLD = register("fools_gold", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> PRISMATIC_POWDER = register("prismatic_powder", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> UU_MATTER = register("uumatter", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> AETHER_WISP = register("aether_wisp", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> PURE_QUARTZ = register("pure_quartz", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> LIGNITE = register("lignite", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> ANTHRACITE = register("anthracite", l -> new Item(defaultPs(l)));
    public static final DeferredItem<Item> BLANKEST_SLATE = register("blankest_slate", l -> new Item(defaultPs(l).rarity(Rarity.EPIC)));
    public static final DeferredItem<Item> INFERNO_ESSENCE = register("inferno_essence", l -> new Item(defaultPs(l).rarity(Rarity.EPIC).fireResistant()));
    public static final DeferredItem<Item> PHILO_STONE = register("magnum_opus", l -> new Item(defaultPs(l).rarity(Rarity.EPIC)));
    public static final DeferredItem<Item> INFERNAL_MECHANISM = register("infernal_mechanism", l -> new Item(defaultPs(l).fireResistant()));
    public static final DeferredItem<Item> ALCHEMICAL_FILTER = register("alchemical_filter", l -> new Item(defaultPs(l)));

    public static final DeferredItem<Item> WARMOGS = registerCurio("warmogs",
            l -> new WarmogsItem(descPs(l).humanoidArmor(ModArmourMaterials.WARMOGS, ArmorType.CHESTPLATE).rarity(Rarity.UNCOMMON)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.MAX_HEALTH, new AttributeModifier(rl("warmogs_hp"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                            .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(rl("warmogs_speed"), 0.04, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.CHEST)
                            .build())
                    .component(ModDataComponents.DESC, new DescComponent(rl("warmogs")))));
    public static final DeferredItem<Item> MOBI_BOOTS = registerCurio("mobi_boots",
            l -> new MobiBootsItem(descPs(l).humanoidArmor(ModArmourMaterials.WARMOGS, ArmorType.BOOTS).fireResistant()
                    .attributes(ItemAttributeModifiers.builder().add(Attributes.MOVEMENT_SPEED, MobiBootsItem.OUT_OF_COMBAT_MODIFIER, EquipmentSlotGroup.FEET).build())));

    public static final DeferredItem<Item> NULL_MAGIC_MANTLE = registerCurio("null_magic_mantle",
            l -> new Item(noStackPs(l)));
    public static final DeferredItem<Item> SPECTRES_COWL = registerCurio("spectres_cowl",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.MR_ITEMS, ArmorType.HELMET)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.MAX_HEALTH, new AttributeModifier(rl("spectres_cowl_hp"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD)
                            .add(ModAttributes.MAGIC_RESIST, new AttributeModifier(rl("spectres_cowl_mr"), 35, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD)
                            .add(ModAttributes.REGEN, new AttributeModifier(rl("spectres_cowl_regen"), ONE_REJUV_BEAD_REGEN, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD)
                            .build())));
    public static final DeferredItem<Item> SPIRIT_VISAGE = registerCurio("spirit_visage",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.MR_ITEMS, ArmorType.CHESTPLATE).rarity(Rarity.UNCOMMON)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.MAX_HEALTH, new AttributeModifier(rl("spirit_visage_hp"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                            .add(ModAttributes.MAGIC_RESIST, new AttributeModifier(rl("spirit_visage_mr"), 50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                            .add(ModAttributes.HEAL_RATE, new AttributeModifier(rl("spirit_visage_heal"), 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.CHEST)
                            .build())));
    public static final DeferredItem<Item> MERC_TREADS = registerCurio("merc_treads",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.MR_ITEMS, ArmorType.BOOTS)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(rl("merc_treads_speed"), 0.015, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                            .add(ModAttributes.MAGIC_RESIST, new AttributeModifier(rl("merc_treads_mr"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET)
                            .add(ModAttributes.TENACITY, new AttributeModifier(rl("merc_treads_tenacity"), 0.35, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.FEET)
                            .build())));
    public static final DeferredItem<Item> THORNMAIL = registerCurio("thornmail",
            l -> new Item(descPs(l).humanoidArmor(ModArmourMaterials.THORNMAIL, ArmorType.CHESTPLATE)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ARMOR, new AttributeModifier(rl("thornmail_armour"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                            .add(Attributes.MAX_HEALTH, new AttributeModifier(rl("thornmail_hp"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST)
                            .build())));
    public static final DeferredItem<Item> RABADONS = registerCurio("rabadons",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.MR_ITEMS, ArmorType.HELMET).rarity(Rarity.UNCOMMON)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(ModAttributes.ABILITY_POWER, new AttributeModifier(rl("rabadons_flat_ap"), 130, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD)
                            .add(ModAttributes.ABILITY_POWER, new AttributeModifier(rl("rabadons_scaling_ap"), 0.30, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.HEAD)
                            .build())));

    public static final DeferredItem<Item> SILVER_HELMET = register("silver_helmet",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.SILVER, ArmorType.HELMET)), CreativeModeTabs.COMBAT);
    public static final DeferredItem<Item> SILVER_CHESTPLATE = register("silver_chestplate",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.SILVER, ArmorType.CHESTPLATE)), CreativeModeTabs.COMBAT);
    public static final DeferredItem<Item> SILVER_LEGGINGS = register("silver_leggings",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.SILVER, ArmorType.LEGGINGS)), CreativeModeTabs.COMBAT);
    public static final DeferredItem<Item> SILVER_BOOTS = register("silver_boots",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.SILVER, ArmorType.BOOTS)), CreativeModeTabs.COMBAT);

    public static final DeferredItem<Item> CLOTH_HELMET = register("cloth_helmet",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.CLOTH, ArmorType.HELMET)), CreativeModeTabs.COMBAT);
    public static final DeferredItem<Item> CLOTH_CHESTPLATE = register("cloth_chestplate",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.CLOTH, ArmorType.CHESTPLATE)), CreativeModeTabs.COMBAT);
    public static final DeferredItem<Item> CLOTH_LEGGINGS = register("cloth_leggings",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.CLOTH, ArmorType.LEGGINGS)), CreativeModeTabs.COMBAT);
    public static final DeferredItem<Item> CLOTH_BOOTS = register("cloth_boots",
            l -> new Item(noStackPs(l).humanoidArmor(ModArmourMaterials.CLOTH, ArmorType.BOOTS)), CreativeModeTabs.COMBAT);


    private static <T extends Item> DeferredItem<T> register(String name, Function<Identifier, T> supplier) {
        DeferredItem<T> r = ITEMS.register(name, supplier);
        tab1Items.add((DeferredItem<Item>) r);
        return r;
    }

    private static <T extends Item> DeferredItem<T> registerCurio(String name, Function<Identifier, T> supplier) {
        DeferredItem<T> r = ITEMS.register(name, supplier);
        tab2Items.add((DeferredItem<Item>) r);
        return r;
    }

    private static <T extends Item> DeferredItem<T> register(String name, Function<Identifier, T> supplier, ResourceKey<CreativeModeTab> tab) {
        DeferredItem<T> r = ITEMS.register(name, supplier);
        itemTabs.putIfAbsent(tab, new ArrayList<>());
        List<DeferredItem<Item>> l = itemTabs.get(tab);
        l.add((DeferredItem<Item>) r);
        return r;
    }

    public static final Set<DeferredItem<Item>> HANDHELD_ITEMS = Set.of(
            MOVER, BOOSTER, SKELETON_WAND, DOWSING_ROD, DEBUG_DOWSING_ROD, RECALL_STAFF,
            SKELETON_STAFF, ENCHANTING_STAFF,
            PORTAL_LIGHTER,
            TIAMAT, TITANIC_HYDRA, NASHORS_TOOTH,
            NETHER_BRASS_AXE, CHARRING_AXE
    );

}
