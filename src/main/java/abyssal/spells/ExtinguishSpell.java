package abyssal.spells;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ExtinguishSpell extends Spell {

    protected ExtinguishSpell(ResourceLocation key) {
        super(key);
    }

    @Override
    public InteractionResultHolder<ItemStack> cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        final double radius = Math.min(20, Math.cbrt(64 +  4*ap)); // cap at 20m for max of 41^3 < 70k blocks at ~2000 AP. Adds around 16 blocks per 1 AP.
        double rsq = radius*radius;
        Vec3 pp = player.getEyePosition();
        AABB aabb = AABB.ofSize(pp, 2*radius, 2*radius, 2*radius);
        level.getEntities(null, aabb).forEach(((entity -> {
            if(entity.distanceTo(player) < radius) {
                entity.clearFire();
            }
        })));

        BlockPos.MutableBlockPos.betweenClosedStream(aabb).forEach((pos) -> {
            if(pos.distToCenterSqr(pp.x, pp.y, pp.z) < rsq) {
                BlockState state = level.getBlockState(pos);
                if(state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)) {
                    level.removeBlock(pos, false);
                }
                // TODO (soul) campfires, candles, possibly torches/lanterns
            }
        });

        return InteractionResultHolder.success(staff);
    }
}
