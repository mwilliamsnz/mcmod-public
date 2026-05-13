package abyssal.blocks;

import abyssal.blocks.blockentities.HiveheartBlockEntity;
import abyssal.init.ModBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class HiveheartBlock extends BaseEntityBlock {

    public static final MapCodec<HiveheartBlock> CODEC = simpleCodec(HiveheartBlock::new);
    @Override
    public MapCodec<HiveheartBlock> codec() {
        return CODEC;
    }

    public HiveheartBlock(Properties props) {
        super(props);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new HiveheartBlockEntity(worldPosition, blockState);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntityTypes.HIVEHEART.get(), HiveheartBlockEntity::serverTick);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof HiveheartBlockEntity portBE) {
            portBE.findSuccessor();
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

}
