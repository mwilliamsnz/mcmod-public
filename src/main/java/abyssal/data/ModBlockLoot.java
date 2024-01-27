package abyssal.data;

import abyssal.init.Gems;
import abyssal.init.ModBlocks;
import abyssal.init.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLoot extends BlockLootSubProvider {

    protected ModBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.DATAGEN_LOOT_TABLE // Get all registered entries
                .stream() // Stream the wrapped objects
                .flatMap(RegistryObject::stream) // Get the object if available
                ::iterator; // Create the iterable
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.REED.get());
        this.dropSelf(ModBlocks.SHRUB.get());
        this.dropSelf(ModBlocks.HEATHER.get());
        this.dropSelf(ModBlocks.CLOVER.get());
        this.dropSelf(ModBlocks.LEAF_LITTER.get());
        this.dropSelf(ModBlocks.ELDER_PINE_PLANKS.get());
        this.dropSelf(ModBlocks.ELDER_PINE_LOG.get());
        this.add(ModBlocks.ELDER_PINE_DOOR.get(), (block) -> LootTable.lootTable().withPool(
            this.applyExplosionCondition(block, LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(block)
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER))
                    )
                )
            ))
        );
        this.dropSelf(ModBlocks.MOSSY_BIRCH.get());
        this.dropSelf(ModBlocks.MOSSY_OAK.get());
        this.dropSelf(ModBlocks.ABYSSAL_STONE.get());
        this.dropSelf(ModBlocks.ENKATITE.get());
        this.dropSelf(ModBlocks.SILVER_BLOCK.get());
        this.dropSelf(ModBlocks.NETHER_BRASS_BLOCK.get());
        this.dropSelf(ModBlocks.ALCHEMICAL_GOLD_BLOCK.get());

        this.dropSelf(ModBlocks.POWDER_BARREL.get());
        this.dropSelf(ModBlocks.POWDER_BARREL_FRAG.get());
        this.dropSelf(ModBlocks.POWDER_BARREL_KNOCK.get());
        this.dropSelf(ModBlocks.LAPIDARY.get());
        this.dropSelf(ModBlocks.HARMONISER.get());


        Gems.forAllGemBlocks(this::dropSelf);

        // Manual
//        LootItemCondition.Builder conditionBuilder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.CORN_SEED.get())
//                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7));
//        this.add(ModBlocks.CORN_SEED.get(), createCropDrops(ModBlocks.CORN_SEED.get(), ModItems.CORN.get(), ModBlocks.CORN_SEED.get().asItem(), conditionBuilder));

        this.add(ModBlocks.POOR_IRON_ORE.get(), this::createPoorIronDrop);
        this.add(ModBlocks.DEEPSLATE_POOR_IRON_ORE.get(), this::createPoorIronDrop);
        this.add(ModBlocks.SILVER_ORE.get(), (block) -> createOreDrop(block, ModItems.RAW_SILVER.get()));
        this.add(ModBlocks.DEEPSLATE_SILVER_ORE.get(), (block) -> createOreDrop(block, ModItems.RAW_SILVER.get()));

        this.add(ModBlocks.NITRE.get(), (block) -> createRedstoneLikeDrop(block, ModItems.SALTPETRE.get()));
        this.add(ModBlocks.DEEPSLATE_NITRE.get(), (block) -> createRedstoneLikeDrop(block, ModItems.SALTPETRE.get()));
        this.add(ModBlocks.SULFUR.get(), (block) -> createRedstoneLikeDrop(block, ModItems.SULFUR.get()));
        this.add(ModBlocks.DEEPSLATE_SULFUR.get(), (block) -> createRedstoneLikeDrop(block, ModItems.SULFUR.get()));

        this.add(ModBlocks.CHARRED_LOG.get(), (block) -> createCharredDrop(block, Items.CHARCOAL));

        this.add(ModBlocks.IVY.get(), BlockLootSubProvider::createShearsOnlyDrop);
        this.add(ModBlocks.BRUSH.get(), BlockLootSubProvider::createShearsOnlyDrop);
        this.add(ModBlocks.THIN_LEAVES.get(), BlockLootSubProvider::createShearsOnlyDrop);
        this.add(ModBlocks.ALPINE_PLANT.get(), BlockLootSubProvider::createShearsOnlyDrop);
        this.add(ModBlocks.FERN_CORE.get(), BlockLootSubProvider::createShearsOnlyDrop);
        this.add(ModBlocks.FERN_FRONDS.get(), BlockLootSubProvider::createShearsOnlyDrop);

        this.dropWhenSilkTouch(ModBlocks.SPIDER_NEST.get());

        // Manual:
        // [deepslate_]gem_cluster
        // [deepslate_]garnet_cluster
    }

//    @Override
//    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
//        this.generate();
////        Set<ResourceLocation> set = Sets.newHashSet();
//
////        for(Block block : getKnownBlocks()) {
////            ResourceLocation resourcelocation = block.getLootTable();
////            if (resourcelocation != BuiltInLootTables.EMPTY && set.add(resourcelocation)) {
////                LootTable.Builder builder = this.map.remove(resourcelocation);
////                if (builder == null) {
////                    throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", resourcelocation, Registry.BLOCK.getKey(block)));
////                }
////
////                consumer.accept(resourcelocation, builder);
////            }
////        }
//
//        for(ResourceLocation loc : lootMap.keySet()) {
//            consumer.accept(loc, lootMap.get(loc));
//        }
//
////        if (!this.map.isEmpty()) {
////            throw new IllegalStateException("Created block loot tables for non-blocks: " + this.lootMap.keySet());
////        }
//    }

//    protected void add(Block block, LootTable.Builder builder) {
//        this.lootMap.put(block.getLootTable(), builder);
//    }

    private LootTable.Builder createPoorIronDrop(Block block) {
        return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(ModItems.POOR_IRON.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,2))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    private LootTable.Builder createRedstoneLikeDrop(Block block, Item result) {
        return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(result).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,4))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    private LootTable.Builder createCharredDrop(Block block, Item result) {
        return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(result).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,1)))));
    }
}
