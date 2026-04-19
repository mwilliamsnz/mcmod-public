package abyssal.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.IShearable;

public class PassableSlowingBlock extends BushBlock implements IShearable {
    public PassableSlowingBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<PassableSlowingBlock> CODEC = simpleCodec(PassableSlowingBlock::new);

//    @Override
//    public MapCodec<PassableSlowingBlock> codec() {
//        return CODEC;
//    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier p_405438_) {
        double factor = 0.6;
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(factor, 1.0D, factor));
    }
}