package abyssal.client.renderer;

import abyssal.Main;
import abyssal.entity.TreeSpider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.resources.ResourceLocation;

public class TreeSpiderRenderer extends SpiderRenderer<TreeSpider> {
    private static final ResourceLocation TREE_SPIDER_LOCATION = new ResourceLocation(Main.MOD_ID,"textures/entity/tree_spider.png");
    private static final float SCALE = 0.7F;

    public TreeSpiderRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, ModelLayers.CAVE_SPIDER);
        this.shadowRadius *= SCALE;
    }

    protected void scale(TreeSpider spider, PoseStack stack, float f) {
        stack.scale(SCALE, SCALE, SCALE);
    }

    public ResourceLocation getTextureLocation(TreeSpider spider) {
        return TREE_SPIDER_LOCATION;
    }
}