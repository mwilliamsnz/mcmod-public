package abyssal.client;

import abyssal.inventory.CoinPurseTooltip;
import abyssal.items.curios.CoinPurseItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class ClientCoinPurseTooltip implements ClientTooltipComponent {
    private static final ResourceLocation BACKGROUND_SPRITE = new ResourceLocation("container/bundle/background");
    private static final int MARGIN_Y = 4;
    private static final int BORDER_WIDTH = 1;
    private static final int SLOT_SIZE_X = 18;
    private static final int SLOT_SIZE_Y = 20;
    private final NonNullList<ItemStack> items;
    private final int weight;

    public ClientCoinPurseTooltip(CoinPurseTooltip toolTip) {
        this.items = toolTip.getItems();
        this.weight = toolTip.getWeight();
    }

    public int getHeight() {
        return this.backgroundHeight() + MARGIN_Y;
    }

    public int getWidth(Font font) {
        return this.backgroundWidth();
    }

    private int backgroundWidth() {
        return this.gridSizeX() * SLOT_SIZE_X + BORDER_WIDTH*2;
    }

    private int backgroundHeight() {
        return this.gridSizeY() * SLOT_SIZE_Y + BORDER_WIDTH*2;
    }

    public void renderImage(Font font, int x, int y, GuiGraphics gfx) {
        int xSize = this.gridSizeX();
        int ySize = this.gridSizeY();
        gfx.blitSprite(BACKGROUND_SPRITE, x, y, this.backgroundWidth(), this.backgroundHeight());
        boolean noMoreFits = this.weight >= CoinPurseItem.MAX_WEIGHT; // The significant change, no longer hard-coded 64
        int slotIdx = 0;

        for(int yi = 0; yi < ySize; ++yi) {
            for(int xi = 0; xi < xSize; ++xi) {
                int xpos = x + xi * SLOT_SIZE_X + BORDER_WIDTH;
                int ypos = y + yi * SLOT_SIZE_Y + BORDER_WIDTH;
                this.renderSlot(xpos, ypos, slotIdx++, noMoreFits, gfx, font);
            }
        }

    }

    private void renderSlot(int x, int y, int slot, boolean noMoreFits, GuiGraphics gfx, Font font) {
        if (slot >= this.items.size()) {
            this.blit(gfx, x, y, noMoreFits ? Texture.BLOCKED_SLOT : Texture.SLOT);
        } else {
            ItemStack stackInSlot = this.items.get(slot);
            this.blit(gfx, x, y, Texture.SLOT);
            gfx.renderItem(stackInSlot, x + 1, y + 1, slot);
            gfx.renderItemDecorations(font, stackInSlot, x + 1, y + 1);
            if (slot == 0) {
                AbstractContainerScreen.renderSlotHighlight(gfx, x + 1, y + 1, 0);
            }

        }
    }

    private void blit(GuiGraphics gfx, int x, int y, Texture texture) {
        gfx.blitSprite(texture.sprite, x, y, 0, texture.w, texture.h);
    }

    private int gridSizeX() {
        return Math.max(2, (int)Math.ceil(Math.sqrt((double)this.items.size() + 1.0D)));
    }

    private int gridSizeY() {
        return (int)Math.ceil(((double)this.items.size() + 1.0D) / (double)this.gridSizeX());
    }

    @OnlyIn(Dist.CLIENT)
    enum Texture {
        BLOCKED_SLOT(new ResourceLocation("container/bundle/blocked_slot"), SLOT_SIZE_X, SLOT_SIZE_Y),
        SLOT(new ResourceLocation("container/bundle/slot"), SLOT_SIZE_X, SLOT_SIZE_Y);

        public final ResourceLocation sprite;
        public final int w;
        public final int h;

        Texture(ResourceLocation resourceLocation, int w, int h) {
            this.sprite = resourceLocation;
            this.w = w;
            this.h = h;
        }
    }
}