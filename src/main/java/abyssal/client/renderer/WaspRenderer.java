package abyssal.client.renderer;

import abyssal.Main;
import abyssal.entity.Wasp;
import abyssal.entity.WaspModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.BeeRenderState;
import net.minecraft.resources.Identifier;

public class WaspRenderer extends MobRenderer<Wasp, BeeRenderState, WaspModel> {
    private static final Identifier WASP_LOCATION = Main.rl("textures/entity/wasp.png");

    public WaspRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new WaspModel(ctx.bakeLayer(WaspModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public BeeRenderState createRenderState() {
        return new BeeRenderState();
    }

    @Override
    public Identifier getTextureLocation(BeeRenderState state) {
        return WASP_LOCATION;
    }
}