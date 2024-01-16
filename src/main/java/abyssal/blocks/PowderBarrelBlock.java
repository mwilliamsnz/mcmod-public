package abyssal.blocks;

import abyssal.entity.PowderBarrelEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class PowderBarrelBlock extends TntBlock {

    private final float damageFactor;
    private final float knockFactor;
    private final float size;


    public PowderBarrelBlock(Properties properties, float size, float damageFactor, float knockFactor) {
        super(properties);
        this.size = size;
        this.damageFactor = damageFactor;
        this.knockFactor = knockFactor;
    }
    @Override
    public void onCaughtFire(BlockState state, Level worldIn, BlockPos pos, @Nullable net.minecraft.core.Direction face, @Nullable LivingEntity igniter) {
        if (!worldIn.isClientSide) {
            PowderBarrelEntity barrelE = new PowderBarrelEntity(worldIn, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, igniter, size, damageFactor, knockFactor);
            worldIn.addFreshEntity(barrelE);
            worldIn.playSound(null, barrelE.getX(), barrelE.getY(), barrelE.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn) {
        if (!worldIn.isClientSide) {
            PowderBarrelEntity barrelE = new PowderBarrelEntity(worldIn, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, explosionIn.getIndirectSourceEntity(), size, damageFactor, knockFactor);
            barrelE.setFuse((short)(worldIn.random.nextInt(barrelE.getFuse() / 4) + barrelE.getFuse() / 8));
            worldIn.addFreshEntity(barrelE);
        }
    }
}
