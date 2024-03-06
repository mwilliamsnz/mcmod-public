package abyssal.init;

import abyssal.Main;
import abyssal.generation.BookshelfProcessor;
import abyssal.generation.features.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModGeneration {

    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_TYPES = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, Main.MOD_ID);

    public static final RegistryObject<TreeDecoratorType<TrunkIvyDecorator>> TREE_IVY = TREE_DECORATOR_TYPES.register("ivy_decorator", () -> (new TreeDecoratorType<>(TrunkIvyDecorator.CODEC)));

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Main.MOD_ID);

    public static final RegistryObject<PlacementModifierType<ChunkDistributionFilter>> CHUNK_FILTER = PLACEMENT_MODIFIER_TYPES.register( "chunk_distribution", () -> () -> ChunkDistributionFilter.CODEC);

    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSORS = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, Main.MOD_ID);
    public static final RegistryObject<StructureProcessorType<BookshelfProcessor>> BOOKSHELF_FILLER = STRUCTURE_PROCESSORS.register( "bookshelf_filler", () -> () -> BookshelfProcessor.CODEC);



    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Main.MOD_ID);
    public static final RegistryObject<MossyLogFeature> MOSS_LOG = FEATURES.register("moss_log", () -> new MossyLogFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<IvyMossyLogFeature> IVY_MOSS_LOG = FEATURES.register("ivy_moss_log", () -> new IvyMossyLogFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<ModOreFeature> MOD_ORE = FEATURES.register("reworked_ore", () -> new ModOreFeature(OreConfiguration.CODEC));
    public static final RegistryObject<SpiderNestFeature> SPIDER_NEST = FEATURES.register("spider_nest", () -> new SpiderNestFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<OutcropFeature> OUTCROP = FEATURES.register("outcrop", () -> new OutcropFeature(BlockStateConfiguration.CODEC));
    public static final RegistryObject<RaggedDiskFeature> RAGGED_DISK = FEATURES.register("ragged_disk", () -> new RaggedDiskFeature(DiskConfiguration.CODEC));

}
