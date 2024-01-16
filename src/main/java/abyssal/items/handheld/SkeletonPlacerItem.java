package abyssal.items.handheld;

import abyssal.Main;
import abyssal.entity.Minion;
import abyssal.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class SkeletonPlacerItem extends Item {

    public SkeletonPlacerItem(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Player p = ctx.getPlayer();
        if(p == null) return InteractionResult.FAIL;

        Level level = ctx.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = ctx.getItemInHand();
            BlockPos blockpos = ctx.getClickedPos();
            Direction direction = ctx.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);

            BlockPos arrivalPos;
            if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
                arrivalPos = blockpos;
            } else {
                arrivalPos = blockpos.relative(direction);
            }

            summon(level, p, arrivalPos, itemstack, !Objects.equals(blockpos, arrivalPos) && direction == Direction.UP);

//            return InteractionResult.CONSUME;
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player p, InteractionHand hand) {
        ItemStack itemstack = p.getItemInHand(hand);

        if (!(level instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemstack);
        } else {
            BlockPos blockpos = p.getOnPos();
            AABB box = AABB.ofSize(blockpos.getCenter().subtract(2f,2f,2f), 4f, 4f, 4f);
            box.expandTowards(p.getLookAngle().normalize().scale(3)); // Summon should appear in general direction of facing
            List<BlockPos> validPositions = BlockPos.MutableBlockPos.betweenClosedStream(box).filter((pos -> level.getBlockState(pos).getCollisionShape(level, pos).isEmpty())).toList();
            if(validPositions.isEmpty()) {
                return InteractionResultHolder.fail(itemstack);
            }
            summon(level, p, validPositions.get((int)(Math.random()* validPositions.size())), itemstack, false);


//            return InteractionResult.CONSUME;
        }

        return InteractionResultHolder.success(itemstack);
    }

    private void summon(Level level, Player p, BlockPos pos, ItemStack stack, boolean expandAABB) {
        Minion e = ModEntityTypes.MINION.get().spawn((ServerLevel)level, stack, p, pos, MobSpawnType.SPAWN_EGG, true, expandAABB);
        if (e != null) {
            e.summonerUUID = p.getUUID();
            Main.LOGGER.info("Summoned by " + e.summonerUUID);
            level.gameEvent(p, GameEvent.ENTITY_PLACE, pos);
        }
    }
}
