package abyssal.items.handheld;

import abyssal.blocks.CharredLogBlock;
import abyssal.init.ModBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class CharAxe extends AxeItem {

    private final boolean superBurn;
    private final int fireTime;
    public CharAxe(Tier tier, float damage, float attackSpeed, Properties properties, boolean superBurn, int fireTime) {
        super(tier, damage, attackSpeed, properties);
        this.superBurn = superBurn;
        this.fireTime = fireTime;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        BlockState blockstate = level.getBlockState(pos);
        ItemStack inHand = ctx.getItemInHand();
        if(blockstate.is(BlockTags.LOGS_THAT_BURN)) {
            BlockState newState = ModBlocks.CHARRED_LOG.get().defaultBlockState();
            newState = newState.setValue(CharredLogBlock.CHAR_BURNING, superBurn);
            level.setBlock(pos, newState, 11);
            if(superBurn) {
                level.scheduleTick(pos, ModBlocks.CHARRED_LOG.get(), 5);
            }

            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, pos, inHand);
            }

            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
            level.playSound(player, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (player != null) {
                inHand.hurtAndBreak(superBurn ? 10 : 5, player, (p_150686_) -> {
                    p_150686_.broadcastBreakEvent(ctx.getHand());
                });
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        entity.setSecondsOnFire(fireTime);
        return super.onLeftClickEntity(stack, player, entity);
    }
}
