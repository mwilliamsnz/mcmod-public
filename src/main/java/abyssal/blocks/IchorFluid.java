package abyssal.blocks;

import abyssal.init.ModBlocks;
import abyssal.init.ModFluids;
import abyssal.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public abstract class IchorFluid extends FlowingFluid {
    public static final float MIN_LEVEL_CUTOFF = 0.44444445F;

    @Override
    public net.minecraftforge.fluids.FluidType getFluidType() {
        return ModFluids.ICHOR_TYPE.get();
    }

    public Fluid getFlowing() {
        System.out.println("Getting flowing from method #1");
        return ModFluids.ICHOR_FLUID.get();
    }

    public FluidState getFlowing(int p_75954_, boolean p_75955_) {
        System.out.println("Getting flowing from method #2 (" + p_75954_ + ")");
        return this.getFlowing().defaultFluidState().setValue(LEVEL, p_75954_).setValue(FALLING, p_75955_);
    }

    public Fluid getSource() {
        return ModFluids.FLOWING_ICHOR_FLUID.get();
    }

    public Item getBucket() {
        return ModItems.ICHOR_BUCKET.get();
    }

    public void animateTick(Level lvl, BlockPos pos, FluidState state, Random rand) {
        BlockPos blockpos = pos.above();
        if (lvl.getBlockState(blockpos).isAir() && !lvl.getBlockState(blockpos).isSolidRender(lvl, blockpos)) {
            if (rand.nextInt(100) == 0) {
                double d0 = (double)pos.getX() + rand.nextDouble();
                double d1 = (double)pos.getY() + 1.0D;
                double d2 = (double)pos.getZ() + rand.nextDouble();
                lvl.addParticle(ParticleTypes.ASH, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                lvl.playLocalSound(d0, d1, d2, SoundEvents.AMETHYST_CLUSTER_PLACE, SoundSource.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }

            if (rand.nextInt(200) == 0) {
                lvl.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }
        }

    }

    @Nullable
    public ParticleOptions getDripParticle() {
        return ParticleTypes.SQUID_INK;
    }

    protected void beforeDestroyingBlock(LevelAccessor p_76216_, BlockPos p_76217_, BlockState p_76218_) {
        this.fizz(p_76216_, p_76217_);
    }

    private static boolean isAbyss(LevelReader levelReader) {
        return levelReader.dimensionType().hasCeiling() && !levelReader.dimensionType().ultraWarm();
    }

    public int getSlopeFindDistance(LevelReader levelReader) {
        return isAbyss(levelReader) ? 4 : 2;
    }

    public BlockState createLegacyBlock(FluidState p_76249_) {
        return ModBlocks.ICHOR.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(p_76249_));
    }

    public boolean isSame(Fluid fluid) {
        return fluid == ModFluids.ICHOR_FLUID.get() || fluid == ModFluids.FLOWING_ICHOR_FLUID.get();
    }

    public int getDropOff(LevelReader levelReader) {
        return isAbyss(levelReader) ? 1 : 2;
    }

    public boolean canBeReplacedWith(FluidState fluidState, BlockGetter p_76234_, BlockPos pos, Fluid fluid, Direction dir) {
        return fluidState.getHeight(p_76234_, pos) >= MIN_LEVEL_CUTOFF && fluid.is(FluidTags.WATER);
    }

    public int getTickDelay(LevelReader levelReader) {
        return isAbyss(levelReader) ? 10 : 30;
    }

    public int getSpreadDelay(Level p_76203_, BlockPos p_76204_, FluidState p_76205_, FluidState p_76206_) {
        int i = this.getTickDelay(p_76203_);
        if (!p_76205_.isEmpty() && !p_76206_.isEmpty() && !p_76205_.getValue(FALLING) && !p_76206_.getValue(FALLING) && p_76206_.getHeight(p_76203_, p_76204_) > p_76205_.getHeight(p_76203_, p_76204_) && p_76203_.getRandom().nextInt(4) != 0) {
            i *= 4;
        }

        return i;
    }

    private void fizz(LevelAccessor p_76213_, BlockPos p_76214_) {
        p_76213_.levelEvent(1501, p_76214_, 0);
    }

    protected boolean canConvertToSource() {
        return false;
    }

    @Override
    protected void spreadTo(LevelAccessor p_76220_, BlockPos p_76221_, BlockState p_76222_, Direction p_76223_, FluidState p_76224_) {
        if (p_76223_ == Direction.DOWN) {
            FluidState fluidstate = p_76220_.getFluidState(p_76221_);
            if (fluidstate.is(FluidTags.WATER)) {
                if (p_76222_.getBlock() instanceof LiquidBlock) {
                    p_76220_.setBlock(p_76221_, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(p_76220_, p_76221_, p_76221_, ModBlocks.ABYSSAL_STONE.get().defaultBlockState()), 3);
                }

                this.fizz(p_76220_, p_76221_);
                return;
            }
        }

        super.spreadTo(p_76220_, p_76221_, p_76222_, p_76223_, p_76224_);
    }

    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
    }

    public static class Flowing extends IchorFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);

            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

        public Fluid getFlowing() {
            return ModFluids.FLOWING_ICHOR_FLUID.get();
        }

        public FluidState getFlowing(int p_75954_, boolean p_75955_) {
            FluidState fs = this.getFlowing().defaultFluidState().setValue(LEVEL, p_75954_).setValue(FALLING, p_75955_);
            return fs;
        }

        public Fluid getSource() {
            return ModFluids.ICHOR_FLUID.get();
        }

    @Override
    protected boolean canConvertToSource(Level level) {
        return false;
    }
}

    public static class Source extends IchorFluid {
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

        public Fluid getFlowing() {
            return ModFluids.FLOWING_ICHOR_FLUID.get();
        }

        public FluidState getFlowing(int p_75954_, boolean p_75955_) {
            return this.getFlowing().defaultFluidState().setValue(LEVEL, p_75954_).setValue(FALLING, p_75955_);
        }

        public Fluid getSource() {
            return ModFluids.ICHOR_FLUID.get();
        }

        @Override
        protected boolean canConvertToSource(Level level) {
            return false;
        }
    }
}
