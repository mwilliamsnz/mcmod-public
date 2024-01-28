package abyssal.client.renderer;

import abyssal.entity.ChargedEndermite;
import net.minecraft.client.model.EndermiteModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChargedEndermitePowerLayer extends EnergySwirlLayer<ChargedEndermite, EndermiteModel<ChargedEndermite>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final EndermiteModel<ChargedEndermite> model;

    public ChargedEndermitePowerLayer(RenderLayerParent<ChargedEndermite, EndermiteModel<ChargedEndermite>> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new EndermiteModel<>(modelSet.bakeLayer(ModelLayers.ENDERMITE));
    }

    protected float xOffset(float factor) {
        return factor * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected EntityModel<ChargedEndermite> model() {
        return this.model;
    }
}