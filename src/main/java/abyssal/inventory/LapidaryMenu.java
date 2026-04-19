package abyssal.inventory;

import abyssal.Main;
import abyssal.init.Gems;
import abyssal.init.ModBlocks;
import abyssal.init.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class LapidaryMenu extends AbstractContainerMenu {

    public static final int INPUT_SLOT = 0;
    public static final int MEDIUM_SLOT = 1;
    public static final int SMALL_SLOT = 2;
    public static final int TINY_SLOT = 3;
    public static final int POWDER_SLOT = 4;

    private static final int INV_SLOT_START = POWDER_SLOT + 1;
    private static final int INV_SLOT_END = INV_SLOT_START + 27;
    private static final int HOTBAR_SLOT_START = INV_SLOT_END;
    private static final int HOTBAR_SLOT_END = HOTBAR_SLOT_START + 9;
    protected final ResultContainer resultSlotPowder = new ResultContainer();
    protected final ResultContainer resultSlotTiny = new ResultContainer();
    protected final ResultContainer resultSlotSmall = new ResultContainer();
    protected final ResultContainer resultSlotMed = new ResultContainer();
    protected final Container inputSlots = new SimpleContainer(1) {
        public void setChanged() {
            super.setChanged();
            LapidaryMenu.this.slotsChanged(this);
        }
    };
    protected final ContainerLevelAccess access;
    protected final Player player;

    public LapidaryMenu(int id, Inventory inv) {
        this(id, inv, ContainerLevelAccess.NULL);
    }

    public LapidaryMenu(int id, Inventory inv, ContainerLevelAccess access) {
        super(ModMenus.LAPIDARY.get(), id);
        this.access = access;
        this.player = inv.player;

        this.addSlot(new Slot(this.inputSlots, INPUT_SLOT, 60, 12));
        this.addSlot(new LapidaryOutSlot(this.resultSlotMed, MEDIUM_SLOT, 24, 52));
        this.addSlot(new LapidaryOutSlot(this.resultSlotSmall, SMALL_SLOT, 45, 52));
        this.addSlot(new LapidaryOutSlot(this.resultSlotTiny, TINY_SLOT, 66, 52));
        this.addSlot(new LapidaryOutSlot(this.resultSlotPowder, POWDER_SLOT, 101, 52));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inv, j + 9*i + 9, 8 + j*18, 84 + i*18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inv, k, 8 + k*18, 142));
        }
    }

    protected boolean isValidBlock(BlockState state) {
        return state.is(ModBlocks.LAPIDARY.get());
    }

    protected boolean mayPickup(Player player, boolean p_40269_) {
        return true;
    }

    protected void onTake(Player player, ItemStack stack) {
        stack.onCraftedBy(player, stack.getCount());
//        this.resultSlots.awardUsedRecipes(player);
//        this.shrinkStackInSlot(0);
//        this.shrinkStackInSlot(1);
//        this.access.execute((accessor, pos) -> {
//            accessor.levelEvent(1044, pos, 0);
//        });
    }

    private void shrinkStackInSlot(int slot) {
        ItemStack itemstack = this.inputSlots.getItem(slot);
        itemstack.shrink(1);
        this.inputSlots.setItem(slot, itemstack);
    }

    public void createResult() {
//        List<UpgradeRecipe> list = this.level.getRecipeManager().getRecipesFor(RecipeType.SMITHING, this.inputSlots, this.level);
//        if (list.isEmpty()) {
//            this.resultSlots.setItem(0, ItemStack.EMPTY);
//        } else {
//            this.selectedRecipe = list.get(0);
//            ItemStack itemstack = this.selectedRecipe.assemble(this.inputSlots);
////            this.resultSlots.setRecipeUsed(this.selectedRecipe);
//            this.resultSlots.setItem(0, itemstack);
//        }
    }

    // Whether shift-click from inventory puts this item in secondaryRL input slot rather than primaryRL
    protected boolean shouldQuickMoveToAdditionalSlot(ItemStack stack) {
        return false; //stack.is(ModTags.Items.GEMS);
    }

    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container == this.inputSlots && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        if (container == this.inputSlots) {
            this.createResult();
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> {
            this.clearContainer(player, this.inputSlots);
            this.clearContainer(player, this.resultSlotMed);
            this.clearContainer(player, this.resultSlotSmall);
            this.clearContainer(player, this.resultSlotTiny);
            this.clearContainer(player, this.resultSlotPowder);
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return this.access.evaluate((level, pos) -> {
            return this.isValidBlock(level.getBlockState(pos)) && player.distanceToSqr((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
        }, true);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIdx) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIdx);
        if (slot != null && slot.hasItem()) {
            ItemStack inSlot = slot.getItem();
            stack = inSlot.copy();
            if (slotIdx >= MEDIUM_SLOT && slotIdx <= POWDER_SLOT) { // Move out of result slot
                if (!this.moveItemStackTo(inSlot, INV_SLOT_START, HOTBAR_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(inSlot, stack);
            } else if (slotIdx != INPUT_SLOT) { // Move out of inventory slot
                if (slotIdx >= INV_SLOT_START && slotIdx < HOTBAR_SLOT_END) { // Sanity
                    if (!this.moveItemStackTo(inSlot, INPUT_SLOT, INPUT_SLOT+1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(inSlot, INV_SLOT_START, HOTBAR_SLOT_END, false)) {  // Move out of input slot
                return ItemStack.EMPTY;
            }

            if (inSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (inSlot.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, inSlot);
        }

        return stack;
    }

//    @OnlyIn(Dist.CLIENT)
    public boolean clickMenuButton(Player player, int button) {
        Slot input = this.slots.get(INPUT_SLOT);
        if(!input.hasItem()) {
            return false;
        }
        ItemStack inputStack = input.getItem();
        if(!Gems.isGemItem(inputStack)) {
            return false;
        }
        Gems.GemSize size = Gems.getSize(inputStack);
        if(size == Gems.GemSize.POWDER) {
            return false;
        }
        Gems.GemType type = Gems.getType(inputStack);

        // Check that output doesn't have other flavours of gem in it
        for(int i = MEDIUM_SLOT; i <= POWDER_SLOT; i++) {
            Slot outputSlot = this.slots.get(i);
            if(outputSlot.hasItem()) {
                ItemStack outStack = outputSlot.getItem();
                if(Gems.getType(outStack) != type) {
                    return false;
                }
            }
        }

        int s = 4;
        if(size == Gems.GemSize.TINY) {
            s = 1;
        } else if(size == Gems.GemSize.SMALL) {
            s = 2;
        } else if(size == Gems.GemSize.REGULAR) {
            s = 3;
        }
        inputStack.shrink(1);
        if(inputStack.getCount() <= 0) {
            input.set(ItemStack.EMPTY);
        }
        if(button == 1) { // Crack
            GemQuartet q = crack(s, 0.5f, new Random());
            Main.LOGGER.info(q.counts);
            if(q.counts[0] > 0) {
                Slot slot = this.slots.get(POWDER_SLOT);
                Item outItem = Gems.gem(Gems.GemSize.POWDER, type);
                if(slot.hasItem()) {
                    ItemStack outStack = slot.getItem();
                    if(!outStack.getItem().equals(outItem)) {
                        throw new IllegalStateException("Incorrect item in powder output slot");
                    }
                    outStack.grow(q.counts[0]);
                } else {
                    slot.set(new ItemStack(outItem, q.counts[0]));
                }
            }
            if(q.counts[1] > 0) {
                Slot slot = this.slots.get(TINY_SLOT);
                Item outItem = Gems.gem(Gems.GemSize.TINY, type);
                if(slot.hasItem()) {
                    ItemStack outStack = slot.getItem();
                    if(!outStack.getItem().equals(outItem)) {
                        throw new IllegalStateException("Incorrect item in tiny gem output slot");
                    }
                    outStack.grow(q.counts[1]);
                } else {
                    slot.set(new ItemStack(outItem, q.counts[1]));
                }
            }
            if(q.counts[2] > 0) {
                Slot slot = this.slots.get(SMALL_SLOT);
                Item outItem = Gems.gem(Gems.GemSize.SMALL, type);
                if(slot.hasItem()) {
                    ItemStack outStack = slot.getItem();
                    if(!outStack.getItem().equals(outItem)) {
                        throw new IllegalStateException("Incorrect item in small gem output slot");
                    }
                    outStack.grow(q.counts[2]);
                } else {
                    slot.set(new ItemStack(outItem, q.counts[2]));
                }
            }
            if(q.counts[3] > 0) {
                Slot slot = this.slots.get(MEDIUM_SLOT);
                Item outItem = Gems.gem(Gems.GemSize.REGULAR, type);
                if(slot.hasItem()) {
                    ItemStack outStack = slot.getItem();
                    if(!outStack.getItem().equals(outItem)) {
                        throw new IllegalStateException("Incorrect item in medium gem output slot");
                    }
                    outStack.grow(q.counts[3]);
                } else {
                    slot.set(new ItemStack(outItem, q.counts[3]));
                }
            }
        } else if(button == 2) { // Become dust
            int r = 1;
            while (s --> 1) {
                r *= 4;
            }
            r *= 2;
            Slot slot = this.slots.get(POWDER_SLOT);
            Item outItem = Gems.gem(Gems.GemSize.POWDER, type);
            if(slot.hasItem()) {
                ItemStack outStack = slot.getItem();
                if(!outStack.getItem().equals(outItem)) {
                    throw new IllegalStateException("Incorrect item in powder output slot");
                }
                outStack.grow(r);
            } else {
                slot.set(new ItemStack(outItem, r));
            }
        }
        return true;
    }

    private GemQuartet crack(int size, float p, Random r) {
//        StringBuilder sb = new StringBuilder();
//        for(int i = 1; i < 3-size; i++) {
//            sb.append("\t");
//        }
//        Main.LOGGER.info(sb +  "Cracking " + size);
        if(size == 1) {
//            Main.LOGGER.info(sb.toString() + "got " + Arrays.toString(new GemQuartet(0, 0, 0, 2).counts));
            return new GemQuartet(0, 0, 0, 2);
        }
        GemQuartet q = new GemQuartet(0, 0, 0, 0);
        for(int i = 0; i < 4; i++) {
            if(r.nextFloat() < p) {
                q.counts[size-1]++;
            } else {
                q = q.add(crack(size-1, p, r));
            }
        }
//        Main.LOGGER.info(sb + "got " + Arrays.toString(q.counts));
        return q;
    }

    private static class GemQuartet {
        public final int[] counts = new int[]{0, 0, 0, 0};

        GemQuartet(int med, int small, int tiny, int powder) {
            counts[0] = powder;
            counts[1] = tiny;
            counts[2] = small;
            counts[3] = med;
        }

        GemQuartet add(GemQuartet g) {
            return new GemQuartet(counts[3] + g.counts[3], counts[2] + g.counts[2], counts[1] + g.counts[1], counts[0] + g.counts[0]);
        }
    }

    private class LapidaryOutSlot extends Slot {
        public LapidaryOutSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        public boolean mayPickup(Player player) {
            return LapidaryMenu.this.mayPickup(player, this.hasItem());
        }

        public void onTake(Player player, ItemStack stack) {
            LapidaryMenu.this.onTake(player, stack);
        }
    }
}
