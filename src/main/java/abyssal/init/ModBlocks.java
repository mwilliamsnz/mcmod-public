package abyssal.init;

import abyssal.Main;
import abyssal.blocks.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModBlocks {

    // materials https://gist.github.com/GizmoTheMoonPig/77a90a48e0aeecd15b4c524e1c7f0a4a
    public static final DeferredRegister<Block> BLOCKS =  DeferredRegister.createBlocks(Main.MOD_ID);

    // Manual loot table blocks are NOT in this list
    public static final List<Holder<Block>> DATAGEN_LOOT_TABLE = new ArrayList<>();
    public static final List<Holder<Block>> DATAGEN_MODEL = new ArrayList<>();

    public static final BlockSetType ELDER_PINE = BlockSetType.register(new BlockSetType(Main.MOD_ID + ":elder_pine"));

    public static BlockBehaviour.Properties defaultPs(Identifier l) {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, l));
    }

    public static BlockBehaviour.Properties pCopy(Identifier l, Block block) {
        return BlockBehaviour.Properties.ofFullCopy(block).setId(ResourceKey.create(Registries.BLOCK, l));
    }

    public static final DeferredHolder<Block, Block> CORN_SEED = BLOCKS.register("corn_seed", l -> new CropBlock(pCopy(l, Blocks.BEETROOTS)));
    public static final DeferredHolder<Block, Block> REED = register("reed", l -> new ReedBlock(pCopy(l, Blocks.SUGAR_CANE)));
    public static final DeferredHolder<Block, RotatedPillarBlock> ELDER_PINE_LOG = register("elder_pine_log", l -> woodenlog(100, 2, l));
    public static final DeferredHolder<Block, Block> ELDER_PINE_PLANKS = register("elder_pine_planks", l -> new Block(defaultPs(l).mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(50f,2)));
    public static final DeferredHolder<Block, Block> ELDER_PINE_DOOR = registerJSONModel("elder_pine_door", l -> new DoorBlock(ELDER_PINE, defaultPs(l).mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(50f,2)));
    public static final DeferredHolder<Block, RotatedPillarBlock> MOSSY_BIRCH = registerJSONModel("mossy_birch", l -> new RotatedPillarBlock(pCopy(l, Blocks.BIRCH_LOG)));
    public static final DeferredHolder<Block, RotatedPillarBlock> MOSSY_OAK = registerJSONModel("mossy_oak", l -> new RotatedPillarBlock(pCopy(l, Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, Block> IVY = registerJSONModel("ivy", l -> new IvyBlock(pCopy(l, Blocks.VINE)));
    public static final DeferredHolder<Block, Block> THIN_LEAVES = register("thin_leaves", l -> new PassableSlowingBlock(pCopy(l, Blocks.OAK_LEAVES).noCollision().speedFactor(0.8f)));
    public static final DeferredHolder<Block, Block> BRUSH = registerJSONModel("brush", l -> new PassableSlowingBlock(pCopy(l, Blocks.BUSH).speedFactor(0.8f)));
    public static final DeferredHolder<Block, Block> SHRUB = registerJSONModel("shrub", l -> new AzaleaBlock(pCopy(l, Blocks.AZALEA)));
    public static final DeferredHolder<Block, Block> HEATHER = register("heather", l -> new FlowerBlock(MobEffects.ABSORPTION, 6, pCopy(l, Blocks.DANDELION)));
    public static final DeferredHolder<Block, Block> ALPINE_PLANT = registerJSONModel("alpine_plant", l -> new DryVegetationBlock(pCopy(l, Blocks.DEAD_BUSH).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final DeferredHolder<Block, Block> CLOVER = registerJSONModel("clover", l -> new GroundCoverBlock(pCopy(l, Blocks.SHORT_GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final DeferredHolder<Block, Block> FERN_CORE = registerJSONModel("fern_core", l -> new FernCentreBlock(pCopy(l, Blocks.FERN).offsetType(BlockBehaviour.OffsetType.NONE)));
    public static final DeferredHolder<Block, Block> FERN_FRONDS = registerJSONModel("fern_fronds", l -> new FernFrondsBlock(pCopy(l, Blocks.FERN).offsetType(BlockBehaviour.OffsetType.NONE)));

    public static final DeferredHolder<Block, Block> LEAF_LITTER = register("leaf_litter", l -> new SnowyBlock(pCopy(l, Blocks.PODZOL)));
    public static final DeferredHolder<Block, Block> SUPER_SOIL = register("super_soil", l -> new SuperSoilBlock(pCopy(l, Blocks.DIRT).randomTicks()));
    public static final DeferredHolder<Block, Block> GRASS_SUPER_SOIL = register("grass_super_soil", l -> new GrassSuperSoilBlock(pCopy(l, Blocks.GRASS_BLOCK).randomTicks()));
    public static final DeferredHolder<Block, Block> PRISM = register("prism", l -> new HalfTransparentBlock(pCopy(l, Blocks.RED_STAINED_GLASS).lightLevel((state) -> {
        return 15;
    })));

    public static final DeferredHolder<Block, Block> ABYSSAL_STONE = register("abyssal_stone", l -> new Block(defaultPs(l).mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(10.0F, 2.0F).sound(SoundType.DEEPSLATE)));
    public static final DeferredHolder<Block, Block> NITRE = register("nitre", l -> new Block(pCopy(l, Blocks.IRON_ORE)));
    public static final DeferredHolder<Block, Block> DEEPSLATE_NITRE = register("deepslate_nitre", l -> new Block(pCopy(l, Blocks.DEEPSLATE_IRON_ORE)));
    public static final DeferredHolder<Block, Block> SULFUR = register("sulfur_ore", l -> new Block(pCopy(l, Blocks.IRON_ORE)));
    public static final DeferredHolder<Block, Block> DEEPSLATE_SULFUR = register("deepslate_sulfur_ore", l -> new Block(pCopy(l, Blocks.DEEPSLATE_IRON_ORE)));
    public static final DeferredHolder<Block, Block> POWDER_BARREL = register("powder_barrel", l -> new PowderBarrelBlock(pCopy(l, Blocks.TNT), 1.6f, 2f, 1.2f));
    public static final DeferredHolder<Block, Block> POWDER_BARREL_FRAG = register("powder_barrel_frag", l -> new PowderBarrelBlock(pCopy(l, Blocks.TNT), 1.3f, 3.85f, 0.2f));
    public static final DeferredHolder<Block, Block> POWDER_BARREL_KNOCK = register("powder_barrel_knock", l -> new PowderBarrelBlock(pCopy(l, Blocks.TNT), 1.6f, 2f, 3f));
    public static final DeferredHolder<Block, Block> ENKATITE = register("enkatite", l -> new Block(defaultPs(l).mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(20.0F, 4F).sound(SoundType.DEEPSLATE)));

    public static final DeferredHolder<Block, Block> GOLD_GRAVEL = BLOCKS.register("gold_gravel", l -> new ColoredFallingBlock(new ColorRGBA(-8356741), pCopy(l, Blocks.GRAVEL)));
    public static final DeferredHolder<Block, Block> POOR_IRON_ORE = register("poor_iron_ore", l -> new Block(pCopy(l, Blocks.IRON_ORE)));
    public static final DeferredHolder<Block, Block> DEEPSLATE_POOR_IRON_ORE = register("poor_deepslate_iron_ore", l -> new Block(pCopy(l, Blocks.DEEPSLATE_IRON_ORE)));
    public static final DeferredHolder<Block, Block> SILVER_ORE = register("silver_ore", l -> new Block(pCopy(l, Blocks.GOLD_ORE)));
    public static final DeferredHolder<Block, Block> DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore", l -> new Block(pCopy(l, Blocks.DEEPSLATE_GOLD_ORE)));
    public static final DeferredHolder<Block, Block> GARNET_CLUSTER = BLOCKS.register("garnet_cluster", l -> new Block(pCopy(l, Blocks.EMERALD_ORE)));
    public static final DeferredHolder<Block, Block> DEEPSLATE_GARNET_CLUSTER = BLOCKS.register("deepslate_garnet_cluster", l -> new Block(pCopy(l, Blocks.DEEPSLATE_EMERALD_ORE)));
    public static final DeferredHolder<Block, Block> ONYX_CLUSTER = BLOCKS.register("onyx_cluster", l -> new Block(pCopy(l, Blocks.EMERALD_ORE)));
    public static final DeferredHolder<Block, Block> DEEPSLATE_ONYX_CLUSTER = BLOCKS.register("deepslate_onyx_cluster", l -> new Block(pCopy(l, Blocks.DEEPSLATE_EMERALD_ORE)));
    public static final DeferredHolder<Block, Block> NETHER_ONYX_CLUSTER = BLOCKS.register("nether_onyx_cluster", l -> new Block(pCopy(l, Blocks.NETHER_GOLD_ORE)));
    public static final DeferredHolder<Block, Block> GEM_CLUSTER = BLOCKS.register("gem_cluster", l -> new Block(pCopy(l, Blocks.EMERALD_ORE)));
    public static final DeferredHolder<Block, Block> DEEPSLATE_GEM_CLUSTER = BLOCKS.register("deepslate_gem_cluster", l -> new Block(pCopy(l, Blocks.DEEPSLATE_EMERALD_ORE)));

    public static final DeferredHolder<Block, RotatedPillarBlock> NETHER_BRASS_BLOCK = register("nether_brass_block", l -> new RotatedPillarBlock(pCopy(l, Blocks.GOLD_BLOCK)));
    public static final DeferredHolder<Block, Block> SILVER_BLOCK = register("silver_block", l -> new Block(pCopy(l, Blocks.GOLD_BLOCK)));
    public static final DeferredHolder<Block, Block> ALCHEMICAL_GOLD_BLOCK = register("alchemical_gold_block", l -> new Block(pCopy(l, Blocks.GOLD_BLOCK)));
    public static final DeferredHolder<Block, Block> AMP_BOOKSHELF = registerJSONModel("amplifying_bookshelf", l -> new AmpBookshelfBlock(pCopy(l, Blocks.BOOKSHELF)));

    public static final DeferredHolder<Block, Block> LAPIDARY = register("lapidary_table", l -> new LapidaryBlock(pCopy(l, Blocks.SMITHING_TABLE)));
    public static final DeferredHolder<Block, Block> HARMONISER = register("harmoniser", l -> new HarmoniserBlock(pCopy(l, Blocks.SMITHING_TABLE)));

//    public static final DeferredHolder<Block, Block> RAGE_CAGE = register("rage_cage", l -> new RageCageBlock(pCopy(l, Blocks.SPAWNER)));
    public static final DeferredHolder<Block, Block> SPIDER_NEST = register("spider_nest", l -> new SpiderNestBlock(pCopy(l, Blocks.MOSS_BLOCK)));
    public static final DeferredHolder<Block, Block> CHARRED_LOG = register("charred_log", l -> new CharredLogBlock(defaultPs(l).mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.XYLOPHONE).strength(0.8f, 1f).sound(SoundType.BASALT)));

    public static final DeferredHolder<Block, Block> HIVEHEART = register("hiveheart", l -> new HiveheartBlock(pCopy(l, Blocks.OBSIDIAN)));
    public static final DeferredHolder<Block, Block> HIVEHEART_DUMMY = register("hiveheart_secondary", l -> new HiveOrganBlock(pCopy(l, Blocks.OBSIDIAN)));
    public static final DeferredHolder<Block, Block> HELLWAX = register("hellwax", l -> new Block(pCopy(l, Blocks.HONEY_BLOCK).strength(0.6F)));
    public static final DeferredHolder<Block, Block> CARAPACE = register("carapace", l -> new Block(pCopy(l, Blocks.BLACKSTONE)));
    public static final DeferredHolder<Block, Block> HATCHERY = register("hatchery", l -> new Block(pCopy(l, Blocks.BLACKSTONE)));
    public static final DeferredHolder<Block, Block> WASP_PORT = register("wasp_port", l -> new WaspPortBlock(pCopy(l, Blocks.BLACKSTONE).forceSolidOff()));


    private static <T extends Block> DeferredHolder<Block, T> register(String name, Function<Identifier, T> sup) {
        DeferredHolder<Block, T> r = BLOCKS.register(name, sup);
        DATAGEN_LOOT_TABLE.add(r);
        DATAGEN_MODEL.add(r);
        return r;
    }

    private static <T extends Block> DeferredHolder<Block, T> registerJSONModel(String name, Function<Identifier, T> sup) {
        DeferredHolder<Block, T> r = BLOCKS.register(name, sup);
        DATAGEN_LOOT_TABLE.add(r);
        return r;
    }

    private static <T extends Block> DeferredHolder<Block, T> registerJSONLoot(String name, Function<Identifier, T> sup) {
        DeferredHolder<Block, T> r = BLOCKS.register(name, sup);
        DATAGEN_MODEL.add(r);
        return r;
    }

    private static <T extends Block> DeferredHolder<Block, T> registerJSON(String name, Function<Identifier, T> sup) {
        DeferredHolder<Block, T> r = BLOCKS.register(name, sup);
        return r;
    }


    private static RotatedPillarBlock woodenlog(float mine, float blast, Identifier l) {
        return new RotatedPillarBlock(defaultPs(l).mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(mine, blast).sound(SoundType.WOOD));
    }

}
