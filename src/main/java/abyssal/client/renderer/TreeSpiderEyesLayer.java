package abyssal.client.renderer;

import abyssal.Main;
import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class TreeSpiderEyesLayer<M extends SpiderModel> extends EyesLayer<LivingEntityRenderState, M> {
    private static final RenderType TREE_SPIDER_EYES = RenderTypes.eyes(Main.rl("textures/entity/tree_spider_eyes.png"));

    public TreeSpiderEyesLayer(RenderLayerParent<LivingEntityRenderState, M> layer) {
        super(layer);
    }

    @Override
    public RenderType renderType() {
        return TREE_SPIDER_EYES;
    }
}