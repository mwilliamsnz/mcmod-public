package abyssal.blocks;

import abyssal.Main;
import abyssal.blocks.blockentities.HiveOrganBlockEntity;
import abyssal.init.ModBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class HiveOrganBlock extends BaseEntityBlock {

    public static final MapCodec<HiveOrganBlock> CODEC = simpleCodec(HiveOrganBlock::new);
    @Override
    public MapCodec<HiveOrganBlock> codec() {
        return CODEC;
    }

    public HiveOrganBlock(Properties props) {
        super(props);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new HiveOrganBlockEntity(worldPosition, blockState);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        level.getBlockEntity(pos, ModBlockEntityTypes.HIVE_ORGAN.get()).ifPresent(be -> {
            if(be.master == null) {
                Main.LOGGER.info("Slave hive has no master!");
                return;
            }
            level.getBlockEntity(be.master, ModBlockEntityTypes.HIVEHEART.get()).ifPresent(hive -> {
                hive.removeOrgan(pos);
            });
        });

        return super.playerWillDestroy(level, pos, state, player);
    }

}
