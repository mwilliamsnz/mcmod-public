package abyssal.client.renderer;

import abyssal.Main;
import abyssal.entity.ChargedEndermite;
import abyssal.entity.ChargedEndermiteModel;
import net.minecraft.client.model.EndermiteModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class ChargedEndermiteRenderer extends MobRenderer<ChargedEndermite, LivingEntityRenderState, ChargedEndermiteModel> {
    private static final ResourceLocation ENDERMITE_LOCATION = Main.rl("textures/entity/endermite.png");

    public ChargedEndermiteRenderer(EntityRendererProvider.Context p_173994_) {
        super(p_173994_, new ChargedEndermiteModel(p_173994_.bakeLayer(ModelLayers.ENDERMITE)), 0.3F);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    protected float getFlipDegrees() {
        return 180.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState renderState) {
        return ENDERMITE_LOCATION;
    }
}