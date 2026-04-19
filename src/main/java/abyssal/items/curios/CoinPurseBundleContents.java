package abyssal.items.curios;

import abyssal.init.ModDataComponents;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Bees;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class CoinPurseBundleContents implements TooltipComponent {
    public static final CoinPurseBundleContents EMPTY = new CoinPurseBundleContents(List.of());
    public static final Codec<CoinPurseBundleContents> CODEC = ItemStack.CODEC
            .listOf()
            .flatXmap(CoinPurseBundleContents::checkAndCreate, p_381696_ -> DataResult.success(p_381696_.items));
    public static final StreamCodec<RegistryFriendlyByteBuf, CoinPurseBundleContents> STREAM_CODEC = ItemStack.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(CoinPurseBundleContents::new, p_331551_ -> p_331551_.items);
    private static final Fraction BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);
    private static final int NO_STACK_INDEX = -1;
    public static final int NO_SELECTED_ITEM_INDEX = -1;
    final List<ItemStack> items;
    final Fraction weight;
    final int selectedItem;

    CoinPurseBundleContents(List<ItemStack> items, Fraction weight, int selectedItem) {
        this.items = items;
        this.weight = weight;
        this.selectedItem = selectedItem;
    }

    private static DataResult<CoinPurseBundleContents> checkAndCreate(List<ItemStack> items) {
        try {
            Fraction fraction = computeContentWeight(items);
            return DataResult.success(new CoinPurseBundleContents(items, fraction, NO_SELECTED_ITEM_INDEX));
        } catch (ArithmeticException arithmeticexception) {
            return DataResult.error(() -> "Excessive total bundle weight");
        }
    }

    public CoinPurseBundleContents(List<ItemStack> items) {
        this(items, computeContentWeight(items), NO_SELECTED_ITEM_INDEX);
    }

    private static Fraction computeContentWeight(List<ItemStack> content) {
        Fraction fraction = Fraction.ZERO;

        for (ItemStack itemstack : content) {
            fraction = fraction.add(getWeight(itemstack).multiplyBy(Fraction.getFraction(itemstack.getCount(), 1)));
        }

        return fraction;
    }

    static Fraction getWeight(ItemStack stack) {
        CoinPurseBundleContents bundlecontents = stack.get(ModDataComponents.COIN_PURSE_BUNDLE_CONTENTS);
        if (bundlecontents != null) {
            return BUNDLE_IN_BUNDLE_WEIGHT.add(bundlecontents.weight());
        } else {
            List<BeehiveBlockEntity.Occupant> list = stack.getOrDefault(DataComponents.BEES, Bees.EMPTY).bees();
            return !list.isEmpty() ? Fraction.ONE : Fraction.getFraction(1, 4*stack.getMaxStackSize());
        }
    }

    public static boolean canItemBeInBundle(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem().canFitInsideContainerItems();
    }

    public int getNumberOfItemsToShow() {
        int i = this.size();
        int j = i > 12 ? 11 : 12;
        int k = i % 4;
        int l = k == 0 ? 0 : 4 - k;
        return Math.min(i, j - l);
    }

    public ItemStack getItemUnsafe(int index) {
        return this.items.get(index);
    }

    public Stream<ItemStack> itemCopyStream() {
        return this.items.stream().map(ItemStack::copy);
    }

    public Iterable<ItemStack> items() {
        return this.items;
    }

    public Iterable<ItemStack> itemsCopy() {
        return Lists.transform(this.items, ItemStack::copy);
    }

    public int size() {
        return this.items.size();
    }

    public Fraction weight() {
        return this.weight;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public int getSelectedItem() {
        return this.selectedItem;
    }

    public boolean hasSelectedItem() {
        return this.selectedItem != NO_SELECTED_ITEM_INDEX;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return !(other instanceof CoinPurseBundleContents bundlecontents)
                    ? false
                    : this.weight.equals(bundlecontents.weight) && ItemStack.listMatches(this.items, bundlecontents.items);
        }
    }

    @Override
    public int hashCode() {
        return ItemStack.hashStackList(this.items);
    }

    @Override
    public String toString() {
        return "CoinPurseBundleContents" + this.items;
    }

    public static class Mutable {
        private final List<ItemStack> items;
        private Fraction weight;
        private int selectedItem;

        public Mutable(CoinPurseBundleContents contents) {
            this.items = new ArrayList<>(contents.items);
            this.weight = contents.weight;
            this.selectedItem = contents.selectedItem;
        }

        public CoinPurseBundleContents.Mutable clearItems() {
            this.items.clear();
            this.weight = Fraction.ZERO;
            this.selectedItem = NO_SELECTED_ITEM_INDEX;
            return this;
        }

        private int findStackIndex(ItemStack stack) {
            if (!stack.isStackable()) {
                return NO_STACK_INDEX;
            } else {
                for (int i = 0; i < this.items.size(); i++) {
                    ItemStack candidate = this.items.get(i);
                    if (ItemStack.isSameItemSameComponents(candidate, stack)) {
                        if(candidate.getCount() == candidate.getMaxStackSize()) {
                            continue;
                        }
                        return i;
                    }
                }

                return NO_STACK_INDEX;
            }
        }

        private int getMaxAmountToAdd(ItemStack stack) {
            Fraction fraction = Fraction.ONE.subtract(this.weight);
            return Math.max(fraction.divideBy(getWeight(stack)).intValue(), 0);
        }

        public int tryInsert(ItemStack inStack) {
            if (!canItemBeInBundle(inStack)) {
                return 0;
            } else {
                int i = Math.min(inStack.getCount(), this.getMaxAmountToAdd(inStack));
                if (i == 0) {
                    return 0;
                } else {
                    this.weight = this.weight.add(getWeight(inStack).multiplyBy(Fraction.getFraction(i, 1)));
                    int j = this.findStackIndex(inStack);
                    if (j != NO_STACK_INDEX) {
                        ItemStack matched = this.items.remove(j);
                        int desiredCount = matched.getCount() + i;
                        ItemStack merged = matched.copyWithCount(Math.min(desiredCount, matched.getMaxStackSize()));
                        int remainderCount = desiredCount - matched.getMaxStackSize();
                        inStack.shrink(i);
                        this.items.add(0, merged);
                        if(remainderCount > 0) {
                            ItemStack remainder = matched.copyWithCount(remainderCount);
                            this.items.add(0, remainder);
                        }

                    } else {
                        this.items.add(0, inStack.split(i));
                    }

                    return i;
                }
            }
        }

        public int tryTransfer(Slot slot, Player player) {
            ItemStack itemstack = slot.getItem();
            int i = this.getMaxAmountToAdd(itemstack);
            return canItemBeInBundle(itemstack) ? this.tryInsert(slot.safeTake(itemstack.getCount(), i, player)) : 0;
        }

        public void toggleSelectedItem(int selectedItem) {
            this.selectedItem = this.selectedItem != selectedItem && !this.indexIsOutsideAllowedBounds(selectedItem) ? selectedItem : NO_SELECTED_ITEM_INDEX;
        }

        private boolean indexIsOutsideAllowedBounds(int index) {
            return index < 0 || index >= this.items.size();
        }

        @Nullable
        public ItemStack removeOne() {
            if (this.items.isEmpty()) {
                return null;
            } else {
                int i = this.indexIsOutsideAllowedBounds(this.selectedItem) ? 0 : this.selectedItem;
                ItemStack itemstack = this.items.remove(i).copy();
                this.weight = this.weight.subtract(getWeight(itemstack).multiplyBy(Fraction.getFraction(itemstack.getCount(), 1)));
                this.toggleSelectedItem(NO_SELECTED_ITEM_INDEX);
                return itemstack;
            }
        }

        public Fraction weight() {
            return this.weight;
        }

        public CoinPurseBundleContents toImmutable() {
            return new CoinPurseBundleContents(List.copyOf(this.items), this.weight, this.selectedItem);
        }
    }
}
