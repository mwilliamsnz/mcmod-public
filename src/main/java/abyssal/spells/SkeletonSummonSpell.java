package abyssal.spells;

import abyssal.Main;
import abyssal.entity.Minion;
import abyssal.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SkeletonSummonSpell extends Spell {
    protected SkeletonSummonSpell(Identifier key, SpellFuelQuantity cost) {
        super(key, cost);
    }

    @Override
    public InteractionResult cast(Level level, Player player, ItemStack staff, ItemStack book, double ap) {
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            BlockPos blockpos = player.getOnPos();
            AABB box = AABB.ofSize(blockpos.getCenter(), 4f, 5f, 4f);
            box.expandTowards(player.getLookAngle().normalize().scale(3)); // Summon should appear in general direction of facing
            List<BlockPos> validPositions = BlockPos.MutableBlockPos.betweenClosedStream(box).filter((pos -> level.getBlockState(pos).getCollisionShape(level, pos).isEmpty())).toList();
            if(validPositions.isEmpty()) {
                return InteractionResult.FAIL;
            }
            summon(level, player, validPositions.get((int)(Math.random()* validPositions.size())), staff, false);
        }
        return InteractionResult.SUCCESS;
    }

    private void summon(Level level, Player p, BlockPos pos, ItemStack stack, boolean expandAABB) {
        Minion e = ModEntityTypes.MINION.get().spawn((ServerLevel)level, stack, p, pos, EntitySpawnReason.MOB_SUMMONED, true, expandAABB);
        if (e != null) {
            e.summonerUUID = p.getUUID();
            level.gameEvent(p, GameEvent.ENTITY_PLACE, pos);
        }
    }
}
