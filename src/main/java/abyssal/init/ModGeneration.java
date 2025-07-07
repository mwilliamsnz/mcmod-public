package abyssal.init;

import abyssal.Main;
import abyssal.generation.BookshelfProcessor;
import abyssal.generation.features.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModGeneration {

    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_TYPES = DeferredRegister.create(BuiltInRegistries.TREE_DECORATOR_TYPE, Main.MOD_ID);

    public static final Supplier<TreeDecoratorType<TrunkIvyDecorator>> TREE_IVY = TREE_DECORATOR_TYPES.register("ivy_decorator", () -> (new TreeDecoratorType<>(TrunkIvyDecorator.CODEC)));

    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Main.MOD_ID);

    public static final Supplier<PlacementModifierType<ChunkDistributionFilter>> CHUNK_FILTER = PLACEMENT_MODIFIER_TYPES.register( "chunk_distribution", () -> () -> ChunkDistributionFilter.CODEC);

    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSORS = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, Main.MOD_ID);
    public static final Supplier<StructureProcessorType<BookshelfProcessor>> BOOKSHELF_FILLER = STRUCTURE_PROCESSORS.register( "bookshelf_filler", () -> () -> BookshelfProcessor.CODEC);


    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_TYPES = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, Main.MOD_ID);
    public static final Supplier<FoliagePlacerType<?>> BIRCH_BLOB = FOLIAGE_TYPES.register("birch_blob", () -> new FoliagePlacerType<>(BirchBlobFoliagePlacer.CODEC));

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, Main.MOD_ID);
    public static final Supplier<MossyLogFeature> MOSS_LOG = FEATURES.register("moss_log", () -> new MossyLogFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<IvyMossyLogFeature> IVY_MOSS_LOG = FEATURES.register("ivy_moss_log", () -> new IvyMossyLogFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<ModOreFeature> MOD_ORE = FEATURES.register("reworked_ore", () -> new ModOreFeature(OreConfiguration.CODEC));
    public static final Supplier<SpiderNestFeature> SPIDER_NEST = FEATURES.register("spider_nest", () -> new SpiderNestFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<OutcropFeature> OUTCROP = FEATURES.register("outcrop", () -> new OutcropFeature(BlockStateConfiguration.CODEC));
    public static final Supplier<RaggedDiskFeature> RAGGED_DISK = FEATURES.register("ragged_disk", () -> new RaggedDiskFeature(DiskConfiguration.CODEC));
    public static final Supplier<ReedDiskFeature> REED_DISK = FEATURES.register("reed_disk", () -> new ReedDiskFeature(DiskConfiguration.CODEC));

}
