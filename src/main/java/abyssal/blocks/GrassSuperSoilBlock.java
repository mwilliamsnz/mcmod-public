package abyssal.blocks;

import abyssal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraftforge.common.extensions.IForgeBlock;

import java.util.List;
import java.util.Optional;

public class GrassSuperSoilBlock extends Block implements IForgeBlock, BonemealableBlock {
    public GrassSuperSoilBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!level.isAreaLoaded(pos, 1)) return;
        for(BlockPos p : BlockPos.betweenClosed(pos.offset(-1,-1,-1), pos.offset(1,1,1))) {
            BlockState s = level.getBlockState(p);
            if(s.getBlock() instanceof BonemealableBlock b && rand.nextInt(20) == 0) {
                b.performBonemeal(level, rand, p, s);
            }
            if(s.is(Blocks.DIRT) && rand.nextInt(10) == 0 && canBeGrass(s, level, p)) {
                level.setBlockAndUpdate(p, Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }
        if (!canBeGrass(state, level, pos)) {
            level.setBlockAndUpdate(pos, ModBlocks.SUPER_SOIL.get().defaultBlockState());
        }
    }

    public static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState stateAbove = levelReader.getBlockState(above);
        if (stateAbove.is(Blocks.SNOW) && stateAbove.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (stateAbove.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int light = LightEngine.getLightBlockInto(levelReader, state, pos, stateAbove, above, Direction.UP, stateAbove.getLightBlock(levelReader, above));
            return light < levelReader.getMaxLightLevel();
        }
    }


    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos pos, BlockState state) {
        return levelReader.getBlockState(pos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();
        BlockState tallGrass = Blocks.GRASS.defaultBlockState();
        Optional<Holder.Reference<PlacedFeature>> optional = level.registryAccess().registryOrThrow(Registries.PLACED_FEATURE).getHolder(VegetationPlacements.GRASS_BONEMEAL);

        label:
        for(int i = 0; i < 128; ++i) {
            BlockPos p = above;

            for(int j = 0; j < i / 16; ++j) {
                p = p.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                if (!level.getBlockState(p.below()).is(this) || level.getBlockState(p).isCollisionShapeFullBlock(level, p)) {
                    continue label;
                }
            }

            BlockState stateAt = level.getBlockState(p);
            if (stateAt.is(tallGrass.getBlock()) && random.nextInt(10) == 0) {
                ((BonemealableBlock)tallGrass.getBlock()).performBonemeal(level, random, p, stateAt);
            }

            if (stateAt.isAir()) {
                Holder<PlacedFeature> featureHolder;
                if (random.nextInt(8) == 0) {
                    List<ConfiguredFeature<?, ?>> flowers = level.getBiome(p).value().getGenerationSettings().getFlowerFeatures();
                    if (flowers.isEmpty()) {
                        continue;
                    }
                    featureHolder = ((RandomPatchConfiguration)flowers.get(0).config()).feature();
                } else {
                    if (optional.isEmpty()) {
                        continue;
                    }
                    featureHolder = optional.get();
                }
                featureHolder.value().place(level, level.getChunkSource().getGenerator(), random, p);
            }
        }
    }
}
