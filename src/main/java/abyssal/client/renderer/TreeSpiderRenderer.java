package abyssal.client.renderer;

import abyssal.Main;
import abyssal.entity.TreeSpider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class TreeSpiderRenderer extends SpiderRenderer<TreeSpider> {
    private static final Identifier TREE_SPIDER_LOCATION = Main.rl("textures/entity/tree_spider.png");
    private static final float SCALE = 0.7F;



    public TreeSpiderRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, ModelLayers.CAVE_SPIDER);
        this.layers.removeLast(); // eyes
        this.addLayer(new TreeSpiderEyesLayer<>(this));
//        this.shadowRadius *= SCALE;
    }

//    @Override
//    protected void scale(TreeSpider spider, PoseStack stack, float f) {
//        stack.scale(SCALE, SCALE, SCALE);
//    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TREE_SPIDER_LOCATION;
    }
}