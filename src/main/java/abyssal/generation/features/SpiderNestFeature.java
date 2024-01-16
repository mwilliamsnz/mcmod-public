package abyssal.generation.features;


import abyssal.Main;
import abyssal.blocks.blockentities.SpiderNestBlockEntity;
import abyssal.init.ModBlocks;
import abyssal.init.ModEntityTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SpiderNestFeature extends Feature<NoneFeatureConfiguration> {

    private static final float SKIP_CHANCE = 0.3f;
    public SpiderNestFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        RandomSource random = ctx.random();
        BlockPos origin = ctx.origin();
        WorldGenLevel level = ctx.level();
        BlockPos projectedOrigin = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin);

        BlockPos raisedOrigin = projectedOrigin;
        boolean canPlace = false;
        for (int i = 0; i < 16; i++) {
            raisedOrigin = projectedOrigin.above(i);
            if(mayPlaceAt(level, raisedOrigin)) {
                if(random.nextFloat() > SKIP_CHANCE) { // skip sometimes to embed deeper in tree
                    canPlace = true;
                    break;
                }
            }
        }
        if(!canPlace) {
            return false;
        }

        // Successful, actually place the nest

        this.tryPlaceBlock(level, raisedOrigin, ModBlocks.SPIDER_NEST.get().defaultBlockState());
        BlockEntity blockentity = level.getBlockEntity(raisedOrigin);
        if (blockentity instanceof SpiderNestBlockEntity) {
            ((SpiderNestBlockEntity)blockentity).getNest().setEntityId(getSpawnEntity());
        } else {
            Main.LOGGER.error("Failed to fetch mob nest entity at ({}, {}, {})", raisedOrigin.getX(), raisedOrigin.getY(), raisedOrigin.getZ());
        }

        return true;
    }

    private boolean mayPlaceAt(LevelAccessor level, BlockPos pos) {
        return isReplaceable(level, pos) && canHangFrom(level, pos.above());
    }

    private boolean isReplaceable(LevelAccessor level, BlockPos pos) {
        return level.isEmptyBlock(pos) || level.getBlockState(pos).is(BlockTags.LEAVES);
    }

    private boolean canHangFrom(LevelAccessor level, BlockPos pos) {
        return level.getBlockState(pos).is(BlockTags.LOGS) || level.getBlockState(pos).is(BlockTags.LEAVES);
    }

    private void tryPlaceBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        if(this.mayPlaceAt(level, pos)) {
            level.setBlock(pos, state, 4);
        }
    }

    protected EntityType getSpawnEntity() {
        return ModEntityTypes.TREE_SPIDER.get();
    }
}