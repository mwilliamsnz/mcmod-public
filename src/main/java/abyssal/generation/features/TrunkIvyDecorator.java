package abyssal.generation.features;


import abyssal.init.ModBlocks;
import abyssal.init.ModGeneration;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class TrunkIvyDecorator extends TreeDecorator {

    public static final MapCodec<TrunkIvyDecorator> CODEC = MapCodec.unit(() -> {
        return TrunkIvyDecorator.INSTANCE;
    });
    public static final TrunkIvyDecorator INSTANCE = new TrunkIvyDecorator();

    protected TreeDecoratorType<?> type() {
        return ModGeneration.TREE_IVY.get();
    }

    @Override
    public void place(Context ctx) {
        RandomSource rand = ctx.random();
        ctx.logs().forEach((logPos) -> {
            if (rand.nextInt(3) > 0) {
                BlockPos w = logPos.west();
                if (ctx.isAir(w)) {
                    placeIvy(ctx, w, VineBlock.EAST);
                }
            }

            if (rand.nextInt(3) > 0) {
                BlockPos e = logPos.east();
                if (ctx.isAir(e)) {
                    placeIvy(ctx, e, VineBlock.WEST);
                }
            }

            if (rand.nextInt(3) > 0) {
                BlockPos n = logPos.north();
                if (ctx.isAir(n)) {
                    placeIvy(ctx, n, VineBlock.SOUTH);
                }
            }

            if (rand.nextInt(3) > 0) {
                BlockPos s = logPos.south();
                if (ctx.isAir(s)) {
                    placeIvy(ctx, s, VineBlock.NORTH);
                }
            }

        });
    }
    protected static void placeIvy(Context ctx, BlockPos pos, BooleanProperty direction) {
        ctx.setBlock(pos, ModBlocks.IVY.get().defaultBlockState().setValue(direction, true));
    }

}