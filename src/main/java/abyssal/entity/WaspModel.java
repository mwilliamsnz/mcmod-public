package abyssal.entity;

import abyssal.Main;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.BeeRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class WaspModel extends EntityModel<BeeRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Main.MOD_ID, "wasp"), "main");
    private final ModelPart bone;
    private final ModelPart leftWing;
    private final ModelPart rightWing;

    public WaspModel(ModelPart root) {
        super(root);
        this.bone = root.getChild("bone");
        this.leftWing = root.getChild("left_wing");
        this.rightWing = root.getChild("right_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 6).addBox(-1.5F, -8.0F, 0.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(12, 13).addBox(-0.5F, -1.0F, -3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(14, 1).addBox(-1.5F, -11.0F, -3.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition rightWing = partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-8F, 0.0F, 0.0F, 8.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 16.0F, 0.0F));
        PartDefinition leftWing = partdefinition.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 3)
                .addBox(0F, 0.0F, 0.0F, 8.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 16.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(BeeRenderState state) {
        super.setupAnim(state);
        if (!state.isOnGround) {
            float speed = state.ageInTicks * 120.32113F * (float) (Math.PI / 180.0);
            this.rightWing.yRot = 0.0F;
            this.rightWing.zRot = Mth.cos(speed) * (float) Math.PI * 0.15F;
            this.leftWing.xRot = this.rightWing.xRot;
            this.leftWing.yRot = this.rightWing.yRot;
            this.leftWing.zRot = -this.rightWing.zRot;
        }

        float rollAmount = state.rollAmount;
        if (rollAmount > 0.0F) {
            this.bone.xRot = Mth.rotLerpRad(rollAmount, this.bone.xRot, 3.0915928F);
        }
    }

}
