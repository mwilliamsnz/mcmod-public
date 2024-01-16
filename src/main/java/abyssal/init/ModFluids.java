package abyssal.init;

import abyssal.Main;
import abyssal.blocks.IchorFluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class ModFluids {

    public static final DeferredRegister<Fluid> FLUIDS =  DeferredRegister.create(ForgeRegistries.FLUIDS, Main.MOD_ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES =  DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Main.MOD_ID);

    public static final RegistryObject<Fluid> ICHOR_FLUID = FLUIDS.register("ichor", IchorFluid.Source::new);
    public static final RegistryObject<Fluid> FLOWING_ICHOR_FLUID = FLUIDS.register("flowing_ichor", IchorFluid.Flowing::new);

    public static final RegistryObject<FluidType> ICHOR_TYPE = FLUID_TYPES.register("ichor", () ->
            new FluidType(FluidType.Properties.create()
                    .descriptionId("block.abyssal.ichor")
                    .canSwim(false)
                    .canDrown(true)
                    .pathType(BlockPathTypes.LAVA)
                    .adjacentPathType(null)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                    .lightLevel(4)
                    .density(3000)
                    .viscosity(6000)
                    .temperature(-50))
            {
                @Override
                public double motionScale(Entity entity)
                {
                    return entity.level().dimensionType().ultraWarm() ? 0.007D : 0.0023333333333333335D;
                }

                @Override
                public void setItemMovement(ItemEntity entity)
                {
                    Vec3 vec3 = entity.getDeltaMovement();
                    entity.setDeltaMovement(vec3.x * (double)0.95F, vec3.y + (double)(vec3.y < (double)0.06F ? 5.0E-4F : 0.0F), vec3.z * (double)0.95F);
                }

                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
                {
                    consumer.accept(new IClientFluidTypeExtensions()
                    {
                        private static final ResourceLocation ICHOR_STILL = new ResourceLocation(Main.MOD_ID, "block/ichor_still"),
                                ICHOR_FLOW = new ResourceLocation(Main.MOD_ID, "block/ichor_flow");

                        @Override
                        public ResourceLocation getStillTexture()
                        {
                            return ICHOR_STILL;
                        }

                        @Override
                        public ResourceLocation getFlowingTexture()
                        {
                            return ICHOR_FLOW;
                        }
                    });
                }
            });
}
