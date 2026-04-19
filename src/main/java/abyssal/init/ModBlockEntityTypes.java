package abyssal.init;

import abyssal.Main;
import abyssal.blocks.blockentities.SpiderNestBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Main.MOD_ID);
    public static final Supplier<BlockEntityType<SpiderNestBlockEntity>> SPIDER_NEST = BLOCK_ENTITY_TYPES.register(
            "spider_nest", ()-> new BlockEntityType<>(SpiderNestBlockEntity::new, ModBlocks.SPIDER_NEST.get()));

}
