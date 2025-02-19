package abyssal.data;

import abyssal.Main;
import abyssal.blocks.ReedBlock;
import abyssal.init.Gems;
import abyssal.init.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Vector;
import java.util.function.Supplier;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.POWDER_BARREL.get(), models().cubeBottomTop(regName(ModBlocks.POWDER_BARREL),
                modLoc( "block/powder_barrel"), modLoc("block/powder_barrel_bottom"), modLoc("block/powder_barrel_top")));
        simpleBlock(ModBlocks.POWDER_BARREL_KNOCK.get(), models().cubeBottomTop(regName(ModBlocks.POWDER_BARREL_KNOCK),
                modLoc( "block/powder_barrel_knock"), modLoc("block/powder_barrel_bottom"), modLoc("block/powder_barrel_top")));
        simpleBlock(ModBlocks.POWDER_BARREL_FRAG.get(), models().cubeBottomTop(regName(ModBlocks.POWDER_BARREL_FRAG),
                modLoc( "block/powder_barrel_frag"), modLoc("block/powder_barrel_bottom"), modLoc("block/powder_barrel_top")));

        simpleBlock(ModBlocks.LEAF_LITTER.get(), models().cubeBottomTop(regName(ModBlocks.LEAF_LITTER),
                modLoc( "block/leaf_litter_side"), mcLoc("block/dirt"), modLoc("block/leaf_litter_top")));
        simpleBlock(ModBlocks.GRASS_SUPER_SOIL.get(), models().cubeBottomTop(regName(ModBlocks.GRASS_SUPER_SOIL),
                modLoc( "block/grass_super_soil_side"), modLoc("block/super_soil"), modLoc("block/grass_super_soil_top")));

        simpleBlock(ModBlocks.LAPIDARY.get(), models().cubeBottomTop(regName(ModBlocks.LAPIDARY),
                modLoc( "block/lapidary_side"), mcLoc("block/cobbled_deepslate"), modLoc("block/lapidary_top")));
        simpleBlock(ModBlocks.HARMONISER.get());

        simpleBlock(ModBlocks.ABYSSAL_STONE.get());
        simpleBlock(ModBlocks.ENKATITE.get());
        simpleBlock(ModBlocks.POOR_IRON_ORE.get());
        simpleBlock(ModBlocks.DEEPSLATE_POOR_IRON_ORE.get());
        simpleBlock(ModBlocks.SILVER_ORE.get());
        simpleBlock(ModBlocks.DEEPSLATE_SILVER_ORE.get());
        simpleBlock(ModBlocks.GARNET_CLUSTER.get());
        simpleBlock(ModBlocks.DEEPSLATE_GARNET_CLUSTER.get());
        simpleBlock(ModBlocks.ONYX_CLUSTER.get());
        simpleBlock(ModBlocks.DEEPSLATE_ONYX_CLUSTER.get());
        simpleBlock(ModBlocks.NETHER_ONYX_CLUSTER.get());
        simpleBlock(ModBlocks.GEM_CLUSTER.get());
        simpleBlock(ModBlocks.DEEPSLATE_GEM_CLUSTER.get());
        simpleBlock(ModBlocks.NITRE.get());
        simpleBlock(ModBlocks.DEEPSLATE_NITRE.get());
        simpleBlock(ModBlocks.SULFUR.get());
        simpleBlock(ModBlocks.DEEPSLATE_SULFUR.get());
        simpleBlock(ModBlocks.SILVER_BLOCK.get());
        simpleBlock(ModBlocks.ALCHEMICAL_GOLD_BLOCK.get());
        simpleBlock(ModBlocks.SUPER_SOIL.get());
        simpleBlock(ModBlocks.PRISM.get(), models().cubeAll(regName(ModBlocks.PRISM), modLoc("block/" + regName(ModBlocks.PRISM))).renderType("translucent"));
        simpleBlock(ModBlocks.GOLD_GRAVEL.get());


        //simpleBlock(ModBlocks.ICHOR.get()); // Needs "particle" for animation texture - how?

        // Needs variants & held item
        //simpleBlock(ModBlocks.CORN_SEED.get(), models().crop(regName(ModBlocks.CORN_SEED),modLoc( "block/" + regName(ModBlocks.CORN_SEED))));

        // Ivy - vines are complicated


        simpleBlock(ModBlocks.HEATHER.get(), models().cross(regName(ModBlocks.HEATHER),modLoc( "block/" + regName(ModBlocks.HEATHER))).renderType("cutout"));
        simpleBlock(ModBlocks.SPIDER_NEST.get(), models().cubeBottomTop(regName(ModBlocks.SPIDER_NEST),
                modLoc( "block/nest_side"), modLoc("block/nest_top"), modLoc("block/nest_top")));

        simpleBlock(ModBlocks.ELDER_PINE_PLANKS.get());
        doorBlock((DoorBlock) ModBlocks.ELDER_PINE_DOOR.get(), modLoc("block/elder_pine_door_bottom"), modLoc("block/elder_pine_door_top"));
        axisBlock(ModBlocks.ELDER_PINE_LOG.get());

        axisBlock(ModBlocks.NETHER_BRASS_BLOCK.get());
        simpleBlock(ModBlocks.CHARRED_LOG.get());


        simpleBlock(ModBlocks.THIN_LEAVES.get());

        //simpleBlock(ModBlocks.BRUSH.get());


        Gems.forAllGemBlocks(this::simpleBlock);

        List<Block> differentItemTexture = new Vector<>();
        differentItemTexture.add(ModBlocks.CORN_SEED.get());
        differentItemTexture.add(ModBlocks.REED.get());
        differentItemTexture.add(ModBlocks.MOSSY_BIRCH.get());
        differentItemTexture.add(ModBlocks.MOSSY_OAK.get());
        differentItemTexture.add(ModBlocks.IVY.get());
        differentItemTexture.add(ModBlocks.ELDER_PINE_DOOR.get());
        differentItemTexture.add(ModBlocks.CLOVER.get());
        differentItemTexture.add(ModBlocks.AMP_BOOKSHELF.get());

        for(Block block : ModBlocks.BLOCKS.getEntries().stream().map(Supplier::get).toList()) {
            String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
            if(differentItemTexture.contains(block)) {
                continue;
            }
            simpleBlockItem(block, models().getExistingFile(modLoc("block/" + name)));
        }

        VariantBlockStateBuilder vb = getVariantBuilder(ModBlocks.REED.get());
        VariantBlockStateBuilder.PartialBlockstate ps0 = vb.partialState().with(ReedBlock.TYPE, 0);
        VariantBlockStateBuilder.PartialBlockstate ps1 = vb.partialState().with(ReedBlock.TYPE, 1);
        VariantBlockStateBuilder.PartialBlockstate ps2 = vb.partialState().with(ReedBlock.TYPE, 2);
        VariantBlockStateBuilder.PartialBlockstate ps3 = vb.partialState().with(ReedBlock.TYPE, 3);
        vb.addModels(ps0, ps0.modelForState().modelFile(
                models().crop(regName(ModBlocks.REED) + "_tips", modLoc( "block/reed_tips")).renderType("cutout")).build());
        vb.addModels(ps1, ps1.modelForState().modelFile(
                models().crop(regName(ModBlocks.REED) + "_base", modLoc( "block/reed_base")).renderType("cutout")).build());
        vb.addModels(ps2, ps2.modelForState().modelFile(
                models().crop(regName(ModBlocks.REED) + "_wet", modLoc( "block/reed_wet")).renderType("cutout")).build());
        vb.addModels(ps3, ps3.modelForState().modelFile(
                models().crop(regName(ModBlocks.REED) + "_heads", modLoc( "block/reed_heads")).renderType("cutout")).build());

    }

    private String regName(Supplier<Block> regOb) {
        return BuiltInRegistries.BLOCK.getKey(regOb.get()).getPath();
    }
}
