package abyssal.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IShearable;

public class PassableSlowingBlock extends BushBlock implements IShearable {
    public PassableSlowingBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<PassableSlowingBlock> CODEC = simpleCodec(PassableSlowingBlock::new);

    @Override
    public MapCodec<PassableSlowingBlock> codec() {
        return CODEC;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        double factor = 0.6;
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(factor, 1.0D, factor));
    }
}