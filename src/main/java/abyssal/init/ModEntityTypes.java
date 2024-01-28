package abyssal.init;

import abyssal.Main;
import abyssal.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =  DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Main.MOD_ID);

    public static final RegistryObject<EntityType<PowderBarrelEntity>> POWDER_BARREL = ENTITY_TYPES.register("powder_barrel",
            () -> EntityType.Builder.<PowderBarrelEntity>of(PowderBarrelEntity::new, MobCategory.MISC)
                    .sized(EntityType.TNT.getWidth(), EntityType.TNT.getHeight()).build(new ResourceLocation(Main.MOD_ID, "powder_barrel").toString())
    );
    public static final RegistryObject<EntityType<FishPainting>> FISH_PAINTING = ENTITY_TYPES.register("fish_painting_entity",
            () -> EntityType.Builder.<FishPainting>of(FishPainting::new,MobCategory.MISC)
                    .sized(EntityType.PAINTING.getWidth(), EntityType.PAINTING.getHeight()).build(new ResourceLocation(Main.MOD_ID, "fish_painting_entity").toString())
    );
    public static final RegistryObject<EntityType<Minion>> MINION = ENTITY_TYPES.register("minion",
            () -> EntityType.Builder.of(Minion::new,MobCategory.MONSTER)
                    .sized(EntityType.SKELETON.getWidth(), EntityType.SKELETON.getHeight()).build(new ResourceLocation(Main.MOD_ID, "minion").toString())
    );

    public static final RegistryObject<EntityType<TreeSpider>> TREE_SPIDER = ENTITY_TYPES.register("tree_spider",
            () -> EntityType.Builder.of(TreeSpider::new, MobCategory.MONSTER)
                    .sized(EntityType.CAVE_SPIDER.getWidth(), EntityType.CAVE_SPIDER.getHeight()).build(new ResourceLocation(Main.MOD_ID, "tree_spider").toString())
    );

    public static final RegistryObject<EntityType<ChargedEndermite>> CHARGED_ENDERMITE = ENTITY_TYPES.register("charged_endermite",
            () -> EntityType.Builder.of(ChargedEndermite::new, MobCategory.MONSTER)
                    .sized(EntityType.ENDERMITE.getWidth(), EntityType.ENDERMITE.getHeight()).build(new ResourceLocation(Main.MOD_ID, "charged_endermite").toString())
    );
}
