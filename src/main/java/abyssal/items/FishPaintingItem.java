package abyssal.items;

import abyssal.entity.FishPainting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Optional;

public class FishPaintingItem extends Item {

    public FishPaintingItem(Properties props) {
        super(props);
    }

    public InteractionResult useOn(UseOnContext ctx) {
        BlockPos clickPos = ctx.getClickedPos();
        Direction direction = ctx.getClickedFace();
        BlockPos placePos = clickPos.relative(direction);
        Player player = ctx.getPlayer();
        ItemStack itemstack = ctx.getItemInHand();
        if (player != null && !this.mayPlace(player, direction, itemstack, placePos)) {
            return InteractionResult.FAIL;
        } else {
            Level level = ctx.getLevel();
            Optional<FishPainting> optional = FishPainting.create(level, placePos, direction);
            if (optional.isEmpty()) {
                return InteractionResult.CONSUME;
            }
            HangingEntity hangingentity = optional.get();
            CompoundTag tag = itemstack.getTag();
            if (tag != null) {
                EntityType.updateCustomEntityTag(level, player, hangingentity, tag);
            }
            if (hangingentity.survives()) {
                if (!level.isClientSide) {
                    hangingentity.playPlacementSound();
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, clickPos);
                    level.addFreshEntity(hangingentity);
                }

                itemstack.shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.CONSUME;
            }
        }
    }

    protected boolean mayPlace(Player player, Direction direction, ItemStack itemStack, BlockPos blockPos) {
        return !direction.getAxis().isVertical() && player.mayUseItemAt(blockPos, direction, itemStack);
    }

}
