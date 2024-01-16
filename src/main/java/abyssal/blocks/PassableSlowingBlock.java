package abyssal.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PassableSlowingBlock extends BushBlock implements net.minecraftforge.common.IForgeShearable  {
    public PassableSlowingBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        double factor = 0.6;
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(factor, 1.0D, factor));
    }
}