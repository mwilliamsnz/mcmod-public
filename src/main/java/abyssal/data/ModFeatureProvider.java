package abyssal.data;

import abyssal.Main;
import abyssal.generation.OreDist;
import abyssal.generation.SupplementNoiseProvider;
import abyssal.generation.features.ChunkDistributionFilter;
import abyssal.generation.features.TrunkIvyDecorator;
import abyssal.init.ModBlocks;
import abyssal.init.ModGeneration;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaPineFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModFeatureProvider extends DatapackBuiltinEntriesProvider {

    private static final RandomPatchConfiguration HEATHER_SPREAD = new RandomPatchConfiguration(
            96, 6, 2,
            PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(
                            new SupplementNoiseProvider(
                                    ModBlocks.HEATHER.get().defaultBlockState(),
                                    new InclusiveRange<Integer>(1, 3),
                                    new NormalNoise.NoiseParameters(-7, 1.0D, 0.5D),
                                    1.0F,
                                    List.of(
                                            Blocks.ALLIUM.defaultBlockState(),
                                            Blocks.DANDELION.defaultBlockState(),
                                            Blocks.SHORT_GRASS.defaultBlockState(),
                                            Blocks.OXEYE_DAISY.defaultBlockState()
                                    ),
                                    2345L,
                                    new NormalNoise.NoiseParameters(-3, 1.0D),
                                    1.0F
                            )
                    )
            )
    );

    public ModFeatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider,
                new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, bootstrap -> {
                            // Register configured features here
                            bootstrap.register(cfk("gen_mossy_birch_log"),
                                    new ConfiguredFeature<>(ModGeneration.MOSS_LOG.get(), new NoneFeatureConfiguration()));
                            bootstrap.register(cfk("gen_mossy_oak_log"),
                                    new ConfiguredFeature<>(ModGeneration.IVY_MOSS_LOG.get(), new NoneFeatureConfiguration()));
                            bootstrap.register(cfk("gen_spider_nest"),
                                    new ConfiguredFeature<>(ModGeneration.SPIDER_NEST.get(), new NoneFeatureConfiguration()));
                            bootstrap.register(cfk("reeds"),
                                new ConfiguredFeature<>(ModGeneration.REED_DISK.get(), new DiskConfiguration(
                                        new RuleBasedBlockStateProvider(BlockStateProvider.simple(ModBlocks.REED.get()), List.of()),
                                        BlockPredicate.wouldSurvive(ModBlocks.REED.get().defaultBlockState(), BlockPos.ZERO),
                                        UniformInt.of(2, 8),
                                        1
                                )));

                            bootstrap.register(cfk("brush"),
                                    new ConfiguredFeature<>(Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
                                            32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BRUSH.get()))))));

                            bootstrap.register(cfk("alpine_plant"),
                                    new ConfiguredFeature<>(Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
                                            32, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.ALPINE_PLANT.get()))))));

                            bootstrap.register(cfk("alpine_rock"),
                                    new ConfiguredFeature<>(ModGeneration.OUTCROP.get(), new BlockStateConfiguration(Blocks.STONE.defaultBlockState())));

                            bootstrap.register(cfk("alpine_rock_andesite"),
                                            new ConfiguredFeature<>(ModGeneration.OUTCROP.get(), new BlockStateConfiguration(Blocks.ANDESITE.defaultBlockState())));

                            bootstrap.register(cfk("shrub"),
                                    new ConfiguredFeature<>(Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
                                            8, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.SHRUB.get()))))));

                            bootstrap.register(cfk("heather"),
                                    new ConfiguredFeature<>(Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(
                                            12, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.HEATHER.get()))))));

                            bootstrap.register(cfk("heath_vegetation"), new ConfiguredFeature<>(Feature.RANDOM_PATCH, HEATHER_SPREAD));

                            bootstrap.register(cfk("surface_moss"),
                                    new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                                            20, 5, 3, PlacementUtils.filtered(
                                                    Feature.SIMPLE_BLOCK,
                                                    new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.MOSS_CARPET)),BlockPredicate.allOf(
                                                            BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                                            BlockPredicate.anyOf(
                                                                    BlockPredicate.matchesTag(new Vec3i(0,-1,0), BlockTags.DIRT),
                                                                    BlockPredicate.matchesTag(new Vec3i(0,-1,0), BlockTags.LOGS),
                                                                    BlockPredicate.matchesBlocks(new Vec3i(0,-1,0), Blocks.MOSSY_COBBLESTONE)
                                                            )
                                                    ))
                                    )));

                            bootstrap.register(cfk("clover"),
                                    new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(30, 3, 2, PlacementUtils.filtered(
                                                    Feature.SIMPLE_BLOCK,
                                                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.CLOVER.get())), BlockPredicate.allOf(
                                                            BlockPredicate.ONLY_IN_AIR_PREDICATE,
                                                            BlockPredicate.anyOf(
                                                                    BlockPredicate.matchesBlocks(new Vec3i(0,-1,0), Blocks.GRASS_BLOCK)
                                                            )
                                                    ))
                                    )));

                            bootstrap.register(cfk("disk_coarse"),
                                    new ConfiguredFeature<>(Feature.DISK, new DiskConfiguration(
                                            new RuleBasedBlockStateProvider(
                                                    BlockStateProvider.simple(Blocks.COARSE_DIRT),
                                                    List.of(new RuleBasedBlockStateProvider.Rule(
                                                            BlockPredicate.not(
                                                                    BlockPredicate.anyOf(
                                                                            BlockPredicate.solid(Direction.UP.getNormal()),
                                                                            BlockPredicate.matchesFluids(Direction.UP.getNormal(), Fluids.WATER)
                                                                    )
                                                            ),
                                                            BlockStateProvider.simple(Blocks.COARSE_DIRT))
                                                    )
                                            ),
                                            BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK, ModBlocks.LEAF_LITTER.get())),
                                            UniformInt.of(2, 6),
                                            2)));

                            bootstrap.register(cfk("disk_litter"),
                                    new ConfiguredFeature<>(Feature.DISK, new DiskConfiguration(
                                            new RuleBasedBlockStateProvider(
                                                    BlockStateProvider.simple(Blocks.DIRT),
                                                    List.of(new RuleBasedBlockStateProvider.Rule(
                                                            BlockPredicate.not(
                                                                    BlockPredicate.anyOf(
                                                                            BlockPredicate.solid(Direction.UP.getNormal()),
                                                                            BlockPredicate.matchesFluids(Direction.UP.getNormal(), Fluids.WATER)
                                                                    )
                                                            ),
                                                            BlockStateProvider.simple(ModBlocks.LEAF_LITTER.get()))
                                                    )
                                            ),
                                            BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT)),
                                            UniformInt.of(2, 6),
                                            2)));

                            bootstrap.register(cfk("disk_mud"),
                                    new ConfiguredFeature<>(ModGeneration.RAGGED_DISK.get(), new DiskConfiguration(
                                            new RuleBasedBlockStateProvider(
                                                    BlockStateProvider.simple(Blocks.DIRT),
                                                    List.of(new RuleBasedBlockStateProvider.Rule(
                                                            BlockPredicate.not(
                                                                    BlockPredicate.anyOf(
                                                                            BlockPredicate.solid(Direction.UP.getNormal())
                                                                    )
                                                            ),
                                                            BlockStateProvider.simple(Blocks.MUD))
                                                    )
                                            ),
                                            BlockPredicate.matchesBlocks(List.of(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT, Blocks.SAND)),
                                            UniformInt.of(2, 6),
                                            2)));


                            bootstrap.register(cfk("tree_elder_pine"),
                                new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
                                        BlockStateProvider.simple(ModBlocks.ELDER_PINE_LOG.get()),
                                        new GiantTrunkPlacer(14, 2, 12),
                                        BlockStateProvider.simple(Blocks.SPRUCE_LEAVES),
                                        new MegaPineFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0), UniformInt.of(8, 14)),
                                        new TwoLayersFeatureSize(1, 1, 2))
                                ).decorators(ImmutableList.of(new AlterGroundDecorator(BlockStateProvider.simple(Blocks.PODZOL)))).build()
                            ));

                            bootstrap.register(cfk("tree_taller_birch"),
                                new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
                                        BlockStateProvider.simple(Blocks.BIRCH_LOG),
                                        new StraightTrunkPlacer(5, 8, 8),
                                        BlockStateProvider.simple(Blocks.ORANGE_STAINED_GLASS),
                                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 5),
                                        new TwoLayersFeatureSize(1, 1, 2))
                                ).build()
                            ));

                            bootstrap.register(cfk("tree_taller_birch_b"),
                                new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
                                        BlockStateProvider.simple(Blocks.BIRCH_LOG),
                                        new StraightTrunkPlacer(5, 8, 8),
                                        BlockStateProvider.simple(Blocks.BLUE_STAINED_GLASS),
                                        new FancyFoliagePlacer(ConstantInt.of(4), ConstantInt.of(4), 4),
                                        new TwoLayersFeatureSize(1, 1, 2))
                                ).build()
                            ));

                            bootstrap.register(cfk("tree_ivy_oak"),
                                new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
                                        BlockStateProvider.simple(Blocks.OAK_LOG),
                                        new StraightTrunkPlacer(5, 2, 0),
                                        BlockStateProvider.simple(Blocks.OAK_LEAVES),
                                        new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                                        new TwoLayersFeatureSize(1, 0, 1))
                                ).decorators(ImmutableList.of(new TrunkIvyDecorator())).build()
                            ));

                            bootstrap.register(cfk("tree_larger_oak"),
                            new ConfiguredFeature<>(Feature.TREE, (new TreeConfiguration.TreeConfigurationBuilder(
                                    BlockStateProvider.simple(Blocks.OAK_LOG),
                                    new FancyTrunkPlacer(6, 8, 8),
                                    BlockStateProvider.simple(Blocks.OAK_LEAVES),
                                    new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
                                    new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
                            )).ignoreVines().decorators(ImmutableList.of(new AlterGroundDecorator(BlockStateProvider.simple(ModBlocks.LEAF_LITTER.get())))).build()
                            ));


                            ImmutableList<OreConfiguration.TargetBlockState> gravelTargets = ImmutableList.of(
                                    OreConfiguration.target(new BlockMatchTest(Blocks.GRAVEL), ModBlocks.GOLD_GRAVEL.get().defaultBlockState())
                            );
                            List<OreConfiguration.TargetBlockState> coalTargets = basicTargetList(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE);
                            List<OreConfiguration.TargetBlockState> copperTargets = basicTargetList(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE);
                            List<OreConfiguration.TargetBlockState> ironTargets = basicTargetList(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE);
                            List<OreConfiguration.TargetBlockState> goldTargets = basicTargetList(Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE);
                            List<OreConfiguration.TargetBlockState> emeraldTargets = basicTargetList(Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE);

                            List<OreConfiguration.TargetBlockState> poorIronTargets = basicTargetList(ModBlocks.POOR_IRON_ORE.get(), ModBlocks.DEEPSLATE_POOR_IRON_ORE.get());
                            List<OreConfiguration.TargetBlockState> nitreTargets = basicTargetList(ModBlocks.NITRE.get(), ModBlocks.DEEPSLATE_NITRE.get());
                            List<OreConfiguration.TargetBlockState> sulfurTargets = basicTargetList(ModBlocks.SULFUR.get(), ModBlocks.DEEPSLATE_SULFUR.get());
                            List<OreConfiguration.TargetBlockState> silverTargets = basicTargetList(ModBlocks.SILVER_ORE.get(), ModBlocks.DEEPSLATE_SILVER_ORE.get());
                            List<OreConfiguration.TargetBlockState> garnetTargets = basicTargetList(ModBlocks.GARNET_CLUSTER.get(), ModBlocks.DEEPSLATE_GARNET_CLUSTER.get());
                            List<OreConfiguration.TargetBlockState> gemTargets = basicTargetList(ModBlocks.GEM_CLUSTER.get(), ModBlocks.DEEPSLATE_GEM_CLUSTER.get());

                            List<OreConfiguration.TargetBlockState> onyxTargets = ImmutableList.of(
                                    OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.ONYX_CLUSTER.get().defaultBlockState()),
                                    OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.DEEPSLATE_ONYX_CLUSTER.get().defaultBlockState()),
                                    OreConfiguration.target(new BlockMatchTest(Blocks.NETHERRACK), ModBlocks.NETHER_ONYX_CLUSTER.get().defaultBlockState())
                            );


                            bootstrap.register(
                                    ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Main.MOD_ID, "river_gold")),
                                    new ConfiguredFeature<>(Feature.SCATTERED_ORE, new OreConfiguration(gravelTargets, 30, 0.8f))
                            );

                            // 9 -> 5
                            makeOre(bootstrap, "reduced_ore_iron", ironTargets, 5);

                            makeOre(bootstrap, "extra_ore_iron", ironTargets, 15);
                            makeOre(bootstrap, "poor_iron", poorIronTargets, 6);
                            makeOre(bootstrap, "extra_poor_iron", poorIronTargets, 30);
                                                        // 20 -> 14
                            makeOre(bootstrap, "reduced_ore_coal", coalTargets, 13);
                            makeOre(bootstrap, "reduced_ore_coal_buried", coalTargets, 10, 0.5f);
                            makeOre(bootstrap, "extra_ore_coal", coalTargets, 30);
                                                        // 10 -> 6
                            makeOre(bootstrap, "reduced_ore_copper", copperTargets, 6);
                            makeOre(bootstrap, "extra_ore_copper", copperTargets, 20);
                                                        // 9 -> 2
                            makeOre(bootstrap, "reduced_ore_gold", goldTargets, 2);
                            makeOre(bootstrap, "mountain_gold", goldTargets, 5, 1f);
                            makeOre(bootstrap, "extra_ore_gold", goldTargets, 5);

                            makeOre(bootstrap, "ore_silver", silverTargets, 6);
                            makeOre(bootstrap, "extra_ore_silver", silverTargets, 15);

                            makeOre(bootstrap, "extra_ore_emerald", emeraldTargets, 6);
                            makeOre(bootstrap, "garnets", garnetTargets, 3);
                            makeOre(bootstrap, "gems", gemTargets, 3);
                            makeOre(bootstrap, "onyxes", onyxTargets, 3);

                            makeOre(bootstrap, "sulfur", sulfurTargets, 8);
                            makeOre(bootstrap, "nitre", nitreTargets, 15);

                        })
                        // Create placed features
                        .add(Registries.PLACED_FEATURE, bootstrap -> {
                            // Register placed features here
                            HolderGetter<ConfiguredFeature<?, ?>> configured = bootstrap.lookup(Registries.CONFIGURED_FEATURE);

                            bootstrap.register(pfk("mossy_birch_log"),
                                    new PlacedFeature(configured.getOrThrow(cfk("gen_mossy_birch_log")),
                                            List.of(RarityFilter.onAverageOnceEvery(5), BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("mossy_oak_log"),
                                    new PlacedFeature(configured.getOrThrow(cfk("gen_mossy_oak_log")),
                                            List.of(RarityFilter.onAverageOnceEvery(5), BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(Blocks.OAK_SAPLING.defaultBlockState(), BlockPos.ZERO)), BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("spider_nest"),
                                    new PlacedFeature(configured.getOrThrow(cfk("gen_spider_nest")),
                                            List.of(RarityFilter.onAverageOnceEvery(5), BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("mass_spider_nest"),
                                    new PlacedFeature(configured.getOrThrow(cfk("gen_spider_nest")),
                                            List.of(RarityFilter.onAverageOnceEvery(25), BiomeFilter.biome())
                                    ));

                            bootstrap.register(pfk("reeds"),
                                    new PlacedFeature(configured.getOrThrow(cfk("reeds")),
                                            List.of(RarityFilter.onAverageOnceEvery(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("brush"),
                                    new PlacedFeature(configured.getOrThrow(cfk("brush")),
                                            List.of(NoiseThresholdCountPlacement.of(-0.5D, 2, 12), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("forest_rock_rare"),
                                    new PlacedFeature(configured.getOrThrow(MiscOverworldFeatures.FOREST_ROCK),
                                            List.of(RarityFilter.onAverageOnceEvery(3), NoiseThresholdCountPlacement.of(-0.0D, 1, 2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("alpine_rock"),
                                    new PlacedFeature(configured.getOrThrow(cfk("alpine_rock")),
                                            List.of(RarityFilter.onAverageOnceEvery(3), NoiseThresholdCountPlacement.of(-0.0D, 1, 2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("alpine_rock_andesite"),
                                    new PlacedFeature(configured.getOrThrow(cfk("alpine_rock_andesite")),
                                            List.of(RarityFilter.onAverageOnceEvery(30), NoiseThresholdCountPlacement.of(-0.4D, 3, 6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("shrub"),
                                    new PlacedFeature(configured.getOrThrow(cfk("shrub")),
                                            List.of(NoiseThresholdCountPlacement.of(-0.8D, 3, 7), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("alpine_plant"),
                                    new PlacedFeature(configured.getOrThrow(cfk("alpine_plant")),
                                            List.of(NoiseThresholdCountPlacement.of(-0.8D, 6, 10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("heather"),
                                    new PlacedFeature(configured.getOrThrow(cfk("heather")),
                                            List.of(NoiseThresholdCountPlacement.of(-0.8D, 5, 10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("heath_vegetation"),
                                    new PlacedFeature(configured.getOrThrow(cfk("heath_vegetation")),
                                            List.of(NoiseThresholdCountPlacement.of(-0.8D, 5, 10), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("surface_moss"),
                                    new PlacedFeature(configured.getOrThrow(cfk("surface_moss")),
                                            List.of(NoiseThresholdCountPlacement.of(-0.8D, 5, 10), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("clover"),
                                    new PlacedFeature(configured.getOrThrow(cfk("clover")),
                                            List.of(NoiseThresholdCountPlacement.of(-0.4D, 2, 8), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome())
                                    ));
                            bootstrap.register(pfk("elder_pine"),
                                    new PlacedFeature(configured.getOrThrow(cfk("tree_elder_pine")),
                                            List.of(RarityFilter.onAverageOnceEvery(25), PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING))
                                    ));

                            bootstrap.register(pfk("taller_birch"),
                                    new PlacedFeature(configured.getOrThrow(cfk("tree_taller_birch")),
                                            VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.1F, 3), Blocks.BIRCH_SAPLING)
                                    ));
                            bootstrap.register(pfk("taller_birch_b"),
                                    new PlacedFeature(configured.getOrThrow(cfk("tree_taller_birch_b")),
                                           VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.1F, 3), Blocks.BIRCH_SAPLING)
                                    ));
                            bootstrap.register(pfk("larger_oak"),
                                    new PlacedFeature(configured.getOrThrow(cfk("tree_larger_oak")),
                                            VegetationPlacements.treePlacement(PlacementUtils.countExtra(10, 0.1F, 1), Blocks.OAK_SAPLING)
                                    ));
//                            bootstrap.register(pfk("ivy_oaks_set"),
//                                    new PlacedFeature(configured.getOrThrow(cfk("ivy_oaks_set")),
//                                            VegetationPlacements.treePlacement(PlacementUtils.countExtra(2, 0.1F, 7), Blocks.OAK_SAPLING)
//                                    ));
                            bootstrap.register(pfk("disk_coarse"),
                                    new PlacedFeature(configured.getOrThrow(cfk("disk_coarse")),
                                            List.of(CountPlacement.of(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK)), BiomeFilter.biome())
                                    ));

                            bootstrap.register(pfk("disk_mud"),
                                    new PlacedFeature(configured.getOrThrow(cfk("disk_mud")),
                                            List.of(NoiseThresholdCountPlacement.of(-0.7D, 3, 0), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_TOP_SOLID, BiomeFilter.biome())
                                    ));

                            bootstrap.register(pfk("river_gold"),
                                    new PlacedFeature(configured.getOrThrow(cfk("river_gold")),
                                            commonOrePlacement(10, hru(50, 64))
                                    ));
                            bootstrap.register(pfk("sulfur"),
                                    new PlacedFeature(configured.getOrThrow(cfk("sulfur")),
                                            commonOrePlacement(8, hrt(-61, -46))
                                    ));
                            bootstrap.register(pfk("nitre"),
                                    new PlacedFeature(configured.getOrThrow(cfk("nitre")),
                                            commonOrePlacement(3, hrt(40, 120))
                                    ));
                            // Count 30 -> 12
                            bootstrap.register(pfk("ore_coal_upper_reduced"),
                                    new PlacedFeature(configured.getOrThrow(cfk("reduced_ore_coal")),
                                            commonOrePlacement(12, hru(136, 300))
                                    ));
                            // Count 20 -> 5
                            bootstrap.register(pfk("ore_coal_lower_reduced"),
                                    new PlacedFeature(configured.getOrThrow(cfk("reduced_ore_coal_buried")),
                                            commonOrePlacement(5, hrt(0, 192))
                                    ));
                            // Count 16 -> 5
                            bootstrap.register(pfk("ore_copper_reduced"),
                                    new PlacedFeature(configured.getOrThrow(cfk("reduced_ore_copper")),
                                            commonOrePlacement(5, hrt(-16, 112))
                                    ));
                            // Count 10 -> 4
                            bootstrap.register(pfk("ore_iron_reduced"),
                                    new PlacedFeature(configured.getOrThrow(cfk("reduced_ore_iron")),
                                            commonOrePlacement(4, hrt(-24, 56))
                                    ));
                            // Count 90 -> 30
                            bootstrap.register(pfk("ore_iron_upper_reduced"),
                                    new PlacedFeature(configured.getOrThrow(cfk("reduced_ore_iron")),
                                            commonOrePlacement(30, hrt(80, 384))
                                    ));
                            bootstrap.register(pfk("ore_poor_iron_wide"),
                                    new PlacedFeature(configured.getOrThrow(cfk("poor_iron")),
                                            commonOrePlacement(8, hru(-60, 72))
                                    ));
                            bootstrap.register(pfk("ore_poor_iron"),
                                    new PlacedFeature(configured.getOrThrow(cfk("poor_iron")),
                                            commonOrePlacement(12, hrt(-30, 70))
                                    ));
                            bootstrap.register(pfk("ore_silver"),
                                    new PlacedFeature(configured.getOrThrow(cfk("ore_silver")),
                                            commonOrePlacement(3, hrt(-60, 36))
                                    ));
                            // Count 4 -> Rarity 2
                            bootstrap.register(pfk("ore_gold_reduced"),
                                    new PlacedFeature(configured.getOrThrow(cfk("reduced_ore_gold")),
                                            rareOrePlacement(3, hrt(-56, 16))
                                    ));
                            bootstrap.register(pfk("mountain_gold"),
                                    new PlacedFeature(configured.getOrThrow(cfk("mountain_gold")),
                                            commonOrePlacement(3, hrt(70, 200))
                                    ));
                            bootstrap.register(pfk("garnet_cluster"),
                                    new PlacedFeature(configured.getOrThrow(cfk("garnets")),
                                            commonOrePlacement(16, hrt(-32, 64))
                                    ));
                            bootstrap.register(pfk("gem_cluster"),
                                    new PlacedFeature(configured.getOrThrow(cfk("gems")),
                                            commonOrePlacement(4, hrt(-48, 16))
                                    ));
                            bootstrap.register(pfk("onyx_cluster"),
                                    new PlacedFeature(configured.getOrThrow(cfk("onyxes")),
                                            commonOrePlacement(3, hrt(-64, -32))
                                    ));
                            bootstrap.register(pfk("nether_onyx_cluster"),
                                    new PlacedFeature(configured.getOrThrow(cfk("onyxes")),
                                            commonOrePlacement(16, hru(8, 120))
                                    ));
//                            /*
//                             * Chunk-distributed ores
//                             */
                            bootstrap.register(pfk("ore_coal_extra"),
                                    new PlacedFeature(configured.getOrThrow(cfk("extra_ore_coal")),
                                            chunkedOrePlacement(25, OreDist.OreChunkType.COAL, hru(-16, 64))
                                    ));
                            bootstrap.register(pfk("ore_copper_extra"),
                                    new PlacedFeature(configured.getOrThrow(cfk("extra_ore_copper")),
                                            chunkedOrePlacement(40, OreDist.OreChunkType.COPPER, hru(-48, 48))
                                    ));
                            bootstrap.register(pfk("ore_poor_iron_extra"),
                                    new PlacedFeature(configured.getOrThrow(cfk("extra_poor_iron")),
                                            chunkedOrePlacement(40, OreDist.OreChunkType.POOR_IRON, hru(0, 48))
                                    ));
                            bootstrap.register(pfk("ore_iron_extra"),
                                    new PlacedFeature(configured.getOrThrow(cfk("extra_ore_iron")),
                                            chunkedOrePlacement(40, OreDist.OreChunkType.IRON, hru(-48, 48))
                                    ));
                            bootstrap.register(pfk("ore_silver_extra"),
                                    new PlacedFeature(configured.getOrThrow(cfk("extra_ore_silver")),
                                            chunkedOrePlacement(25, OreDist.OreChunkType.SILVER, hru(-48, 48))
                                    ));
                            bootstrap.register(pfk("ore_gold_extra"),
                                    new PlacedFeature(configured.getOrThrow(cfk("extra_ore_gold")),
                                            chunkedOrePlacement(18, OreDist.OreChunkType.GOLD, hru(-48, 16))
                                    ));
                            bootstrap.register(pfk("ore_garnets_extra"),
                                    new PlacedFeature(configured.getOrThrow(cfk("garnets")),
                                            chunkedOrePlacement(40, OreDist.OreChunkType.GARNET, hru(-48, 48))
                                    ));
                            bootstrap.register(pfk("ore_gems_extra"),
                                    new PlacedFeature(configured.getOrThrow(cfk("gems")),
                                            chunkedOrePlacement(40, OreDist.OreChunkType.GEMS, hru(-48, 48))
                                    ));
                            bootstrap.register(pfk("ore_emeralds_extra"),
                                    new PlacedFeature(configured.getOrThrow(cfk("extra_ore_emerald")),
                                            chunkedOrePlacement(40, OreDist.OreChunkType.EMERALD, hru(-48, 48))
                                    ));
                        }),
                Set.of(Main.MOD_ID)
        );
    }

    private static ResourceKey<ConfiguredFeature<?,?>> cfk(String s) {
        return  ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Main.MOD_ID, s));
    }

    private static ResourceKey<PlacedFeature> pfk(String s) {
        return  ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Main.MOD_ID, s));
    }

    private static void makeOre(BootstapContext<ConfiguredFeature<?,?>> bootstrap, String regName, List<OreConfiguration.TargetBlockState> targets, int veinSize, float airDiscardChance) {
        bootstrap.register(
              ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Main.MOD_ID, regName)),
              new ConfiguredFeature<>(ModGeneration.MOD_ORE.get(), new OreConfiguration(targets, veinSize, airDiscardChance))
        );
    }

    private static void makeOre(BootstapContext<ConfiguredFeature<?,?>> bootstrap, String regName, List<OreConfiguration.TargetBlockState> targets, int veinSize) {
        makeOre(bootstrap, regName, targets, veinSize, 0f);
    }

    private static List<OreConfiguration.TargetBlockState> basicTargetList(Block stoneOre, Block deepslateOre) {
        return ImmutableList.of(
                OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), stoneOre.defaultBlockState()),
                OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), deepslateOre.defaultBlockState())
        );
    }


    private static HeightRangePlacement hru(int l, int u) {
        return HeightRangePlacement.uniform(VerticalAnchor.absolute(l), VerticalAnchor.absolute(u));
    }

    private static HeightRangePlacement hrt(int l, int u) {
        return HeightRangePlacement.triangle(VerticalAnchor.absolute(l), VerticalAnchor.absolute(u));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier countOrRarity, PlacementModifier heightModifier) {
        return List.of(BiomeFilter.biome(), countOrRarity, InSquarePlacement.spread(), heightModifier);
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightModifier) {
        return orePlacement(CountPlacement.of(count), heightModifier);
    }

    private static List<PlacementModifier> rareOrePlacement(int rarity, PlacementModifier heightModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(rarity), heightModifier);
    }

    private static List<PlacementModifier> chunkedOrePlacement(int count, OreDist.OreChunkType chunkType, PlacementModifier heightModifier) {
        return List.of(BiomeFilter.biome(), ChunkDistributionFilter.forType(chunkType), CountPlacement.of(count), InSquarePlacement.spread(), heightModifier);
    }

}