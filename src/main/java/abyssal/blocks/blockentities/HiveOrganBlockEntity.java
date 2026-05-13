package abyssal.blocks.blockentities;

import abyssal.init.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class HiveOrganBlockEntity extends BlockEntity {

    public BlockPos master;

    public HiveOrganBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.HIVE_ORGAN.get(), pos, state);
    }

    public void linkToHeart(BlockPos pos) {
        master = pos;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.master = input.read("master", BlockPos.CODEC).orElse(null);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.storeNullable("master", BlockPos.CODEC, master);
    }

    public String debugString() {
        return "master=" + master;
    }
}
