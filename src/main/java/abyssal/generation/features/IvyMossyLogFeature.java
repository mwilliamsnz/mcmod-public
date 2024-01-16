package abyssal.generation.features;


import abyssal.init.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;


// TODO: this should probably be a configuration of the base log
public class IvyMossyLogFeature extends Feature<NoneFeatureConfiguration> {
    public IvyMossyLogFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        RandomSource random = ctx.random();
        BlockPos origin = ctx.origin();//.offset(random.nextInt(16),0,random.nextInt(16));
        WorldGenLevel level = ctx.level();
        BlockPos projectedOrigin = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin);

        float r = random.nextFloat();
        Direction dir;
        if(r < 0.25) {
            dir = Direction.NORTH;
        } else if(r < 0.50) {
            dir = Direction.SOUTH;
        } else if(r < 0.75) {
            dir = Direction.EAST;
        } else {
            dir = Direction.WEST;
        }

        if(!mayPlaceAt(level, projectedOrigin, false)) {
            return false;
        }
        BlockPos placePos = origin.relative(dir).relative(dir);
        Direction displace = Direction.getRandom(random);
        if(displace != Direction.UP && displace != Direction.DOWN) {
            placePos = placePos.relative(displace);
        }
        placePos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, placePos);
        if(placePos.getY() > projectedOrigin.getY() + 1) {
            return false;
        }

        int placed = 0;
        BlockPos testPos = placePos;
        int overSolid = 0;
        for(; placed < 6; placed++) {
            if(!mayPlaceAt(level, testPos, true)) {
                break;
            }
            if(mayPlaceAt(level, testPos, false)) {
                overSolid = placed + 1; // Bridge over gaps is fine
            }
            // Chance:
            // Len: 1  2  3  4   5   6
            // >=:  1  1  1 2/3 1/3 1/9
            // ==:( 0  0  3  3   2   1  ) in 9
            if(placed >= 2 && random.nextFloat() < placed/6.0) {
                placed++;
                break;
            }
            testPos = testPos.relative(dir);
        }
        placed = Math.min(overSolid * 2, placed); // No unbalanced logs
        if(placed < 3) {
            return false;
        }

        // Successful, actually place the log

        float stumpRand = random.nextFloat();
        if (stumpRand < 0.1) {
            this.tryPlaceBlock(level, projectedOrigin, Blocks.OAK_LOG.defaultBlockState(), false);
            this.tryPlaceBlock(level, projectedOrigin.above(), ModBlocks.MOSSY_OAK.get().defaultBlockState(), false);
            projectedOrigin = projectedOrigin.above();
        } else if (stumpRand < 0.2) {
            this.tryPlaceBlock(level, projectedOrigin, Blocks.OAK_LOG.defaultBlockState(), false);
        } else if (stumpRand < 0.5) {
            this.tryPlaceBlock(level, projectedOrigin, ModBlocks.MOSSY_OAK.get().defaultBlockState(), false);
        }
        float topTypeRand = random.nextFloat();
        if(topTypeRand < 0.2 && stumpRand < 0.5) {
            this.tryPlaceBlock(level, projectedOrigin.above(), Blocks.MOSS_CARPET.defaultBlockState(), false);
        } else if(topTypeRand < 0.5 && stumpRand < 0.5) {
            this.tryPlaceBlock(level, projectedOrigin.above(), Blocks.RED_MUSHROOM.defaultBlockState(), false);
        }

        for(int i = 0; i < overSolid; i++) {
            if(random.nextFloat() < 0.80) {
                this.tryPlaceBlock(level, placePos, ModBlocks.MOSSY_OAK.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, dir.getAxis()), true);
                topTypeRand = random.nextFloat();
                if(topTypeRand < 0.5) {
                    this.tryPlaceBlock(level, placePos.above(), Blocks.MOSS_CARPET.defaultBlockState(), false);
                } else if(topTypeRand < 0.6) {
                    this.tryPlaceBlock(level, placePos.above(), Blocks.RED_MUSHROOM.defaultBlockState(), false);
                }
            } else {
                this.tryPlaceBlock(level, placePos, Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, dir.getAxis()), true);
                if(random.nextFloat() < 0.7) {
                    Direction ivyDir = dir.getClockWise();
                    BooleanProperty prop = VineBlock.getPropertyForFace(ivyDir.getOpposite());
                    this.tryPlaceBlock(level, placePos.relative(ivyDir), ModBlocks.IVY.get().defaultBlockState().setValue(prop, true), true);
                }
                if(random.nextFloat() < 0.7) {
                    Direction ivyDir = dir.getCounterClockWise();
                    BooleanProperty prop = VineBlock.getPropertyForFace(ivyDir.getOpposite());
                    this.tryPlaceBlock(level, placePos.relative(ivyDir), ModBlocks.IVY.get().defaultBlockState().setValue(prop, true), true);
                }
            }
            placePos = placePos.relative(dir);
        }

        return true;
    }

    private boolean mayPlaceAt(LevelAccessor level, BlockPos pos, boolean overAirFine) {
        return level.isEmptyBlock(pos) && ( (overAirFine && level.getBlockState(pos.below()).isAir()) || level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP));
    }

    private void tryPlaceBlock(LevelAccessor level, BlockPos pos, BlockState state, boolean overAirFine) {
        if(this.mayPlaceAt(level, pos, overAirFine)) {
            level.setBlock(pos, state, 4);
        }
    }
}