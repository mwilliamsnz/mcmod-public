package abyssal.inventory;

import abyssal.Main;
import abyssal.alchemy.Alchemy;
import abyssal.alchemy.AlchemyReagent;
import abyssal.alchemy.AlchemyReagents;
import abyssal.alchemy.AlchemyState;
import abyssal.init.ModBlocks;
import abyssal.init.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AlchemyMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int REAGENT_SLOT = 1;
    public static final int OUT_SLOT = 2;

    private static final int INV_SLOT_START = OUT_SLOT + 1;
    private static final int INV_SLOT_END = INV_SLOT_START + 27;
    private static final int HOTBAR_SLOT_START = INV_SLOT_END;
    private static final int HOTBAR_SLOT_END = HOTBAR_SLOT_START + 9;
    protected final ResultContainer resultSlot = new ResultContainer();
    protected final Container inputSlots = new SimpleContainer(9) {
        public void setChanged() {
            super.setChanged();
            AlchemyMenu.this.slotsChanged(this);
        }
    };
    protected final ContainerLevelAccess access;
    protected final Player player;

    private AlchemyState alchemyState = null;

    public AlchemyMenu(int id, Inventory inv) {
        this(id, inv, ContainerLevelAccess.NULL);
        Main.LOGGER.info("Creating harmoniser menu with null level access");
    }

    public AlchemyMenu(int id, Inventory inv, ContainerLevelAccess access) {
        super(ModMenus.HARMONISER.get(), id);
        Main.LOGGER.info("Created harmoniser menu");
        this.access = access;
        this.player = inv.player;

        this.addSlot(new Slot(this.inputSlots, INPUT_SLOT, 64, 35));
        this.addSlot(new Slot(this.inputSlots, REAGENT_SLOT, 64, 8));
        this.addSlot(new AlchemyBenchOutSlot(this.resultSlot, OUT_SLOT, 134, 35));

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
        return state.is(ModBlocks.HARMONISER.get());
    }

    protected boolean mayPickup(Player player, boolean p_40269_) {
        return true;
    }

    protected void onTake(Player player, ItemStack stack) {
        stack.onCraftedBy(player.level(), player, stack.getCount());
    }

    private void shrinkStackInSlot(int slot) {
        ItemStack itemstack = this.inputSlots.getItem(slot);
        itemstack.shrink(1);
        this.inputSlots.setItem(slot, itemstack);
    }

    public void createResult() {

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
            this.clearContainer(player, this.resultSlot);
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
            if (slotIdx == OUT_SLOT) { // Move out of result slot
                if (!this.moveItemStackTo(inSlot, INV_SLOT_START, HOTBAR_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(inSlot, stack);
            } else if (slotIdx != INPUT_SLOT) { // Move out of inventory slot
                if (slotIdx >= INV_SLOT_START && slotIdx < HOTBAR_SLOT_END) { // Sanity
                    if (!this.moveItemStackTo(inSlot, INPUT_SLOT, OUT_SLOT+1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(inSlot, INV_SLOT_START, HOTBAR_SLOT_END, false)) {  // Move out of input slots
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

    @Override
    public boolean clickMenuButton(Player player, int button) {
        if(button == 2) { // dissolve button
            return dissolve();
        }
        if(button == 3) { // Precipitate button
            return precipitate();
        }
        if(button != 1) {
            Main.LOGGER.info("Invalid button??");
            return false; // skip server-side run
        }
        // button #1 - try to react with reagent
        return react();
    }

    private boolean dissolve() {
        Slot input = this.slots.get(INPUT_SLOT);
        Main.LOGGER.info("State: " + alchemyState);

        if(!input.hasItem()) {
            Main.LOGGER.info("No input item");
            return false; // skip server-side run
        }
        ItemStack inputStack = input.getItem();
        // check item validity
        if(!Alchemy.isMaterial(inputStack.getItem())) {
            Main.LOGGER.info("Input invalid");
            return false; // skip server-side run
        }
        AlchemyState newState = AlchemyState.fromStack(inputStack, 1);
        if(alchemyState != null && alchemyState.position.equals(newState.position)) {
            this.alchemyState.quantity += newState.quantity;
        } else {
            this.alchemyState = AlchemyState.fromStack(inputStack, 1);
        }
        // Overwrite alchemy state
        Main.LOGGER.info("Updated state: " + alchemyState);

        input.set(ItemStack.EMPTY);
        return true; // Always run this server-side
    }

    private boolean react() {
        Slot reagentInput = this.slots.get(REAGENT_SLOT);
        if(!reagentInput.hasItem()) {
            Main.LOGGER.info("No N input item");
            return false; // skip server-side run
        }
        ItemStack reagentStack = reagentInput.getItem();
        AlchemyReagent reagent = AlchemyReagents.getReagentForItem(reagentStack.getItem());
        if(reagent == null) {
            Main.LOGGER.info("Input invalid");
            return false; // skip server-side run
        }
        if(alchemyState == null) {
            Main.LOGGER.info("No state to react with");
            return false; // skip server-side run
        }
        alchemyState.apply(reagent);
        Main.LOGGER.info("Resulting state: " + alchemyState);

        reagentStack.shrink(1);
        if(reagentStack.getCount() <= 0) {
            reagentInput.set(ItemStack.EMPTY);
        }
        return true;
    }

    private boolean precipitate() {
        if(alchemyState == null) {
            Main.LOGGER.info("No state to precipitate");
            return false;
        }
        Main.LOGGER.info("Precipitating state " + alchemyState);

        if(!alchemyState.canPrecipitate()) {
            Main.LOGGER.info("Cannot precipitate state " + alchemyState);
            return false;
        }

        alchemyState.getPrecipitationItemStacks().forEach(stack -> this.access.execute((level, pos) -> {
            ItemEntity ie = new ItemEntity(level, pos.getX(), pos.getY() - 0.15625D, pos.getZ(), stack);
            level.addFreshEntity(ie);
        }));

//        if(!resultStacks.isEmpty()) {
//            ItemStack firstStack = resultStacks.get(0);
//            Slot slot = this.slots.get(OUT_SLOT);
//            if(slot.hasItem()) {
//                ItemStack origOutStack = slot.getItem();
//                if(!origOutStack.getItem().equals(firstStack.getItem())) {
//                    Main.LOGGER.info("Item blocking output slot");
//                    return false;
//                }
//                origOutStack.grow(firstStack.getCount());
//            } else {
//                slot.set(firstStack);
//            }
//        }

        alchemyState = null;
        return true; // Always run this server-side
    }

    private class AlchemyBenchOutSlot extends Slot {
        public AlchemyBenchOutSlot(Container container, int id, int x, int y) {
            super(container, id, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        public boolean mayPickup(Player player) {
            return AlchemyMenu.this.mayPickup(player, this.hasItem());
        }

        public void onTake(Player player, ItemStack stack) {
            AlchemyMenu.this.onTake(player, stack);
        }
    }
}
