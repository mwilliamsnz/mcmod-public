package abyssal.init;

import abyssal.Main;
import abyssal.blocks.blockentities.HiveOrganBlockEntity;
import abyssal.blocks.blockentities.SpiderNestBlockEntity;
import abyssal.blocks.blockentities.HiveheartBlockEntity;
import abyssal.blocks.blockentities.WaspPortBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Main.MOD_ID);
    public static final Supplier<BlockEntityType<SpiderNestBlockEntity>> SPIDER_NEST = BLOCK_ENTITY_TYPES.register(
            "spider_nest", ()-> new BlockEntityType<>(SpiderNestBlockEntity::new, ModBlocks.SPIDER_NEST.get()));
    public static final Supplier<BlockEntityType<HiveheartBlockEntity>> HIVEHEART = BLOCK_ENTITY_TYPES.register(
            "hiveheart", ()-> new BlockEntityType<>(HiveheartBlockEntity::new, ModBlocks.HIVEHEART.get()));
    public static final Supplier<BlockEntityType<HiveOrganBlockEntity>> HIVE_ORGAN = BLOCK_ENTITY_TYPES.register(
            "hiveheart_secondary", ()-> new BlockEntityType<>(HiveOrganBlockEntity::new, ModBlocks.HIVEHEART_DUMMY.get()));
    public static final Supplier<BlockEntityType<WaspPortBlockEntity>> WASP_PORT = BLOCK_ENTITY_TYPES.register(
            "wasp_port", ()-> new BlockEntityType<>(WaspPortBlockEntity::new, ModBlocks.WASP_PORT.get()));

}
