package abyssal.blocks;

import abyssal.blocks.blockentities.SpiderNestBlockEntity;
import abyssal.init.ModBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class SpiderNestBlock extends BaseEntityBlock {
    public SpiderNestBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<SpiderNestBlock> CODEC = simpleCodec(SpiderNestBlock::new);

    @Override
    public MapCodec<SpiderNestBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpiderNestBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntityTypes.SPIDER_NEST.get(), level.isClientSide() ? SpiderNestBlockEntity::clientTick : SpiderNestBlockEntity::serverTick);
    }

    public RenderShape getRenderShape(BlockState p_56794_) {
        return RenderShape.MODEL;
    }

    @Override
    public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos, BlockEntity blockEntity, Entity breaker, ItemStack tool) {
        return 2 + level.getRandom().nextInt(5) + level.getRandom().nextInt(5);
    }

}
