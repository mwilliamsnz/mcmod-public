package abyssal.init;

import abyssal.Main;
import abyssal.blocks.blockentities.SpiderNestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MOD_ID);
    public static final RegistryObject<BlockEntityType<SpiderNestBlockEntity>> SPIDER_NEST = BLOCK_ENTITY_TYPES.register(
            "spider_nest", ()-> BlockEntityType.Builder.of(SpiderNestBlockEntity::new, ModBlocks.SPIDER_NEST.get()).build(null));

}
