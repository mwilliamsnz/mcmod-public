package abyssal.blocks;

import abyssal.entity.PowderBarrelEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;

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
    public boolean onCaughtFire(BlockState sdtate, Level worldIn, BlockPos pos, @Nullable net.minecraft.core.Direction face, @Nullable LivingEntity igniter) {
        if (worldIn instanceof ServerLevel serverlevel && serverlevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
            PowderBarrelEntity barrelE = new PowderBarrelEntity(worldIn, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, igniter, size, damageFactor, knockFactor);
            worldIn.addFreshEntity(barrelE);
            worldIn.playSound(null, barrelE.getX(), barrelE.getY(), barrelE.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void wasExploded(ServerLevel level, BlockPos pos, Explosion explosion) {
        if (level.getGameRules().get(GameRules.TNT_EXPLODES)) {
            PowderBarrelEntity barrelE = new PowderBarrelEntity(level, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, explosion.getIndirectSourceEntity(), size, damageFactor, knockFactor);
            barrelE.setFuse((short)(level.getRandom().nextInt(barrelE.getFuse() / 4) + barrelE.getFuse() / 8));
            level.addFreshEntity(barrelE);
        }
    }
}
