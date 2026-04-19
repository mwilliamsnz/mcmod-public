package abyssal.items.handheld;

import abyssal.Main;
import abyssal.init.Gems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class SpawnerMoverItem extends ProjectileWeaponItem {
    private static final String SAVE_AS_TAG = "spawner";
    private static final String SPAWNER_DATA_TAG = "SpawnData";
    private static final String ID_TAG = "id";
    private static final String ENTITY_TAG = "entity";

    private static final Component MOVER_EMPTY_MSG = Component.translatable("abyssal.mover.empty");
    private static final Component MOVER_MALFORMED_MSG = Component.translatable("abyssal.mover.malformed1");
    private static final Component MOVER_MALFORMED_2_MSG = Component.translatable("abyssal.mover.malformed2");

    public SpawnerMoverItem(Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (stack) -> stack.is(Gems.gem(Gems.GemSize.POWDER, Gems.GemType.GARNET));
    }

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }

    @Override
    protected void shootProjectile(LivingEntity pShooter, Projectile pProjectile, int pIndex, float pVelocity, float pInaccuracy, float pAngle, @org.jetbrains.annotations.Nullable LivingEntity pTarget) {

    }

    @Nullable
    private static ResourceLocation getEntityId(ItemStack stack) {
        Main.LOGGER.info("NOT YET IMPLEMENTED! SpawnerMoverItem.java");
//        Main.LOGGER.info("Getting entity ID:");
//        CompoundTag tag = stack.getTagElement(SAVE_AS_TAG);
//        Main.LOGGER.info("\t" + tag);
//        if(tag == null) {
//            Main.LOGGER.info("\tNo tag saved.");
//            return null;
//        }
//        if (tag.contains(SPAWNER_DATA_TAG)) {
//            tag = tag.getCompound(SPAWNER_DATA_TAG);
//            if (tag.contains(ENTITY_TAG)) {
//                tag = tag.getCompound(ENTITY_TAG);
//                if (tag.contains(ID_TAG)) {
//                    Main.LOGGER.info("\ttrying to parse:" + tag.getString(ID_TAG));
//                    return ResourceLocation.tryParse(tag.getString(ID_TAG));
//                }
//            }
//            Main.LOGGER.info("\tno spawner ID tag:");
//            Main.LOGGER.info(ID_TAG);
//            return null;
//        }
//        Main.LOGGER.info("\tno spawner data tag:");
//        Main.LOGGER.info(SPAWNER_DATA_TAG);
        return null;
    }

    public static boolean hasData(ItemStack stack) {
        return getEntityId(stack) != null;
    }

//    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> infoList, TooltipFlag flags) {
        Main.LOGGER.info("NOT YET IMPLEMENTED 2! SpawnerMoverItem.java");
        return;
//        CompoundTag tag = stack.getTagElement(SAVE_AS_TAG);
//        if(tag == null) {
//            infoList.add(MOVER_EMPTY_MSG);
//            return;
//        }
//        if(!tag.contains(SPAWNER_DATA_TAG)) {
//            infoList.add(MOVER_MALFORMED_MSG);
//            return;
//        }
//        if (!tag.contains(ID_TAG)) {
//            infoList.add(MOVER_MALFORMED_2_MSG);
//            return;
//        }
//        ResourceLocation id = getEntityId(stack);
//        if (id != null) {
//            BuiltInRegistries.ENTITY_TYPE.getOptional(id).ifPresent(type -> infoList.add(type.getDescription()));
//        }
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (getEntityId(ctx.getItemInHand()) == null) {
            return captureSpawner(ctx);
        } else {
            return placeSpawner(ctx);
        }
    }

    private InteractionResult placeSpawner(UseOnContext ctx) {
        Main.LOGGER.info("NOT YET IMPLEMENTED 4! SpawnerMoverItem.java");
//        Main.LOGGER.info("Trying place");
//        Level level = ctx.getLevel();
//        Player player = ctx.getPlayer();
//        ItemStack item = ctx.getItemInHand();
//
//        BlockPlaceContext bpc = new BlockPlaceContext(ctx);
//
//        ItemStack powder = player.getProjectile(ctx.getItemInHand());
//        if(powder == ItemStack.EMPTY) {
//            level.playLocalSound(player.getX(),player.getY(),player.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, player.getSoundSource(), 0.8f, 0.8f+level.random.nextFloat()*0.4f, false);
//            Main.LOGGER.info("No powder, aborting.");
//            return InteractionResult.FAIL;
//        }
//
//        level.playLocalSound(player.getX(),player.getY(),player.getZ(), SoundEvents.FURNACE_FIRE_CRACKLE, player.getSoundSource(), 0.8f, 0.8f+level.random.nextFloat()*0.4f, false);
//        ((BlockItem)Blocks.SPAWNER.asItem()).place(bpc);
//
//        if (!level.isClientSide) {
//            Main.LOGGER.info("(server side, doing the business)");
//            BlockPos pos = bpc.getClickedPos();
//            BlockEntity blockEntity = level.getBlockEntity(pos);
//            if (blockEntity instanceof SpawnerBlockEntity) {
//                Main.LOGGER.info("(found the BE)");
//                powder.shrink(1);
//                CompoundTag spawnerTag = ctx.getItemInHand().getTagElement(SAVE_AS_TAG).copy();
//                spawnerTag.putInt("x", pos.getX());
//                spawnerTag.putInt("y", pos.getY());
//                spawnerTag.putInt("z", pos.getZ());
//                blockEntity.load(spawnerTag);
//                item.removeTagKey(SAVE_AS_TAG);
//            }
//        }
//        Main.LOGGER.info("All clear, success");
        return InteractionResult.SUCCESS;
    }

    private InteractionResult captureSpawner(UseOnContext ctx) {
        Main.LOGGER.info("NOT YET IMPLEMENTED 4! SpawnerMoverItem.java");
//        Main.LOGGER.info("Trying capture");
//        Level level = ctx.getLevel();
//        BlockPos pos = ctx.getClickedPos();
//        ItemStack stack = ctx.getItemInHand();
//        Player player = ctx.getPlayer();
//
//        ItemStack powder = player.getProjectile(ctx.getItemInHand());
//        if(powder == ItemStack.EMPTY) {
//            level.playLocalSound(player.getX(),player.getY(),player.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, player.getSoundSource(), 0.8f, 0.8f+level.random.nextFloat()*0.4f, false);
//            return InteractionResult.FAIL;
//        }
//
//        if (level.getBlockState(pos).is(Blocks.SPAWNER)) {
//            level.playLocalSound(player.getX(),player.getY(),player.getZ(), SoundEvents.CHAIN_STEP, player.getSoundSource(), 0.8f, 0.8f+level.random.nextFloat()*0.4f, false);
//            if (!level.isClientSide) {
//                SpawnerBlockEntity blockEntity = (SpawnerBlockEntity) level.getBlockEntity(pos);
//                stack.getOrCreateTag().put(SAVE_AS_TAG, blockEntity.getSpawner().save(new CompoundTag()));
//                //stack.getOrCreateTag().put(SPAWNER_TAG, blockEntity.save(new CompoundTag()));
//                level.destroyBlock(pos, false);
//                powder.shrink(1);
//                player.getCooldowns().addCooldown(this, 20);
//                player.broadcastBreakEvent(ctx.getHand());
//
//            }
//            return InteractionResult.SUCCESS;
//        } else {
            return InteractionResult.PASS;
//        }
    }
}
