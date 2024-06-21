package abyssal.client.renderer;

import abyssal.entity.FishPainting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.PaintingTextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


public class FishPaintingRenderer extends EntityRenderer<FishPainting> {
    public FishPaintingRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    public void render(FishPainting painting, float rot, float p_115554_, PoseStack poseStack, MultiBufferSource bufferSource, int p_115557_) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rot));
        PaintingVariant paintingvariant = painting.getVariant().value();
        float scale = 0.0625F;
        poseStack.scale(scale, scale, scale);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entitySolid(this.getTextureLocation(painting)));
        PaintingTextureManager paintingtexturemanager = Minecraft.getInstance().getPaintingTextures();
        this.renderPainting(poseStack, vertexconsumer, painting, paintingvariant.getWidth(), paintingvariant.getHeight(), paintingtexturemanager.get(paintingvariant), paintingtexturemanager.getBackSprite());
        poseStack.popPose();
        super.render(painting, rot, p_115554_, poseStack, bufferSource, p_115557_);
    }

    public ResourceLocation getTextureLocation(FishPainting painting) {
        return Minecraft.getInstance().getPaintingTextures().getBackSprite().atlasLocation();
    }

    private void renderPainting(PoseStack poseStack, VertexConsumer vertexConsumer, FishPainting painting, int p_115562_, int p_115563_, TextureAtlasSprite sprite, TextureAtlasSprite sprite2) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        float f = (float)(-p_115562_) / 2.0F;
        float f1 = (float)(-p_115563_) / 2.0F;
        float f2 = 0.5F;
        float f3 = sprite2.getU0();
        float f4 = sprite2.getU1();
        float f5 = sprite2.getV0();
        float f6 = sprite2.getV1();
        float f7 = sprite2.getU0();
        float f8 = sprite2.getU1();
        float f9 = sprite2.getV0();
        float f10 = sprite2.getV(0.0625F);
        float f11 = sprite2.getU0();
        float f12 = sprite2.getU(0.0625F);
        float f13 = sprite2.getV0();
        float f14 = sprite2.getV1();
        int i = p_115562_ / 16;
        int j = p_115563_ / 16;
        double d0 = 1.0D / (double)i;
        double d1 = 1.0D / (double)j;

        for(int k = 0; k < i; ++k) {
            for(int l = 0; l < j; ++l) {
                float f15 = f + (float)((k + 1) * 16);
                float f16 = f + (float)(k * 16);
                float f17 = f1 + (float)((l + 1) * 16);
                float f18 = f1 + (float)(l * 16);
                int x = painting.getBlockX();
                int j1 = Mth.floor(painting.getY() + (double)((f17 + f18) / 2.0F / 16.0F));
                int z = painting.getBlockZ();
                Direction direction = painting.getDirection();
                if (direction == Direction.NORTH) {
                    x = Mth.floor(painting.getX() + (double)((f15 + f16) / 2.0F / 16.0F));
                }

                if (direction == Direction.WEST) {
                    z = Mth.floor(painting.getZ() - (double)((f15 + f16) / 2.0F / 16.0F));
                }

                if (direction == Direction.SOUTH) {
                    x = Mth.floor(painting.getX() - (double)((f15 + f16) / 2.0F / 16.0F));
                }

                if (direction == Direction.EAST) {
                    z = Mth.floor(painting.getZ() + (double)((f15 + f16) / 2.0F / 16.0F));
                }

                int l1 = LevelRenderer.getLightColor(painting.level(), new BlockPos(x, j1, z));
                float f19 = sprite.getU((float) (d0 * (double)(i - k)));
                float f20 = sprite.getU((float) (d0 * (double)(i - (k + 1))));
                float f21 = sprite.getV((float)  (d1 * (double)(j - l)));
                float f22 = sprite.getV((float)  (d1 * (double)(j - (l + 1))));
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f18, f20, f21, -0.5F, 0, 0, -1, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f18, f19, f21, -0.5F, 0, 0, -1, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f17, f19, f22, -0.5F, 0, 0, -1, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f17, f20, f22, -0.5F, 0, 0, -1, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f17, f4, f5, 0.5F, 0, 0, 1, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f17, f3, f5, 0.5F, 0, 0, 1, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f18, f3, f6, 0.5F, 0, 0, 1, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f18, f4, f6, 0.5F, 0, 0, 1, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f17, f7, f9, -0.5F, 0, 1, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f17, f8, f9, -0.5F, 0, 1, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f17, f8, f10, 0.5F, 0, 1, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f17, f7, f10, 0.5F, 0, 1, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f18, f7, f9, 0.5F, 0, -1, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f18, f8, f9, 0.5F, 0, -1, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f18, f8, f10, -0.5F, 0, -1, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f18, f7, f10, -0.5F, 0, -1, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f17, f12, f13, 0.5F, -1, 0, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f18, f12, f14, 0.5F, -1, 0, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f18, f11, f14, -0.5F, -1, 0, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f15, f17, f11, f13, -0.5F, -1, 0, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f17, f12, f13, -0.5F, 1, 0, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f18, f12, f14, -0.5F, 1, 0, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f18, f11, f14, 0.5F, 1, 0, 0, l1);
                this.vertex(matrix4f, matrix3f, vertexConsumer, f16, f17, f11, f13, 0.5F, 1, 0, 0, l1);
            }
        }

    }

    private void vertex(Matrix4f p_115537_, Matrix3f p_115538_, VertexConsumer p_115539_, float p_115540_, float p_115541_, float p_115542_, float p_115543_, float p_115544_, int p_115545_, int p_115546_, int p_115547_, int p_115548_) {
        p_115539_.vertex(p_115537_, p_115540_, p_115541_, p_115544_).color(255, 255, 255, 255).uv(p_115542_, p_115543_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_115548_).normal(p_115538_, (float)p_115545_, (float)p_115546_, (float)p_115547_).endVertex();
    }
}