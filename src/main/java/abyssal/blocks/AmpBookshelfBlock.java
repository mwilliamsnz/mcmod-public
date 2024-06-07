package abyssal.blocks;

import abyssal.data.ModTags;
import abyssal.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.extensions.IForgeBlock;

public class AmpBookshelfBlock extends HorizontalDirectionalBlock implements IForgeBlock {

    public AmpBookshelfBlock(Properties properties) {
        super(properties);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
        return 0.5f + (
                amplificationAt(level, pos.below()) + amplificationAt(level, pos.above())
                + amplificationAt(level, pos.relative(facing.getClockWise())) + amplificationAt(level, pos.relative(facing.getCounterClockWise()))
        ) / 6f;
    }

    private static int amplificationAt(LevelReader levelReader, BlockPos pos) {
        BlockState state = levelReader.getBlockState(pos);
        int power = 0;
        if(state.is(Blocks.CHISELED_BOOKSHELF)) {
            BlockEntity e = levelReader.getBlockEntity(pos);
            if(e instanceof ChiseledBookShelfBlockEntity c) {
                for (int i = 0; i < 6; i++) {
                    ItemStack s = c.getItem(i);
                    if (s.isEmpty()) { continue; }
                    power += s.is(ModTags.Items.SHELF_AMPLIFIERS) ? 2 : 1;
                }
            }
        } else if(state.is(ModBlocks.AMP_BOOKSHELF.get())) {
            power += 1;
        } else if(state.is(Tags.Blocks.BOOKSHELVES)) {
            power += 6;
        }
        return power;
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_54779_) {
        return (BlockState)this.defaultBlockState().setValue(FACING, p_54779_.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54794_) {
        p_54794_.add(new Property[]{FACING});
    }

}
