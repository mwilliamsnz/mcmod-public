package abyssal.blocks;

import abyssal.blocks.blockentities.WaspPortBlockEntity;
import abyssal.entity.Wasp;
import abyssal.init.ModBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Util;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;

public class WaspPortBlock extends BaseEntityBlock {
    public static final MapCodec<WaspPortBlock> CODEC = simpleCodec(WaspPortBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    @Override
    public MapCodec<WaspPortBlock> codec() {
        return CODEC;
    }

    public WaspPortBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack destroyedWith) {
        super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
        if (!level.isClientSide() && blockEntity instanceof WaspPortBlockEntity portBE) {
            if (!EnchantmentHelper.hasTag(destroyedWith, EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING)) {
                portBE.emptyAllLivingFromPort(player, state, WaspPortBlockEntity.WaspReleaseStatus.EMERGENCY);
                Containers.updateNeighboursAfterDestroy(state, level, pos);
            }
            this.angerNearbyWasps(level, pos);
            portBE.notifyRemoval();
//            CriteriaTriggers.BEE_NEST_DESTROYED.trigger((ServerPlayer)player, state, destroyedWith, portBE.getOccupantCount());
        }
    }

    @Override
    protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
        super.onExplosionHit(state, level, pos, explosion, onHit);
        this.angerNearbyWasps(level, pos);
    }

    private void angerNearbyWasps(Level level, BlockPos pos) {
        AABB surroundings = new AABB(pos).inflate(12.0, 6.0, 12.0);
        List<Wasp> waspsToAnger = level.getEntitiesOfClass(Wasp.class, surroundings);
        if (!waspsToAnger.isEmpty()) {
            List<Player> playersToBeAngryAt = level.getEntitiesOfClass(Player.class, surroundings);
            if (playersToBeAngryAt.isEmpty()) {
                return;
            }

            for (Wasp wasp : waspsToAnger) {
                if (wasp.getTarget() == null) {
                    Player angerTarget = Util.getRandom(playersToBeAngryAt, level.getRandom());
                    wasp.setTarget(angerTarget);
                }
            }
        }
    }

    private boolean portContainsWasps(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof WaspPortBlockEntity portBE && !portBE.isEmpty();
    }

    public void releaseWasps(
            Level level, BlockState state, BlockPos pos, @Nullable Player player, WaspPortBlockEntity.WaspReleaseStatus waspReleaseStatus
    ) {
        if (level.getBlockEntity(pos) instanceof WaspPortBlockEntity portBE) {
            portBE.emptyAllLivingFromPort(player, state, waspReleaseStatus);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new WaspPortBlockEntity(worldPosition, blockState);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, ModBlockEntityTypes.WASP_PORT.get(), WaspPortBlockEntity::serverTick);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level instanceof ServerLevel serverLevel
                && player.preventsBlockDrops()
                && serverLevel.getGameRules().get(GameRules.BLOCK_DROPS)
                && level.getBlockEntity(pos) instanceof WaspPortBlockEntity portBE) {
            boolean hasWasps = !portBE.isEmpty();
            if (hasWasps) {
                ItemStack itemStack = new ItemStack(this);
                itemStack.applyComponents(portBE.collectComponents());
                ItemEntity entity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                entity.setDefaultPickUpDelay();
                level.addFreshEntity(entity);
            }
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        Entity entity = params.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof PrimedTnt
                || entity instanceof Creeper
                || entity instanceof WitherSkull
                || entity instanceof WitherBoss
                || entity instanceof MinecartTNT) {
            BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if (blockEntity instanceof WaspPortBlockEntity portBE) {
                portBE.emptyAllLivingFromPort(null, state, WaspPortBlockEntity.WaspReleaseStatus.EMERGENCY);
            }
        }

        return super.getDrops(state, params);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}