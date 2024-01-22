package abyssal.init;

import abyssal.Main;
import abyssal.blocks.*;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {

    // materials https://gist.github.com/GizmoTheMoonPig/77a90a48e0aeecd15b4c524e1c7f0a4a
    public static final DeferredRegister<Block> BLOCKS =  DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MOD_ID);

    // Manual loot table blocks are NOT in this list
    public static final List<RegistryObject<Block>> DATAGEN_LOOT_TABLE = new ArrayList<>();

    public static final RegistryObject<Block> CORN_SEED = BLOCKS.register("corn_seed", () -> new CropBlock(BlockBehaviour.Properties.copy(Blocks.BEETROOTS)));
    public static final RegistryObject<Block> REED = registerWithDataLoot("reed", () -> new SugarCaneBlock(BlockBehaviour.Properties.copy(Blocks.SUGAR_CANE)));
    public static final RegistryObject<RotatedPillarBlock> ELDER_PINE_LOG = registerWithDataLoot("elder_pine_log", () -> woodenlog(100, 2));
    public static final RegistryObject<Block> ELDER_PINE_PLANKS = registerWithDataLoot("elder_pine_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(50f,2)));
    public static final RegistryObject<Block> ELDER_PINE_DOOR = registerWithDataLoot("elder_pine_door", () -> new DoorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(50f,2), BlockSetType.OAK));
    public static final RegistryObject<RotatedPillarBlock> MOSSY_BIRCH = registerWithDataLoot("mossy_birch", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_LOG)));
    public static final RegistryObject<RotatedPillarBlock> MOSSY_OAK = registerWithDataLoot("mossy_oak", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> IVY = registerWithDataLoot("ivy", () -> new IvyBlock(BlockBehaviour.Properties.copy(Blocks.VINE)));
    public static final RegistryObject<Block> THIN_LEAVES = registerWithDataLoot("thin_leaves", () -> new PassableSlowingBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).noCollission().speedFactor(0.8f)));
    public static final RegistryObject<Block> BRUSH = registerWithDataLoot("brush", () -> new PassableSlowingBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).noCollission().speedFactor(0.8f)));
    public static final RegistryObject<Block> SHRUB = registerWithDataLoot("shrub", () -> new AzaleaBlock(BlockBehaviour.Properties.copy(Blocks.AZALEA)));
    public static final RegistryObject<Block> HEATHER = registerWithDataLoot("heather", () -> new FlowerBlock(MobEffects.ABSORPTION, 6, BlockBehaviour.Properties.copy(Blocks.DANDELION)));
    public static final RegistryObject<Block> ALPINE_PLANT = registerWithDataLoot("alpine_plant", () -> new DeadBushBlock(BlockBehaviour.Properties.copy(Blocks.DEAD_BUSH).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> CLOVER = registerWithDataLoot("clover", () -> new GroundCoverBlock(2.0D, BlockBehaviour.Properties.copy(Blocks.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> FERN_CORE = registerWithDataLoot("fern_core", () -> new FernCentreBlock(BlockBehaviour.Properties.copy(Blocks.FERN).offsetType(BlockBehaviour.OffsetType.NONE)));
    public static final RegistryObject<Block> FERN_FRONDS = registerWithDataLoot("fern_fronds", () -> new FernFrondsBlock(BlockBehaviour.Properties.copy(Blocks.FERN).offsetType(BlockBehaviour.OffsetType.NONE)));

    public static final RegistryObject<Block> LEAF_LITTER = registerWithDataLoot("leaf_litter", () -> new SnowyDirtBlock(BlockBehaviour.Properties.copy(Blocks.PODZOL)));

    public static final RegistryObject<Block> ABYSSAL_STONE = registerWithDataLoot("abyssal_stone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(10.0F, 2.0F).sound(SoundType.DEEPSLATE)));
    public static final RegistryObject<Block> NITRE = registerWithDataLoot("nitre", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_NITRE = registerWithDataLoot("deepslate_nitre", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> SULFUR = registerWithDataLoot("sulfur_ore", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_SULFUR = registerWithDataLoot("deepslate_sulfur_ore", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> POWDER_BARREL = registerWithDataLoot("powder_barrel", () -> new PowderBarrelBlock(BlockBehaviour.Properties.copy(Blocks.TNT), 1.6f, 2f, 1.2f));
    public static final RegistryObject<Block> POWDER_BARREL_FRAG = registerWithDataLoot("powder_barrel_frag", () -> new PowderBarrelBlock(BlockBehaviour.Properties.copy(Blocks.TNT), 1.3f, 3.85f, 0.2f));
    public static final RegistryObject<Block> POWDER_BARREL_KNOCK = registerWithDataLoot("powder_barrel_knock", () -> new PowderBarrelBlock(BlockBehaviour.Properties.copy(Blocks.TNT), 1.6f, 2f, 3f));
    public static final RegistryObject<Block> ENKATITE = registerWithDataLoot("enkatite", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(20.0F, 4F).sound(SoundType.DEEPSLATE)));
    public static final RegistryObject<Block> ICHOR = BLOCKS.register("ichor", () -> new ModLiquidBlock(() -> (FlowingFluid) ModFluids.ICHOR_FLUID.get(), BlockBehaviour.Properties.of().replaceable().mapColor(MapColor.COLOR_GRAY).pushReaction(PushReaction.DESTROY).liquid().strength(100.0F))); //TODO //.noDrops()));

    public static final RegistryObject<Block> GOLD_GRAVEL = BLOCKS.register("gold_gravel", () -> new GravelBlock(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
    public static final RegistryObject<Block> POOR_IRON_ORE = registerWithDataLoot("poor_iron_ore", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_POOR_IRON_ORE = registerWithDataLoot("poor_deepslate_iron_ore", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)));
    public static final RegistryObject<Block> SILVER_ORE = registerWithDataLoot("silver_ore", () -> new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = registerWithDataLoot("deepslate_silver_ore", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_GOLD_ORE)));
    public static final RegistryObject<Block> GARNET_CLUSTER = BLOCKS.register("garnet_cluster", () -> new Block(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_GARNET_CLUSTER = BLOCKS.register("deepslate_garnet_cluster", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_EMERALD_ORE)));
    public static final RegistryObject<Block> ONYX_CLUSTER = BLOCKS.register("onyx_cluster", () -> new Block(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_ONYX_CLUSTER = BLOCKS.register("deepslate_onyx_cluster", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_EMERALD_ORE)));
    public static final RegistryObject<Block> NETHER_ONYX_CLUSTER = BLOCKS.register("nether_onyx_cluster", () -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHER_GOLD_ORE)));
    public static final RegistryObject<Block> GEM_CLUSTER = BLOCKS.register("gem_cluster", () -> new Block(BlockBehaviour.Properties.copy(Blocks.EMERALD_ORE)));
    public static final RegistryObject<Block> DEEPSLATE_GEM_CLUSTER = BLOCKS.register("deepslate_gem_cluster", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_EMERALD_ORE)));

    public static final RegistryObject<RotatedPillarBlock> NETHER_BRASS_BLOCK = registerWithDataLoot("nether_brass_block", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistryObject<Block> SILVER_BLOCK = registerWithDataLoot("silver_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistryObject<Block> ALCHEMICAL_GOLD_BLOCK = registerWithDataLoot("alchemical_gold_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));

    public static final RegistryObject<Block> LAPIDARY = registerWithDataLoot("lapidary_table", () -> new LapidaryBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE)));
    public static final RegistryObject<Block> HARMONISER = registerWithDataLoot("harmoniser", () -> new HarmoniserBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE)));

//    public static final RegistryObject<Block> RAGE_CAGE = register("rage_cage", () -> new RageCageBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER)));
    public static final RegistryObject<Block> SPIDER_NEST = registerWithDataLoot("spider_nest", () -> new SpiderNestBlock(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK)));
    public static final RegistryObject<Block> CHARRED_LOG = registerWithDataLoot("charred_log", () -> new CharredLogBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.XYLOPHONE).strength(0.8f, 1f).sound(SoundType.BASALT)));

    private static <T extends Block> RegistryObject<T> registerWithDataLoot(String name, Supplier<T> sup) {
        RegistryObject<T> r = BLOCKS.register(name, sup);
        DATAGEN_LOOT_TABLE.add((RegistryObject<Block>) r);
        return r;
    }

    private static RotatedPillarBlock woodenlog(float mine, float blast) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(mine, blast).sound(SoundType.WOOD));
    }

}
