package abyssal.init;

import abyssal.Main;
import abyssal.entity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =  DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Main.MOD_ID);

    public static final Supplier<EntityType<PowderBarrelEntity>> POWDER_BARREL = ENTITY_TYPES.register("powder_barrel",
            () -> EntityType.Builder.<PowderBarrelEntity>of(PowderBarrelEntity::new, MobCategory.MISC)
                    .sized(EntityType.TNT.getWidth(), EntityType.TNT.getHeight()).build(key("powder_barrel"))
    );
    public static final Supplier<EntityType<FishPainting>> FISH_PAINTING = ENTITY_TYPES.register("fish_painting_entity",
            () -> EntityType.Builder.<FishPainting>of(FishPainting::new,MobCategory.MISC)
                    .sized(EntityType.PAINTING.getWidth(), EntityType.PAINTING.getHeight()).build(key("fish_painting_entity"))
    );
    public static final Supplier<EntityType<Minion>> MINION = ENTITY_TYPES.register("minion",
            () -> EntityType.Builder.of(Minion::new,MobCategory.MONSTER)
                    .sized(EntityType.SKELETON.getWidth(), EntityType.SKELETON.getHeight()).build(key("minion"))
    );

    public static final Supplier<EntityType<TreeSpider>> TREE_SPIDER = ENTITY_TYPES.register("tree_spider",
            () -> EntityType.Builder.of(TreeSpider::new, MobCategory.MONSTER)
                    .sized(EntityType.CAVE_SPIDER.getWidth(), EntityType.CAVE_SPIDER.getHeight()).build(key("tree_spider"))
    );

    public static final Supplier<EntityType<Wasp>> WASP = ENTITY_TYPES.register("wasp",
            () -> EntityType.Builder.of(Wasp::new, MobCategory.MONSTER)
                    .sized(EntityType.BEE.getWidth(), EntityType.BEE.getHeight()).build(key("wasp"))
    );

    public static final Supplier<EntityType<ChargedEndermite>> CHARGED_ENDERMITE = ENTITY_TYPES.register("charged_endermite",
            () -> EntityType.Builder.of(ChargedEndermite::new, MobCategory.MONSTER)
                    .sized(EntityType.ENDERMITE.getWidth(), EntityType.ENDERMITE.getHeight()).build(key("charged_endermite"))
    );

    private static ResourceKey<EntityType<?>> key(String s) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Main.rl(s));
    }
}
