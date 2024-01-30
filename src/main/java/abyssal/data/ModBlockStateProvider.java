package abyssal.data;

import abyssal.Main;
import abyssal.init.Gems;
import abyssal.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Vector;

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

        simpleBlock(ModBlocks.REED.get(), models().cross(regName(ModBlocks.REED),modLoc( "block/" + regName(ModBlocks.REED))).renderType("cutout"));
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

        for(Block block : ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).toList()) {
            String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
            if(differentItemTexture.contains(block)) {
                continue;
            }
            simpleBlockItem(block, models().getExistingFile(modLoc("block/" + name)));
        }
    }

    private String regName(RegistryObject<Block> regOb) {
        return ForgeRegistries.BLOCKS.getKey(regOb.get()).getPath();
    }
}
