package abyssal.data;

import abyssal.Main;
import abyssal.blocks.ReedBlock;
import abyssal.init.Gems;
import abyssal.init.ModBlocks;
import abyssal.init.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.CreakingHeartState;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, Main.MOD_ID);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return ModBlocks.DATAGEN_MODEL.stream();
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for(DeferredHolder<Item, ? extends Item> regOb : ModItems.ITEMS.getEntries()) {
            Item item = regOb.get();
            if(item instanceof BlockItem) continue;
            if(ModItems.HANDHELD_ITEMS.contains(regOb)) {
                itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
            } else {
                itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
            }
        }

        // Any block that looks different in the inventory goes in here
        List<Block> differentItemTexture = new ArrayList<>();
        differentItemTexture.add(ModBlocks.CORN_SEED.get());
        differentItemTexture.add(ModBlocks.REED.get());
//        differentItemTexture.add(ModBlocks.MOSSY_BIRCH.get());
//        differentItemTexture.add(ModBlocks.MOSSY_OAK.get());
        differentItemTexture.add(ModBlocks.IVY.get());
        differentItemTexture.add(ModBlocks.CLOVER.get());
        differentItemTexture.add(ModBlocks.AMP_BOOKSHELF.get());
        
        ModBlocks.BLOCKS.getEntries().stream().map(Supplier::get).forEach(block -> {
            if (differentItemTexture.contains(block)) {
                blockModels.registerSimpleFlatItemModel(block, "_inv");
            } else {
                // automatic cube
            }
        });

        MultiVariant multivariant;

        Gems.forAllGemBlocks(blockModels::createTrivialCube);

        blockModels.createTrivialBlock(ModBlocks.POWDER_BARREL.get(), TexturedModel.CUBE_TOP_BOTTOM);
        blockModels.createTrivialBlock(ModBlocks.POWDER_BARREL_KNOCK.get(),
                TexturedModel.CUBE_TOP_BOTTOM.updateTexture(mapping ->
                        mapping.put(TextureSlot.SIDE, new Material(modLocation("block/powder_barrel_knock_side")))
                                .put(TextureSlot.BOTTOM, new Material(modLocation("block/powder_barrel_bottom")))
                                .put(TextureSlot.TOP, new Material(modLocation("block/powder_barrel_top")))
                )
        );
        blockModels.createTrivialBlock(ModBlocks.POWDER_BARREL_FRAG.get(),
                TexturedModel.CUBE_TOP_BOTTOM.updateTexture(mapping ->
                        mapping.put(TextureSlot.SIDE, new Material(modLocation("block/powder_barrel_frag_side")))
                                .put(TextureSlot.BOTTOM, new Material(modLocation("block/powder_barrel_bottom")))
                                .put(TextureSlot.TOP, new Material(modLocation("block/powder_barrel_top")))
                )
        );

        blockModels.createTrivialBlock(ModBlocks.LEAF_LITTER.get(),
                TexturedModel.CUBE_TOP_BOTTOM.updateTexture(mapping ->
                        mapping.put(TextureSlot.SIDE, new Material(modLocation("block/leaf_litter_side")))
                                .put(TextureSlot.BOTTOM, new Material(mcLocation("block/dirt")))
                                .put(TextureSlot.TOP, new Material(modLocation("block/leaf_litter_top")))
                )
        );
        blockModels.createTrivialBlock(ModBlocks.GRASS_SUPER_SOIL.get(),
                TexturedModel.CUBE_TOP_BOTTOM.updateTexture(mapping ->
                        mapping.put(TextureSlot.SIDE, new Material(modLocation("block/grass_super_soil_side")))
                                .put(TextureSlot.BOTTOM, new Material(modLocation("block/super_soil")))
                                .put(TextureSlot.TOP, new Material(modLocation("block/grass_super_soil_top")))
                )
        );
        blockModels.createTrivialBlock(ModBlocks.LAPIDARY.get(),
                TexturedModel.CUBE_TOP_BOTTOM.updateTexture(mapping ->
                        mapping.put(TextureSlot.SIDE, new Material(modLocation("block/lapidary_side")))
                                .put(TextureSlot.BOTTOM, new Material(mcLocation("block/cobbled_deepslate")))
                                .put(TextureSlot.TOP, new Material(modLocation("block/lapidary_top")))
                )
        );
        blockModels.createTrivialCube(ModBlocks.HARMONISER.get());

        blockModels.createTrivialCube(ModBlocks.ABYSSAL_STONE.get());
        blockModels.createTrivialCube(ModBlocks.ENKATITE.get());
        blockModels.createTrivialCube(ModBlocks.POOR_IRON_ORE.get());
        blockModels.createTrivialCube(ModBlocks.DEEPSLATE_POOR_IRON_ORE.get());
        blockModels.createTrivialCube(ModBlocks.SILVER_ORE.get());
        blockModels.createTrivialCube(ModBlocks.DEEPSLATE_SILVER_ORE.get());
        blockModels.createTrivialCube(ModBlocks.GARNET_CLUSTER.get());
        blockModels.createTrivialCube(ModBlocks.DEEPSLATE_GARNET_CLUSTER.get());
        blockModels.createTrivialCube(ModBlocks.ONYX_CLUSTER.get());
        blockModels.createTrivialCube(ModBlocks.DEEPSLATE_ONYX_CLUSTER.get());
        blockModels.createTrivialCube(ModBlocks.NETHER_ONYX_CLUSTER.get());
        blockModels.createTrivialCube(ModBlocks.GEM_CLUSTER.get());
        blockModels.createTrivialCube(ModBlocks.DEEPSLATE_GEM_CLUSTER.get());
        blockModels.createTrivialCube(ModBlocks.NITRE.get());
        blockModels.createTrivialCube(ModBlocks.DEEPSLATE_NITRE.get());
        blockModels.createTrivialCube(ModBlocks.SULFUR.get());
        blockModels.createTrivialCube(ModBlocks.DEEPSLATE_SULFUR.get());
        blockModels.createTrivialCube(ModBlocks.SILVER_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.ALCHEMICAL_GOLD_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.SUPER_SOIL.get());
        blockModels.createTrivialCube(ModBlocks.GOLD_GRAVEL.get());

        multivariant = plainVariant(blockModels.createSuffixedVariant(ModBlocks.PRISM.get(), "", ModelTemplates.CUBE_ALL, TextureMapping::cube));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.PRISM.get(), multivariant));


        //blockModels.createTrivialCube(ModBlocks.ICHOR.get()); // Needs "particle" for animation texture - how?

        // Needs variants & held item
        //blockModels.createTrivialCube(ModBlocks.CORN_SEED.get(), models().crop(regName(ModBlocks.CORN_SEED),modLoc( "block/" + regName(ModBlocks.CORN_SEED))));

        // Ivy - vines are complicated

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.REED.get()).with(PropertyDispatch.initial(ReedBlock.TYPE)
                .select(0, plainVariant(blockModels.createSuffixedVariant(ModBlocks.REED.get(), "_tips", ModelTemplates.CROP, TextureMapping::crop)))
                .select(1, plainVariant(blockModels.createSuffixedVariant(ModBlocks.REED.get(), "_base", ModelTemplates.CROP, TextureMapping::crop)))
                .select(2, plainVariant(blockModels.createSuffixedVariant(ModBlocks.REED.get(), "_wet", ModelTemplates.CROP, TextureMapping::crop)))
                .select(3, plainVariant(blockModels.createSuffixedVariant(ModBlocks.REED.get(), "_heads", ModelTemplates.CROP, TextureMapping::crop)))
        ));

        multivariant = plainVariant(blockModels.createSuffixedVariant(ModBlocks.HEATHER.get(), "", ModelTemplates.CROSS, TextureMapping::cross));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(ModBlocks.HEATHER.get(), multivariant));

        blockModels.createTrivialBlock(ModBlocks.SPIDER_NEST.get(), TexturedModel.COLUMN);

        blockModels.createTrivialCube(ModBlocks.ELDER_PINE_PLANKS.get());
        blockModels.createDoor(ModBlocks.ELDER_PINE_DOOR.get());
        blockModels.createRotatedPillarWithHorizontalVariant(ModBlocks.ELDER_PINE_LOG.get(), TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);

        blockModels.createRotatedPillarWithHorizontalVariant(ModBlocks.NETHER_BRASS_BLOCK.get(), TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
        blockModels.createRotatedPillarWithHorizontalVariant(ModBlocks.CHARRED_LOG.get(), TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);

        blockModels.createTrivialCube(ModBlocks.THIN_LEAVES.get());
        
    }
}
