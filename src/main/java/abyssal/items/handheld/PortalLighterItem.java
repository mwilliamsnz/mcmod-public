package abyssal.items.handheld;

import abyssal.init.Gems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Predicate;

public class PortalLighterItem extends ProjectileWeaponItem {


    public PortalLighterItem(Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (stack) -> stack.is(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.ONYX));
    }

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }

    @Override
    protected void shootProjectile(LivingEntity pShooter, Projectile pProjectile, int pIndex, float pVelocity, float pInaccuracy, float pAngle, @Nullable LivingEntity pTarget) {

    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        if (!level.isClientSide()) {
            Player p = ctx.getPlayer();
            BlockState block = ctx.getLevel().getBlockState(ctx.getClickedPos());
            BlockPos pos = ctx.getClickedPos();
            if(block.isPortalFrame(level,pos)) {
                if (inPortalDimension(level)) {
                    for(Direction direction : Direction.values()) {
                        Optional<PortalShape> optional = PortalShape.findEmptyPortalShape(level, pos.relative(direction), Direction.Axis.X);
                        if (optional.isPresent()) {
                            if(p.getAbilities().instabuild) {
                                optional.get().createPortalBlocks(ctx.getLevel());
                                return InteractionResult.CONSUME;
                            }
                            ItemStack powder = p.getProjectile(ctx.getItemInHand());
                            if(powder == ItemStack.EMPTY) {
                                level.playLocalSound(p.getX(),p.getY(),p.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, p.getSoundSource(), 0.8f, 0.8f+level.random.nextFloat()*0.4f, false);
                                return InteractionResult.FAIL;
                            }
                            powder.shrink(1);
                            level.playLocalSound(p.getX(),p.getY(),p.getZ(), SoundEvents.AMETHYST_BLOCK_CHIME, p.getSoundSource(), 0.8f, 0.8f+level.random.nextFloat()*0.4f, false);
                            optional.get().createPortalBlocks(ctx.getLevel());
                            return InteractionResult.CONSUME;
                        }
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static boolean inPortalDimension(Level level) {
        return level.dimension() == Level.OVERWORLD || level.dimension() == Level.NETHER;
    }


}
