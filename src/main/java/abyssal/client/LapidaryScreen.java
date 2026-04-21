package abyssal.client;

import abyssal.Main;
import abyssal.inventory.LapidaryMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

public class LapidaryScreen extends AbstractContainerScreen<LapidaryMenu> implements ContainerListener {
    private static final Identifier LAPIDARY_TABLE_LOCATION = Main.rl("textures/gui/lapidary.png");

    public LapidaryScreen(LapidaryMenu menu, Inventory inv, Component component) {
        super(menu, inv, component);
        this.titleLabelX = 85;
        this.titleLabelY = 12;
    }

//    @Override
//    protected void renderLabels(PoseStack poseStack, int dummy1, int dummy2) {
//        RenderSystem.disableBlend();
//        super.renderLabels(poseStack, dummy1, dummy2);
//    }

    protected void init() {
        super.init();
        this.menu.addSlotListener(this);
    }

    public void removed() {
        super.removed();
        this.menu.removeSlotListener(this);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double x = event.x();
        double y = event.y();
        int xstart = (this.width - this.imageWidth) / 2;
        int ystart = (this.height - this.imageHeight) / 2;

        double xl = x - (double)(xstart + 126);
        double yl = y - (double)(ystart + 24);
        double yl1 = y - (double)(ystart + 51);
        if (xl >= 0.0D && yl >= 0.0D && xl < 36D && yl < 18.0D) {
            if (this.menu.clickMenuButton(this.minecraft.player, 1)) {
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, 1);
                return true;
            }
        }
        if (xl >= 0.0D && yl1 >= 0.0D && xl < 36D && yl1 < 18.0D) {
            if (this.menu.clickMenuButton(this.minecraft.player, 2)) {
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, 2);
                return true;
            }
        }

        return super.mouseClicked(event, doubleClick);
    }


    @Override
    public void extractBackground(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float a) {
        int xstart = (this.width - this.imageWidth) / 2;
        int ystart = (this.height - this.imageHeight) / 2;
        gfx.blit(RenderPipelines.GUI_TEXTURED, LAPIDARY_TABLE_LOCATION, xstart, ystart, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        gfx.blit(RenderPipelines.GUI_TEXTURED, LAPIDARY_TABLE_LOCATION, xstart + 59, ystart + 20, 0, this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 110, 16, 256, 256);
        if(isHovering(126,24,36,18,mouseX,mouseY)) {
            gfx.blit(RenderPipelines.GUI_TEXTURED, LAPIDARY_TABLE_LOCATION, xstart + 126, ystart + 24, this.imageWidth, 54, 36, 18, 256, 256);
        }
        if(isHovering(126,51,36,18,mouseX,mouseY)) {
            gfx.blit(RenderPipelines.GUI_TEXTURED, LAPIDARY_TABLE_LOCATION, xstart + 126, ystart + 51, this.imageWidth, 92, 36, 18, 256, 256);
        }
    }


    public void dataChanged(AbstractContainerMenu p_169759_, int p_169760_, int p_169761_) {
    }

    public void slotChanged(AbstractContainerMenu p_98910_, int p_98911_, ItemStack p_98912_) {
    }
}
