package abyssal.generation.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class OutcropFeature extends Feature<BlockStateConfiguration> {
    public OutcropFeature(Codec<BlockStateConfiguration> configurationCodec) {
        super(configurationCodec);
    }

    public boolean place(FeaturePlaceContext<BlockStateConfiguration> ctx) {
        BlockPos origin = ctx.origin();
        WorldGenLevel level = ctx.level();
        RandomSource rand = ctx.random();

        BlockStateConfiguration bsc = ctx.config();
        while (origin.getY() > level.getMinY() + 10) {
            if (!level.isEmptyBlock(origin.below())) {
                BlockState stateBelow = level.getBlockState(origin.below());
                if (isDirt(stateBelow) || isStone(stateBelow)) {
                    break;
                }
            }
            origin = origin.below();
        }

        if (origin.getY() <= level.getMinY() + 10) {
            return false;
        } else {
            int lumps = rand.nextInt(5) -3;
            for(int blob = 0; blob < lumps; ++blob) {
                int dx = rand.nextInt(2);
                int dy = rand.nextInt(3);
                int dz = rand.nextInt(2);
                float r = (float)(dx + dy + dz) * 0.333F + 0.5F;

                double centreX = origin.getX() + rand.nextFloat() - 0.5;
                double centreY = origin.getY() + rand.nextFloat() - 0.5;
                double centreZ = origin.getZ() + rand.nextFloat() - 0.5;

                for(BlockPos candidatePos : BlockPos.betweenClosed(origin.offset(-dx, -dy, -dz), origin.offset(dx, dy, dz))) {
                    double distx = candidatePos.getX() - centreX;
                    double disty = candidatePos.getY() - centreY;
                    double distz = candidatePos.getZ() - centreZ;
                    double distSqr = distx*distx + disty*disty + distz*distz;
                    if (distSqr <= (double)(r * r)) {
                        level.setBlock(candidatePos, bsc.state, 4);
                    }
                }

                origin = origin.offset(-1 + rand.nextInt(3), 0, -1 + rand.nextInt(3));
            }
            origin = origin.below();
            for(int blob = 0; blob < 4; ++blob) {
                int dx = rand.nextInt(3) + 1;
                int dy = rand.nextInt(3);
                int dz = rand.nextInt(3) + 1;
                float r = (float)(dx + dy + dz) * 0.333F + 0.5F;

                double centreX = origin.getX() + rand.nextFloat() - 0.5;
                double centreY = origin.getY() + rand.nextFloat() - 0.5;
                double centreZ = origin.getZ() + rand.nextFloat() - 0.5;

                for(BlockPos candidatePos : BlockPos.betweenClosed(origin.offset(-dx, -dy, -dz), origin.offset(dx, dy, dz))) {
                    double distx = candidatePos.getX() - centreX;
                    double disty = candidatePos.getY() - centreY;
                    double distz = candidatePos.getZ() - centreZ;
                    double distSqr = distx*distx + 2*disty*disty + distz*distz;
                    if (distSqr <= (double)(r * r)) {
                        level.setBlock(candidatePos, bsc.state, 4);
                    }
                }

                origin = origin.offset(-2 + rand.nextInt(5), -rand.nextInt(2), -2 + rand.nextInt(5));
            }
            for(int blob = 0; blob < 2; ++blob) {
                int dx = rand.nextInt(3) + 2;
                int dy = rand.nextInt(2);
                int dz = rand.nextInt(3) + 2;
                float r = (float)(dx + dy + dz) * 0.333F + 0.5F;

                for(BlockPos candidatePos : BlockPos.betweenClosed(origin.offset(-dx, -dy, -dz), origin.offset(dx, dy, dz))) {
                    double distx = candidatePos.getX() - origin.getX();
                    double disty = candidatePos.getY() - origin.getY();
                    double distz = candidatePos.getZ() - origin.getZ();
                    double distSqr = distx*distx + 2*disty*disty + distz*distz;
                    if (distSqr <= (double)(r * r)) {
                        level.setBlock(candidatePos, bsc.state, 4);
                    }
                }

                origin = origin.offset(-1 + rand.nextInt(3), -1, -1 + rand.nextInt(3));
            }

            return true;
        }
    }

    private boolean isStone(BlockState stateBelow) {
        return stateBelow.is(BlockTags.STONE_ORE_REPLACEABLES);
    }

    private boolean isDirt(BlockState stateBelow) {
        return stateBelow.is(BlockTags.SUBSTRATE_OVERWORLD);
    }
}