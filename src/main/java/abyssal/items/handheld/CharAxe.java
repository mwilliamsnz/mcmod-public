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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class CharAxe extends AxeItem {

    private final boolean superBurn;
    private final int fireTime;
    public CharAxe(ToolMaterial material, float damage, float speed, Properties properties, boolean superBurn, int fireTime) {
        super(material, damage, speed, properties);
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
            level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (player != null) {
                inHand.hurtAndBreak(superBurn ? 10 : 5, player, ctx.getHand());
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        entity.igniteForSeconds(fireTime);
        return super.onLeftClickEntity(stack, player, entity);
    }
}
