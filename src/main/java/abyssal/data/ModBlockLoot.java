package abyssal.data;

import abyssal.init.Gems;
import abyssal.init.ModBlocks;
import abyssal.init.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class ModBlockLoot extends BlockLootSubProvider {

    HolderLookup.RegistryLookup<Enchantment> enchants;

    public ModBlockLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.DATAGEN_LOOT_TABLE.stream()
                .map(Holder::value)::iterator;
    }

    @Override
    protected void generate() {
        enchants = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        this.dropSelf(ModBlocks.REED.get());
        this.dropSelf(ModBlocks.SHRUB.get());
        this.dropSelf(ModBlocks.HEATHER.get());
        this.add(ModBlocks.CLOVER.get(), (block) -> createSilkTouchDispatchTable(block,
                this.applyExplosionCondition(block, LootItem.lootTableItem(ModItems.FOUR_LEAF_CLOVER.get())
                        .when(BonusLevelTableCondition.bonusLevelFlatChance(
                                enchants.getOrThrow(Enchantments.FORTUNE), 0.001F, 0.005F, 0.025F, 0.125F))
                        .otherwise(LootItem.lootTableItem(block)))));
        this.add(ModBlocks.LEAF_LITTER.get(), (block) -> createSingleItemTableWithSilkTouch(block, Blocks.DIRT));
        this.dropSelf(ModBlocks.SUPER_SOIL.get());
        this.add(ModBlocks.GRASS_SUPER_SOIL.get(), (block) -> createSingleItemTableWithSilkTouch(block, ModBlocks.SUPER_SOIL.get()));
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
        this.dropWhenSilkTouch(ModBlocks.PRISM.get());

        this.dropSelf(ModBlocks.POWDER_BARREL.get());
        this.dropSelf(ModBlocks.POWDER_BARREL_FRAG.get());
        this.dropSelf(ModBlocks.POWDER_BARREL_KNOCK.get());
        this.dropSelf(ModBlocks.LAPIDARY.get());
        this.dropSelf(ModBlocks.HARMONISER.get());
        this.dropSelf(ModBlocks.AMP_BOOKSHELF.get());


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

        this.add(ModBlocks.IVY.get(), (block) -> createShearsOnlyDrop(ModBlocks.IVY.get()));
        this.add(ModBlocks.BRUSH.get(), (block) -> createShearsOnlyDrop(ModBlocks.BRUSH.get()));
        this.add(ModBlocks.THIN_LEAVES.get(), (block) -> createShearsOnlyDrop(ModBlocks.THIN_LEAVES.get()));
        this.add(ModBlocks.ALPINE_PLANT.get(), (block) -> createShearsOnlyDrop(ModBlocks.ALPINE_PLANT.get()));
        this.add(ModBlocks.FERN_CORE.get(), (block) -> createShearsOnlyDrop(ModBlocks.FERN_CORE.get()));
        this.add(ModBlocks.FERN_FRONDS.get(), (block) -> createShearsOnlyDrop(ModBlocks.FERN_FRONDS.get()));

        this.dropWhenSilkTouch(ModBlocks.SPIDER_NEST.get());

        // Manual:
        // [deepslate_]gem_cluster
        // [deepslate_]garnet_cluster
    }



    private LootTable.Builder createPoorIronDrop(Block block) {
        return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(ModItems.POOR_IRON.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,2))).apply(ApplyBonusCount.addOreBonusCount(enchants.getOrThrow(Enchantments.FORTUNE)))));
    }

    private LootTable.Builder createRedstoneLikeDrop(Block block, Item result) {
        return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(result).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,4))).apply(ApplyBonusCount.addOreBonusCount(enchants.getOrThrow(Enchantments.FORTUNE)))));
    }

    private LootTable.Builder createCharredDrop(Block block, Item result) {
        return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(result).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,1)))));
    }
}
