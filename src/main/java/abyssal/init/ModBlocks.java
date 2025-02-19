package abyssal.init;

import abyssal.Main;
import abyssal.blocks.*;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {

    // materials https://gist.github.com/GizmoTheMoonPig/77a90a48e0aeecd15b4c524e1c7f0a4a
    public static final DeferredRegister<Block> BLOCKS =  DeferredRegister.createBlocks(Main.MOD_ID);

    // Manual loot table blocks are NOT in this list
    public static final List<Supplier<Block>> DATAGEN_LOOT_TABLE = new ArrayList<>();

    public static final BlockSetType ELDER_PINE = BlockSetType.register(new BlockSetType(Main.MOD_ID + ":elder_pine"));

    public static final Supplier<Block> CORN_SEED = BLOCKS.register("corn_seed", () -> new CropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEETROOTS)));
    public static final Supplier<Block> REED = registerWithDataLoot("reed", () -> new ReedBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SUGAR_CANE)));
    public static final Supplier<RotatedPillarBlock> ELDER_PINE_LOG = registerWithDataLoot("elder_pine_log", () -> woodenlog(100, 2));
    public static final Supplier<Block> ELDER_PINE_PLANKS = registerWithDataLoot("elder_pine_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(50f,2)));
    public static final Supplier<Block> ELDER_PINE_DOOR = registerWithDataLoot("elder_pine_door", () -> new DoorBlock(ELDER_PINE, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(50f,2)));
    public static final Supplier<RotatedPillarBlock> MOSSY_BIRCH = registerWithDataLoot("mossy_birch", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_LOG)));
    public static final Supplier<RotatedPillarBlock> MOSSY_OAK = registerWithDataLoot("mossy_oak", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final Supplier<Block> IVY = registerWithDataLoot("ivy", () -> new IvyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.VINE)));
    public static final Supplier<Block> THIN_LEAVES = registerWithDataLoot("thin_leaves", () -> new PassableSlowingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noCollission().speedFactor(0.8f)));
    public static final Supplier<Block> BRUSH = registerWithDataLoot("brush", () -> new PassableSlowingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).noCollission().speedFactor(0.8f)));
    public static final Supplier<Block> SHRUB = registerWithDataLoot("shrub", () -> new AzaleaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AZALEA)));
    public static final Supplier<Block> HEATHER = registerWithDataLoot("heather", () -> new FlowerBlock(MobEffects.ABSORPTION, 6, BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION)));
    public static final Supplier<Block> ALPINE_PLANT = registerWithDataLoot("alpine_plant", () -> new DeadBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEAD_BUSH).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final Supplier<Block> CLOVER = registerWithDataLoot("clover", () -> new GroundCoverBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final Supplier<Block> FERN_CORE = registerWithDataLoot("fern_core", () -> new FernCentreBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FERN).offsetType(BlockBehaviour.OffsetType.NONE)));
    public static final Supplier<Block> FERN_FRONDS = registerWithDataLoot("fern_fronds", () -> new FernFrondsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FERN).offsetType(BlockBehaviour.OffsetType.NONE)));

    public static final Supplier<Block> LEAF_LITTER = registerWithDataLoot("leaf_litter", () -> new SnowyDirtBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PODZOL)));
    public static final Supplier<Block> SUPER_SOIL = registerWithDataLoot("super_soil", () -> new SuperSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).randomTicks()));
    public static final Supplier<Block> GRASS_SUPER_SOIL = registerWithDataLoot("grass_super_soil", () -> new GrassSuperSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK).randomTicks()));
    public static final Supplier<Block> PRISM = registerWithDataLoot("prism", () -> new HalfTransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_STAINED_GLASS).lightLevel((state) -> {
        return 15;
    })));

    public static final Supplier<Block> ABYSSAL_STONE = registerWithDataLoot("abyssal_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(10.0F, 2.0F).sound(SoundType.DEEPSLATE)));
    public static final Supplier<Block> NITRE = registerWithDataLoot("nitre", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final Supplier<Block> DEEPSLATE_NITRE = registerWithDataLoot("deepslate_nitre", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final Supplier<Block> SULFUR = registerWithDataLoot("sulfur_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final Supplier<Block> DEEPSLATE_SULFUR = registerWithDataLoot("deepslate_sulfur_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final Supplier<Block> POWDER_BARREL = registerWithDataLoot("powder_barrel", () -> new PowderBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), 1.6f, 2f, 1.2f));
    public static final Supplier<Block> POWDER_BARREL_FRAG = registerWithDataLoot("powder_barrel_frag", () -> new PowderBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), 1.3f, 3.85f, 0.2f));
    public static final Supplier<Block> POWDER_BARREL_KNOCK = registerWithDataLoot("powder_barrel_knock", () -> new PowderBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT), 1.6f, 2f, 3f));
    public static final Supplier<Block> ENKATITE = registerWithDataLoot("enkatite", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(20.0F, 4F).sound(SoundType.DEEPSLATE)));

    public static final Supplier<Block> GOLD_GRAVEL = BLOCKS.register("gold_gravel", () -> new ColoredFallingBlock(new ColorRGBA(-8356741), BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final Supplier<Block> POOR_IRON_ORE = registerWithDataLoot("poor_iron_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final Supplier<Block> DEEPSLATE_POOR_IRON_ORE = registerWithDataLoot("poor_deepslate_iron_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)));
    public static final Supplier<Block> SILVER_ORE = registerWithDataLoot("silver_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE)));
    public static final Supplier<Block> DEEPSLATE_SILVER_ORE = registerWithDataLoot("deepslate_silver_ore", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_GOLD_ORE)));
    public static final Supplier<Block> GARNET_CLUSTER = BLOCKS.register("garnet_cluster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE)));
    public static final Supplier<Block> DEEPSLATE_GARNET_CLUSTER = BLOCKS.register("deepslate_garnet_cluster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_EMERALD_ORE)));
    public static final Supplier<Block> ONYX_CLUSTER = BLOCKS.register("onyx_cluster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE)));
    public static final Supplier<Block> DEEPSLATE_ONYX_CLUSTER = BLOCKS.register("deepslate_onyx_cluster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_EMERALD_ORE)));
    public static final Supplier<Block> NETHER_ONYX_CLUSTER = BLOCKS.register("nether_onyx_cluster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_GOLD_ORE)));
    public static final Supplier<Block> GEM_CLUSTER = BLOCKS.register("gem_cluster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE)));
    public static final Supplier<Block> DEEPSLATE_GEM_CLUSTER = BLOCKS.register("deepslate_gem_cluster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_EMERALD_ORE)));

    public static final Supplier<RotatedPillarBlock> NETHER_BRASS_BLOCK = registerWithDataLoot("nether_brass_block", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK)));
    public static final Supplier<Block> SILVER_BLOCK = registerWithDataLoot("silver_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK)));
    public static final Supplier<Block> ALCHEMICAL_GOLD_BLOCK = registerWithDataLoot("alchemical_gold_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK)));
    public static final Supplier<Block> AMP_BOOKSHELF = registerWithDataLoot("amplifying_bookshelf", () -> new AmpBookshelfBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BOOKSHELF)));

    public static final Supplier<Block> LAPIDARY = registerWithDataLoot("lapidary_table", () -> new LapidaryBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMITHING_TABLE)));
    public static final Supplier<Block> HARMONISER = registerWithDataLoot("harmoniser", () -> new HarmoniserBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMITHING_TABLE)));

//    public static final Supplier<Block> RAGE_CAGE = register("rage_cage", () -> new RageCageBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPAWNER)));
    public static final Supplier<Block> SPIDER_NEST = registerWithDataLoot("spider_nest", () -> new SpiderNestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK)));
    public static final Supplier<Block> CHARRED_LOG = registerWithDataLoot("charred_log", () -> new CharredLogBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.XYLOPHONE).strength(0.8f, 1f).sound(SoundType.BASALT)));

    private static <T extends Block> Supplier<T> registerWithDataLoot(String name, Supplier<T> sup) {
        Supplier<T> r = BLOCKS.register(name, sup);
        DATAGEN_LOOT_TABLE.add((Supplier<Block>) r);
        return r;
    }

    private static RotatedPillarBlock woodenlog(float mine, float blast) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(mine, blast).sound(SoundType.WOOD));
    }

}
