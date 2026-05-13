package abyssal.items;

import abyssal.init.ModBlockEntityTypes;
import abyssal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DebugWand extends Item {

    public DebugWand(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        BlockState blockstate = level.getBlockState(pos);
        ItemStack inHand = ctx.getItemInHand();
        if(!(level instanceof ServerLevel)) return InteractionResult.PASS;
        if(blockstate.is(ModBlocks.HIVEHEART)) {
            level.getBlockEntity(pos, ModBlockEntityTypes.HIVEHEART.get()).ifPresent(be -> {
                player.sendSystemMessage(Component.literal("True heart: " + be.debugString()));
            });
            level.playSound(null, pos, SoundEvents.VILLAGER_HURT, SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        } else if(blockstate.is(ModBlocks.HIVEHEART_DUMMY)) {
            level.getBlockEntity(pos, ModBlockEntityTypes.HIVE_ORGAN.get()).ifPresent(be -> {
                player.sendSystemMessage(Component.literal("Dummy heart: " + be.debugString()));
            });
            level.playSound(null, pos, SoundEvents.VILLAGER_HURT, SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        } else if(blockstate.is(ModBlocks.WASP_PORT)) {
            level.getBlockEntity(pos, ModBlockEntityTypes.WASP_PORT.get()).ifPresent(be -> {
                player.sendSystemMessage(Component.literal("Port occupants: " + be.getOccupantCount() + ", occupancy " + be.getEstimatedOccupancy()));
            });
            level.playSound(null, pos, SoundEvents.VILLAGER_HURT, SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }
}
