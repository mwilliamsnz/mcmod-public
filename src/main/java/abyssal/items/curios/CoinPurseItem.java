package abyssal.items.curios;

import abyssal.data.ModTags;
import abyssal.init.ModItems;
import abyssal.inventory.CoinPurseTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CoinPurseItem extends ModCurioItem {
    private static final String TAG_ITEMS = "Items";
    public static final int MAX_WEIGHT = 256;
    private static final int BUNDLE_IN_BUNDLE_WEIGHT = 4;
    private static final int BAR_COLOR = Mth.color(0.9F, 0.75F, 0.2F);

    public CoinPurseItem(Item.Properties properties) {
        super(properties);
    }

    public static float getFullnessDisplay(ItemStack stack) {
        return (float)getContentWeight(stack) / MAX_WEIGHT;
    }

    public boolean overrideStackedOnOther(ItemStack stackedOnto, Slot slot, ClickAction clickAction, Player player) {
        if (stackedOnto.getCount() != 1 || clickAction != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack inputStack = slot.getItem();
            if (inputStack.isEmpty()) { // take one out
                this.playRemoveOneSound(player);
                removeOne(stackedOnto).ifPresent((itemStack) -> {
                    add(stackedOnto, slot.safeInsert(itemStack));
                });
            } else if (inputStack.getItem().canFitInsideContainerItems() && inputStack.is(ModTags.Items.COIN_PURSE_ITEMS)) { // put some in
                int i = (MAX_WEIGHT - getContentWeight(stackedOnto)) / getWeight(inputStack);
                int j = add(stackedOnto, slot.safeTake(inputStack.getCount(), i, player));
                if (j > 0) {
                    this.playInsertSound(player);
                }
            }

            return true;
        }
    }

    public boolean overrideOtherStackedOnMe(ItemStack purseStack, ItemStack stackedOnBy, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (purseStack.getCount() != 1) return false;
        if (clickAction == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (stackedOnBy.isEmpty()) {
                removeOne(purseStack).ifPresent((stack) -> {
                    this.playRemoveOneSound(player);
                    slotAccess.set(stack);
                });
            } else {
                int i = add(purseStack, stackedOnBy);
                if (i > 0) {
                    this.playInsertSound(player);
                    stackedOnBy.shrink(i);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (dropContents(itemstack, player)) {
            this.playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public boolean isBarVisible(ItemStack stack) {
        return getContentWeight(stack) > 0;
    }

    public int getBarWidth(ItemStack stack) {
        return Math.min(1 + 12 * getContentWeight(stack) / MAX_WEIGHT, 13);
    }

    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    private static int add(ItemStack purseStack, ItemStack coinStack) {
        if (!coinStack.isEmpty() && coinStack.is(ModTags.Items.COIN_PURSE_ITEMS) && coinStack.getItem().canFitInsideContainerItems()) {
            CompoundTag purseTag = purseStack.getOrCreateTag();
            if (!purseTag.contains(TAG_ITEMS)) {
                purseTag.put(TAG_ITEMS, new ListTag());
            }

            int currentWeight = getContentWeight(purseStack);
            int weightPerItem = getWeight(coinStack);
            int numThatFit = Math.min(coinStack.getCount(), (MAX_WEIGHT - currentWeight) / weightPerItem);
            if (numThatFit == 0) {
                return 0;
            } else {
                ListTag itemsTag = purseTag.getList(TAG_ITEMS, Tag.TAG_COMPOUND);
                Stream<CompoundTag> matching = getMatchingItems(coinStack, itemsTag);
                var ref = new Object() { // lambdas hate him
                    int needToPutIn = numThatFit;
                };
                matching.forEach((tag) -> {
                    ItemStack stackhere = ItemStack.of(tag);
                    int canFitHere = stackhere.getMaxStackSize() - stackhere.getCount();
                    int goingIn = Math.min(canFitHere, ref.needToPutIn);
                    stackhere.grow(goingIn);
                    ref.needToPutIn -= goingIn;

                    stackhere.save(tag);
                    itemsTag.remove(tag);
                    itemsTag.add(0, tag);
                });
                if (ref.needToPutIn > 0) {
                    ItemStack coinsGoingIn = coinStack.copyWithCount(ref.needToPutIn);
                    CompoundTag tag = new CompoundTag();
                    coinsGoingIn.save(tag);
                    itemsTag.add(0, tag);
                }

                return numThatFit;
            }
        } else {
            return 0;
        }
    }

    private static Stream<CompoundTag> getMatchingItems(ItemStack toMatch, ListTag tags) {
        return toMatch.is(ModItems.COIN_PURSE.get()) ? Stream.empty() : tags.stream().filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).filter((tag) -> {
            return ItemStack.isSameItemSameTags(ItemStack.of(tag), toMatch);
        });
    }

    private static int getWeight(ItemStack stack) {
        if (stack.is(ModItems.COIN_PURSE.get())) {
            return BUNDLE_IN_BUNDLE_WEIGHT + getContentWeight(stack);
        } else {
            return 64 / stack.getMaxStackSize();
        }
    }

    private static int getContentWeight(ItemStack stack) {
        return getContents(stack).mapToInt((itemStack) -> {
            return getWeight(itemStack) * itemStack.getCount();
        }).sum();
    }

    private static Optional<ItemStack> removeOne(ItemStack purseStack) {
        CompoundTag compoundtag = purseStack.getOrCreateTag();
        if (!compoundtag.contains(TAG_ITEMS)) {
            return Optional.empty();
        } else {
            ListTag itemsTag = compoundtag.getList(TAG_ITEMS, Tag.TAG_COMPOUND);
            if (itemsTag.isEmpty()) {
                return Optional.empty();
            } else {
                int i = 0;
                CompoundTag firstItemTag = itemsTag.getCompound(0);
                ItemStack stack = ItemStack.of(firstItemTag);
                itemsTag.remove(0);
                if (itemsTag.isEmpty()) {
                    purseStack.removeTagKey(TAG_ITEMS);
                }

                return Optional.of(stack);
            }
        }
    }

    private static boolean dropContents(ItemStack purseStack, Player player) {
        CompoundTag tag = purseStack.getOrCreateTag();
        if (!tag.contains(TAG_ITEMS)) {
            return false;
        } else {
            if (player instanceof ServerPlayer) {
                ListTag itemsTag = tag.getList(TAG_ITEMS, Tag.TAG_COMPOUND);

                for(int i = 0; i < itemsTag.size(); ++i) {
                    CompoundTag itemTag = itemsTag.getCompound(i);
                    ItemStack stack = ItemStack.of(itemTag);
                    player.drop(stack, true);
                }
            }

            purseStack.removeTagKey(TAG_ITEMS);
            return true;
        }
    }

    private static Stream<ItemStack> getContents(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return Stream.empty();
        } else {
            ListTag tagList = tag.getList(TAG_ITEMS, Tag.TAG_COMPOUND);
            return tagList.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        NonNullList<ItemStack> contents = NonNullList.create();
        getContents(stack).forEach(contents::add);
        return Optional.of(new CoinPurseTooltip(contents, getContentWeight(stack)));
    }

    public void appendHoverText(ItemStack stack, Level level, List<Component> componentList, TooltipFlag tooltipFlag) {
        componentList.add(Component.translatable("item.minecraft.bundle.fullness", getContentWeight(stack), MAX_WEIGHT).withStyle(ChatFormatting.GRAY));
    }

    public void onDestroyed(ItemEntity entity) {
        ItemUtils.onContainerDestroyed(entity, getContents(entity.getItem()));
    }

    private void playRemoveOneSound(Entity remover) {
        remover.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + remover.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity inserter) {
        inserter.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + inserter.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity dropper) {
        dropper.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + dropper.level().getRandom().nextFloat() * 0.4F);
    }
}

