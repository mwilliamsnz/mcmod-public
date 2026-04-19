package abyssal.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GroundCoverBlock extends BushBlock {
    protected final VoxelShape shape;

    public static final MapCodec<GroundCoverBlock> CODEC = simpleCodec(GroundCoverBlock::new);

//    @Override
//    public MapCodec<GroundCoverBlock> codec() {
//        return CODEC;
//    }

    public GroundCoverBlock(BlockBehaviour.Properties p_53514_) {
        super(p_53514_);
        shape = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
//        Vec3 vec3 = state.getOffset(getter, pos);
//        return shape.move(vec3.x, vec3.y, vec3.z);
        return shape;
    }

}