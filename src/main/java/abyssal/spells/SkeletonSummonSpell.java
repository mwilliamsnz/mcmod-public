package abyssal.spells;

import abyssal.Main;
import abyssal.entity.Minion;
import abyssal.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SkeletonSummonSpell extends Spell {
    protected SkeletonSummonSpell(ResourceLocation key) {
        super(key);
    }

    @Override
    public InteractionResultHolder<ItemStack> cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        if (!(level instanceof ServerLevel)) {
            return InteractionResultHolder.success(staff);
        } else {
            BlockPos blockpos = player.getOnPos();
            AABB box = AABB.ofSize(blockpos.getCenter().subtract(2f,2f,2f), 4f, 4f, 4f);
            box.expandTowards(player.getLookAngle().normalize().scale(3)); // Summon should appear in general direction of facing
            //
            List<BlockPos> validPositions = BlockPos.MutableBlockPos.betweenClosedStream(box).filter((pos -> level.getBlockState(pos).getCollisionShape(level, pos).isEmpty())).toList();
            if(validPositions.isEmpty()) {
                return InteractionResultHolder.fail(staff);
            }
            summon(level, player, validPositions.get((int)(Math.random()* validPositions.size())), staff, false);


//            return InteractionResult.CONSUME;
        }

        return InteractionResultHolder.success(staff);
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
