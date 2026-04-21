package abyssal.items.curios;

import abyssal.data.ModTags;
import abyssal.init.ModDataComponents;
import abyssal.inventory.CoinPurseTooltip;
import com.mojang.serialization.DataResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class CoinPurseItem extends BundleItem {
    public static final int MAX_WEIGHT = 256;

    public static final int MAX_SHOWN_GRID_ITEMS_X = 4;
    public static final int MAX_SHOWN_GRID_ITEMS_Y = 3;
    public static final int MAX_SHOWN_GRID_ITEMS = 12;
    public static final int OVERFLOWING_MAX_SHOWN_GRID_ITEMS = 11;
    private static final int FULL_BAR_COLOR = ARGB.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
    private static final int BAR_COLOR = ARGB.colorFromFloat(1f, 0.9F, 0.75F, 0.2F);
    private static final int TICKS_AFTER_FIRST_THROW = 10;
    private static final int TICKS_BETWEEN_THROWS = 2;
    private static final int TICKS_MAX_THROW_DURATION = 200;

    public CoinPurseItem(Item.Properties properties) {
        super(properties);
    }

    private static Fraction getWeightSafe(CoinPurseBundleContents contents) {
        return switch (contents.weight()) {
            case DataResult.Success<Fraction> success -> (Fraction)success.value();
            case DataResult.Error<?> error -> Fraction.ONE;
            default -> throw new MatchException(null, null);
        };
    }

    public static float getFullnessDisplay(ItemStack stack) {
        CoinPurseBundleContents bundlecontents = stack.getOrDefault(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, CoinPurseBundleContents.EMPTY);
        return getWeightSafe(bundlecontents).floatValue();
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack purse, Slot slot, ClickAction action, Player player) {
        CoinPurseBundleContents contents = purse.get(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS);
        if (contents == null || purse.getCount() != 1) {
            return false;
        } else {
            ItemStack inputStack = slot.getItem();
            CoinPurseBundleContents.Mutable contentsM = new CoinPurseBundleContents.Mutable(contents);
            if (action == ClickAction.PRIMARY && !inputStack.isEmpty()) {
                if (inputStack.is(ModTags.Items.COIN_PURSE_ITEMS) && contentsM.tryTransfer(slot, player) > 0) {
                    playInsertSound(player);
                } else {
                    playInsertFailSound(player);
                }

                purse.set(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, contentsM.toImmutable());
                this.broadcastChangesOnContainerMenu(player);
                return true;
            } else if (action == ClickAction.SECONDARY && inputStack.isEmpty()) {
                ItemStack removed = contentsM.removeOne();
                if (removed != null) {
                    ItemStack inserted = slot.safeInsert(removed);
                    if (inserted.getCount() > 0) {
                        contentsM.tryInsert(inserted);
                    } else {
                        playRemoveOneSound(player);
                    }
                }

                purse.set(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, contentsM.toImmutable());
                this.broadcastChangesOnContainerMenu(player);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe(
            ItemStack purse, ItemStack held, Slot slot, ClickAction action, Player player, SlotAccess access
    ) {
        if (purse.getCount() != 1) return false;
        if (action == ClickAction.PRIMARY && held.isEmpty()) {
            toggleSelectedItem(purse, -1);
            return false;
        } else {
            CoinPurseBundleContents contents = purse.get(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS);
            if (contents == null) {
                return false;
            } else {
                CoinPurseBundleContents.Mutable contentsM = new CoinPurseBundleContents.Mutable(contents);
                if (action == ClickAction.PRIMARY && !held.isEmpty()) {
                    if (held.is(ModTags.Items.COIN_PURSE_ITEMS) && slot.allowModification(player) && contentsM.tryInsert(held) > 0) {
                        playInsertSound(player);
                    } else {
                        playInsertFailSound(player);
                    }

                    purse.set(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, contentsM.toImmutable());
                    this.broadcastChangesOnContainerMenu(player);
                    return true;
                } else if (action == ClickAction.SECONDARY && held.isEmpty()) {
                    if (slot.allowModification(player)) {
                        ItemStack removed = contentsM.removeOne();
                        if (removed != null) {
                            playRemoveOneSound(player);
                            access.set(removed);
                        }
                    }

                    purse.set(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, contentsM.toImmutable());
                    this.broadcastChangesOnContainerMenu(player);
                    return true;
                } else {
                    toggleSelectedItem(purse, -1);
                    return false;
                }
            }
        }
    }

    @Override
    public InteractionResult use(Level p_150760_, Player p_150761_, InteractionHand p_150762_) {
        p_150761_.startUsingItem(p_150762_);
        return InteractionResult.SUCCESS;
    }

    private void dropContent(Level level, Player player, ItemStack stack) {
        if (this.dropContent(stack, player)) {
            playDropContentsSound(level, player);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public boolean isBarVisible(ItemStack p_150769_) {
        CoinPurseBundleContents bundlecontents = p_150769_.getOrDefault(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, CoinPurseBundleContents.EMPTY);
        return getWeightSafe(bundlecontents).compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public int getBarWidth(ItemStack p_150771_) {
        CoinPurseBundleContents bundlecontents = p_150771_.getOrDefault(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, CoinPurseBundleContents.EMPTY);
        return Math.min(1 + Mth.mulAndTruncate(getWeightSafe(bundlecontents), 12), 13);
    }

    @Override
    public int getBarColor(ItemStack p_150773_) {
        CoinPurseBundleContents bundlecontents = p_150773_.getOrDefault(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, CoinPurseBundleContents.EMPTY);
        return getWeightSafe(bundlecontents).compareTo(Fraction.ONE) >= 0 ? FULL_BAR_COLOR : BAR_COLOR;
    }

    public static void toggleSelectedItem(ItemStack purse, int selectedItem) {
        CoinPurseBundleContents contents = purse.get(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS);
        if (contents != null) {
            CoinPurseBundleContents.Mutable contentsM = new CoinPurseBundleContents.Mutable(contents);
            contentsM.toggleSelectedItem(selectedItem);
            purse.set(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, contentsM.toImmutable());
        }
    }

    public static boolean hasSelectedItem(ItemStack purse) {
        CoinPurseBundleContents contents = purse.get(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS);
        return contents != null && contents.getSelectedItemIndex() != -1;
    }

    public static int getSelectedItemIndex(ItemStack purse) {
        CoinPurseBundleContents contents = purse.getOrDefault(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, CoinPurseBundleContents.EMPTY);
        return contents.getSelectedItemIndex();
    }

    public static @Nullable ItemStackTemplate getSelectedItem(ItemStack purse) {
        return purse.getOrDefault(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, CoinPurseBundleContents.EMPTY).getSelectedItem();
    }

    public static int getNumberOfItemsToShow(ItemStack purse) {
        CoinPurseBundleContents contents = purse.getOrDefault(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, CoinPurseBundleContents.EMPTY);
        return contents.getNumberOfItemsToShow();
    }

    private boolean dropContent(ItemStack purse, Player player) {
        CoinPurseBundleContents contents = purse.get(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS);
        if (contents != null && !contents.isEmpty()) {
            Optional<ItemStack> removed = removeOneItemFromBundle(purse, player, contents);
            if (removed.isPresent()) {
                player.drop(removed.get(), true);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static Optional<ItemStack> removeOneItemFromBundle(ItemStack purse, Player player, CoinPurseBundleContents contents) {
        CoinPurseBundleContents.Mutable contentsM = new CoinPurseBundleContents.Mutable(contents);
        ItemStack itemstack = contentsM.removeOne();
        if (itemstack != null) {
            playRemoveOneSound(player);
            purse.set(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS, contentsM.toImmutable());
            return Optional.of(itemstack);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int num) {
        if (entity instanceof Player player) {
            int i = this.getUseDuration(stack, entity);
            boolean flag = num == i;
            if (flag || num < i - 10 && num % 2 == 0) {
                this.dropContent(level, player, stack);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack p_371683_, LivingEntity p_371530_) {
        return 200;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack p_389672_) {
        return ItemUseAnimation.BUNDLE;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack p_150775_) {
        TooltipDisplay tooltipdisplay = p_150775_.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
        return !tooltipdisplay.shows(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS.get())
                ? Optional.empty()
                : Optional.ofNullable(p_150775_.get(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS)).map(CoinPurseTooltip::new);
    }

    @Override
    public void onDestroyed(ItemEntity entity) {
        CoinPurseBundleContents contents = entity.getItem().get(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS.get());
        if (contents != null) {
            entity.getItem().set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
            ItemUtils.onContainerDestroyed(entity, contents.itemCopyStream());
        }
    }

    private static void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertFailSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT_FAIL, 1.0F, 1.0F);
    }

    private static void playDropContentsSound(Level level, Entity entity) {
        level.playSound(
                null,
                entity.blockPosition(),
                SoundEvents.BUNDLE_DROP_CONTENTS,
                SoundSource.PLAYERS,
                0.8F,
                0.8F + entity.level().getRandom().nextFloat() * 0.4F
        );
    }

    private void broadcastChangesOnContainerMenu(Player player) {
        AbstractContainerMenu abstractcontainermenu = player.containerMenu;
        if (abstractcontainermenu != null) {
            abstractcontainermenu.slotsChanged(player.getInventory());
        }
    }

}

