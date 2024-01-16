package abyssal.client;

import abyssal.Main;
import abyssal.inventory.AlchemyMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HarmoniserScreen extends AbstractContainerScreen<AlchemyMenu> implements ContainerListener {
    private static final ResourceLocation HARMONISER_LOCATION = new ResourceLocation(Main.MOD_ID, "textures/gui/alchemy.png");

    public HarmoniserScreen(AlchemyMenu menu, Inventory inv, Component component) {
        super(menu, inv, component);
        this.titleLabelX = 90;
        this.titleLabelY = 12;
    }

    protected void init() {
        super.init();
        this.menu.addSlotListener(this);
    }

    public void removed() {
        super.removed();
        this.menu.removeSlotListener(this);
    }

    @Override
    public boolean mouseClicked(double x, double y, int p_98760_) {
        int xstart = (this.width - this.imageWidth) / 2;
        int ystart = (this.height - this.imageHeight) / 2;

        double xl = x - (double)(xstart + 20);
        double yl = y - (double)(ystart + 7);
        double yl1 = y - (double)(ystart + 34);
        double xl2 = x - (double)(xstart + 91);

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
        if (xl2 >= 0.0D && yl1 >= 0.0D && xl2 < 36D && yl1 < 18.0D) {
            if (this.menu.clickMenuButton(this.minecraft.player, 3)) {
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, 3);
                return true;
            }
        }

        return super.mouseClicked(x, y, p_98760_);
    }

    @Override
    public void render(GuiGraphics gfx, int x, int y, float f) {
        f = this.minecraft.getFrameTime();
        super.render(gfx, x, y, f);
        this.renderTooltip(gfx, x, y);
    }
    @Override
    protected void renderBg(GuiGraphics gfx, float f, int mouseX, int mouseY) {
        int xstart = (this.width - this.imageWidth) / 2;
        int ystart = (this.height - this.imageHeight) / 2;
        gfx.blit(HARMONISER_LOCATION, xstart, ystart, 0, 0, this.imageWidth, this.imageHeight);
        gfx.blit(HARMONISER_LOCATION, xstart + 59, ystart + 20, 0, this.imageHeight + (this.menu.getSlot(0).hasItem() ? 0 : 16), 110, 16);
        if(isHovering(20,7,36,18,mouseX,mouseY)) {
            gfx.blit(HARMONISER_LOCATION, xstart + 20, ystart + 7, this.imageWidth, 54, 36, 18);
        }
        if(isHovering(20,34,36,18,mouseX,mouseY)) {
            gfx.blit(HARMONISER_LOCATION, xstart + 20, ystart + 34, this.imageWidth, 92, 36, 18);
        }
        if(isHovering(91,34,36,18,mouseX,mouseY)) {
            gfx.blit(HARMONISER_LOCATION, xstart + 91, ystart + 34, this.imageWidth, 130, 36, 18);
        }

    }

    public void dataChanged(AbstractContainerMenu p_169759_, int p_169760_, int p_169761_) {
    }

    public void slotChanged(AbstractContainerMenu p_98910_, int p_98911_, ItemStack p_98912_) {
    }
}
