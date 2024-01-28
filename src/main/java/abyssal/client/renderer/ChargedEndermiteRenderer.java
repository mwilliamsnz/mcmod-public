package abyssal.client.renderer;

import abyssal.entity.ChargedEndermite;
import net.minecraft.client.model.EndermiteModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ChargedEndermiteRenderer extends MobRenderer<ChargedEndermite, EndermiteModel<ChargedEndermite>> {
    private static final ResourceLocation ENDERMITE_LOCATION = new ResourceLocation("textures/entity/endermite.png");

    public ChargedEndermiteRenderer(EntityRendererProvider.Context p_173994_) {
        super(p_173994_, new EndermiteModel<>(p_173994_.bakeLayer(ModelLayers.ENDERMITE)), 0.3F);
        this.addLayer(new ChargedEndermitePowerLayer(this, p_173994_.getModelSet()));

    }

    protected float getFlipDegrees(ChargedEndermite p_114352_) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(ChargedEndermite p_114354_) {
        return ENDERMITE_LOCATION;
    }
}